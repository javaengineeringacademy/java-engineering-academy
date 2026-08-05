# Visual SourceSafe (VSS)

## Overview

Visual SourceSafe was Microsoft's entry-level version control system, commonly bundled with Visual Studio. Released in 1994, it targeted individual developers and small teams working on Windows applications.

## Repository Architecture

VSS used a file-based repository stored on a shared network drive. Each project maintained its own database with file versions, project history, and user information.

## Check-Out Model

VSS employed exclusive check-out where only one developer could modify a file at a time. This pessimistic locking prevented merge conflicts but limited concurrent development.

## Integration with Visual Studio

VSS integrated directly into Visual Studio IDE, providing version control commands within the development environment. This tight integration simplified adoption for Microsoft-centric development teams.

## Project Sharing

VSS supported project sharing where multiple projects could reference common files. Changes to shared files propagated to all projects containing references to those files.

## Limitations

VSS had serious reliability issues including repository corruption, limited scalability, no atomic commits, and poor support for distributed teams. Microsoft ended support in 2017.

## Migration to TFS/Azure DevOps

Microsoft recommended migrating from VSS to Team Foundation Server (now Azure DevOps) using the VSS Converter tool. The migration preserves history while providing modern version control capabilities.

## Modern Alternatives

Git with Azure DevOps or GitHub provides superior version control capabilities. Teams migrating from VSS should consider training on distributed version control concepts and workflows.
