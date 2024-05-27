# Development

- Java 21
- Spring Boot 3.3.0

# Commit Message

Commit message could help reviewers better understand what is the purpose of submitted PR. It could help accelerate the code review procedure as well. We encourage contributors to use **EXPLICIT** commit message rather than ambiguous message. In general, we advocate the following commit message type:
- Features: commit message start with `feat`, For example: "feat: add user authentication module"
- Bug Fixes: commit message start with `fix`, For example: "fix: resolve null pointer exception in user service"
- Documentation, commit message start with `doc`, For example: "doc: update API documentation for user endpoints"
- Performance: commit message start with `perf`, For example: "perf: improve the performance of user service"
- Refactor: commit message start with `refactor`, For example: "refactor: refactor user service to improve code readability"
- Test: commit message start with `test`, For example: "test: add unit test for user service"
- Chore: commit message start with `chore`, For example: "chore: update dependencies in pom.xml"
- Style: commit message start with `style`, For example: "style: format the code in user service"
- Revert: commit message start with `revert`, For example: "revert: revert the changes in user service"

# GitHub Workflow

Developers work in their own forked copy of the repository and when ready,
submit pull requests to have their changes considered and merged into the
project's repository.

1. Fork your own copy of the repository to your GitHub account by clicking on
   `Fork` button on [quicksense-backend's GitHub repository](https://github.com/cr7258/quicksense-backend).
2. Clone the forked repository on your local setup.

    ```bash
    git clone https://github.com/$user/quicksense-backend
    ```

   Add a remote upstream to track upstream `quicksense-backend` repository.

    ```bash
    git remote add upstream https://github.com/cr7258/quicksense-backend
    ```

3. Create a topic branch.

    ```bash
    git checkout -b <branch-name>
    ```

4. Make changes and commit it locally.

    ```bash
    git add <modifiedFile>
    git commit
    ```

5. Push local branch to your forked repository.

    ```bash
    git push
    ```

6. Create a Pull request on GitHub.
   Visit your fork at `https://github.com/cr7258/quicksense-backend` and click
   `Compare & Pull Request` button next to your `<branch-name>`.

   
# Keeping branch in sync with upstream.

Click `Sync fork` button on your forked repository to keep your forked repository in sync with the upstream repository. 

If you have already created a branch and want to keep it in sync with the upstream repository, follow the below steps:

```bash
git checkout <branch-name>
git fetch upstream
git rebase upstream/main
```

# Release

Create a new tag from the main branch to release a new version of the project.

```bash
git checkout main
git tag -a <tag-name> -m "Release version xxxxx"
git push origin <tag-name>
```