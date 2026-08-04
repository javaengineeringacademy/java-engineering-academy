# Integration Protocols - SFTP

## Overview

SFTP (SSH File Transfer Protocol) provides secure file transfer over SSH, encrypting both commands and data.

## Table of Contents

1. [SFTP Basics](#sftp-basics)
2. [SFTP Operations](#sftp-operations)
3. [JSch Library](#jsch-library)
4. [SFTP Configuration](#sftp-configuration)
5. [Best Practices](#best-practices)

## SFTP Basics

### SFTP vs FTP

| Feature | FTP | SFTP |
|---------|-----|------|
| Security | Plain text | Encrypted |
| Port | 21 | 22 |
| Protocol | FTP | SSH |
| Firewall | Complex | Simple |

## SFTP Operations

### Connect

```java
JSch jsch = new JSch();
Session session = jsch.getSession("user", "host", 22);
session.setPassword("password");

// Disable strict host key checking
Properties config = new Properties();
config.put("StrictHostKeyChecking", "no");
session.setConfig(config);

session.connect();
ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
channel.connect();
```

### Upload

```java
channel.put("/local/order.txt", "/remote/order.txt");
// Or with InputStream
channel.put(inputStream, "/remote/order.txt");
```

### Download

```java
channel.get("/remote/order.txt", "/local/order.txt");
// Or with OutputStream
channel.get("/remote/order.txt", outputStream);
```

### List Files

```java
Vector<ChannelSftp.LsEntry> files = channel.ls("/remote");
for (ChannelSftp.LsEntry entry : files) {
    System.out.println(entry.getFilename() + " - " + entry.getAttrs().getSize());
}
```

### Delete

```java
channel.rm("/remote/order.txt");
```

### Rename

```java
channel.rename("/remote/old.txt", "/remote/new.txt");
```

## JSch Library

### Complete Example

```java
public class SftpClient {
    private Session session;
    private ChannelSftp channel;
    
    public void connect(String host, int port, String user, String pass) 
            throws JSchException {
        JSch jsch = new JSch();
        session = jsch.getSession(user, host, port);
        session.setPassword(pass);
        
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        
        session.connect();
        
        Channel channel = session.openChannel("sftp");
        channel.connect();
        this.channel = (ChannelSftp) channel;
    }
    
    public void connectWithKey(String host, int port, String user, 
                               String privateKey) throws JSchException {
        JSch jsch = new JSch();
        jsch.addIdentity(privateKey);
        
        session = jsch.getSession(user, host, port);
        
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        
        session.connect();
        
        Channel channel = session.openChannel("sftp");
        channel.connect();
        this.channel = (ChannelSftp) channel;
    }
    
    public void upload(String remotePath, File localFile) throws SftpException {
        channel.put(localFile.getAbsolutePath(), remotePath);
    }
    
    public void download(String remotePath, File localFile) throws SftpException {
        channel.get(remotePath, localFile.getAbsolutePath());
    }
    
    public List<String> listFiles(String remotePath) throws SftpException {
        Vector<ChannelSftp.LsEntry> files = channel.ls(remotePath);
        return files.stream()
            .map(ChannelSftp.LsEntry::getFilename)
            .collect(Collectors.toList());
    }
    
    public void disconnect() {
        if (channel != null && channel.isConnected()) {
            channel.disconnect();
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }
}
```

## SFTP Configuration

### Key-Based Authentication

```java
JSch jsch = new JSch();
jsch.addIdentity("/path/to/private/key");

Session session = jsch.getSession("user", "host", 22);
session.setConfig("PreferredAuthentications", "publickey");
session.connect();
```

### Known Hosts

```java
JSch jsch = new JSch();
jsch.setKnownHosts("/home/user/.ssh/known_hosts");

Session session = jsch.getSession("user", "host", 22);
session.connect();
```

## Best Practices

1. **Use key-based auth**: More secure than passwords
2. **Verify host keys**: Validate server identity
3. **Use known hosts**: Store known host keys
4. **Handle timeouts**: Set connection timeouts
5. **Error handling**: Handle SFTP exceptions
6. **Connection pooling**: Pool SFTP connections
7. **Cleanup**: Close connections properly
8. **Logging**: Log SFTP operations

## References

- [JSch Library](http://www.jcraft.com/jsch/)
- [SFTP RFC](https://datatracker.ietf.org/doc/html/draft-ietf-secsh-filexfer)
