"""Unit tests for the Mini Banking System."""

import pytest
import tempfile
import shutil
from services import BankingService, BankingError
from storage import Storage
from models import Account, Transaction


@pytest.fixture
def temp_storage():
    """Create temporary storage for testing."""
    temp_dir = tempfile.mkdtemp()
    storage = Storage(temp_dir)
    yield storage
    shutil.rmtree(temp_dir)


@pytest.fixture
def service(temp_storage):
    """Create banking service for testing."""
    return BankingService(temp_storage)


class TestAccountModel:
    """Tests for Account data model."""
    
    def test_account_creation(self):
        """Test account initializes with correct defaults."""
        account = Account(holder_name="John Doe")
        assert account.holder_name == "John Doe"
        assert account.balance == 0.0
        assert account.account_number.startswith("ACC-")
        assert len(account.transactions) == 0
    
    def test_account_to_dict(self):
        """Test account serialization to dictionary."""
        account = Account(holder_name="Jane", balance=100.0)
        data = account.to_dict()
        assert data["holder_name"] == "Jane"
        assert data["balance"] == 100.0
    
    def test_account_from_dict(self):
        """Test account deserialization from dictionary."""
        data = {
            "account_number": "ACC-TEST123",
            "holder_name": "Test User",
            "balance": 500.0,
            "transactions": [],
            "created_at": "2024-01-01"
        }
        account = Account.from_dict(data)
        assert account.account_number == "ACC-TEST123"
        assert account.balance == 500.0


class TestTransactionModel:
    """Tests for Transaction data model."""
    
    def test_transaction_creation(self):
        """Test transaction initializes correctly."""
        txn = Transaction(transaction_type="deposit", amount=100.0)
        assert txn.transaction_type == "deposit"
        assert txn.amount == 100.0
        assert len(txn.transaction_id) == 8
    
    def test_transaction_to_dict(self):
        """Test transaction serialization."""
        txn = Transaction(transaction_type="withdraw", amount=50.0)
        data = txn.to_dict()
        assert data["transaction_type"] == "withdraw"
        assert data["amount"] == 50.0


class TestBankingService:
    """Tests for banking service operations."""
    
    def test_create_account(self, service):
        """Test account creation."""
        account = service.create_account("Alice", 100.0)
        assert account.holder_name == "Alice"
        assert account.balance == 100.0
        assert len(account.transactions) == 1
    
    def test_create_account_empty_name(self, service):
        """Test account creation with empty name raises error."""
        with pytest.raises(BankingError):
            service.create_account("")
    
    def test_create_account_negative_deposit(self, service):
        """Test account creation with negative deposit raises error."""
        with pytest.raises(BankingError):
            service.create_account("Bob", -100.0)
    
    def test_deposit(self, service):
        """Test deposit operation."""
        account = service.create_account("Charlie")
        updated, txn = service.deposit(account.account_number, 250.0)
        assert updated.balance == 250.0
        assert txn.transaction_type == "deposit"
    
    def test_deposit_negative_amount(self, service):
        """Test deposit with negative amount raises error."""
        account = service.create_account("Dave")
        with pytest.raises(BankingError):
            service.deposit(account.account_number, -50.0)
    
    def test_deposit_nonexistent_account(self, service):
        """Test deposit to nonexistent account raises error."""
        with pytest.raises(BankingError):
            service.deposit("ACC-FAKE", 100.0)
    
    def test_withdraw(self, service):
        """Test withdrawal operation."""
        account = service.create_account("Eve", 500.0)
        updated, txn = service.withdraw(account.account_number, 200.0)
        assert updated.balance == 300.0
        assert txn.transaction_type == "withdraw"
    
    def test_withdraw_insufficient_funds(self, service):
        """Test withdrawal with insufficient funds raises error."""
        account = service.create_account("Frank", 100.0)
        with pytest.raises(BankingError):
            service.withdraw(account.account_number, 200.0)
    
    def test_transfer(self, service):
        """Test transfer operation."""
        acc1 = service.create_account("Grace", 1000.0)
        acc2 = service.create_account("Henry")
        
        out_txn, in_txn = service.transfer(acc1.account_number, acc2.account_number, 300.0)
        
        updated1 = service.get_account(acc1.account_number)
        updated2 = service.get_account(acc2.account_number)
        
        assert updated1.balance == 700.0
        assert updated2.balance == 300.0
    
    def test_transfer_same_account(self, service):
        """Test transfer to same account raises error."""
        account = service.create_account("Ivy", 100.0)
        with pytest.raises(BankingError):
            service.transfer(account.account_number, account.account_number, 50.0)


class TestStorage:
    """Tests for storage operations."""
    
    def test_save_and_load_account(self, temp_storage):
        """Test saving and loading accounts."""
        account = Account(holder_name="Test", balance=100.0)
        temp_storage.save_account(account)
        
        loaded = temp_storage.load_account(account.account_number)
        assert loaded is not None
        assert loaded.holder_name == "Test"
        assert loaded.balance == 100.0
    
    def test_list_accounts(self, temp_storage):
        """Test listing all accounts."""
        acc1 = Account(holder_name="One")
        acc2 = Account(holder_name="Two")
        temp_storage.save_account(acc1)
        temp_storage.save_account(acc2)
        
        accounts = temp_storage.list_accounts()
        assert len(accounts) == 2
    
    def test_delete_account(self, temp_storage):
        """Test account deletion."""
        account = Account(holder_name="Delete Me")
        temp_storage.save_account(account)
        
        assert temp_storage.delete_account(account.account_number)
        assert temp_storage.load_account(account.account_number) is None
    
    def test_account_exists(self, temp_storage):
        """Test account existence check."""
        account = Account(holder_name="Exist")
        temp_storage.save_account(account)
        
        assert temp_storage.account_exists(account.account_number)
        assert not temp_storage.account_exists("ACC-NONEXISTENT")


if __name__ == "__main__":
    pytest.main([__file__, "-v"])
