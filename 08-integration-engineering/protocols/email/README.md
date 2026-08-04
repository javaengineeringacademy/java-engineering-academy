# Integration Protocols - Email

## Overview

Email integration enables sending and receiving emails using SMTP, IMAP, and POP3 protocols.

## Table of Contents

1. [Email Protocols](#email-protocols)
2. [JavaMail API](#javamail-api)
3. [Sending Email](#sending-email)
4. [Receiving Email](#receiving-email)
5. [Email Configuration](#email-configuration)

## Email Protocols

### Protocol Comparison

| Protocol | Description | Port |
|----------|-------------|------|
| SMTP | Send email | 25, 587 |
| IMAP | Receive email | 143, 993 |
| POP3 | Download email | 110, 995 |

## JavaMail API

### Maven Dependency

```xml
<dependency>
    <groupId>com.sun.mail</groupId>
    <artifactId>jakarta.mail</artifactId>
    <version>2.0.1</version>
</dependency>
```

## Sending Email

### Simple Email

```java
Properties props = new Properties();
props.put("mail.smtp.host", "smtp.example.com");
props.put("mail.smtp.port", "587");
props.put("mail.smtp.auth", "true");
props.put("mail.smtp.starttls.enable", "true");

Session session = Session.getInstance(props, new Authenticator() {
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication("user", "password");
    }
});

Message message = new MimeMessage(session);
message.setFrom(new InternetAddress("sender@example.com"));
message.setRecipients(Message.RecipientType.TO, 
    InternetAddress.parse("recipient@example.com"));
message.setSubject("Order Confirmation");
message.setText("Your order has been processed.");

Transport.send(message);
```

### HTML Email

```java
Message message = new MimeMessage(session);
message.setFrom(new InternetAddress("sender@example.com"));
message.setRecipients(Message.RecipientType.TO, 
    InternetAddress.parse("recipient@example.com"));
message.setSubject("Order Confirmation");

// HTML content
String htmlContent = "<h1>Order Confirmation</h1>" +
    "<p>Your order has been processed.</p>";
message.setContent(htmlContent, "text/html");

Transport.send(message);
```

### Email with Attachment

```java
Message message = new MimeMessage(session);
message.setFrom(new InternetAddress("sender@example.com"));
message.setRecipients(Message.RecipientType.TO, 
    InternetAddress.parse("recipient@example.com"));
message.setSubject("Order with Attachment");

// Create multipart
Multipart multipart = new MimeMultipart();

// Text part
BodyPart textPart = new MimeBodyPart();
textPart.setText("Please find the order attached.");
multipart.addBodyPart(textPart);

// Attachment part
BodyPart attachmentPart = new MimeBodyPart();
DataSource source = new FileDataSource("/path/to/order.pdf");
attachmentPart.setDataHandler(new DataHandler(source));
attachmentPart.setFileName("order.pdf");
multipart.addBodyPart(attachmentPart);

message.setContent(multipart);

Transport.send(message);
```

## Receiving Email

### IMAP Client

```java
Properties props = new Properties();
props.put("mail.store.protocol", "imaps");
props.put("mail.imaps.host", "imap.example.com");
props.put("mail.imaps.port", "993");

Session session = Session.getInstance(props);
Store store = session.getStore("imaps");
store.connect("imap.example.com", "user", "password");

Folder inbox = store.getFolder("INBOX");
inbox.open(Folder.READ_ONLY);

Message[] messages = inbox.getMessages();
for (Message message : messages) {
    System.out.println("Subject: " + message.getSubject());
    System.out.println("From: " + message.getFrom()[0]);
}

inbox.close(true);
store.close();
```

### POP3 Client

```java
Properties props = new Properties();
props.put("mail.store.protocol", "pop3s");
props.put("mail.pop3s.host", "pop3.example.com");
props.put("mail.pop3s.port", "995");

Session session = Session.getInstance(props);
Store store = session.getStore("pop3s");
store.connect("pop3.example.com", "user", "password");

Folder inbox = store.getFolder("INBOX");
inbox.open(Folder.READ_ONLY);

Message[] messages = inbox.getMessages();
for (Message message : messages) {
    System.out.println("Subject: " + message.getSubject());
}

inbox.close(true);
store.close();
```

## Email Configuration

### JavaMail Properties

```properties
# SMTP
mail.smtp.host=smtp.example.com
mail.smtp.port=587
mail.smtp.auth=true
mail.smtp.starttls.enable=true

# IMAP
mail.store.protocol=imaps
mail.imaps.host=imap.example.com
mail.imaps.port=993

# POP3
mail.pop3s.host=pop3.example.com
mail.pop3s.port=995
```

## Best Practices

1. **Use TLS**: Encrypt email connections
2. **Authenticate**: Always authenticate SMTP
3. **Handle exceptions**: Handle messaging exceptions
4. **Connection pooling**: Pool email connections
5. **Rate limiting**: Limit email sending rate
6. **Validation**: Validate email addresses
7. **Logging**: Log email operations
8. **Testing**: Test email sending/receiving

## References

- [JavaMail API](https://jakarta.ee/specifications/mail/2.1/apidocs/)
- [JavaMail Tutorial](https://www.baeldung.com/java-email-integration-essentials)
