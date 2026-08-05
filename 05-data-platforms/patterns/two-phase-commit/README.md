# Two-Phase Commit Pattern

## Overview

Two-Phase Commit (2PC) is a distributed transaction protocol that ensures atomicity across multiple databases or services. It uses a coordinator to manage prepare and commit phases, ensuring that all participants either commit or rollback together.

Phase one (prepare) asks all participants if they can commit. Phase two (commit or rollback) executes the decision based on all participants agreeing. This guarantees ACID properties across distributed systems at the cost of blocking and reduced availability.

## When to Use

- Strong ACID guarantees are required across multiple databases
- Financial systems demand strict transactional consistency
- Data cannot tolerate eventual consistency
- Two or more distinct data stores must update atomically
- Regulatory requirements mandate immediate consistency

## Implementation

### TypeScript

```typescript
class TwoPhaseCommitCoordinator {
  private participants: TransactionParticipant[] = [];

  addParticipant(participant: TransactionParticipant): void {
    this.participants.push(participant);
  }

  async execute(transaction: DistributedTransaction): Promise<boolean> {
    const prepared: TransactionParticipant[] = [];

    try {
      for (const participant of this.participants) {
        await participant.prepare(transaction.id);
        prepared.push(participant);
      }

      for (const participant of prepared) {
        await participant.commit(transaction.id);
      }
      return true;
    } catch (error) {
      for (const participant of prepared) {
        await participant.rollback(transaction.id);
      }
      return false;
    }
  }
}

class TransactionParticipant {
  async prepare(transactionId: string): Promise<void> {
    const tx = this.db.beginTransaction();
    try {
      await this.executePending(transactionId);
      this.preparedTransactions.set(transactionId, tx);
    } catch (error) {
      await tx.rollback();
      throw new Error('Prepare failed');
    }
  }

  async commit(transactionId: string): Promise<void> {
    const tx = this.preparedTransactions.get(transactionId);
    if (tx) {
      await tx.commit();
      this.preparedTransactions.delete(transactionId);
    }
  }

  async rollback(transactionId: string): Promise<void> {
    const tx = this.preparedTransactions.get(transactionId);
    if (tx) {
      await tx.rollback();
      this.preparedTransactions.delete(transactionId);
    }
  }
}
```

### Java

```java
public class TwoPhaseCommitCoordinator {
    private final List<Participant> participants = new ArrayList<>();

    public void addParticipant(Participant participant) {
        participants.add(participant);
    }

    public boolean execute(Transaction transaction) {
        List<Participant> prepared = new ArrayList<>();
        try {
            for (Participant p : participants) {
                p.prepare(transaction.getId());
                prepared.add(p);
            }
            for (Participant p : prepared) {
                p.commit(transaction.getId());
            }
            return true;
        } catch (Exception e) {
            for (Participant p : prepared) {
                p.rollback(transaction.getId());
            }
            return false;
        }
    }
}

public interface Participant {
    void prepare(String transactionId) throws Exception;
    void commit(String transactionId);
    void rollback(String transactionId);
}
```

### Python

```python
from typing import List
import uuid

class TwoPhaseCommitCoordinator:
    def __init__(self):
        self.participants: List['Participant'] = []

    def add_participant(self, participant: 'Participant'):
        self.participants.append(participant)

    def execute(self) -> bool:
        transaction_id = str(uuid.uuid4())
        prepared = []
        try:
            for participant in self.participants:
                participant.prepare(transaction_id)
                prepared.append(participant)

            for participant in prepared:
                participant.commit(transaction_id)
            return True
        except Exception as e:
            for participant in reversed(prepared):
                participant.rollback(transaction_id)
            return False

class Participant:
    def __init__(self, name: str, db):
        self.name = name
        self.db = db
        self.pending_transactions = {}

    def prepare(self, transaction_id: str):
        tx = self.db.begin_transaction()
        self.pending_transactions[transaction_id] = tx

    def commit(self, transaction_id: str):
        tx = self.pending_transactions.pop(transaction_id, None)
        if tx:
            tx.commit()

    def rollback(self, transaction_id: str):
        tx = self.pending_transactions.pop(transaction_id, None)
        if tx:
            tx.rollback()
```

### C\#

```csharp
public interface IParticipant {
    Task PrepareAsync(string transactionId);
    Task CommitAsync(string transactionId);
    Task RollbackAsync(string transactionId);
}

public class TwoPhaseCommitCoordinator {
    private readonly List<IParticipant> _participants = new();

    public void AddParticipant(IParticipant participant) =>
        _participants.Add(participant);

    public async Task<bool> ExecuteAsync(Func<Task> transactionalWork) {
        var prepared = new List<IParticipant>();
        var transactionId = Guid.NewGuid().ToString();

        try {
            foreach (var participant in _participants) {
                await participant.PrepareAsync(transactionId);
                prepared.Add(participant);
            }

            await transactionalWork();

            foreach (var participant in prepared) {
                await participant.CommitAsync(transactionId);
            }
            return true;
        } catch {
            foreach (var participant in prepared) {
                await participant.RollbackAsync(transactionId);
            }
            return false;
        }
    }
}
```

## Best Practices

- Prefer Saga Pattern over 2PC for microservice architectures
- Implement timeout handling for unresponsive participants
- Use logging and recovery mechanisms for coordinator failures
- Consider 3PC (Three-Phase Commit) for improved fault tolerance
- Monitor lock holding duration to detect blocking issues
- Have a recovery procedure for coordinator crashes during commit phase

## Interview Questions

1. What are the two phases of Two-Phase Commit?
2. What happens if the coordinator crashes between phases?
3. How does 2PC compare to the Saga Pattern?
4. What is the blocking problem in 2PC?
5. When is 2PC still the right choice despite its drawbacks?

## References

- Gray, Jim. *Notes on Database Operating Systems* (1979)
- Lampson, Butler. *Atomic Transactions*
- Kleppmann, Martin. *Designing Data-Intensive Applications*
- Oracle. *Two-Phase Commit Protocol*
