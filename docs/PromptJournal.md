# Prompt Journal — GameFlix

## Purpose

This journal documents AI-assisted development prompts used during the GameFlix university project. Each entry records what was asked, which tool was used, what was generated, and what changes were made during review. Maintaining this log supports academic integrity transparency and helps track design decisions over time.

---

## Entries

| Date | Prompt Summary | Tool | Output | Changes Made | Why |
|------|---------------|------|--------|--------------|-----|
| 2026-06-11 | Full end-to-end scaffold of GameFlix: Spring Boot 3 backend, JWT auth, game catalog, subscriptions, admin API, Thymeleaf dark-theme frontend, tests, Docker, CI/CD, Postman collection, and project documentation (SRS, user stories, architecture) | Cursor AI (Claude) | Complete Maven project with ~60 source files, 10 Thymeleaf templates, 3 test classes, data.sql seed data, Dockerfile, docker-compose.yml, GitHub Actions workflow, Postman collection, and 4 docs files | *Pending review* — Run `mvn verify` locally, test login with seed credentials (`admin@gameflix.com` / `Admin@123`), import Postman collection, and verify Docker build. Adjust any UI copy, seed game titles, or plan pricing to match course rubric requirements. | Initial project generation; review ensures generated code compiles on your machine, meets grading rubric specifics, and reflects your own understanding before submission |

---

## Template for Future Entries

| Date | Prompt Summary | Tool | Output | Changes Made | Why |
|------|---------------|------|--------|--------------|-----|
| YYYY-MM-DD | [Brief description of what you asked] | [Cursor / ChatGPT / etc.] | [What was generated] | [What you changed after review] | [Reason for changes] |

### Tips
- Be specific about what you changed — graders want to see your engineering judgment
- Note any bugs found during review and how you fixed them
- Record rejected AI suggestions and why you chose a different approach
