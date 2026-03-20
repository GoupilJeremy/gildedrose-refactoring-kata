# CLAUDE.md — Gilded Rose Refactoring Kata

This file provides AI assistants with context about the codebase, workflows, and conventions for the Gilded Rose Refactoring Kata repository.

## Project Overview

This is a **multi-language refactoring kata** for teaching how to safely refactor legacy code using tests. It contains 65+ language implementations, all sharing the same business problem: a shop inventory system with special item rules.

The primary goal is to **add the "Conjured" item feature** by first writing tests (characterization / approval tests), then refactoring the existing messy code, then adding the new feature.

**Key principle**: Do NOT submit solutions. This repo is a kata starting point, not a solution repository.

## Business Domain

See `GildedRoseRequirements.md` for the full specification. In summary:

- Each `Item` has `name`, `sell_in` (days until sale), and `quality` (value).
- `update_quality()` is called once per day.
- Quality is always between 0 and 50, except Sulfuras (always 80).
- After `sell_in` drops below 0, quality degrades twice as fast.
- **Aged Brie**: quality increases over time (capped at 50).
- **Sulfuras, Hand of Ragnaros**: legendary item — never changes.
- **Backstage passes to a TAFKAL80ETC concert**: quality increases by 1 normally, +2 when ≤10 days, +3 when ≤5 days, drops to 0 after the concert.
- **Conjured items**: degrade in quality twice as fast as normal items.
- The `Item` class **must not be modified** (goblin's orders).

## Repository Structure

```
/
├── GildedRoseRequirements.md       # English specification (source of truth)
├── GildedRoseRequirements_*.md     # Translations (de, es, fr, it, jp, etc.)
├── README.md                       # Kata overview and quick start
├── CONTRIBUTING.md                 # How to contribute language implementations
├── texttests/                      # Approval testing (TextTest framework)
│   ├── config.gr                   # TextTest config (active language per run)
│   ├── ThirtyDays/stdout.gr        # Golden master: expected 30-day output
│   └── ThirtyDays/options.gr       # Test options
├── .github/
│   ├── workflows/pr-validation.yml # Prevents solution submissions via PRs
│   └── workflows/jq.yml            # CI for jq implementation
├── start_texttest.sh               # Run TextTest on Linux/Mac
├── start_texttest.bat              # Run TextTest on Windows
└── <language>/                     # 65+ language directories (see below)
```

## Language Implementations

Each language directory follows this pattern:

```
<language>/
├── gilded_rose.*           # Main production code (primary refactoring target)
├── *test.*                 # Unit tests (start with one failing "fixme" test)
├── texttest_fixture.*      # TextTest fixture (for approval/golden master testing)
├── README.md               # Language-specific instructions
└── <build config>          # e.g., pom.xml, package.json, Cargo.toml, etc.
```

### Key Languages and Their Tooling

| Language | Directory | Build Tool | Test Framework |
|---|---|---|---|
| Python | `python/` | pip / pytest | pytest, approvaltests |
| JavaScript | `js-mocha/`, `js-jest/`, `js-jasmine/` | npm | Mocha/Jest/Jasmine + Babel |
| TypeScript | `TypeScript/` | npm | Jest, Mocha, or Vitest |
| Java | `Java/` | Maven or Gradle | JUnit 5 |
| Java (BDD) | `Java-Cucumber/` | Gradle | Cucumber |
| Java (Approval) | `Java-Approvals/` | Gradle | Approval Testing |
| Kotlin | `Kotlin/` | Gradle (kts) | JUnit 5 |
| C# | `csharp.NUnit/`, `csharp.xUnit/` | dotnet | NUnit / xUnit |
| C++ | `cpp/`, `cpp-catch2/` | CMake | Catch2 |
| Ruby | `ruby/` | bundler | RSpec |
| Go | `go/` | go mod | go test |
| Rust | `rust/` | Cargo | built-in |
| Elixir | `elixir/` | Mix | ExUnit |
| Clojure | `clojure/` | Clojure CLI | clojure.test |
| Gleam | `gleam/` | gleam | glint + stdlib |

## Development Workflows

### Running Tests for a Specific Language

Navigate to the language directory and use its native tooling:

```bash
# Python
cd python && pip install -r requirements.txt && pytest

# JavaScript (Mocha)
cd js-mocha && npm install && npm test

# TypeScript
cd TypeScript && npm install && npm test

# Java (Maven)
cd Java && mvn test

# Java (Gradle)
cd Java && gradle test

# C#
cd csharp.NUnit && dotnet test

# Ruby
cd ruby && bundle install && rspec

# Go
cd go && go test ./...

# Rust
cd rust && cargo test

# Elixir
cd elixir && mix test
```

### Running Approval Tests (TextTest / Golden Master)

TextTest runs all language variants against a 30-day golden master output:

```bash
# Linux/Mac
./start_texttest.sh

# Windows
start_texttest.bat
```

Edit `texttests/config.gr` to select which language to test. The golden master is in `texttests/ThirtyDays/stdout.gr`.

### Typical Kata Workflow

1. **Read** `GildedRoseRequirements.md` to understand the domain.
2. **Understand** the existing code in your chosen language (it's intentionally messy).
3. **Write tests** — use approval/characterization tests to capture existing behavior before changing anything.
4. **Refactor** the `update_quality()` method while keeping all tests green.
5. **Add the Conjured item feature** (the actual new requirement).

## Conventions and Constraints

### Immutable Classes

- **`Item` class must NOT be modified** — it is owned by a goblin who will kill you.
- Only `GildedRose` (or `Shop`) and its `update_quality()` method should be changed.

### Quality Rules (always enforce)

- Quality is never negative.
- Quality never exceeds 50 (except Sulfuras which is always exactly 80).
- Sulfuras's `sell_in` and `quality` never change.

### Test File Conventions

- Start with the provided failing test (usually checks `"fixme" != "foo"`).
- Replace or expand with characterization tests before refactoring.
- Keep the `texttest_fixture` file unchanged — it's used by TextTest for golden master comparison.

### Naming Conventions (by language)

- **Python**: `snake_case` for files and functions; `PascalCase` for classes.
- **JavaScript/TypeScript**: `camelCase` for methods; `PascalCase` for classes.
- **Java/Kotlin**: `camelCase` methods; `PascalCase` classes; package `com.gildedrose`.
- **C#**: `PascalCase` for methods and classes; namespace `GildedRoseKata`.
- **Ruby**: `snake_case` files and methods; `PascalCase` classes.
- **Go**: exported names `PascalCase`; unexported `camelCase`.

## CI/CD

### PR Validation (`.github/workflows/pr-validation.yml`)

- Runs on pull_request (opened).
- Verifies the PR template checkbox is checked: `"I acknowledge this PR is not a Gilded Rose solution"`.
- Auto-closes PRs that don't include this acknowledgment.
- **When contributing**: Always check the acknowledgment checkbox in the PR description.

### jq CI (`.github/workflows/jq.yml`)

- Tests the `jq/` implementation using Rust's `jaq`.
- Verifies TextTest fixture output matches the golden master.
- Expects the initial test to fail (kata starting point behavior).

## Contributing a New Language

1. Create a new directory named after the language (e.g., `mylang/`).
2. Follow the standard structure: production code, failing test, texttest fixture.
3. Add a `README.md` with setup instructions.
4. Update `texttests/config.gr` to include the new language.
5. See `CONTRIBUTING.md` for full guidelines and translation instructions.

## Important Files Reference

| File | Purpose |
|---|---|
| `GildedRoseRequirements.md` | Source of truth for business rules |
| `texttests/ThirtyDays/stdout.gr` | Golden master for approval testing |
| `texttests/config.gr` | TextTest active language configuration |
| `.gitignore` | Ignores `bin/`, `obj/`, `vendor/`, `venv/`, `**/*.received.*`, `.idea/` |
| `.github/pull_request_template.md` | PR checklist (must acknowledge no solution) |

## What NOT to Do

- Do not commit solutions to the kata (business logic answers).
- Do not modify the `Item` class in any language implementation.
- Do not push to `main` or `master` directly.
- Do not skip the PR acknowledgment checkbox.
- Do not change the golden master (`stdout.gr`) unless you are intentionally updating expected behavior.
