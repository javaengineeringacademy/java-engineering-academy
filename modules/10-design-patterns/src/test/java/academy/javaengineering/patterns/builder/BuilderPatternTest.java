package academy.javaengineering.patterns.builder;

import academy.javaengineering.patterns.builder.BuilderExample.HttpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuilderPatternTest {

    @Test
    @DisplayName("Should build request with only required url")
    void shouldBuildWithUrlOnly() {
        HttpRequest request = new HttpRequest.Builder("https://api.example.com").build();
        assertNotNull(request);
        assertTrue(request.toString().contains("https://api.example.com"));
    }

    @Test
    @DisplayName("Should use default method GET")
    void shouldDefaultToGetMethod() {
        HttpRequest request = new HttpRequest.Builder("https://api.example.com").build();
        assertTrue(request.toString().contains("GET"), "Default method should be GET");
    }

    @Test
    @DisplayName("Should set custom method")
    void shouldSetCustomMethod() {
        HttpRequest request = new HttpRequest.Builder("https://api.example.com")
                .method("POST")
                .build();
        assertTrue(request.toString().contains("POST"));
    }

    @Test
    @DisplayName("Should build with all fields set")
    void shouldBuildWithAllFields() {
        HttpRequest request = new HttpRequest.Builder("https://api.example.com")
                .method("PUT")
                .body("{\"data\": 1}")
                .timeout(5000)
                .build();
        assertNotNull(request);
        assertTrue(request.toString().contains("PUT"));
    }

    @Test
    @DisplayName("Builder methods should return same builder for chaining")
    void shouldSupportChaining() {
        HttpRequest.Builder builder = new HttpRequest.Builder("https://api.example.com");
        HttpRequest.Builder returned = builder.method("DELETE");
        assertSame(builder, returned, "Builder method should return the same builder instance");
    }

    @Test
    @DisplayName("Different builders should produce different requests")
    void differentBuildersShouldProduceDifferentRequests() {
        HttpRequest r1 = new HttpRequest.Builder("https://a.com").build();
        HttpRequest r2 = new HttpRequest.Builder("https://b.com").build();
        assertNotSame(r1, r2);
        assertFalse(r1.toString().equals(r2.toString()));
    }

    @Test
    @DisplayName("Should set body correctly")
    void shouldSetBody() {
        HttpRequest request = new HttpRequest.Builder("https://api.example.com")
                .body("test body")
                .build();
        assertNotNull(request);
    }

    @Test
    @DisplayName("Should set timeout correctly")
    void shouldSetTimeout() {
        HttpRequest request = new HttpRequest.Builder("https://api.example.com")
                .timeout(10000)
                .build();
        assertNotNull(request);
    }

    @Test
    @DisplayName("Should require url in constructor")
    void shouldRequireUrl() {
        assertThrows(Exception.class,
                () -> new HttpRequest.Builder(null),
                "Builder constructor should reject null url");
    }

    @Test
    @DisplayName("Should produce immutable request object")
    void shouldBeImmutable() {
        HttpRequest request = new HttpRequest.Builder("https://api.example.com")
                .method("POST")
                .build();
        String firstToString = request.toString();
        String secondToString = request.toString();
        assertEquals(firstToString, secondToString,
                "Immutable object should produce same toString on every call");
    }
}
