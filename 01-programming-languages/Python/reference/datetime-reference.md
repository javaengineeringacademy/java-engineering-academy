# Python datetime Reference

## What is datetime?

The datetime module provides classes for working with dates, times, and time intervals. It's essential for handling temporal data in Python.

## Why does datetime matter?

Understanding datetime helps you:
- Work with dates and times
- Perform date arithmetic
- Format and parse dates
- Handle timezones

---

## 1. date

```python
from datetime import date

# Create date
today = date.today()
print(today)  # 2024-01-01

d = date(2024, 1, 1)
print(d)  # 2024-01-01

# Properties
print(d.year)    # 2024
print(d.month)   # 1
print(d.day)     # 1
print(d.weekday())  # 0 (Monday)
print(d.isoweekday())  # 1 (Monday)

# Methods
print(d.replace(year=2025))  # 2025-01-01
print(d.timetuple())  # time.struct_time
```

---

## 2. time

```python
from datetime import time

# Create time
t = time(12, 30, 45)
print(t)  # 12:30:45

# Properties
print(t.hour)    # 12
print(t.minute)  # 30
print(t.second)  # 45

# Methods
print(t.replace(hour=13))  # 13:30:45
print(t.isoformat())  # 12:30:45
```

---

## 3. datetime

```python
from datetime import datetime

# Create datetime
now = datetime.now()
print(now)  # 2024-01-01 12:30:45.123456

dt = datetime(2024, 1, 1, 12, 30, 45)
print(dt)  # 2024-01-01 12:30:45

# Properties (combines date and time)
print(dt.year)    # 2024
print(dt.month)   # 1
print(dt.day)     # 1
print(dt.hour)    # 12
print(dt.minute)  # 30
print(dt.second)  # 45

# Methods
print(dt.date())  # 2024-01-01
print(dt.time())  # 12:30:45
print(dt.replace(year=2025))  # 2025-01-01 12:30:45
```

---

## 4. timedelta

```python
from datetime import datetime, timedelta

# Create timedelta
td = timedelta(days=1)
print(td)  # 1 day, 0:00:00

td = timedelta(hours=1, minutes=30)
print(td)  # 1:30:00

# Arithmetic
now = datetime.now()
tomorrow = now + timedelta(days=1)
yesterday = now - timedelta(days=1)

# Difference
diff = tomorrow - yesterday
print(diff)  # 2 days, 0:00:00
print(diff.total_seconds())  # 172800.0
```

---

## 5. Formatting

```python
from datetime import datetime

dt = datetime(2024, 1, 15, 12, 30, 45)

# strftime
print(dt.strftime('%Y-%m-%d'))  # 2024-01-15
print(dt.strftime('%H:%M:%S'))  # 12:30:45
print(dt.strftime('%Y-%m-%d %H:%M:%S'))  # 2024-01-15 12:30:45

# strptime
dt = datetime.strptime('2024-01-15', '%Y-%m-%d')
print(dt)  # 2024-01-15 00:00:00
```

---

## 6. Timezone

```python
from datetime import datetime, timezone, timedelta

# Create timezone-aware datetime
tz = timezone(timedelta(hours=5, minutes=30))
dt = datetime(2024, 1, 15, 12, 30, 45, tzinfo=tz)
print(dt)  # 2024-01-15 12:30:45+05:30

# UTC
utc_dt = datetime.now(timezone.utc)
print(utc_dt)

# Convert timezone
from datetime import timezone
est = timezone(timedelta(hours=-5))
dt_est = utc_dt.astimezone(est)
print(dt_est)
```

---

## One-Minute Revision Table

| Class | Description | Example |
|-------|-------------|---------|
| **date** | Date only | `date(2024, 1, 1)` |
| **time** | Time only | `time(12, 30, 45)` |
| **datetime** | Date and time | `datetime(2024, 1, 1, 12, 30)` |
| **timedelta** | Duration | `timedelta(days=1)` |
| **timezone** | Timezone info | `timezone(timedelta(hours=5))` |

---

## Common Mistakes

### 1. Naive vs Aware Datetimes

```python
# WRONG
dt = datetime(2024, 1, 1)  # Naive (no timezone)

# RIGHT
from datetime import timezone
dt = datetime(2024, 1, 1, tzinfo=timezone.utc)  # Aware
```

### 2. Timezone Conversions

```python
# WRONG
dt = datetime.now()  # Naive
dt_est = dt.astimezone(est)  # Error

# RIGHT
dt = datetime.now(timezone.utc)  # Aware
dt_est = dt.astimezone(est)
```

### 3. Forgetting strftime/strptime

```python
# WRONG
print(dt)  # 2024-01-01 12:30:45.123456

# RIGHT
print(dt.strftime('%Y-%m-%d'))  # 2024-01-01
```

---

## Production Notes

1. **Use timezone-aware datetimes** - Avoid ambiguity
2. **Store dates in UTC** - Convert to local time for display
3. **Use `isoformat()` for serialization** - Standard format
4. **Use `fromisoformat()` for parsing** - Handle ISO format
5. **Be careful with daylight saving time** - Use proper timezone libraries
6. **Use `pytz` or `dateutil` for complex timezone handling** - More robust
7. **Use `timedelta` for arithmetic** - Don't manipulate dates directly
8. **Use `replace()` to create modified copies** - Don't modify in place
9. **Document date formats** - Especially for user input
10. **Test edge cases** - Leap years, month boundaries, etc.

---

## Further Reading

- Python documentation on datetime module
- PEP 3101 - Advanced string formatting
- dateutil documentation
- pytz documentation
