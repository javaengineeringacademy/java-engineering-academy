package academy.javaengineering.senior.security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;

public class EncryptionDemo {

    private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String RSA_ALGORITHM = "RSA";
    private static final int AES_KEY_SIZE = 256;
    private static final int RSA_KEY_SIZE = 2048;

    public static void main(String[] args) throws Exception {
        System.out.println("=== AES Encryption/Decryption ===");
        String plaintext = "Hello, Encryption World!";
        SecretKey aesKey = generateAesKey();
        IvParameterSpec iv = generateIv();

        String encrypted = aesEncrypt(plaintext, aesKey, iv);
        String decrypted = aesDecrypt(encrypted, aesKey, iv);

        System.out.println("Plaintext: " + plaintext);
        System.out.println("Encrypted: " + encrypted);
        System.out.println("Decrypted: " + decrypted);
        System.out.println();

        System.out.println("=== RSA Key Generation ===");
        KeyPair rsaKeyPair = generateRsaKeyPair();
        System.out.println("RSA Public Key: " + rsaKeyPair.getPublic());
        System.out.println("RSA Private Key: " + rsaKeyPair.getPrivate());
        System.out.println();

        System.out.println("=== RSA Encryption/Decryption ===");
        String rsaEncrypted = rsaEncrypt(plaintext, rsaKeyPair.getPublic());
        String rsaDecrypted = rsaDecrypt(rsaEncrypted, rsaKeyPair.getPrivate());

        System.out.println("Plaintext: " + plaintext);
        System.out.println("Encrypted: " + rsaEncrypted);
        System.out.println("Decrypted: " + rsaDecrypted);
        System.out.println();

        System.out.println("=== SHA-256 Hashing ===");
        String sha256Hash = sha256Hash(plaintext);
        System.out.println("Plaintext: " + plaintext);
        System.out.println("SHA-256: " + sha256Hash);
        System.out.println();

        System.out.println("=== Hash Comparison ===");
        System.out.println("Same input produces same hash: " + sha256Hash.equals(sha256Hash(plaintext)));
        System.out.println("Different input: " + sha256Hash.equals(sha256Hash("Different")));
    }

    private static SecretKey generateAesKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(AES_KEY_SIZE, new SecureRandom());
        return keyGen.generateKey();
    }

    private static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    private static String aesEncrypt(String plaintext, SecretKey key, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    private static String aesDecrypt(String encrypted, SecretKey key, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encrypted));
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    private static KeyPair generateRsaKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        keyGen.initialize(RSA_KEY_SIZE, new SecureRandom());
        return keyGen.generateKeyPair();
    }

    private static String rsaEncrypt(String plaintext, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    private static String rsaDecrypt(String encrypted, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encrypted));
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    private static String sha256Hash(String data) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(hash);
    }
}
