"""JSON file-based storage for banking data."""

import json
import os
from typing import Optional
from models import Account


class Storage:
    """Handles persistence using JSON files."""
    
    def __init__(self, data_dir: str = "data"):
        """Initialize storage with data directory."""
        self.data_dir = data_dir
        self.accounts_file = os.path.join(data_dir, "accounts.json")
        self._ensure_directory()
        self._load_accounts()
    
    def _ensure_directory(self) -> None:
        """Create data directory if it doesn't exist."""
        if not os.path.exists(self.data_dir):
            os.makedirs(self.data_dir)
    
    def _load_accounts(self) -> None:
        """Load accounts from JSON file."""
        if os.path.exists(self.accounts_file):
            with open(self.accounts_file, "r") as f:
                self.accounts_data = json.load(f)
        else:
            self.accounts_data = {}
    
    def _save_accounts(self) -> None:
        """Save accounts to JSON file."""
        with open(self.accounts_file, "w") as f:
            json.dump(self.accounts_data, f, indent=2)
    
    def save_account(self, account: Account) -> None:
        """Save or update an account."""
        self.accounts_data[account.account_number] = account.to_dict()
        self._save_accounts()
    
    def load_account(self, account_number: str) -> Optional[Account]:
        """Load an account by account number."""
        if account_number in self.accounts_data:
            return Account.from_dict(self.accounts_data[account_number])
        return None
    
    def list_accounts(self) -> list:
        """List all account numbers."""
        return list(self.accounts_data.keys())
    
    def delete_account(self, account_number: str) -> bool:
        """Delete an account."""
        if account_number in self.accounts_data:
            del self.accounts_data[account_number]
            self._save_accounts()
            return True
        return False
    
    def account_exists(self, account_number: str) -> bool:
        """Check if account exists."""
        return account_number in self.accounts_data
