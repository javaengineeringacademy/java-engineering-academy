# Fork-Join Framework

## Overview

`ForkJoinPool` is designed for divide-and-conquer parallelism. Tasks split work into subtasks, fork them to the pool, then join results.

## Key Concepts

| Concept | Description |
|---------|-------------|
| Fork | Split task into subtasks |
| Join | Wait for subtask result |
| Work Stealing | Idle threads steal from busy threads |
| RecursiveTask | Fork-join task returning a value |
| RecursiveAction | Fork-join task with no return |
