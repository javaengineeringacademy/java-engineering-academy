// Module 12: Networking — Solutions
// Study these after attempting the exercises yourself.

#include <iostream>
#include <string>
#include <vector>
#include <functional>
#include <cassert>
#include <sstream>
#include <map>
#include <algorithm>

// ============================================================================
// Exercise 1 Solution: URL Parser
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

URL parse_url(const std::string& url_str) {
    URL result;
    result.port = 0;

    std::string s = url_str;

    // Parse protocol
    auto protocol_end = s.find("://");
    if (protocol_end != std::string::npos) {
        result.protocol = s.substr(0, protocol_end);
        s = s.substr(protocol_end + 3);
    }

    // Parse query params
    auto query_start = s.find('?');
    std::string path_part;
    if (query_start != std::string::npos) {
        std::string query_str = s.substr(query_start + 1);
        s = s.substr(0, query_start);

        std::istringstream iss(query_str);
        std::string pair;
        while (std::getline(iss, pair, '&')) {
            auto eq = pair.find('=');
            if (eq != std::string::npos) {
                result.query_params[pair.substr(0, eq)] = pair.substr(eq + 1);
            }
        }
    }

    // Parse path
    auto path_start = s.find('/');
    if (path_start != std::string::npos) {
        result.path = s.substr(path_start);
        s = s.substr(0, path_start);
    } else {
        result.path = "/";
    }

    // Parse host:port
    auto port_start = s.find(':');
    if (port_start != std::string::npos) {
        result.host = s.substr(0, port_start);
        result.port = std::stoi(s.substr(port_start + 1));
    } else {
        result.host = s;
        result.port = (result.protocol == "https") ? 443 : 80;
    }

    return result;
}

void exercise1() {
    std::cout << "\n=== Exercise 1: URL Parser ===\n";

    auto url = parse_url("https://example.com:8080/api/users?page=1&limit=10");
    assert(url.protocol == "https");
    assert(url.host == "example.com");
    assert(url.port == 8080);
    assert(url.path == "/api/users");
    assert(url.query_params["page"] == "1");
    assert(url.query_params["limit"] == "10");
    url.print();

    auto url2 = parse_url("http://google.com/search");
    assert(url2.protocol == "http");
    assert(url2.host == "google.com");
    assert(url2.port == 80);
    assert(url2.path == "/search");

    std::cout << "Exercise 1 passed!\n";
}

// ============================================================================
// Exercise 2 Solution: HTTP Request Builder
// ============================================================================

struct HTTPRequest {
    std::string method;
    std::string path;
    std::map<std::string, std::string> headers;
    std::string body;

    std::string build() const {
        std::ostringstream oss;
        oss << method << " " << path << " HTTP/1.1\r\n";
        for (const auto& [key, value] : headers) {
            oss << key << ": " << value << "\r\n";
        }
        if (!body.empty()) {
            oss << "Content-Length: " << body.size() << "\r\n";
        }
        oss << "\r\n";
        if (!body.empty()) {
            oss << body;
        }
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
    assert(request.find("GET /api/data HTTP/1.1") != std::string::npos);
    assert(request.find("Host: example.com") != std::string::npos);
    std::cout << "Request:\n" << request << "\n";

    HTTPRequest post_req;
    post_req.method = "POST";
    post_req.path = "/api/users";
    post_req.headers["Host"] = "example.com";
    post_req.headers["Content-Type"] = "application/json";
    post_req.body = "{\"name\":\"Alice\"}";

    std::string post_request = post_req.build();
    assert(post_request.find("POST /api/users HTTP/1.1") != std::string::npos);
    assert(post_request.find("Content-Length: 15") != std::string::npos);

    std::cout << "Exercise 2 passed!\n";
}

// ============================================================================
// Exercise 3 Solution: HTTP Response Parser
// ============================================================================

struct HTTPResponse {
    int status_code;
    std::string status_text;
    std::map<std::string, std::string> headers;
    std::string body;
};

HTTPResponse parse_response(const std::string& raw_response) {
    HTTPResponse response;
    std::istringstream iss(raw_response);
    std::string line;

    // Parse status line
    std::getline(iss, line);
    if (!line.empty() && line.back() == '\r') line.pop_back();
    auto space1 = line.find(' ');
    auto space2 = line.find(' ', space1 + 1);
    if (space1 != std::string::npos) {
        response.status_code = std::stoi(line.substr(space1 + 1));
        response.status_text = line.substr(space2 + 1);
    }

    // Parse headers
    while (std::getline(iss, line)) {
        if (!line.empty() && line.back() == '\r') line.pop_back();
        if (line.empty()) break;

        auto colon = line.find(':');
        if (colon != std::string::npos) {
            std::string key = line.substr(0, colon);
            std::string value = line.substr(colon + 2);
            response.headers[key] = value;
        }
    }

    // Rest is body
    std::string body_line;
    while (std::getline(iss, body_line)) {
        if (!response.body.empty()) response.body += "\n";
        response.body += body_line;
    }

    return response;
}

void exercise3() {
    std::cout << "\n=== Exercise 3: HTTP Response Parser ===\n";

    std::string raw = "HTTP/1.1 200 OK\r\n"
                      "Content-Type: application/json\r\n"
                      "Content-Length: 27\r\n"
                      "\r\n"
                      "{\"message\":\"Hello, World!\"}";

    auto response = parse_response(raw);
    assert(response.status_code == 200);
    assert(response.status_text == "OK");
    assert(response.headers["Content-Type"] == "application/json");
    assert(response.body == "{\"message\":\"Hello, World!\"}");
    std::cout << "Status: " << response.status_code << " " << response.status_text << "\n";
    std::cout << "Body: " << response.body << "\n";

    std::cout << "Exercise 3 passed!\n";
}

// ============================================================================
// Exercise 4 Solution: TCP Client
// ============================================================================

class SimpleTCPClient {
    bool connected_;
    std::string host_;
    int port_;

public:
    SimpleTCPClient() : connected_(false), port_(0) {}

    bool connect(const std::string& host, int port) {
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

    std::cout << "Exercise 4 passed!\n";
}

// ============================================================================
// Exercise 5 Solution: JSON Builder
// ============================================================================

class JSONBuilder {
    std::ostringstream oss;
    bool first_;

public:
    JSONBuilder() : first_(true) {
        oss << "{";
    }

    JSONBuilder& string(const std::string& key, const std::string& value) {
        if (!first_) oss << ",";
        oss << "\"" << key << "\":\"" << value << "\"";
        first_ = false;
        return *this;
    }

    JSONBuilder& number(const std::string& key, int value) {
        if (!first_) oss << ",";
        oss << "\"" << key << "\":" << value;
        first_ = false;
        return *this;
    }

    JSONBuilder& boolean(const std::string& key, bool value) {
        if (!first_) oss << ",";
        oss << "\"" << key << "\":" << (value ? "true" : "false");
        first_ = false;
        return *this;
    }

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

    std::cout << "Exercise 5 passed!\n";
}

// ============================================================================
// Main
// ============================================================================

int main() {
    std::cout << "=== Module 12: Networking Solutions ===\n";

    exercise1();
    exercise2();
    exercise3();
    exercise4();
    exercise5();

    std::cout << "\nAll exercises completed!\n";
    return 0;
}
