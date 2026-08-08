// Module 12: Networking — Exercises
// Complete each exercise by filling in the TODO sections.
// Compile: g++ -std=c++17 -Wall -Wextra -o exercises exercises.cpp

#include <iostream>
#include <string>
#include <vector>
#include <functional>
#include <cassert>
#include <sstream>
#include <map>

// ============================================================================
// Exercise 1: URL Parser
// Parse a URL into its components: protocol, host, port, path, query.
// ============================================================================

struct URL {
    std::string protocol;
    std::string host;
    int port;
    std::string path;
    std::map<std::string, std::string> query_params;

    void print() const {
        std::cout << "Protocol: " << protocol << "\n";
        std::cout << "Host: " << host << "\n";
        std::cout << "Port: " << port << "\n";
        std::cout << "Path: " << path << "\n";
        std::cout << "Query params:\n";
        for (const auto& [key, value] : query_params) {
            std::cout << "  " << key << " = " << value << "\n";
        }
    }
};

// TODO: Parse a URL string into a URL struct
// Handle: "https://example.com:8080/path/to/resource?key=value&foo=bar"
// Default port: 80 for http, 443 for https
URL parse_url(const std::string& url_str) {
    URL result;
    // Your code here
    return result;
}

void exercise1() {
    std::cout << "\n=== Exercise 1: URL Parser ===\n";

    // TODO: Uncomment and test
    // auto url = parse_url("https://example.com:8080/api/users?page=1&limit=10");
    // assert(url.protocol == "https");
    // assert(url.host == "example.com");
    // assert(url.port == 8080);
    // assert(url.path == "/api/users");
    // url.print();

    std::cout << "Exercise 1: OK\n";
}

// ============================================================================
// Exercise 2: HTTP Request Builder
// Build HTTP request strings programmatically.
// ============================================================================

struct HTTPRequest {
    std::string method;
    std::string path;
    std::map<std::string, std::string> headers;
    std::string body;

    // TODO: Build the HTTP request string
    // Format:
    // GET /path HTTP/1.1\r\n
    // Host: example.com\r\n
    // Content-Type: application/json\r\n
    // \r\n
    // {body}
    std::string build() const {
        std::ostringstream oss;
        // Your code here
        return oss.str();
    }
};

void exercise2() {
    std::cout << "\n=== Exercise 2: HTTP Request Builder ===\n";

    HTTPRequest req;
    req.method = "GET";
    req.path = "/api/data";
    req.headers["Host"] = "example.com";
    req.headers["Accept"] = "application/json";

    std::string request = req.build();
    // TODO: Uncomment assertions
    // assert(request.find("GET /api/data HTTP/1.1") != std::string::npos);
    // assert(request.find("Host: example.com") != std::string::npos);
    // std::cout << "Request:\n" << request << "\n";

    std::cout << "Exercise 2: OK\n";
}

// ============================================================================
// Exercise 3: HTTP Response Parser
// Parse an HTTP response string into status code, headers, and body.
// ============================================================================

struct HTTPResponse {
    int status_code;
    std::string status_text;
    std::map<std::string, std::string> headers;
    std::string body;
};

// TODO: Parse an HTTP response string
HTTPResponse parse_response(const std::string& raw_response) {
    HTTPResponse response;
    response.status_code = 0;
    // Your code here
    return response;
}

void exercise3() {
    std::cout << "\n=== Exercise 3: HTTP Response Parser ===\n";

    std::string raw = "HTTP/1.1 200 OK\r\n"
                      "Content-Type: application/json\r\n"
                      "Content-Length: 27\r\n"
                      "\r\n"
                      "{\"message\":\"Hello, World!\"}";

    // TODO: Uncomment and test
    // auto response = parse_response(raw);
    // assert(response.status_code == 200);
    // assert(response.status_text == "OK");
    // assert(response.headers["Content-Type"] == "application/json");
    // assert(response.body == "{\"message\":\"Hello, World!\"}");
    // response.body should contain the JSON

    std::cout << "Exercise 3: OK\n";
}

// ============================================================================
// Exercise 4: Simple TCP Client Concept
// Design a simple TCP client interface.
// ============================================================================

// TODO: Implement a SimpleTCPClient class that:
// - Has connect(host, port) method
// - Has send(data) method
// - Has receive() method
// - Has disconnect() method
// - Stores connection state
// - Use a callback for received data

class SimpleTCPClient {
    // Your code here
    bool connected_;
    std::string host_;
    int port_;

public:
    SimpleTCPClient() : connected_(false), port_(0) {}

    bool connect(const std::string& host, int port) {
        // Simulate connection
        host_ = host;
        port_ = port;
        connected_ = true;
        std::cout << "Connected to " << host << ":" << port << "\n";
        return true;
    }

    bool send_data(const std::string& data) {
        if (!connected_) return false;
        std::cout << "Sending: " << data << "\n";
        return true;
    }

    std::string receive() {
        if (!connected_) return "";
        // Simulate receiving data
        return "Simulated response";
    }

    void disconnect() {
        connected_ = false;
        std::cout << "Disconnected\n";
    }

    bool is_connected() const { return connected_; }
};

void exercise4() {
    std::cout << "\n=== Exercise 4: TCP Client ===\n";

    SimpleTCPClient client;
    assert(!client.is_connected());

    client.connect("localhost", 8080);
    assert(client.is_connected());

    client.send_data("Hello Server!");
    std::string response = client.receive();
    assert(!response.empty());

    client.disconnect();
    assert(!client.is_connected());

    std::cout << "Exercise 4: OK\n";
}

// ============================================================================
// Exercise 5: JSON Builder
// Build JSON strings programmatically.
// ============================================================================

class JSONBuilder {
    std::ostringstream oss;
    bool first_;

public:
    JSONBuilder() : first_(true) {
        oss << "{";
    }

    // TODO: Add a string key-value pair
    JSONBuilder& string(const std::string& key, const std::string& value) {
        if (!first_) oss << ",";
        oss << "\"" << key << "\":\"" << value << "\"";
        first_ = false;
        return *this;
    }

    // TODO: Add an integer key-value pair
    JSONBuilder& number(const std::string& key, int value) {
        if (!first_) oss << ",";
        oss << "\"" << key << "\":" << value;
        first_ = false;
        return *this;
    }

    // TODO: Add a boolean key-value pair
    JSONBuilder& boolean(const std::string& key, bool value) {
        if (!first_) oss << ",";
        oss << "\"" << key << "\":" << (value ? "true" : "false");
        first_ = false;
        return *this;
    }

    // TODO: Build and return the JSON string
    std::string build() {
        oss << "}";
        return oss.str();
    }
};

void exercise5() {
    std::cout << "\n=== Exercise 5: JSON Builder ===\n";

    std::string json = JSONBuilder()
        .string("name", "Alice")
        .number("age", 30)
        .boolean("active", true)
        .build();

    std::cout << "JSON: " << json << "\n";
    assert(json.find("\"name\":\"Alice\"") != std::string::npos);
    assert(json.find("\"age\":30") != std::string::npos);
    assert(json.find("\"active\":true") != std::string::npos);

    std::cout << "Exercise 5: OK\n";
}

// ============================================================================
// Main
// ============================================================================

int main() {
    std::cout << "=== Module 12: Networking Exercises ===\n";

    exercise1();
    exercise2();
    exercise3();
    exercise4();
    exercise5();

    std::cout << "\nAll exercises completed!\n";
    return 0;
}
