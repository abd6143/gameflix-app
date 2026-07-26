package com.gameflix.controller;

import com.gameflix.dto.GameDto;
import com.gameflix.dto.PlanDto;
import com.gameflix.dto.SubscriptionDto;
import com.gameflix.dto.UserDto;
import com.gameflix.entity.PlanTier;
import com.gameflix.entity.SubscriptionStatus;
import com.gameflix.entity.User;
import com.gameflix.exception.ResourceNotFoundException;
import com.gameflix.service.AdminService;
import com.gameflix.service.GameService;
import com.gameflix.service.SubscriptionService;
import com.gameflix.service.UserService;
import com.gameflix.util.SecurityUtils;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    private final GameService gameService;
    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final AdminService adminService;
    private final SecurityUtils securityUtils;

    public WebController(
            GameService gameService,
            SubscriptionService subscriptionService,
            UserService userService,
            AdminService adminService,
            SecurityUtils securityUtils) {
        this.gameService = gameService;
        this.subscriptionService = subscriptionService;
        this.userService = userService;
        this.adminService = adminService;
        this.securityUtils = securityUtils;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<PlanDto> plans = subscriptionService.listPlans();
        Page<GameDto> featured = gameService.findAll(null, null, null,
                PageRequest.of(0, 6, Sort.by(Sort.Direction.DESC, "rating")));
        model.addAttribute("plans", plans);
        model.addAttribute("featuredGames", featured.getContent());
        return "index";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, Model model) {
        model.addAttribute("error", error != null);
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        UserDto user = userService.getCurrentUser(authentication.getName());
        Page<GameDto> recent = gameService.findAll(null, null, null,
                PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "createdAt")));
        model.addAttribute("user", user);
        model.addAttribute("recentGames", recent.getContent());
        try {
            User entity = userService.findByEmail(authentication.getName());
            SubscriptionDto subscription = subscriptionService.getMySubscription(entity);
            model.addAttribute("subscription", subscription);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("subscription", null);
        }
        return "dashboard";
    }

    @GetMapping("/catalog")
    public String catalog(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String platform,
            @RequestParam(required = false) PlanTier plan,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        PageRequest pageable = PageRequest.of(page, 12, Sort.by("title"));
        Page<GameDto> games;
        if (q != null && !q.isBlank()) {
            games = gameService.search(q, pageable);
        } else {
            games = gameService.findAll(genre, platform, plan, pageable);
        }
        model.addAttribute("games", games);
        model.addAttribute("genres", Arrays.asList("Action", "RPG", "Strategy", "Sports", "FPS"));
        model.addAttribute("platforms", Arrays.asList("PC", "PlayStation", "Xbox", "Nintendo Switch"));
        model.addAttribute("plans", PlanTier.values());
        model.addAttribute("selectedGenre", genre);
        model.addAttribute("selectedPlatform", platform);
        model.addAttribute("selectedPlan", plan);
        model.addAttribute("searchQuery", q);
        return "catalog";
    }

    @GetMapping("/games/{id}")
    public String gameDetail(@PathVariable Long id, Model model, Authentication authentication) {
        GameDto game = gameService.findById(id);
        Page<GameDto> similar = gameService.findSimilar(game.getGenre(), id, PageRequest.of(0, 4));
        model.addAttribute("game", game);
        model.addAttribute("similarGames", similar.getContent());
        model.addAttribute("canPlay", canPlayGame(authentication, game));
        return "game-detail";
    }

    @GetMapping("/subscription")
    public String subscription(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        User user = userService.findByEmail(authentication.getName());
        try {
            SubscriptionDto subscription = subscriptionService.getMySubscription(user);
            model.addAttribute("subscription", subscription);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("subscription", null);
        }
        model.addAttribute("plans", subscriptionService.listPlans());
        return "subscription";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("stats", adminService.getStats());
        model.addAttribute("users", adminService.listUsers());
        return "admin/dashboard";
    }

    @GetMapping("/admin/games")
    public String adminGames(Model model) {
        Page<GameDto> games = gameService.findAll(null, null, null,
                PageRequest.of(0, 50, Sort.by("title")));
        model.addAttribute("games", games.getContent());
        return "admin/games";
    }

    @GetMapping("/admin/users")
    public String adminUsers(Model model) {
        model.addAttribute("users", adminService.listUsers());
        return "admin/users";
    }

    private boolean canPlayGame(Authentication authentication, GameDto game) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        try {
            User user = userService.findByEmail(authentication.getName());
            SubscriptionDto sub = subscriptionService.getMySubscription(user);
            if (sub.getStatus() != SubscriptionStatus.ACTIVE) {
                return false;
            }
            PlanTier userTier = planNameToTier(sub.getPlanName());
            return userTier.ordinal() >= game.getAvailableOnPlan().ordinal();
        } catch (Exception ex) {
            return false;
        }
    }

    private PlanTier planNameToTier(String planName) {
        if (planName == null) {
            return PlanTier.BASIC;
        }
        return switch (planName.toLowerCase()) {
            case "premium" -> PlanTier.PREMIUM;
            case "standard" -> PlanTier.STANDARD;
            default -> PlanTier.BASIC;
        };
    }
}
