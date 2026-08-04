# Integration Protocols - FTP

## Overview

FTP (File Transfer Protocol) integration enables transferring files between systems over a network.

## Table of Contents

1. [FTP Basics](#ftp-basics)
2. [FTP Operations](#ftp-operations)
3. [FTP Configuration](#ftp-configuration)
4. [Java FTP Client](#java-ftp-client)
5. [FTP Best Practices](#ftp-best-practices)

## FTP Basics

### FTP Modes

| Mode | Description |
|------|-------------|
| Active | Server connects to client |
| Passive | Client connects to server |
| Binary | Transfer binary files |
| ASCII | Transfer text files |

## FTP Operations

### Connect

```java
FTPClient ftpClient = new FTPClient();
ftpClient.connect("ftp.example.com", 21);
ftpClient.login("user", "password");
ftpClient.enterLocalPassiveMode();
```

### Upload

```java
File localFile = new File("/local/order.txt");
InputStream inputStream = new FileInputStream(localFile);

boolean success = ftpClient.storeFile("/remote/order.txt", inputStream);
inputStream.close();
```

### Download

```java
OutputStream outputStream = new FileOutputStream("/local/order.txt");
boolean success = ftpClient.retrieveFile("/remote/order.txt", outputStream);
outputStream.close();
```

### List Files

```java
FTPFile[] files = ftpClient.listFiles("/remote");
for (FTPFile file : files) {
    System.out.println(file.getName() + " - " + file.getSize());
}
```

### Delete

```java
boolean deleted = ftpClient.deleteFile("/remote/order.txt");
```

### Rename

```java
boolean renamed = ftpClient.rename("/remote/old.txt", "/remote/new.txt");
```

## FTP Configuration

### FTPClient Configuration

```java
FTPClient ftpClient = new FTPClient();
ftpClient.setConnectTimeout(5000);
ftpClient.setDataTimeout(10000);
ftpClient.setSoTimeout(10000);
ftpClient.setControlEncoding("UTF-8");

// Set file type
ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

// Enter local passive mode
ftpClient.enterLocalPassiveMode();
```

## Java FTP Client

### Complete Example

```java
public class FtpClient {
    private FTPClient ftpClient;
    
    public void connect(String host, int port, String user, String pass) 
            throws IOException {
        ftpClient = new FTPClient();
        ftpClient.connect(host, port);
        
        int replyCode = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(replyCode)) {
            throw new IOException("FTP server refused connection");
        }
        
        boolean success = ftpClient.login(user, pass);
        if (!success) {
            throw new IOException("FTP login failed");
        }
        
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
    }
    
    public void upload(String remotePath, File localFile) throws IOException {
        try (InputStream is = new FileInputStream(localFile)) {
            boolean success = ftpClient.storeFile(remotePath, is);
            if (!success) {
                throw new IOException("FTP upload failed");
            }
        }
    }
    
    public void download(String remotePath, File localFile) throws IOException {
        try (OutputStream os = new FileOutputStream(localFile)) {
            boolean success = ftpClient.retrieveFile(remotePath, os);
            if (!success) {
                throw new IOException("FTP download failed");
            }
        }
    }
    
    public void disconnect() throws IOException {
        if (ftpClient != null && ftpClient.isConnected()) {
            ftpClient.logout();
            ftpClient.disconnect();
        }
    }
}
```

## FTP Best Practices

1. **Use passive mode**: For firewall compatibility
2. **Handle timeouts**: Set appropriate timeouts
3. **Binary mode**: Use binary mode for non-text files
4. **Error handling**: Handle FTP exceptions
5. **Connection pooling**: Pool FTP connections
6. **Security**: Use FTPS for secure transfer
7. **Cleanup**: Close connections properly
8. **Logging**: Log FTP operations

## References

- [Apache Commons Net FTP](https://commons.apache.org/proper/commons-net/apidocs/org/apache/commons/net/ftp/FTPClient.html)
- [FTP RFC](https://datatracker.ietf.org/doc/html/rfc959)
