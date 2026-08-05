# IBM AS/400 (IBM i)

## Overview

The IBM AS/400, launched in 1988 and now known as IBM i, is a mid-range computing platform with a uniquely integrated architecture. It combines hardware, operating system, database, and security into a single unified system.

## Architecture

The AS/400 uses Technology Independent Machine Interface (TIMI) that separates applications from underlying hardware. Programs compile to an intermediate representation that runs on any compatible processor without recompilation.

## RPG Programming Language

RPG (Report Program Generator) is the primary language for AS/400 development. Modern RPG IV uses free-format syntax similar to other languages while maintaining access to the platform's integrated capabilities.

```rpgle
Dcl-S CustName Char(50);
Dcl-S Balance  Packed(9:2);

Exec SQL
  SELECT CUSTOMER_NAME, BALANCE
  INTO :CustName, :Balance
  FROM CUSTOMERS
  WHERE ID = :CustID;
```

## CL Command Language

CL provides system management and control flow capabilities. It interacts directly with the operating system for user management, job control, and system configuration.

## ILE (Integrated Language Environment)

ILE enables modular programming by binding multiple program objects together. It supports binding directories, service programs, and activation groups for efficient resource sharing.

## DB2 for i

The integrated DB2 database requires no separate installation or licensing. It supports SQL, record-level access, and automatic indexing with minimal administration overhead.

## Modernization

IBM i supports modern development through Node.js, Python, Java, PHP, and open-source databases alongside traditional RPG and COBOL applications.
