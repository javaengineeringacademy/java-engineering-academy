import sqlite3
from contextlib import contextmanager

@contextmanager
def get_connection(db_path=':memory:'):
    conn = sqlite3.connect(db_path)
    conn.row_factory = sqlite3.Row
    try:
        yield conn
        conn.commit()
    except Exception:
        conn.rollback()
        raise
    finally:
        conn.close()

def create_table(conn):
    conn.execute('''
        CREATE TABLE IF NOT EXISTS users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            email TEXT UNIQUE NOT NULL,
            age INTEGER
        )
    ''')

def insert_user(conn, name, email, age):
    conn.execute(
        'INSERT INTO users (name, email, age) VALUES (?, ?, ?)',
        (name, email, age)
    )

def get_all_users(conn):
    return conn.execute('SELECT * FROM users').fetchall()

def get_user_by_id(conn, user_id):
    return conn.execute('SELECT * FROM users WHERE id = ?', (user_id,)).fetchone()

def update_user_age(conn, user_id, new_age):
    conn.execute('UPDATE users SET age = ? WHERE id = ?', (new_age, user_id))

def delete_user(conn, user_id):
    conn.execute('DELETE FROM users WHERE id = ?', (user_id,))

if __name__ == "__main__":
    with get_connection() as conn:
        create_table(conn)
        insert_user(conn, 'Alice', 'alice@example.com', 30)
        insert_user(conn, 'Bob', 'bob@example.com', 25)
        print("All users:", get_all_users(conn))
        update_user_age(conn, 1, 31)
        print("Updated user:", get_user_by_id(conn, 1))
        delete_user(conn, 2)
        print("After deletion:", get_all_users(conn))
