"""CLI interface for the Mini Banking System."""

from services import BankingService, BankingError
from storage import Storage


def print_menu():
    """Display the main menu."""
    print("\n" + "=" * 40)
    print("       MINI BANKING SYSTEM")
    print("=" * 40)
    print("1. Create Account")
    print("2. Deposit")
    print("3. Withdraw")
    print("4. Transfer")
    print("5. View Account")
    print("6. List Accounts")
    print("7. Exit")
    print("=" * 40)


def create_account(service: BankingService):
    """Handle account creation."""
    name = input("Enter account holder name: ")
    try:
        initial = float(input("Enter initial deposit (0 for none): $"))
    except ValueError:
        initial = 0.0
    
    try:
        account = service.create_account(name, initial)
        print(f"\nAccount created successfully!")
        print(f"Account Number: {account.account_number}")
        print(f"Balance: ${account.balance:.2f}")
    except BankingError as e:
        print(f"Error: {e}")


def deposit(service: BankingService):
    """Handle deposit operation."""
    account_num = input("Enter account number: ")
    try:
        amount = float(input("Enter deposit amount: $"))
    except ValueError:
        print("Invalid amount")
        return
    
    try:
        account, txn = service.deposit(account_num, amount)
        print(f"\nDeposit successful!")
        print(f"New balance: ${account.balance:.2f}")
        print(f"Transaction ID: {txn.transaction_id}")
    except BankingError as e:
        print(f"Error: {e}")


def withdraw(service: BankingService):
    """Handle withdrawal operation."""
    account_num = input("Enter account number: ")
    try:
        amount = float(input("Enter withdrawal amount: $"))
    except ValueError:
        print("Invalid amount")
        return
    
    try:
        account, txn = service.withdraw(account_num, amount)
        print(f"\nWithdrawal successful!")
        print(f"New balance: ${account.balance:.2f}")
        print(f"Transaction ID: {txn.transaction_id}")
    except BankingError as e:
        print(f"Error: {e}")


def transfer(service: BankingService):
    """Handle transfer operation."""
    from_acc = input("Enter source account number: ")
    to_acc = input("Enter destination account number: ")
    try:
        amount = float(input("Enter transfer amount: $"))
    except ValueError:
        print("Invalid amount")
        return
    
    try:
        out_txn, in_txn = service.transfer(from_acc, to_acc, amount)
        print(f"\nTransfer successful!")
        print(f"Transaction ID: {out_txn.transaction_id}")
    except BankingError as e:
        print(f"Error: {e}")


def view_account(service: BankingService):
    """View account details and history."""
    account_num = input("Enter account number: ")
    account = service.get_account(account_num)
    
    if not account:
        print("Account not found")
        return
    
    print(f"\nAccount: {account.account_number}")
    print(f"Holder: {account.holder_name}")
    print(f"Balance: ${account.balance:.2f}")
    print(f"Created: {account.created_at}")
    
    if account.transactions:
        print("\nTransaction History:")
        print("-" * 50)
        for txn in account.transactions:
            print(f"  {txn.timestamp[:10]} | {txn.transaction_type:12} | ${txn.amount:>10.2f}")
    else:
        print("\nNo transactions yet.")


def list_accounts(service: BankingService):
    """List all accounts."""
    accounts = service.storage.list_accounts()
    if not accounts:
        print("No accounts found.")
        return
    
    print("\nAll Accounts:")
    print("-" * 50)
    for acc_num in accounts:
        account = service.get_account(acc_num)
        print(f"  {acc_num} | {account.holder_name:20} | ${account.balance:>10.2f}")


def main():
    """Main entry point."""
    storage = Storage()
    service = BankingService(storage)
    
    while True:
        print_menu()
        choice = input("Select an option (1-7): ")
        
        if choice == "1":
            create_account(service)
        elif choice == "2":
            deposit(service)
        elif choice == "3":
            withdraw(service)
        elif choice == "4":
            transfer(service)
        elif choice == "5":
            view_account(service)
        elif choice == "6":
            list_accounts(service)
        elif choice == "7":
            print("Thank you for using Mini Banking System!")
            break
        else:
            print("Invalid option. Please try again.")


if __name__ == "__main__":
    main()
