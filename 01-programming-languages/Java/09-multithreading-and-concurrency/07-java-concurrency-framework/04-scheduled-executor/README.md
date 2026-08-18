# Scheduled Executor

## Overview

`ScheduledExecutorService` executes tasks after a delay or periodically. Preferred over `Timer` for production use.

## Key Methods

| Method | Description |
|--------|-------------|
| schedule(task, delay, unit) | Execute once after delay |
| scheduleAtFixedRate(task, initial, period, unit) | Execute at fixed rate |
| scheduleWithFixedDelay(task, initial, delay, unit) | Execute with fixed delay between end and start |
