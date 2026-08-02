# Quality Guidelines

## Coding Style

- **Java 21** syntax required; no deprecated features without explanation
- **Google Java Style** for formatting (4-space indent, proper naming)
- Meaningful variable and method names
- Avoid magic numbers; use constants
- Use `var` where type is obvious
- Use records for immutable data carriers
- Use sealed classes for restricted hierarchies

## Documentation

- Each topic must follow the [OpenCode Master Instructions](OPENCODE_MASTER_INSTRUCTIONS.md)
- Write in active voice, clear language
- Provide citations for facts about Java (JLS, Oracle docs, etc.)
- No placeholder content; every section must have real content
- No TODO/FIXME in production documentation

## Examples

- All code examples **must compile** on Java 21
- Use `@code` formatting for code; include explanatory comments
- Each example must include:
  - Problem statement
  - Complete code with class and main method
  - Inline comments explaining each step
  - Expected output
  - Enterprise usage snippet

## Diagrams

- Only use **GitHub-compatible Mermaid** syntax
- Validate with Mermaid Live Editor before commit
- No raw image files unless necessary; prefer text-based diagrams
- Use class diagrams for OOP relationships
- Use flowcharts for decision trees
- Use sequence diagrams for process flows

## Markdown

- Use headings in order (`#` for title, `##` for sections)
- Check all links (relative and absolute) before commit
- Use tables for comparisons
- Use code fences with language specification
- No broken links; validate with link checker

## Testing

- All Java examples must compile without errors
- All Mermaid diagrams must render on GitHub
- All internal links must resolve correctly
- No spelling or grammar errors

## Review Process

1. Self-review before commit
2. Check compilation of all code examples
3. Verify Mermaid rendering
4. Validate all links
5. Peer review for technical accuracy

## Definition of Done

- [ ] All code compiles on Java 21
- [ ] All diagrams render correctly
- [ ] Documentation passes spellcheck
- [ ] All links are valid
- [ ] Content answers: What? Why? How? When? When Not?
- [ ] Performance considerations discussed
- [ ] Enterprise examples provided
- [ ] Interview questions included
- [ ] Exercises with solutions provided
