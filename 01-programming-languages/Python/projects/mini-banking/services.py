"""Banking service layer with business logic."""

from typing import Optional, Tuple
from models import Account, Transaction
from storage import Storage


class BankingError(Exception):
    """Custom exception for banking operations."""
    pass


class BankingService:
    """Handles all banking operations."""
    
    def __init__(self, storage: Storage):
        """Initialize with storage backend."""
        self.storage = storage
    
    def create_account(self, holder_name: str, initial_deposit: float = 0.0) -> Account:
        """Create a new account with optional initial deposit."""
        if not holder_name or not holder_name.strip():
            raise BankingError("Account holder name cannot be empty")
        
        if initial_deposit < 0:
            raise BankingError("Initial deposit cannot be negative")
        
        account = Account(holder_name=holder_name.strip(), balance=initial_deposit)
        
        if initial_deposit > 0:
            transaction = Transaction(
                transaction_type="deposit",
                amount=initial_deposit,
                description="Initial deposit"
            )
            account.add_transaction(transaction)
        
        self.storage.save_account(account)
        return account
    
    def get_account(self, account_number: str) -> Optional[Account]:
        """Retrieve account by account number."""
        return self.storage.load_account(account_number)
    
    def deposit(self, account_number: str, amount: float) -> Tuple[Account, Transaction]:
        """Deposit funds to an account."""
        if amount <= 0:
            raise BankingError("Deposit amount must be positive")
        
        account = self.storage.load_account(account_number)
        if not account:
            raise BankingError(f"Account {account_number} not found")
        
        account.balance += amount
        transaction = Transaction(
            transaction_type="deposit",
            amount=amount,
            description=f"Deposit of ${amount:.2f}"
        )
        account.add_transaction(transaction)
        self.storage.save_account(account)
        
        return account, transaction
    
    def withdraw(self, account_number: str, amount: float) -> Tuple[Account, Transaction]:
        """Withdraw funds from an account."""
        if amount <= 0:
            raise BankingError("Withdrawal amount must be positive")
        
        account = self.storage.load_account(account_number)
        if not account:
            raise BankingError(f"Account {account_number} not found")
        
        if account.balance < amount:
            raise BankingError("Insufficient funds")
        
        account.balance -= amount
        transaction = Transaction(
            transaction_type="withdraw",
            amount=amount,
            description=f"Withdrawal of ${amount:.2f}"
        )
        account.add_transaction(transaction)
        self.storage.save_account(account)
        
        return account, transaction
    
    def transfer(self, from_number: str, to_number: str, amount: float) -> Tuple[Transaction, Transaction]:
        """Transfer funds between accounts."""
        if amount <= 0:
            raise BankingError("Transfer amount must be positive")
        
        if from_number == to_number:
            raise BankingError("Cannot transfer to the same account")
        
        from_account = self.storage.load_account(from_number)
        if not from_account:
            raise BankingError(f"Source account {from_number} not found")
        
        to_account = self.storage.load_account(to_number)
        if not to_account:
            raise BankingError(f"Destination account {to_number} not found")
        
        if from_account.balance < amount:
            raise BankingError("Insufficient funds for transfer")
        
        from_account.balance -= amount
        to_account.balance += amount
        
        withdraw_txn = Transaction(
            transaction_type="transfer_out",
            amount=amount,
            description=f"Transfer to {to_number}"
        )
        deposit_txn = Transaction(
            transaction_type="transfer_in",
            amount=amount,
            description=f"Transfer from {from_number}"
        )
        
        from_account.add_transaction(withdraw_txn)
        to_account.add_transaction(deposit_txn)
        
        self.storage.save_account(from_account)
        self.storage.save_account(to_account)
        
        return withdraw_txn, deposit_txn
