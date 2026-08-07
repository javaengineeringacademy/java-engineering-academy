"""
SQLAlchemy Basic ORM
Demonstrates models, CRUD operations, relationships, and queries using SQLite
"""

from sqlalchemy import create_engine, Column, Integer, String, Float, ForeignKey, DateTime
from sqlalchemy.orm import declarative_base, relationship, Session, sessionmaker
from datetime import datetime

# ============================================
# Engine Setup (SQLite for demo)
# ============================================

engine = create_engine('sqlite:///demo.db', echo=False)
SessionLocal = sessionmaker(bind=engine)
Base = declarative_base()

# ============================================
# Models
# ============================================

class Author(Base):
    __tablename__ = 'authors'

    id = Column(Integer, primary_key=True)
    name = Column(String(100), nullable=False)
    email = Column(String(100), unique=True)

    # One-to-many: Author -> Books
    books = relationship('Book', back_populates='author', cascade='all, delete-orphan')

    def __repr__(self):
        return f'<Author(id={self.id}, name="{self.name}")>'

class Book(Base):
    __tablename__ = 'books'

    id = Column(Integer, primary_key=True)
    title = Column(String(200), nullable=False)
    price = Column(Float, default=0.0)
    author_id = Column(Integer, ForeignKey('authors.id'), nullable=False)

    # Many-to-one: Book -> Author
    author = relationship('Author', back_populates='books')

    def __repr__(self):
        return f'<Book(id={self.id}, title="{self.title}")>'

# Create tables
Base.metadata.create_all(engine)

# ============================================
# CRUD Operations
# ============================================

def create_operations():
    print("\n=== CREATE ===")

    with SessionLocal() as session:
        # Create authors
        author1 = Author(name='Alice Smith', email='alice@example.com')
        author2 = Author(name='Bob Jones', email='bob@example.com')

        # Create books
        book1 = Book(title='Python Basics', price=29.99, author=author1)
        book2 = Book(title='Advanced Python', price=49.99, author=author1)
        book3 = Book(title='Data Science 101', price=39.99, author=author2)

        session.add_all([author1, author2, book1, book2, book3])
        session.commit()

        print(f"Created authors: {author1}, {author2}")
        print(f"Created books: {book1}, {book2}, {book3}")

def read_operations():
    print("\n=== READ ===")

    with SessionLocal() as session:
        # Get by ID
        author = session.get(Author, 1)
        print(f"Get by ID: {author}")

        # Query with filter
        all_authors = session.query(Author).all()
        print(f"All authors: {all_authors}")

        # Filter
        expensive = session.query(Book).filter(Book.price > 35).all()
        print(f"Expensive books: {expensive}")

        # First match
        first = session.query(Author).filter_by(name='Alice Smith').first()
        print(f"First match: {first}")

        # Count
        count = session.query(Book).count()
        print(f"Total books: {count}")

        # Ordering
        ordered = session.query(Book).order_by(Book.price.desc()).all()
        print(f"Books by price desc: {[(b.title, b.price) for b in ordered]}")

def update_operations():
    print("\n=== UPDATE ===")

    with SessionLocal() as session:
        # Update a single record
        author = session.get(Author, 1)
        old_email = author.email
        author.email = 'alice.smith@example.com'
        session.commit()

        print(f"Updated email: {old_email} -> {author.email}")

        # Bulk update
        session.query(Book).filter(Book.price < 30).update({'price': 34.99})
        session.commit()

        cheap_books = session.query(Book).filter(Book.price < 40).all()
        print(f"Updated cheap books: {[(b.title, b.price) for b in cheap_books]}")

def delete_operations():
    print("\n=== DELETE ===")

    with SessionLocal() as session:
        # Delete single record (cascade deletes books)
        author = session.get(Author, 2)
        session.delete(author)
        session.commit()

        remaining = session.query(Author).all()
        print(f"After deleting author 2: {remaining}")

        # Delete by filter
        session.query(Book).filter(Book.price > 40).delete()
        session.commit()

        remaining_books = session.query(Book).all()
        print(f"Remaining books: {remaining_books}")

# ============================================
# Relationships
# ============================================

def relationship_queries():
    print("\n=== RELATIONSHIPS ===")

    # Reset data
    Base.metadata.drop_all(engine)
    Base.metadata.create_all(engine)

    with SessionLocal() as session:
        # Create fresh data
        author = Author(name='Charlie', email='charlie@example.com')
        book1 = Book(title='Web Dev', price=45.0, author=author)
        book2 = Book(title='DevOps', price=55.0, author=author)
        session.add_all([author, book1, book2])
        session.commit()

        # Access relationship from author
        session.refresh(author)
        print(f"Author: {author.name}")
        print(f"Books: {author.books}")

        # Access relationship from book
        print(f"Book author: {book1.author.name}")

        # Join query
        results = session.query(Book.title, Author.name).join(Author).all()
        print(f"Join results: {results}")

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    print("SQLAlchemy ORM Demo (SQLite)")
    print("=" * 50)

    # Clean start
    Base.metadata.drop_all(engine)
    Base.metadata.create_all(engine)

    create_operations()
    read_operations()
    update_operations()
    delete_operations()
    relationship_queries()

    # Cleanup
    import os
    if os.path.exists('demo.db'):
        os.remove('demo.db')
        print("\nCleaned up demo.db")

    print("\nAll SQLAlchemy examples completed!")
