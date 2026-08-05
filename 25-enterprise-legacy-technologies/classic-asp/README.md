# Classic ASP (Active Server Pages)

## Overview

Classic ASP, introduced by Microsoft in 1996, was the first server-side scripting platform for building dynamic web applications on Windows. It uses VBScript or JScript embedded in HTML to generate dynamic content.

## Architecture

ASP pages are processed by Internet Information Services (IIS) on the server. The ASP engine parses the page, executes server-side scripts, and returns HTML to the client browser. No compilation step is required.

## VBScript Integration

VBScript serves as the primary scripting language for Classic ASP. It provides access to COM objects, file system operations, database connectivity, and session management through内置 objects.

```asp
<%
Dim conn, rs
Set conn = Server.CreateObject("ADODB.Connection")
conn.Open "Provider=SQLOLEDB;Server=myServer;Database=myDB;"

Set rs = conn.Execute("SELECT * FROM Customers")
Do While Not rs.EOF
    Response.Write "<tr><td>" & rs("Name") & "</td></tr>"
    rs.MoveNext
Loop
%>
```

## Built-in Objects

ASP provides five built-in objects: Request, Response, Session, Application, and Server. These objects handle HTTP communication, state management, and server resource access.

## COM Component Integration

Classic ASP relies heavily on COM components for business logic, data access, and utility functions. VB6 and COM+ were commonly used to create these components.

## Limitations

Classic ASP lacks built-in compilation, type safety, and separation of concerns. Debugging is difficult, and performance degrades with complex pages without proper optimization techniques.

## Migration to ASP.NET

Microsoft provides migration guides for moving Classic ASP applications to ASP.NET. The process typically involves rewriting page logic, replacing VBScript with C# or VB.NET, and implementing proper architecture patterns.
