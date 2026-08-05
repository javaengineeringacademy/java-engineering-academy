# Subversion (SVN)

## Overview

Apache Subversion is a centralized version control system designed as a successor to CVS. Released in 2000, it became the dominant version control system before distributed systems like Git gained popularity.

## Repository Structure

SVN repositories use a directory-based model with trunk, branches, and tags conventions. The trunk contains main development, branches isolate feature work, and tags mark release points.

```
repository/
  trunk/
    src/
    pom.xml
  branches/
    feature-x/
    release-2.0/
  tags/
    v1.0/
    v1.1/
```

## Working Copy Model

SVN creates working copies that track repository state through .svn directories. Updates and commits communicate with the central server to synchronize changes.

## Branching and Merging

SVN supports cheap directory copies for branching. Merge tracking records revision history to facilitate merging branches back to trunk, though complex merges can be challenging.

## Atomic Commits

SVN guarantees atomic commits where all file changes in a transaction either succeed or fail together. This prevents partial commits that could leave the repository in an inconsistent state.

## Revision History

Every commit creates a global revision number that applies to the entire repository. This simplifies referencing changes and comparing repository states across time.

## Migration to Git

Migrating from SVN to Git involves using tools like svn2git or git-svn to preserve history. The transition requires training teams on distributed workflows and adapting branching strategies.

## Continued Usage

Some organizations maintain SVN repositories for legacy projects or due to regulatory requirements. VisualSVN Server and TortoiseSVN provide user-friendly management interfaces.
