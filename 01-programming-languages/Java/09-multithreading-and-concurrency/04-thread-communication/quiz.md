# Thread Communication Quiz

## Question 1
Why must `wait()` be called inside a `synchronized` block?

- A) It's a syntax requirement
- B) To prevent missed notifications (race condition)
- C) For performance
- D) It doesn't need to be

**Answer: B**
Without synchronization, a notification could be sent before the thread starts waiting, causing it to be missed.

## Question 2
What happens if you call `notify()` when no threads are waiting?

- A) Exception thrown
- B) Notification is lost (no effect)
- C) The notification is stored
- D) All threads are notified

**Answer: B**
The notification is simply lost. This is why `notifyAll()` is safer — it doesn't have this problem as often.
