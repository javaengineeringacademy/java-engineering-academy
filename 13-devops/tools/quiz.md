# Git Quiz

## Question 1
What is the difference between `git merge` and `git rebase`?
- A) They are identical operations
- B) `merge` preserves the full branch history with a merge commit, `rebase` replays commits to create a linear history
- C) `rebase` is always preferred over `merge`
- D) `merge` deletes the source branch, `rebase` does not

**Answer: B**
**Explanation:** `git merge` creates a merge commit preserving the full branching history. `git rebase` replays commits on top of another branch, creating a linear history. Rebase gives cleaner history but rewrites commit hashes.

## Question 2
What does `git stash` do?
- A) Permanently deletes uncommitted changes
- B) Temporarily stores uncommitted changes so you can work on a clean working directory
- C) Creates a new branch with your changes
- D) Pushes changes to the remote repository

**Answer: B**
**Explanation:** `git stash` saves your modified tracked files and reverts the working directory to match the HEAD commit. Use `git stash pop` to restore the stashed changes later.

## Question 3
What is a detached HEAD state in Git?
- A) When the repository is corrupted
- B) When HEAD points directly to a commit instead of a branch name
- C) When you are on the main branch
- D) When there are merge conflicts

**Answer: B**
**Explanation:** A detached HEAD occurs when you checkout a specific commit, tag, or remote branch. HEAD points to the commit directly rather than a branch reference. Changes made in this state are not automatically associated with any branch.

## Question 4
What is the purpose of `git bisect`?
- A) To split a repository into multiple parts
- B) To perform a binary search through commit history to find the exact commit that introduced a bug
- C) To merge two branches together
- D) To create a backup of the repository

**Answer: B**
**Explanation:** `git bisect` uses binary search to efficiently find the commit that introduced a bug. You mark a known bad commit and a known good commit, and Git checks out middle commits for you to test.

## Question 5
What is the difference between `git fetch` and `git pull`?
- A) They do the same thing
- B) `fetch` downloads changes without merging, `fetch` + `merge` equals `pull`
- C) `pull` is faster than `fetch`
- D) `fetch` requires network access, `pull` does not

**Answer: B**
**Explanation:** `git fetch` downloads new data from the remote but doesn't integrate it into your working files. `git pull` is equivalent to `git fetch` followed by `git merge`, automatically merging remote changes.