"""Data models for the banking system."""

from dataclasses import dataclass, field
from datetime import datetime
from typing import List
import uuid


@dataclass
class Transaction:
    """Represents a single banking transaction."""
    
    transaction_id: str = field(default_factory=lambda: str(uuid.uuid4())[:8])
    transaction_type: str = ""  # deposit, withdraw, transfer
    amount: float = 0.0
    timestamp: str = field(default_factory=lambda: datetime.now().isoformat())
    description: str = ""
    
    def to_dict(self) -> dict:
        """Convert transaction to dictionary."""
        return {
            "transaction_id": self.transaction_id,
            "transaction_type": self.transaction_type,
            "amount": self.amount,
            "timestamp": self.timestamp,
            "description": self.description
        }
    
    @classmethod
    def from_dict(cls, data: dict) -> "Transaction":
        """Create transaction from dictionary."""
        return cls(**data)


@dataclass
class Account:
    """Represents a bank account."""
    
    account_number: str = field(default_factory=lambda: f"ACC-{uuid.uuid4().hex[:8].upper()}")
    holder_name: str = ""
    balance: float = 0.0
    transactions: List[Transaction] = field(default_factory=list)
    created_at: str = field(default_factory=lambda: datetime.now().isoformat())
    
    def add_transaction(self, transaction: Transaction) -> None:
        """Add a transaction to account history."""
        self.transactions.append(transaction)
    
    def get_balance(self) -> float:
        """Get current balance."""
        return self.balance
    
    def to_dict(self) -> dict:
        """Convert account to dictionary."""
        return {
            "account_number": self.account_number,
            "holder_name": self.holder_name,
            "balance": self.balance,
            "transactions": [t.to_dict() for t in self.transactions],
            "created_at": self.created_at
        }
    
    @classmethod
    def from_dict(cls, data: dict) -> "Account":
        """Create account from dictionary."""
        transactions = [Transaction.from_dict(t) for t in data.get("transactions", [])]
        return cls(
            account_number=data["account_number"],
            holder_name=data["holder_name"],
            balance=data["balance"],
            transactions=transactions,
            created_at=data.get("created_at", "")
        )
