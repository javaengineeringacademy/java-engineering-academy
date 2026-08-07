# Python Projects — Hands-On Practice

Theory without practice is trivia. Build real things. Break things. Fix them. That's how you learn.

---

## How to Use These Projects

1. **Start with the project that excites you** — motivation beats curriculum
2. **Read the requirements first**, then try building it yourself before looking at any solution
3. **Push past the MVP** — the learning happens when you add "just one more feature"
4. **Time-box yourself** — spend 1-3 hours per project, then move on if stuck
5. **Revisit projects** after learning new concepts — refactor with better patterns

---

## Project Ideas by Skill Level

### Beginner (0-3 months)

| Project | What You'll Learn | Stretch Goal |
|---------|-------------------|--------------|
| **Password Generator** | Random module, string manipulation, CLI args | Add a password strength estimator |
| **Unit Converter** | Functions, input validation, error handling | Support 20+ units, add a GUI |
| **Quiz App** | Dictionaries, loops, scoring logic | Load questions from a JSON file |
| **Expense Tracker** | File I/O, CSV handling, data structures | Generate monthly reports |
| **Caesar Cipher** | String manipulation, ASCII, modular arithmetic | Brute-force decoder |

### Intermediate (3-12 months)

| Project | What You'll Learn | Stretch Goal |
|---------|-------------------|--------------|
| **CLI Todo App** | Click/Typer, JSON persistence, task management | Add due dates, priorities, search |
| **Web Scraper** | requests + BeautifulSoup, data extraction | Schedule with cron, store in SQLite |
| **API Wrapper** | requests, authentication, error handling | Publish to PyPI |
| **File Organizer** | os/shutil, file monitoring, automation | Watch directory in real-time with watchdog |
| **Markdown to HTML** | Parsing, string processing, templates | Support all CommonMark syntax |

### Advanced (12+ months)

| Project | What You'll Learn | Stretch Goal |
|---------|-------------------|--------------|
| **Async Web Crawler** | asyncio, aiohttp, concurrency control | Distributed crawling with Redis |
| **Task Queue** | Threading/multiprocessing, serialization, persistence | Add web UI dashboard |
| **Database ORM** | SQLAlchemy, metaclasses, descriptor protocol | Query builder with type safety |
| **Linter Plugin** | AST parsing, visitor pattern, plugin architecture | Integrate with VS Code |
| **ORM + Migrations** | Code generation, database introspection, CLI | Alembic-like migration system |

---

## Mini Project Ideas (1-2 hours each)

These are bite-sized projects perfect for a single study session:

### Data & Files
- [ ] **Log Parser** — Parse nginx/Apache logs, extract stats (top IPs, 404s, etc.)
- [ ] **CSV Analyzer** — Read CSV, compute statistics, detect data types
- [ ] **Config Manager** — Merge YAML/TOML/INI configs with env var overrides
- [ ] **Duplicate Finder** — Hash files, group duplicates, suggest cleanup
- [ ] **Backup Script** — Incremental backups with tar, compression, rotation

### Automation
- [ ] **Git Hook Runner** — Pre-commit hooks that lint, type-check, and test
- [ ] **Dependency Checker** — Detect unused imports, outdated packages
- [ ] **File Watcher** — React to file changes (recompile, reload, etc.)
- [ ] **Environment Setup** — One-command dev environment bootstrap
- [ ] **Deploy Script** — SSH + rsync + health check automation

### Learning Reinforcement
- [ ] **Implement `@cache`** — Build a memoization decorator from scratch
- [ ] **Mini `itertools`** — Implement `chain`, `groupby`, `product` without importing them
- [ ] **Context Manager** — Build a `@contextmanager` equivalent using `__enter__`/`__exit__`
- [ ] **Property Factory** — Build a `property` replacement using descriptors
- [ ] **Event Emitter** — Build an observer pattern with type-safe callbacks

### CLI & Developer Tools
- [ ] **Color Logger** — Colored, leveled logging with context variables
- [ ] **JSON CLI** — `jq`-like tool built with `jq.py` or raw parsing
- [ ] **Prompt Toolkit** — Interactive CLI with fuzzy search, autocomplete
- [ ] **Code Metrics** — Count LOC, cyclomatic complexity, naming conventions
- [ ] **Test Runner** — Minimal pytest clone that discovers and runs tests

---

## Learning Objectives

Each project should teach you at least **two** of these skills:

| Skill | How to Practice |
|-------|----------------|
| **Error handling** | Every project should handle edge cases gracefully |
| **Testing** | Write tests for every project — even a few basic ones |
| **Documentation** | Write a README with usage examples |
| **Packaging** | Structure as a proper Python package with `pyproject.toml` |
| **Version control** | Use Git with meaningful commit messages |
| **Code review** | Get feedback — from a mentor, peer, or AI |

---

## Project Structure Template

```
project-name/
├── README.md           # What it does, how to use it
├── pyproject.toml      # Package metadata, dependencies
├── src/
│   └── project_name/
│       ├── __init__.py
│       ├── main.py     # Entry point
│       └── ...
├── tests/
│   ├── __init__.py
│   └── test_main.py
└── .gitignore
```

**Rules:**
- Flat imports: `from project_name import module` — not `from src.project_name`
- Tests mirror source structure: `src/foo.py` → `tests/test_foo.py`
- One file, one responsibility — split when files exceed 300 lines

---

## Tips for Maximum Learning

1. **Build for yourself** — Solve a real problem you have
2. **Read the docs** — Python's official docs are exceptional
3. **Study other implementations** — Read how others solved similar problems
4. **Refactor ruthlessly** — First working version is just the starting point
5. **Ship it** — Even a small CLI tool on GitHub counts as portfolio material

---

*Pick one. Start now. Finish it. Then pick the next one.*
