# Data Model Practices

## Practice 1: CRUD Operations

### Objective
Master basic znode operations.

### Tasks

1. **Create znodes**
   ```bash
   # Create persistent
   create /config/app1 "config data"
   
   # Create with parent
   create /services/api/instance-001 "instance data"
   
   # Create ephemeral
   create -e /temp/session "session data"
   
   # Create sequential
   create -s /queue/task "task data"
   ```

2. **Read znodes**
   ```bash
   # Get data
   get /config/app1
   
   # Get children
   ls /services/api
   
   # Check existence
   stat /config/app1
   ```

3. **Update znodes**
   ```bash
   # Update data
   set /config/app1 "new config data"
   
   # Update with version
   set /config/app1 "versioned data" 1
   ```

4. **Delete znodes**
   ```bash
   # Delete leaf
   delete /config/app1
   
   # Delete recursively
   deleteall /services
   ```

---

## Practice 2: Znode Types

### Objective
Understand different znode types and their behaviors.

### Tasks

1. **Create different types**
   ```bash
   # Persistent
   create /persistent "data"
   
   # Ephemeral
   create -e /ephemeral "data"
   
   # Sequential
   create -s /sequential/prefix- "data"
   
   # Ephemeral sequential
   create -e -s /eph-seq/prefix- "data"
   ```

2. **Observe ephemeral behavior**
   ```bash
   # Create ephemeral
   create -e /temp/test "data"
   
   # List to see it
   ls /
   
   # Disconnect client (Ctrl+C)
   # Reconnect and check
   ls /
   ```

3. **Observe sequential numbering**
   ```bash
   # Create multiple sequential
   for i in {1..5}; do
       create -s /queue/item- "item $i"
   done
   
   # List to see numbering
   ls /queue
   ```

---

## Practice 3: ACL Management

### Objective
Implement access control on znodes.

### Tasks

1. **Set up authentication**
   ```bash
   # Add auth
   addauth digest user1:password1
   addauth digest user2:password2
   ```

2. **Create ACL-protected znodes**
   ```bash
   # Create with digest ACL
   setAcl /secure/data digest:user1:password1:rwcda
   
   # Create with world ACL
   setAcl /public/data world:anyone:r
   ```

3. **Test access**
   ```bash
   # Try to read as user1
   addauth digest user1:password1
   get /secure/data
   
   # Try to read as user2
   addauth digest user2:password2
   get /secure/data
   ```

---

## Practice 4: Watch Mechanism

### Objective
Implement watches for data changes.

### Tasks

1. **Set up watch**
   ```bash
   # Watch for data changes
   get /config/app1 watch
   
   # Watch for child changes
   ls /services watch
   ```

2. **Trigger watches**
   ```bash
   # In another client
   set /config/app1 "new data"
   
   # Add child
   create /services/new-service "data"
   ```

3. **Observe watch events**
   - Note the event type
   - Note the path
   - Note the state

---

## Practice 5: Data Serialization

### Objective
Practice storing structured data.

### Tasks

1. **JSON serialization**
   ```bash
   # Store JSON
   set /config/app1 '{"host":"10.0.0.1","port":8080}'
   
   # Read and parse
   get /config/app1
   ```

2. **Versioned data**
   ```bash
   # Store with version
   set /config/app1 '{"version":1,"data":"v1"}'
   
   # Update version
   set /config/app1 '{"version":2,"data":"v2"}'
   ```

3. **Binary data**
   ```bash
   # Store binary
   set /data/binary [binary data]
   
   # Read binary
   get /data/binary
   ```

---

## Discussion Questions

1. When would you use ephemeral vs persistent znodes?
2. How do sequential znodes help with ordering?
3. What are the tradeoffs of different ACL schemes?
4. How do watches work in Zookeeper?
5. What are the best practices for data serialization?
