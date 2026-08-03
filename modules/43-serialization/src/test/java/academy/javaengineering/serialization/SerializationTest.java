package academy.javaengineering.serialization;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

class SerializationTest {

    @TempDir
    Path tempDir;

    @Test
    void testBasicSerialization() throws Exception {
        SerializationExample.Person original = new SerializationExample.Person("John", 30);
        SerializationExample.Person restored = SerializationExample.serializePerson(original);
        
        assertNotSame(original, restored);
        assertEquals(original.getName(), restored.getName());
        assertEquals(original.getAge(), restored.getAge());
    }

    @Test
    void testTransientFields() throws Exception {
        SerializationExample.Employee original = new SerializationExample.Employee(
            "Alice", 75000, "token123");
        
        SerializationExample.Employee restored = SerializationExample.demonstrateTransient(original);
        
        assertNotSame(original, restored);
        assertEquals(original.getName(), restored.getName());
        assertEquals(original.getSalary(), restored.getSalary());
        assertNull(restored.getTemporaryToken()); // Transient field
        assertNull(restored.getCachedData()); // Transient field
    }

    @Test
    void testCustomSerialization() throws Exception {
        SerializationExample.BankAccount original = new SerializationExample.BankAccount(
            "123456789", 10000.00, "1234");
        
        SerializationExample.BankAccount restored = 
            SerializationExample.demonstrateCustomSerialization(original);
        
        assertNotSame(original, restored);
        assertEquals(original.getEncryptedPin(), restored.getEncryptedPin());
    }

    @Test
    void testExternalizable() throws Exception {
        Map<String, String> attrs = new HashMap<>();
        attrs.put("color", "red");
        attrs.put("size", "large");
        
        SerializationExample.Product original = new SerializationExample.Product(
            "P001", "Widget", 29.99, attrs);
        
        SerializationExample.Product restored = 
            SerializationExample.demonstrateExternalizable(original);
        
        assertNotSame(original, restored);
        assertEquals("P001", restored.id);
        assertEquals("Widget", restored.name);
        assertEquals(29.99, restored.price, 0.001);
        assertEquals(attrs, restored.attributes);
    }

    @Test
    void testInheritanceSerialization() throws Exception {
        SerializationExample.Dog dog = new SerializationExample.Dog("Buddy", "Golden Retriever");
        SerializationExample.Animal restoredDog = SerializationExample.demonstrateInheritance(dog);
        
        assertNotSame(dog, restoredDog);
        assertTrue(restoredDog instanceof SerializationExample.Dog);
        assertEquals("Buddy", restoredDog.getName());
        assertEquals("Golden Retriever", ((SerializationExample.Dog) restoredDog).breed);
        
        SerializationExample.Cat cat = new SerializationExample.Cat("Whiskers", true);
        SerializationExample.Animal restoredCat = SerializationExample.demonstrateInheritance(cat);
        
        assertNotSame(cat, restoredCat);
        assertTrue(restoredCat instanceof SerializationExample.Cat);
        assertEquals("Whiskers", restoredCat.getName());
        assertTrue(((SerializationExample.Cat) restoredCat).isIndoor);
    }

    @Test
    void testMultipleObjects() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(new SerializationExample.Person("Alice", 25));
            oos.writeObject(new SerializationExample.Person("Bob", 35));
            oos.writeObject(new SerializationExample.Person("Charlie", 45));
        }
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            SerializationExample.Person p1 = (SerializationExample.Person) ois.readObject();
            SerializationExample.Person p2 = (SerializationExample.Person) ois.readObject();
            SerializationExample.Person p3 = (SerializationExample.Person) ois.readObject();
            
            assertEquals("Alice", p1.getName());
            assertEquals("Bob", p2.getName());
            assertEquals("Charlie", p3.getName());
        }
    }

    @Test
    void testEmptyObject() throws Exception {
        SerializationExample.Person original = new SerializationExample.Person(null, 0);
        SerializationExample.Person restored = SerializationExample.serializePerson(original);
        
        assertNull(restored.getName());
        assertEquals(0, restored.getAge());
    }

    @Test
    void testLargeData() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append("data").append(i).append(",");
        }
        
        SerializationExample.Person original = new SerializationExample.Person(sb.toString(), 100);
        SerializationExample.Person restored = SerializationExample.serializePerson(original);
        
        assertEquals(sb.toString(), restored.getName());
        assertEquals(100, restored.getAge());
    }
}
