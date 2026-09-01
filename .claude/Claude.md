# Guardrails & System Constraints
- NEVER run `git log`, `git show`, `git diff`, or examine commit history.
- Do not attempt to analyze past commits to find the context of code changes.
- Focus exclusively on the current, local state of the code files.
- If you need context on why code was written, ask the user directly instead of looking at git logs.


- Do not use `git worktree` or isolation environments that interact with git metadata.
- Disable automatic history checks before writing or proposing code fixes.
