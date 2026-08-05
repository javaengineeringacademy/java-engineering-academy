# Active Record Pattern

## Overview

The Active Record Pattern wraps a database row or record in an object that is simultaneously a domain object and its own data mapper. The object knows how to persist itself, including methods for CRUD operations, validation, and query building.

Active Record objects are directly responsible for database interactions. The entity class itself contains find, save, update, and delete methods along with its domain properties and business logic.

## When to Use

- Simple CRUD applications with straightforward data models
- Rapid prototyping where speed of development matters
- Domain logic is minimal and closely tied to the data structure
- Framework support makes it the natural fit (Rails, Django, Laravel)
- Team is familiar with ActiveRecord-style frameworks

## Implementation

### TypeScript (Sequelize-style)

```typescript
import { Model, DataTypes } from 'sequelize';

class User extends Model {
  declare id: number;
  declare name: string;
  declare email: string;

  static initModel(sequelize: any) {
    return User.init({
      id: { type: DataTypes.INTEGER, primaryKey: true, autoIncrement: true },
      name: { type: DataTypes.STRING, allowNull: false },
      email: { type: DataTypes.STRING, allowNull: false, unique: true }
    }, { sequelize, modelName: 'User', tableName: 'users' });
  }

  async updateProfile(name: string, email: string) {
    this.name = name;
    this.email = email;
    return this.save();
  }
}

// Usage
const user = await User.findByPk(1);
await user.updateProfile('New Name', 'new@email.com');
```

### Python (Django-style)

```python
from django.db import models

class User(models.Model):
    name = models.CharField(max_length=255)
    email = models.EmailField(unique=True)
    created_at = models.DateTimeField(auto_now_add=True)

    def activate(self):
        self.is_active = True
        self.save()

    @classmethod
    def find_by_email(cls, email):
        return cls.objects.filter(email=email).first()

# Usage
user = User.objects.get(pk=1)
user.activate()
```

### Ruby (Rails-style)

```ruby
class User < ApplicationRecord
  validates :name, presence: true
  validates :email, presence: true, uniqueness: true

  def full_name
    "#{first_name} #{last_name}"
  end

  def deactivate!
    update!(active: false)
  end

  scope :active, -> { where(active: true) }
end

# Usage
user = User.find(1)
user.deactivate!
active_users = User.active
```

### C\#

```csharp
public class User : BaseEntity {
    public string Name { get; set; }
    public string Email { get; set; }

    public void UpdateProfile(string name, string email) {
        Name = name;
        Email = email;
        Save();
    }

    public static User FindById(int id) {
        return Database.Query<User>("SELECT * FROM users WHERE id = @id", new { id })
            .FirstOrDefault();
    }

    private void Save() {
        Database.Execute(
            "UPDATE users SET name = @Name, email = @Email WHERE id = @Id", this);
    }
}
```

## Best Practices

- Keep business logic in the model but avoid complex domain behavior
- Use validations within the model for data integrity
- Move complex queries to scopes or static methods
- Avoid mixing presentation concerns into Active Record models
- Consider switching to Data Mapper when complexity grows
- Use callbacks sparingly to maintain clear control flow

## Interview Questions

1. What are the advantages of Active Record over Data Mapper?
2. When does Active Record become a liability in complex domains?
3. How do you handle complex queries that do not fit Active Record methods?
4. What test strategies work best with Active Record models?
5. How does Active Record relate to the ORM choice in your framework?

## References

- Fowler, Martin. *Patterns of Enterprise Application Architecture*, Chapter 10
- Ruby on Rails Guides. *Active Record Basics*
- Django Documentation. *Making queries*
- Martin Fowler. *AnemicDomainModel* (anti-pattern discussion)
