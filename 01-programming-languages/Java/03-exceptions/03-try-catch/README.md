# 03 - Try-Catch Exception Handling

> "The only way to avoid mistakes is to do nothing. That's the biggest mistake of all."

## Parts

- [Part 1: Why, try, catch, execution flow, single/multiple catch](README-Part1.md)
- [Part 2: Multi-catch deep dive, nested try-catch](README-Part2.md)
- [Part 3: Rethrowing exceptions, common mistakes, best practices](README-Part3.md)
- [Part 4: Production examples, summary, key takeaways](README-Part4.md)

## Engineering Story

### "The Silent Payment"

At a fintech startup, the payment processing service looked clean on paper. Every API call wrapped its core logic in a try-catch block. If something went wrong during a transaction, the exception was caught, logged to a local file, and the method returned an HTTP 200 with a success payload. The code review had approved it. The tests passed. It shipped on a Friday.

By Monday, the support queue was overflowing. Customers were reporting duplicate charges. One user had been billed fourteen times for a single purchase. The finance team discovered that the payment gateway was confirming transactions successfully, but the internal ledger writes were failing due to a constraint violation on a newly added column. The catch block caught the exception, wrote it to a log file nobody was monitoring, and returned success to the client. The client, trusting the 200 response, retried the call. Each retry created another charge on the gateway side while the ledger continued to reject the write.

The root cause was three lines of code. The catch block swallowed the exception without rethrowing it. The API returned success because the exception never propagated upward. The retry logic on the client side assumed success meant the transaction was fully recorded. The team spent the next week manually reconciling accounts, issuing refunds, and rebuilding trust with affected customers. Two engineers were reassigned to build a payment reconciliation pipeline that ran every fifteen minutes as a safety net.

The fix was straightforward but painful to admit: the catch block needed to rethrow the exception or propagate a failure signal. The API needed to return a 500 or 503 so the client knew something went wrong. The retry logic needed an idempotency key so retries would not create duplicate charges. But the real lesson was about exception swallowing. A catch block that catches and does nothing is a lie. It tells the caller that everything is fine when it is not. In a financial system, that lie compounds with every request. Silent failures do not stay silent. They accumulate, and the longer they accumulate, the harder and more expensive the remediation becomes. Never catch an exception in a financial system without either handling it fully or letting it propagate. The cost of a visible failure is always less than the cost of a hidden one.

| Version | Change |
|---------|--------|
| JDK 1.0 | `try-catch` introduced with basic exception handling |
| JDK 1.2 | Exception chaining improved error context preservation |
| JDK 7 | Multi-catch syntax (`catch (A | B e)`) introduced |
| JDK 7 | Precise rethrow — catch and rethrow without declaring caught type |
| JDK 11 | Effectively final variables in try-with-resources |
