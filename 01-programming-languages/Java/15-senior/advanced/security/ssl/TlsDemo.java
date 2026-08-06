package academy.javaengineering.senior.security;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.security.cert.X509Certificate;

public class TlsDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=== SSLContext Creation ===");
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, null, null);
        System.out.println("SSLContext initialized with default settings");
        System.out.println("Protocol: " + sslContext.getProtocol());
        System.out.println();

        System.out.println("=== Custom TrustManager ===");
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
        };

        SSLContext customSslContext = SSLContext.getInstance("TLS");
        customSslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        System.out.println("Custom SSLContext created (trust all certs - for testing only!)");
        System.out.println();

        System.out.println("=== HTTPS Client Setup ===");
        try {
            URL url = new URL("https://www.example.com");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestProperty("User-Agent", "JavaSSLDemo/1.0");

            System.out.println("Connecting to: " + url);
            System.out.println("Response Code: " + connection.getResponseCode());
            System.out.println("Cipher Suite: " + connection.getCipherSuite());
            System.out.println("SSL Protocol: " + connection.getSSLSession().getProtocol());

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                String line;
                int lineCount = 0;
                while ((line = reader.readLine()) != null && lineCount < 5) {
                    System.out.println("Line " + (lineCount + 1) + ": " + line);
                    lineCount++;
                }
                if (line != null) System.out.println("...");
            }
            connection.disconnect();
        } catch (IOException e) {
            System.out.println("Connection error: " + e.getMessage());
        }

        System.out.println();
        System.out.println("=== TLS Version Info ===");
        System.out.println("Supported protocols:");
        for (String protocol : SSLContext.getDefault().getSupportedSSLParameters().getProtocols()) {
            System.out.println("  - " + protocol);
        }
    }
}
