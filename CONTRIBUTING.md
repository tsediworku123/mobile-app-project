# Contributing to Mobile App Project

Thank you for your interest in contributing to the Mobile App Project! We welcome contributions from everyone. This document provides guidelines and instructions for contributing.

## Getting Started

1. Fork the repository on GitHub
2. Clone your forked repository locally:
   ```bash
   git clone https://github.com/YOUR_USERNAME/mobile-app-project.git
   cd mobile-app-project
   ```
3. Add the upstream repository:
   ```bash
   git remote add upstream https://github.com/tsediworku123/mobile-app-project.git
   ```
4. Create a new branch for your feature:
   ```bash
   git checkout -b Haile/your-feature-name
   ```

## Development Setup

### Prerequisites
- Android Studio (latest version)
- JDK 11 or higher
- Kotlin 1.8+
- Gradle 7.0+

### Build Instructions
```bash
./gradlew build
./gradlew assembleDebug  # Build debug APK
./gradlew assembleRelease  # Build release APK
```

### Running Tests
```bash
./gradlew test
./gradlew testDebugUnitTest
```

## Code Style Guidelines

- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Keep functions small and focused
- Add documentation comments for public APIs
- Use proper naming conventions for branches, commits, and PRs

### Code Formatting
```bash
./gradlew ktlintFormat
```

## Commit Guidelines

- Use clear, descriptive commit messages
- Format: `<type>: <description>`
- Types: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`
- Example: `feat: Add user authentication service`

## Pull Request Process

1. **Update your branch** with latest changes from main:
   ```bash
   git fetch upstream
   git rebase upstream/main
   ```

2. **Push your changes**:
   ```bash
   git push origin Haile/your-feature-name
   ```

3. **Create a Pull Request**:
   - Provide a clear description of changes
   - Reference related issues with `Closes #issue_number`
   - Ensure all CI/CD checks pass
   - Request review from maintainers

4. **Address Review Comments**:
   - Make requested changes
   - Push additional commits if needed
   - Respond to all feedback

## Feature Development

### Creating a New Feature
1. Create a feature branch from `main`
2. Implement your feature with tests
3. Update relevant documentation
4. Submit a PR with detailed description

### Bug Fixes
1. Create an issue describing the bug
2. Create a branch for the fix
3. Include regression tests
4. Reference the issue in your PR

## Documentation

- Update README.md if adding new features
- Add inline code comments for complex logic
- Update ROADMAP.md with major changes
- Document new modules and utilities

## Testing

- Write unit tests for new features
- Aim for >80% code coverage
- Test edge cases and error scenarios
- Run full test suite before submitting PR

```bash
./gradlew test --info
./gradlew jacocoTestReport  # Generate coverage report
```

## Reporting Issues

When reporting bugs:
1. Use a descriptive title
2. Include steps to reproduce
3. Provide expected vs actual behavior
4. Include relevant logs or screenshots
5. Specify Android version and device type

## Feature Requests

- Describe the use case
- Explain the benefits
- Provide examples or mockups
- Discuss potential implementation

## Code of Conduct

- Be respectful and inclusive
- Provide constructive feedback
- Help others learn and grow
- Report violations to maintainers

## Licensing

By contributing, you agree that your contributions will be licensed under the same license as the project.

## Questions?

- Check existing issues and PRs
- Review documentation in ROADMAP.md
- Ask in PR comments
- Contact maintainers

---

Happy contributing! 🚀
