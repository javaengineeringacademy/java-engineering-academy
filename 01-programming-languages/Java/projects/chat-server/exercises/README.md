# Chat Server Exercises

## Exercise 1: File Transfer Support
Add the ability for users to transfer files through the chat server.

**Requirements:**
- Add a `/sendfile <filename>` command
- Implement file chunking for large files
- Add file received notification
- Store files in a server directory

**Hints:**
- Use Base64 encoding for file content
- Consider adding file metadata (size, type)
- Add progress indicators for large transfers

---

## Exercise 2: Persistent Message History
Implement message persistence using file storage or a database.

**Requirements:**
- Save chat history to files (one per room)
- Load history when room is created
- Add `/history` command to view recent messages
- Implement message search functionality

**Hints:**
- Use JSON format for message storage
- Consider using SQLite for better querying
- Implement message expiration/TTL

---

## Exercise 3: User Authentication System
Add proper user authentication with passwords and user profiles.

**Requirements:**
- User registration with password hashing
- Login/logout commands
- User profiles (display name, status)
- Admin role with moderation powers
- Ban/kick commands

**Hints:**
- Use bcrypt or PBKDF2 for password hashing
- Store user data in a JSON file or database
- Implement role-based access control
