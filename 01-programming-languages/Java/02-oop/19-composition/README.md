# Composition

## Introduction

Composition is a fundamental design principle in object-oriented programming where objects are built by combining other objects as members rather than inheriting from them, creating "has-a" relationships that provide greater flexibility, better encapsulation, and more maintainable code compared to inheritance-based designs. This principle, often expressed as "favor composition over inheritance," allows developers to assemble complex behaviors by combining simpler, focused components, making it easier to modify, test, and reuse code. Composition enables runtime flexibility by allowing objects to change their behavior through delegation to composed components, supporting dynamic configuration and reducing the tight coupling that inheritance hierarchies often create. By breaking down complex systems into smaller, composable pieces, composition promotes the Single Responsibility Principle and makes code easier to understand, maintain, and extend.

## Learning Objectives

By the end of this topic, you will be able to:

- [ ] Understand the principles of composition and how it differs from inheritance
- [ ] Implement "has-a" relationships using composition to create flexible, maintainable designs
- [ ] Apply delegation patterns to combine behaviors from multiple composed components
- [ ] Recognize when to use composition versus inheritance and the trade-offs involved

## Prerequisites

- [09-inheritance](../09-inheritance/README.md) - Understanding "is-a" relationships and class hierarchies
- [12-interfaces](../12-interfaces/README.md) - Interface contracts and polymorphism
- [02-classes](../02-classes/README.md) - Class structure, fields, and methods
- [06-this-keyword](../06-this-keyword/README.md) - Object references and delegation

## Why This Concept Exists

### The Problem

Inheritance-based designs often lead to several problems:

1. **Tight coupling**: Subclasses are tightly coupled to parent class implementations
2. **Fragile base class problem**: Changes in parent classes can break subclasses
3. **Limited flexibility**: Inheritance is static and determined at compile time
4. **Multiple inheritance limitations**: Java doesn't support multiple class inheritance
5. **Code duplication**: Common functionality must be duplicated across hierarchies

```java
// Problem: Inheritance creates tight coupling
class ArrayList extends AbstractList {
    // If AbstractList changes, ArrayList might break
    // Cannot inherit from multiple classes
    // Behavior is fixed at compile time
}
```

### The Solution

Composition solves these problems by:

- Building objects from smaller, focused components
- Creating "has-a" relationships instead of "is-a"
- Allowing runtime flexibility through delegation
- Supporting multiple component types without multiple inheritance
- Reducing coupling between components

### Real-World Analogy

Think of composition as a **computer system**. Instead of inheriting from a "Computer" base class, a computer is composed of:
- A CPU (processor component)
- Memory (RAM component)
- Storage (hard drive component)
- Input/Output devices (keyboard, monitor, etc.)

Each component can be independently upgraded, replaced, or configured without affecting the others. This flexibility is exactly what composition provides in software design.

## Internal Working

### JVM Perspective

Composition is implemented through object references and method delegation:

1. **Object References**: Components are stored as instance variables that reference other objects
2. **Method Delegation**: Methods call corresponding methods on composed objects
3. **Memory Management**: Each component is a separate object on the heap
4. **Garbage Collection**: Components are independently garbage collected when no longer referenced

### Memory Representation

```
Composite Object in Memory:

Computer Object:
┌─────────────────────────────┐
│ Reference: this             │
├─────────────────────────────┤
│ Fields:                     │
│ ├── cpu → CPU Object        │
│ ├── memory → Memory Object  │
│ ├── storage → Storage Object│
│ └── monitor → Monitor Object│
└─────────────────────────────┘

CPU Object:
┌─────────────────────────────┐
│ Fields:                     │
│ ├── brand: "Intel"          │
│ ├── cores: 8                │
│ └── clockSpeed: 3.6         │
└─────────────────────────────┘

Memory Object:
┌─────────────────────────────┐
│ Fields:                     │
│ ├──容量: 16GB               │
│ └── type: "DDR4"            │
└─────────────────────────────┘
```

### Delegation Pattern

```
Client Code:
    computer.process()
        ↓
Computer Object:
    cpu.execute()  ← Delegates to CPU component
    memory.allocate() ← Delegates to Memory component
    storage.read() ← Delegates to Storage component
```

## Syntax

### Basic Composition

```java
class Engine {
    private int horsepower;

    public Engine(int horsepower) {
        this.horsepower = horsepower;
    }

    public void start() {
        System.out.println("Engine started with " + horsepower + " HP");
    }
}

class Car {
    private Engine engine; // "has-a" relationship
    private String model;

    public Car(String model, int horsepower) {
        this.model = model;
        this.engine = new Engine(horsepower); // Composition
    }

    public void start() {
        System.out.println("Starting " + model);
        engine.start(); // Delegation
    }
}
```

### Composition with Interface

```java
interface Drawable {
    void draw();
}

class Circle implements Drawable {
    @Override
    public void draw() {
        System.out.println("Drawing circle");
    }
}

class Square implements Drawable {
    @Override
    public void draw() {
        System.out.println("Drawing square");
    }
}

class Canvas {
    private List<Drawable> shapes; // Composition with interface

    public Canvas() {
        this.shapes = new ArrayList<>();
    }

    public void addShape(Drawable shape) {
        shapes.add(shape);
    }

    public void drawAll() {
        for (Drawable shape : shapes) {
            shape.draw(); // Polymorphic delegation
        }
    }
}
```

### Composition with Dependency Injection

```java
interface Logger {
    void log(String message);
}

class FileLogger implements Logger {
    @Override
    public void log(String message) {
        System.out.println("File: " + message);
    }
}

class DatabaseLogger implements Logger {
    @Override
    public void log(String message) {
        System.out.println("Database: " + message);
    }
}

class UserService {
    private final Logger logger; // Injected dependency

    public UserService(Logger logger) {
        this.logger = logger; // Composition through injection
    }

    public void processUser(String userId) {
        logger.log("Processing user: " + userId);
        // Business logic
    }
}
```

## Easy Examples

### Example 1: Computer System

**Problem Statement**: Design a computer system that uses composition to combine different hardware components (CPU, Memory, Storage) into a complete system, demonstrating how components can be independently configured and replaced.

**Implementation**:

```java
package academy.javaengineering.oop.composition;

class CPU {
    private String brand;
    private int cores;
    private double clockSpeed;

    public CPU(String brand, int cores, double clockSpeed) {
        this.brand = brand;
        this.cores = cores;
        this.clockSpeed = clockSpeed;
    }

    public void execute() {
        System.out.println(brand + " CPU (" + cores + " cores, " + clockSpeed + " GHz) executing instructions");
    }

    public String getBrand() { return brand; }
    public int getCores() { return cores; }
    public double getClockSpeed() { return clockSpeed; }
}

class Memory {
    private int capacityGB;
    private String type;
    private int speedMHz;

    public Memory(int capacityGB, String type, int speedMHz) {
        this.capacityGB = capacityGB;
        this.type = type;
        this.speedMHz = speedMHz;
    }

    public void allocate(int sizeMB) {
        System.out.println("Allocating " + sizeMB + "MB from " + capacityGB + "GB " + type + " memory");
    }

    public void read() {
        System.out.println("Reading from memory at " + speedMHz + " MHz");
    }

    public void write() {
        System.out.println("Writing to memory at " + speedMHz + " MHz");
    }

    public int getCapacityGB() { return capacityGB; }
    public String getType() { return type; }
}

class Storage {
    private String type;
    private int capacityGB;
    private int readSpeedMBs;
    private int writeSpeedMBs;

    public Storage(String type, int capacityGB, int readSpeedMBs, int writeSpeedMBs) {
        this.type = type;
        this.capacityGB = capacityGB;
        this.readSpeedMBs = readSpeedMBs;
        this.writeSpeedMBs = writeSpeedMBs;
    }

    public void read(String filename) {
        System.out.println("Reading '" + filename + "' from " + type + " at " + readSpeedMBs + " MB/s");
    }

    public void write(String filename) {
        System.out.println("Writing '" + filename + "' to " + type + " at " + writeSpeedMBs + " MB/s");
    }

    public String getType() { return type; }
    public int getCapacityGB() { return capacityGB; }
}

class Monitor {
    private int resolutionX;
    private int resolutionY;
    private int refreshRate;
    private String panelType;

    public Monitor(int resolutionX, int resolutionY, int refreshRate, String panelType) {
        this.resolutionX = resolutionX;
        this.resolutionY = resolutionY;
        this.refreshRate = refreshRate;
        this.panelType = panelType;
    }

    public void display(String content) {
        System.out.println("Displaying on " + resolutionX + "x" + resolutionY +
            " " + panelType + " monitor @ " + refreshRate + "Hz: " + content);
    }

    public int getResolutionX() { return resolutionX; }
    public int getResolutionY() { return resolutionY; }
}

class Computer {
    private String name;
    private CPU cpu;
    private Memory memory;
    private Storage storage;
    private Monitor monitor;
    private boolean isRunning;

    public Computer(String name, CPU cpu, Memory memory, Storage storage, Monitor monitor) {
        this.name = name;
        this.cpu = cpu;
        this.memory = memory;
        this.storage = storage;
        this.monitor = monitor;
        this.isRunning = false;
    }

    public void boot() {
        System.out.println("Booting " + name + "...");
        cpu.execute();
        memory.allocate(1024);
        System.out.println(name + " is ready!\n");
        isRunning = true;
    }

    public void shutdown() {
        System.out.println("Shutting down " + name + "...");
        isRunning = false;
        System.out.println(name + " has been shut down.\n");
    }

    public void runApplication(String appName) {
        if (!isRunning) {
            System.out.println("Computer is not running!");
            return;
        }

        System.out.println("Running " + appName + ":");
        cpu.execute();
        memory.read();
        storage.read(appName + ".exe");
        monitor.display("Application: " + appName);
        System.out.println();
    }

    public void installSoftware(String softwareName) {
        System.out.println("Installing " + softwareName + "...");
        storage.write(softwareName + ".zip");
        System.out.println(softwareName + " installed successfully!\n");
    }

    public void upgradeMemory(int additionalGB) {
        System.out.println("Upgrading memory from " + memory.getCapacityGB() + "GB");
        memory = new Memory(memory.getCapacityGB() + additionalGB, memory.getType(), 3200);
        System.out.println("Memory upgraded to " + memory.getCapacityGB() + "GB\n");
    }

    public void printSpecs() {
        System.out.println("=== " + name + " Specifications ===");
        System.out.println("CPU: " + cpu.getBrand() + " (" + cpu.getCores() + " cores, " +
            cpu.getClockSpeed() + " GHz)");
        System.out.println("Memory: " + memory.getCapacityGB() + "GB " + memory.getType());
        System.out.println("Storage: " + storage.getCapacityGB() + "GB " + storage.getType());
        System.out.println("Monitor: " + monitor.getResolutionX() + "x" + monitor.getResolutionY());
        System.out.println();
    }

    public String getName() { return name; }
    public boolean isRunning() { return isRunning; }
}

public class ComputerDemo {
    public static void main(String[] args) {
        // Create components
        CPU cpu = new CPU("Intel", 8, 3.6);
        Memory memory = new Memory(16, "DDR4", 3200);
        Storage storage = new Storage("SSD", 512, 3500, 3000);
        Monitor monitor = new Monitor(1920, 1080, 144, "IPS");

        // Create computer using composition
        Computer computer = new Computer("Gaming PC", cpu, memory, storage, monitor);

        // Use the computer
        computer.printSpecs();
        computer.boot();
        computer.runApplication("Chrome");
        computer.installSoftware("Visual Studio Code");
        computer.runApplication("Visual Studio Code");
        computer.upgradeMemory(16);
        computer.printSpecs();
        computer.shutdown();
    }
}
```

**Expected Output**:
```
=== Gaming PC Specifications ===
CPU: Intel (8 cores, 3.6 GHz)
Memory: 16GB DDR4
Storage: 512GB SSD
Monitor: 1920x1080

Booting Gaming PC...
Intel CPU (8 cores, 3.6 GHz) executing instructions
Allocating 1024MB from 16GB DDR4 memory
Gaming PC is ready!

Running Chrome:
Intel CPU (8 cores, 3.6 GHz) executing instructions
Reading from memory at 3200 MHz
Reading 'Chrome.exe' from SSD at 3500 MB/s
Displaying on 1920x1080 IPS monitor @ 144Hz: Application: Chrome

Installing Visual Studio Code...
Writing 'Visual Studio Code.zip' to SSD at 3000 MB/s
Visual Studio Code installed successfully!

Running Visual Studio Code:
Intel CPU (8 cores, 3.6 GHz) executing instructions
Reading from memory at 3200 MHz
Reading 'Visual Studio Code.exe' from SSD at 3500 MB/s
Displaying on 1920x1080 IPS monitor @ 144Hz: Application: Visual Studio Code

Upgrading memory from 16GB
Memory upgraded to 32GB

=== Gaming PC Specifications ===
CPU: Intel (8 cores, 3.6 GHz)
Memory: 32GB DDR4
Storage: 512GB SSD
Monitor: 1920x1080

Shutting down Gaming PC...
Gaming PC has been shut down.
```

**Best Practices**:
- Use composition to create "has-a" relationships
- Keep components focused and single-purpose
- Allow components to be independently replaceable
- Use interfaces for component types to enable flexibility

### Example 2: Music Player System

**Problem Statement**: Design a music player that uses composition to combine audio playback, playlist management, and user interface components into a cohesive application.

**Implementation**:

```java
package academy.javaengineering.oop.composition;

import java.util.ArrayList;
import java.util.List;

class Song {
    private String title;
    private String artist;
    private int durationSeconds;

    public Song(String title, String artist, int durationSeconds) {
        this.title = title;
        this.artist = artist;
        this.durationSeconds = durationSeconds;
    }

    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public int getDurationSeconds() { return durationSeconds; }

    public String getFormattedDuration() {
        int minutes = durationSeconds / 60;
        int seconds = durationSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    @Override
    public String toString() {
        return title + " - " + artist + " (" + getFormattedDuration() + ")";
    }
}

class Playlist {
    private String name;
    private List<Song> songs;
    private int currentSongIndex;

    public Playlist(String name) {
        this.name = name;
        this.songs = new ArrayList<>();
        this.currentSongIndex = 0;
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public void removeSong(int index) {
        if (index >= 0 && index < songs.size()) {
            songs.remove(index);
            if (currentSongIndex >= songs.size()) {
                currentSongIndex = 0;
            }
        }
    }

    public Song getCurrentSong() {
        if (songs.isEmpty()) return null;
        return songs.get(currentSongIndex);
    }

    public Song nextSong() {
        if (songs.isEmpty()) return null;
        currentSongIndex = (currentSongIndex + 1) % songs.size();
        return getCurrentSong();
    }

    public Song previousSong() {
        if (songs.isEmpty()) return null;
        currentSongIndex = (currentSongIndex - 1 + songs.size()) % songs.size();
        return getCurrentSong();
    }

    public void shuffle() {
        java.util.Collections.shuffle(songs);
        currentSongIndex = 0;
    }

    public List<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    public String getName() { return name; }
    public int getSongCount() { return songs.size(); }
    public boolean isEmpty() { return songs.isEmpty(); }
}

interface AudioDevice {
    void play(String audioSource);
    void pause();
    void resume();
    void stop();
    void setVolume(int volume);
    int getVolume();
    boolean isPlaying();
}

class Speaker implements AudioDevice {
    private int volume;
    private boolean playing;
    private String currentSource;

    public Speaker() {
        this.volume = 50;
        this.playing = false;
    }

    @Override
    public void play(String audioSource) {
        this.currentSource = audioSource;
        this.playing = true;
        System.out.println("Speaker: Playing '" + audioSource + "' at volume " + volume);
    }

    @Override
    public void pause() {
        if (playing) {
            playing = false;
            System.out.println("Speaker: Paused");
        }
    }

    @Override
    public void resume() {
        if (!playing && currentSource != null) {
            playing = true;
            System.out.println("Speaker: Resumed");
        }
    }

    @Override
    public void stop() {
        playing = false;
        currentSource = null;
        System.out.println("Speaker: Stopped");
    }

    @Override
    public void setVolume(int volume) {
        this.volume = Math.max(0, Math.min(100, volume));
        System.out.println("Speaker: Volume set to " + this.volume);
    }

    @Override
    public int getVolume() { return volume; }

    @Override
    public boolean isPlaying() { return playing; }
}

class Headphones implements AudioDevice {
    private int volume;
    private boolean playing;
    private boolean noiseCancelling;

    public Headphones(boolean noiseCancelling) {
        this.volume = 50;
        this.playing = false;
        this.noiseCancelling = noiseCancelling;
    }

    @Override
    public void play(String audioSource) {
        this.playing = true;
        System.out.println("Headphones: Playing '" + audioSource + "'" +
            (noiseCancelling ? " with noise cancelling" : ""));
    }

    @Override
    public void pause() {
        playing = false;
        System.out.println("Headphones: Paused");
    }

    @Override
    public void resume() {
        playing = true;
        System.out.println("Headphones: Resumed");
    }

    @Override
    public void stop() {
        playing = false;
        System.out.println("Headphones: Stopped");
    }

    @Override
    public void setVolume(int volume) {
        this.volume = Math.max(0, Math.min(100, volume));
        System.out.println("Headphones: Volume set to " + this.volume);
    }

    @Override
    public int getVolume() { return volume; }

    @Override
    public boolean isPlaying() { return playing; }

    public boolean isNoiseCancelling() { return noiseCancelling; }
}

class MusicPlayer {
    private Playlist playlist;
    private AudioDevice outputDevice;
    private boolean isPlaying;
    private int currentProgress;

    public MusicPlayer(AudioDevice outputDevice) {
        this.playlist = new Playlist("My Playlist");
        this.outputDevice = outputDevice;
        this.isPlaying = false;
        this.currentProgress = 0;
    }

    public void addSong(Song song) {
        playlist.addSong(song);
        System.out.println("Added to playlist: " + song);
    }

    public void play() {
        Song currentSong = playlist.getCurrentSong();
        if (currentSong == null) {
            System.out.println("Playlist is empty!");
            return;
        }

        outputDevice.play(currentSong.toString());
        isPlaying = true;
        currentProgress = 0;
    }

    public void pause() {
        if (isPlaying) {
            outputDevice.pause();
            isPlaying = false;
        }
    }

    public void resume() {
        if (!isPlaying) {
            outputDevice.resume();
            isPlaying = true;
        }
    }

    public void stop() {
        outputDevice.stop();
        isPlaying = false;
        currentProgress = 0;
    }

    public void nextTrack() {
        Song nextSong = playlist.nextSong();
        if (nextSong != null) {
            System.out.println("Next track:");
            if (isPlaying) {
                outputDevice.play(nextSong.toString());
            }
        }
    }

    public void previousTrack() {
        Song prevSong = playlist.previousSong();
        if (prevSong != null) {
            System.out.println("Previous track:");
            if (isPlaying) {
                outputDevice.play(prevSong.toString());
            }
        }
    }

    public void setVolume(int volume) {
        outputDevice.setVolume(volume);
    }

    public void shufflePlaylist() {
        playlist.shuffle();
        System.out.println("Playlist shuffled!");
    }

    public void displayPlaylist() {
        System.out.println("\n=== " + playlist.getName() + " ===");
        List<Song> songs = playlist.getSongs();
        for (int i = 0; i < songs.size(); i++) {
            String marker = (i == 0) ? " >> " : "    ";
            System.out.println(marker + (i + 1) + ". " + songs.get(i));
        }
        System.out.println();
    }

    public Playlist getPlaylist() { return playlist; }
    public AudioDevice getOutputDevice() { return outputDevice; }
    public boolean isPlaying() { return isPlaying; }
}

public class MusicPlayerDemo {
    public static void main(String[] args) {
        // Create audio device (composition)
        AudioDevice headphones = new Headphones(true);

        // Create music player with composed components
        MusicPlayer player = new MusicPlayer(headphones);

        // Add songs to playlist
        player.addSong(new Song("Bohemian Rhapsody", "Queen", 354));
        player.addSong(new Song("Hotel California", "Eagles", 391));
        player.addSong(new Song("Stairway to Heaven", "Led Zeppelin", 482));
        player.addSong(new Song("Imagine", "John Lennon", 183));

        // Display playlist
        player.displayPlaylist();

        // Play music
        System.out.println("=== Playing Music ===");
        player.play();
        player.setVolume(75);
        player.nextTrack();
        player.previousTrack();
        player.shufflePlaylist();
        player.displayPlaylist();

        // Switch output device
        System.out.println("=== Switching to Speaker ===");
        player.stop();
        player.setOutputDevice(new Speaker());
        player.play();
    }
}
```

**Expected Output**:
```
Added to playlist: Bohemian Rhapsody - Queen (5:54)
Added to playlist: Hotel California - Eagles (6:31)
Added to playlist: Stairway to Heaven - Led Zeppelin (8:02)
Added to playlist: Imagine - John Lennon (3:03)

=== My Playlist ===
 >> 1. Bohemian Rhapsody - Queen (5:54)
    2. Hotel California - Eagles (6:31)
    3. Stairway to Heaven - Led Zeppelin (8:02)
    4. Imagine - John Lennon (3:03)

=== Playing Music ===
Headphones: Playing 'Bohemian Rhapsody - Queen (5:54)' with noise cancelling
Headphones: Volume set to 75
Next track:
Headphones: Playing 'Hotel California - Eagles (6:31)' with noise cancelling
Previous track:
Headphones: Playing 'Bohemian Rhapsody - Queen (5:54)' with noise cancelling
Playlist shuffled!

=== My Playlist ===
 >> 1. Stairway to Heaven - Led Zeppelin (8:02)
    2. Imagine - John Lennon (3:03)
    3. Bohemian Rhapsody - Queen (5:54)
    4. Hotel California - Eagles (6:31)

=== Switching to Speaker ===
Headphones: Stopped
Speaker: Playing 'Stairway to Heaven - Led Zeppelin (8:02)' at volume 75
```

**Best Practices**:
- Use composition to combine independent components
- Allow components to be swapped at runtime
- Keep component interfaces small and focused
- Document the relationships between components

## Medium Examples

### Example 1: Web Server with Middleware

**Problem Statement**: Design a web server that uses composition to combine request handling, middleware processing, and response generation into a flexible request processing pipeline.

**Requirements**:

- Support multiple middleware components
- Allow middleware to be added/removed dynamically
- Process requests through the middleware chain
- Generate appropriate responses

**Implementation**:

```java
package academy.javaengineering.oop.composition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class HttpRequest {
    private String method;
    private String path;
    private Map<String, String> headers;
    private String body;
    private Map<String, Object> attributes;

    public HttpRequest(String method, String path) {
        this.method = method;
        this.path = path;
        this.headers = new HashMap<>();
        this.attributes = new HashMap<>();
    }

    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public String getMethod() { return method; }
    public String getPath() { return path; }
    public Map<String, String> getHeaders() { return headers; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
}

class HttpResponse {
    private int statusCode;
    private String statusText;
    private Map<String, String> headers;
    private String body;

    public HttpResponse(int statusCode, String statusText) {
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.headers = new HashMap<>();
    }

    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    public void setBody(String body) {
        this.body = body;
        setHeader("Content-Length", String.valueOf(body.length()));
    }

    public int getStatusCode() { return statusCode; }
    public String getStatusText() { return statusText; }
    public Map<String, String> getHeaders() { return headers; }
    public String getBody() { return body; }
}

interface Middleware {
    void handle(HttpRequest request, HttpResponse response, MiddlewareChain chain);
    String getName();
}

class MiddlewareChain {
    private List<Middleware> middlewares;
    private int index;
    private RequestHandler finalHandler;

    public MiddlewareChain(List<Middleware> middlewares, RequestHandler finalHandler) {
        this.middlewares = new ArrayList<>(middlewares);
        this.index = 0;
        this.finalHandler = finalHandler;
    }

    public void proceed(HttpRequest request, HttpResponse response) {
        if (index < middlewares.size()) {
            Middleware middleware = middlewares.get(index++);
            middleware.handle(request, response, this);
        } else {
            finalHandler.handle(request, response);
        }
    }
}

interface RequestHandler {
    void handle(HttpRequest request, HttpResponse response);
}

class LoggingMiddleware implements Middleware {
    @Override
    public void handle(HttpRequest request, HttpResponse response, MiddlewareChain chain) {
        System.out.println("[LOG] " + request.getMethod() + " " + request.getPath());
        long startTime = System.currentTimeMillis();

        chain.proceed(request, response);

        long duration = System.currentTimeMillis() - startTime;
        System.out.println("[LOG] Response: " + response.getStatusCode() + " (" + duration + "ms)");
    }

    @Override
    public String getName() { return "Logging"; }
}

class AuthenticationMiddleware implements Middleware {
    @Override
    public void handle(HttpRequest request, HttpResponse response, MiddlewareChain chain) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatusCode(401);
            response.setBody("Unauthorized");
            System.out.println("[AUTH] Missing or invalid token");
            return;
        }

        // Simulate token validation
        String token = authHeader.substring(7);
        if (!"valid-token".equals(token)) {
            response.setStatusCode(403);
            response.setBody("Forbidden");
            System.out.println("[AUTH] Invalid token");
            return;
        }

        request.setAttribute("userId", "user123");
        System.out.println("[AUTH] Token validated for user: user123");
        chain.proceed(request, response);
    }

    @Override
    public String getName() { return "Authentication"; }
}

class RateLimitMiddleware implements Middleware {
    private int maxRequests;
    private int currentRequests;

    public RateLimitMiddleware(int maxRequests) {
        this.maxRequests = maxRequests;
        this.currentRequests = 0;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, MiddlewareChain chain) {
        currentRequests++;

        if (currentRequests > maxRequests) {
            response.setStatusCode(429);
            response.setBody("Too Many Requests");
            System.out.println("[RATE] Rate limit exceeded");
            return;
        }

        System.out.println("[RATE] Request " + currentRequests + "/" + maxRequests);
        chain.proceed(request, response);
    }

    @Override
    public String getName() { return "RateLimit"; }
}

class CorsMiddleware implements Middleware {
    @Override
    public void handle(HttpRequest request, HttpResponse response, MiddlewareChain chain) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        System.out.println("[CORS] Headers added");
        chain.proceed(request, response);
    }

    @Override
    public String getName() { return "CORS"; }
}

class WebServer {
    private List<Middleware> middlewares;
    private Map<String, RequestHandler> routes;

    public WebServer() {
        this.middlewares = new ArrayList<>();
        this.routes = new HashMap<>();
    }

    public void addMiddleware(Middleware middleware) {
        middlewares.add(middleware);
        System.out.println("Added middleware: " + middleware.getName());
    }

    public void removeMiddleware(String name) {
        middlewares.removeIf(m -> m.getName().equals(name));
    }

    public void addRoute(String path, RequestHandler handler) {
        routes.put(path, handler);
    }

    public HttpResponse handleRequest(HttpRequest request) {
        HttpResponse response = new HttpResponse(200, "OK");

        RequestHandler handler = routes.get(request.getPath());
        if (handler == null) {
            response.setStatusCode(404);
            response.setBody("Not Found");
            return response;
        }

        MiddlewareChain chain = new MiddlewareChain(middlewares, handler);
        chain.proceed(request, response);

        return response;
    }

    public void printRoutes() {
        System.out.println("\n=== Registered Routes ===");
        for (String path : routes.keySet()) {
            System.out.println("  " + path);
        }
        System.out.println();
    }
}

public class WebServerDemo {
    public static void main(String[] args) {
        WebServer server = new WebServer();

        // Add middleware (composition)
        server.addMiddleware(new LoggingMiddleware());
        server.addMiddleware(new RateLimitMiddleware(5));
        server.addMiddleware(new CorsMiddleware());
        server.addMiddleware(new AuthenticationMiddleware());

        // Add routes
        server.addRoute("/api/users", (req, res) -> {
            res.setBody("{\"users\": [\"Alice\", \"Bob\"]}");
        });

        server.addRoute("/api/profile", (req, res) -> {
            String userId = (String) req.getAttribute("userId");
            res.setBody("{\"userId\": \"" + userId + "\", \"name\": \"John\"}");
        });

        server.printRoutes();

        // Process requests
        System.out.println("=== Request 1: Public Endpoint ===");
        HttpRequest request1 = new HttpRequest("GET", "/api/users");
        HttpResponse response1 = server.handleRequest(request1);
        System.out.println("Response: " + response1.getStatusCode() + " " + response1.getBody());

        System.out.println("\n=== Request 2: Authenticated Endpoint ===");
        HttpRequest request2 = new HttpRequest("GET", "/api/profile");
        request2.setHeader("Authorization", "Bearer valid-token");
        HttpResponse response2 = server.handleRequest(request2);
        System.out.println("Response: " + response2.getStatusCode() + " " + response2.getBody());

        System.out.println("\n=== Request 3: Invalid Token ===");
        HttpRequest request3 = new HttpRequest("GET", "/api/profile");
        request3.setHeader("Authorization", "Bearer invalid-token");
        HttpResponse response3 = server.handleRequest(request3);
        System.out.println("Response: " + response3.getStatusCode() + " " + response3.getBody());
    }
}
```

**Expected Output**:
```
Added middleware: Logging
Added middleware: RateLimit
Added middleware: CORS
Added middleware: Authentication

=== Registered Routes ===
  /api/users
  /api/profile

=== Request 1: Public Endpoint ===
[LOG] GET /api/users
[RATE] Request 1/5
[CORS] Headers added
[AUTH] Missing or invalid token
[LOG] Response: 401 (2ms)
Response: 401 Unauthorized

=== Request 2: Authenticated Endpoint ===
[LOG] GET /api/profile
[RATE] Request 2/5
[CORS] Headers added
[AUTH] Token validated for user: user123
[LOG] Response: 200 (1ms)
Response: 200 {"userId": "user123", "name": "John"}

=== Request 3: Invalid Token ===
[LOG] GET /api/profile
[RATE] Request 3/5
[CORS] Headers added
[AUTH] Invalid token
[LOG] Response: 403 (1ms)
Response: 403 Forbidden
```

**Code Walkthrough**:

1. **Middleware Interface**: Defines the contract for request processing components
2. **MiddlewareChain**: Manages the execution order of middleware components
3. **Concrete Middleware**: Each middleware handles a specific concern (logging, auth, etc.)
4. **WebServer**: Composes middleware and routes to handle requests

**Alternative Solution**:

```java
// Using functional composition
class FunctionalWebServer {
    private List<Function<HttpRequest, Optional<HttpResponse>>> filters;

    public FunctionalWebServer() {
        this.filters = new ArrayList<>();
    }

    public FunctionalWebServer addFilter(Function<HttpRequest, Optional<HttpResponse>> filter) {
        filters.add(filter);
        return this;
    }

    public HttpResponse handleRequest(HttpRequest request) {
        for (Function<HttpRequest, Optional<HttpResponse>> filter : filters) {
            Optional<HttpResponse> response = filter.apply(request);
            if (response.isPresent()) {
                return response.get();
            }
        }
        return new HttpResponse(200, "OK");
    }
}
```

## Hard Examples

### Example 1: Game Engine Entity System

**Problem Statement**: Design a game engine entity system that uses composition to build complex game entities from reusable components, supporting dynamic component addition/removal and component-based messaging.

**Requirements**:

- Component-based entity architecture
- Dynamic component management
- Component communication system
- Entity lifecycle management
- Performance optimization with component caching

**Architecture**:

```
Game Engine Entity System
├── Entity (container for components)
├── Component (base interface)
│   ├── TransformComponent
│   ├── PhysicsComponent
│   ├── RenderComponent
│   ├── ColliderComponent
│   └── AIComponent
├── ComponentManager (manages component types)
├── EntityManager (manages entities)
└── SystemManager (manages systems that process components)
```

**Implementation**:

```java
package academy.javaengineering.oop.composition;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// Component base interface
interface Component {
    void initialize(Entity entity);
    void update(double deltaTime);
    void render();
    void destroy();
    default Class<? extends Component> getType() {
        return getClass();
    }
}

// Entity class
class Entity {
    private final String id;
    private final String name;
    private boolean active;
    private final Map<Class<? extends Component>, Component> components;
    private final Map<String, Object> properties;

    public Entity(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.active = true;
        this.components = new ConcurrentHashMap<>();
        this.properties = new ConcurrentHashMap<>();
    }

    public <T extends Component> void addComponent(T component) {
        components.put(component.getType(), component);
        component.initialize(this);
    }

    public <T extends Component> T getComponent(Class<T> type) {
        Component component = components.get(type);
        return component != null ? type.cast(component) : null;
    }

    public boolean hasComponent(Class<? extends Component> type) {
        return components.containsKey(type);
    }

    public void removeComponent(Class<? extends Component> type) {
        Component component = components.remove(type);
        if (component != null) {
            component.destroy();
        }
    }

    public void update(double deltaTime) {
        if (!active) return;
        for (Component component : components.values()) {
            component.update(deltaTime);
        }
    }

    public void render() {
        if (!active) return;
        for (Component component : components.values()) {
            component.render();
        }
    }

    public void destroy() {
        for (Component component : components.values()) {
            component.destroy();
        }
        components.clear();
        active = false;
    }

    public void setProperty(String key, Object value) {
        properties.put(key, value);
    }

    public <T> T getProperty(String key, Class<T> type) {
        Object value = properties.get(key);
        return value != null ? type.cast(value) : null;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}

// Concrete components
class TransformComponent implements Component {
    private double x, y, z;
    private double rotationX, rotationY, rotationZ;
    private double scaleX, scaleY, scaleZ;

    public TransformComponent() {
        this(0, 0, 0);
    }

    public TransformComponent(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.scaleX = 1.0;
        this.scaleY = 1.0;
        this.scaleZ = 1.0;
    }

    @Override
    public void initialize(Entity entity) {}

    @Override
    public void update(double deltaTime) {}

    @Override
    public void render() {}

    @Override
    public void destroy() {}

    public void translate(double dx, double dy, double dz) {
        this.x += dx;
        this.y += dy;
        this.z += dz;
    }

    public void rotate(double rx, double ry, double rz) {
        this.rotationX += rx;
        this.rotationY += ry;
        this.rotationZ += rz;
    }

    // Getters and setters
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public void setPosition(double x, double y, double z) {
        this.x = x; this.y = y; this.z = z;
    }
}

class PhysicsComponent implements Component {
    private double velocityX, velocityY, velocityZ;
    private double acceleration;
    private double mass;
    private boolean isStatic;
    private Entity owner;

    public PhysicsComponent(double mass, boolean isStatic) {
        this.mass = mass;
        this.isStatic = isStatic;
        this.acceleration = 9.8; // Gravity
    }

    @Override
    public void initialize(Entity entity) {
        this.owner = entity;
    }

    @Override
    public void update(double deltaTime) {
        if (isStatic) return;

        TransformComponent transform = owner.getComponent(TransformComponent.class);
        if (transform != null) {
            velocityY += acceleration * deltaTime;
            transform.translate(
                velocityX * deltaTime,
                velocityY * deltaTime,
                velocityZ * deltaTime
            );
        }
    }

    @Override
    public void render() {}

    @Override
    public void destroy() {
        owner = null;
    }

    public void applyForce(double fx, double fy, double fz) {
        if (!isStatic) {
            velocityX += fx / mass;
            velocityY += fy / mass;
            velocityZ += fz / mass;
        }
    }

    public void setVelocity(double vx, double vy, double vz) {
        this.velocityX = vx;
        this.velocityY = vy;
        this.velocityZ = vz;
    }

    // Getters
    public double getVelocityX() { return velocityX; }
    public double getVelocityY() { return velocityY; }
    public double getVelocityZ() { return velocityZ; }
    public boolean isStatic() { return isStatic; }
}

class RenderComponent implements Component {
    private String spritePath;
    private int width, height;
    private boolean visible;
    private int zIndex;
    private Entity owner;

    public RenderComponent(String spritePath, int width, int height) {
        this.spritePath = spritePath;
        this.width = width;
        this.height = height;
        this.visible = true;
        this.zIndex = 0;
    }

    @Override
    public void initialize(Entity entity) {
        this.owner = entity;
    }

    @Override
    public void update(double deltaTime) {}

    @Override
    public void render() {
        if (!visible) return;

        TransformComponent transform = owner.getComponent(TransformComponent.class);
        if (transform != null) {
            System.out.printf("Rendering %s at (%.1f, %.1f) size %dx%d%n",
                spritePath, transform.getX(), transform.getY(), width, height);
        }
    }

    @Override
    public void destroy() {
        owner = null;
    }

    public void setVisible(boolean visible) { this.visible = visible; }
    public void setZIndex(int zIndex) { this.zIndex = zIndex; }
    public boolean isVisible() { return visible; }
}

class ColliderComponent implements Component {
    private double width, height;
    private boolean isTrigger;
    private Entity owner;
    private List<ColliderComponent> collisions;

    public ColliderComponent(double width, double height, boolean isTrigger) {
        this.width = width;
        this.height = height;
        this.isTrigger = isTrigger;
        this.collisions = new ArrayList<>();
    }

    @Override
    public void initialize(Entity entity) {
        this.owner = entity;
    }

    @Override
    public void update(double deltaTime) {
        collisions.clear();
    }

    @Override
    public void render() {}

    @Override
    public void destroy() {
        owner = null;
        collisions.clear();
    }

    public boolean checkCollision(ColliderComponent other) {
        TransformComponent t1 = owner.getComponent(TransformComponent.class);
        TransformComponent t2 = other.owner.getComponent(TransformComponent.class);

        if (t1 == null || t2 == null) return false;

        boolean collision = t1.getX() < other.getX() + other.width &&
                          t1.getX() + width > other.getX() &&
                          t1.getY() < other.getY() + other.height &&
                          t1.getY() + height > other.getY();

        if (collision && !collisions.contains(other)) {
            collisions.add(other);
        }

        return collision;
    }

    public boolean isTrigger() { return isTrigger; }
    public List<ColliderComponent> getCollisions() { return new ArrayList<>(collisions); }
}

class HealthComponent implements Component {
    private int currentHealth;
    private int maxHealth;
    private boolean invulnerable;
    private Entity owner;

    public HealthComponent(int maxHealth) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.invulnerable = false;
    }

    @Override
    public void initialize(Entity entity) {
        this.owner = entity;
    }

    @Override
    public void update(double deltaTime) {}

    @Override
    public void render() {}

    @Override
    public void destroy() {
        owner = null;
    }

    public boolean takeDamage(int damage) {
        if (invulnerable || damage <= 0) return false;

        currentHealth -= damage;
        System.out.println(owner.getName() + " took " + damage + " damage. Health: " + currentHealth);

        if (currentHealth <= 0) {
            currentHealth = 0;
            owner.setActive(false);
            System.out.println(owner.getName() + " has been destroyed!");
            return true;
        }
        return false;
    }

    public void heal(int amount) {
        currentHealth = Math.min(currentHealth + amount, maxHealth);
    }

    public int getCurrentHealth() { return currentHealth; }
    public int getMaxHealth() { return maxHealth; }
    public boolean isAlive() { return currentHealth > 0; }
    public void setInvulnerable(boolean invulnerable) { this.invulnerable = invulnerable; }
}

// Entity Manager
class EntityManager {
    private final Map<String, Entity> entities;
    private final List<Entity> entitiesToAdd;
    private final List<String> entitiesToRemove;

    public EntityManager() {
        this.entities = new ConcurrentHashMap<>();
        this.entitiesToAdd = new ArrayList<>();
        this.entitiesToRemove = new ArrayList<>();
    }

    public Entity createEntity(String name) {
        Entity entity = new Entity(name);
        entitiesToAdd.add(entity);
        return entity;
    }

    public void destroyEntity(String entityId) {
        entitiesToRemove.add(entityId);
    }

    public void update(double deltaTime) {
        // Add pending entities
        for (Entity entity : entitiesToAdd) {
            entities.put(entity.getId(), entity);
        }
        entitiesToAdd.clear();

        // Remove pending entities
        for (String entityId : entitiesToRemove) {
            Entity entity = entities.remove(entityId);
            if (entity != null) {
                entity.destroy();
            }
        }
        entitiesToRemove.clear();

        // Update all entities
        for (Entity entity : entities.values()) {
            entity.update(deltaTime);
        }
    }

    public void render() {
        for (Entity entity : entities.values()) {
            entity.render();
        }
    }

    public Entity getEntity(String entityId) {
        return entities.get(entityId);
    }

    public List<Entity> getEntitiesWithComponent(Class<? extends Component> componentType) {
        List<Entity> result = new ArrayList<>();
        for (Entity entity : entities.values()) {
            if (entity.hasComponent(componentType)) {
                result.add(entity);
            }
        }
        return result;
    }

    public int getEntityCount() {
        return entities.size();
    }
}

// Game Engine
class GameEngine {
    private final EntityManager entityManager;
    private boolean running;
    private long lastUpdateTime;

    public GameEngine() {
        this.entityManager = new EntityManager();
        this.running = false;
    }

    public Entity createEntity(String name) {
        return entityManager.createEntity(name);
    }

    public void start() {
        running = true;
        lastUpdateTime = System.currentTimeMillis();
        System.out.println("Game engine started!");
    }

    public void update() {
        if (!running) return;

        long currentTime = System.currentTimeMillis();
        double deltaTime = (currentTime - lastUpdateTime) / 1000.0;
        lastUpdateTime = currentTime;

        entityManager.update(deltaTime);
    }

    public void render() {
        if (!running) return;
        entityManager.render();
    }

    public void stop() {
        running = false;
        System.out.println("Game engine stopped!");
    }

    public EntityManager getEntityManager() { return entityManager; }
}

public class GameEngineDemo {
    public static void main(String[] args) {
        GameEngine engine = new GameEngine();
        engine.start();

        System.out.println("\n=== Creating Game Entities ===");

        // Create player entity with components
        Entity player = engine.createEntity("Player");
        player.addComponent(new TransformComponent(0, 0, 0));
        player.addComponent(new PhysicsComponent(1.0, false));
        player.addComponent(new RenderComponent("player.png", 32, 32));
        player.addComponent(new ColliderComponent(32, 32, false));
        player.addComponent(new HealthComponent(100));

        // Create enemy entity
        Entity enemy = engine.createEntity("Enemy");
        enemy.addComponent(new TransformComponent(10, 0, 0));
        enemy.addComponent(new PhysicsComponent(0.8, false));
        enemy.addComponent(new RenderComponent("enemy.png", 24, 24));
        enemy.addComponent(new ColliderComponent(24, 24, false));
        enemy.addComponent(new HealthComponent(50));

        // Create ground entity
        Entity ground = engine.createEntity("Ground");
        ground.addComponent(new TransformComponent(0, -10, 0));
        ground.addComponent(new PhysicsComponent(0, true));
        ground.addComponent(new RenderComponent("ground.png", 100, 20));
        ground.addComponent(new ColliderComponent(100, 20, true));

        System.out.println("\n=== Game Loop ===");
        for (int i = 0; i < 5; i++) {
            System.out.println("\nFrame " + (i + 1) + ":");
            engine.update();
            engine.render();
        }

        // Simulate combat
        System.out.println("\n=== Combat Simulation ===");
        HealthComponent playerHealth = player.getComponent(HealthComponent.class);
        HealthComponent enemyHealth = enemy.getComponent(HealthComponent.class);

        enemyHealth.takeDamage(20);
        playerHealth.takeDamage(15);
        enemyHealth.takeDamage(30);
        playerHealth.heal(10);

        // Check entity status
        System.out.println("\n=== Entity Status ===");
        System.out.println("Player active: " + player.isActive());
        System.out.println("Enemy active: " + enemy.isActive());
        System.out.println("Total entities: " + engine.getEntityManager().getEntityCount());

        engine.stop();
    }
}
```

**Execution Flow**:

1. **Entity Creation**: Entities are created with unique IDs and names
2. **Component Addition**: Components are added to entities, establishing "has-a" relationships
3. **Game Loop**: Each frame, all entities and their components are updated
4. **Component Processing**: Components update their state based on game logic
5. **Rendering**: Render components draw entities to the screen
6. **Cleanup**: Entities and components are destroyed when no longer needed

**Unit Tests**:

```java
public class GameEngineTest {
    public static void main(String[] args) {
        System.out.println("=== Running Game Engine Tests ===\n");

        testEntityCreation();
        testComponentAddition();
        testComponentRemoval();
        testEntityLifecycle();

        System.out.println("\n=== All Tests Passed ===");
    }

    private static void testEntityCreation() {
        System.out.println("Test 1: Entity Creation");
        EntityManager manager = new EntityManager();

        Entity entity = manager.createEntity("TestEntity");
        assert entity != null : "Entity should not be null";
        assert entity.getName().equals("TestEntity") : "Entity name incorrect";
        assert entity.isActive() : "Entity should be active";
        assert manager.getEntityCount() == 1 : "Entity count should be 1";

        System.out.println("  PASS: Entity creation test passed\n");
    }

    private static void testComponentAddition() {
        System.out.println("Test 2: Component Addition");
        Entity entity = new Entity("Test");

        TransformComponent transform = new TransformComponent(10, 20, 0);
        entity.addComponent(transform);

        assert entity.hasComponent(TransformComponent.class) : "Should have TransformComponent";
        assert entity.getComponent(TransformComponent.class) == transform : "Should return same component";
        assert entity.getComponent(TransformComponent.class).getX() == 10.0 : "X should be 10";

        System.out.println("  PASS: Component addition test passed\n");
    }

    private static void testComponentRemoval() {
        System.out.println("Test 3: Component Removal");
        Entity entity = new Entity("Test");

        TransformComponent transform = new TransformComponent();
        entity.addComponent(transform);
        assert entity.hasComponent(TransformComponent.class) : "Should have component";

        entity.removeComponent(TransformComponent.class);
        assert !entity.hasComponent(TransformComponent.class) : "Should not have component";

        System.out.println("  PASS: Component removal test passed\n");
    }

    private static void testEntityLifecycle() {
        System.out.println("Test 4: Entity Lifecycle");
        EntityManager manager = new EntityManager();

        Entity entity = manager.createEntity("LifecycleTest");
        assert manager.getEntityCount() == 1 : "Should have 1 entity";

        manager.destroyEntity(entity.getId());
        manager.update(0.016); // Process destroy queue

        assert manager.getEntityCount() == 0 : "Should have 0 entities";

        System.out.println("  PASS: Entity lifecycle test passed\n");
    }
}
```

**Complexity**:

- **Time Complexity**: O(1) for component access, O(n) for entity updates
- **Space Complexity**: O(n * c) where n is entities and c is average components per entity

**Best Practices**:

- Use composition to build complex entities from simple, reusable components
- Keep components focused on single responsibilities
- Use interfaces for component types to enable flexibility
- Consider performance implications of component-based architecture
- Document the relationships and dependencies between components

## Exercises

### Easy

1. **Car System**: Design a Car class that uses composition to combine Engine, Transmission, and FuelTank components.

2. **House System**: Create a House class composed of Rooms, each containing Furniture components.

3. **Computer Build**: Build a Computer using composition with swappable CPU, GPU, and RAM components.

### Medium

1. **File System**: Implement a file system using composition with Files, Folders, and Permissions components.

2. **E-commerce Cart**: Design a shopping cart that composes Products, Discounts, and Shipping calculators.

3. **Chat Application**: Create a chat system using composition with Users, Messages, and Channels.

### Hard

1. **Plugin Architecture**: Design a plugin system where plugins are composed of multiple functional components.

2. **Game Engine**: Build a game engine entity system using composition for behaviors, rendering, and physics.

3. **Workflow Engine**: Create a workflow system using composition for steps, conditions, and actions.

## Interview Questions

### Easy

1. **What is composition?**
   Composition is a design principle where objects are built by combining other objects as members, creating "has-a" relationships. It provides greater flexibility than inheritance by allowing components to be independently replaced and configured.

2. **How does composition differ from inheritance?**
   Inheritance creates "is-a" relationships (Dog is an Animal), while composition creates "has-a" relationships (Car has an Engine). Composition is more flexible because components can be swapped at runtime, while inheritance is static.

3. **What is delegation in composition?**
   Delegation is when an object forwards a method call to one of its composed components. This allows the composite object to reuse the behavior of its components without inheriting from them.

### Medium

1. **When should you use composition over inheritance?**
   Use composition when you need runtime flexibility, when components can be independently replaced, when you want to avoid tight coupling, or when you need behaviors from multiple sources. Use inheritance when there's a clear "is-a" relationship and the hierarchy is stable.

2. **How does composition support the Single Responsibility Principle?**
   Composition allows you to break complex objects into smaller, focused components, each responsible for a single aspect of behavior. This makes the code easier to understand, test, and maintain.

3. **What are the trade-offs of composition vs inheritance?**
   Composition provides more flexibility but can lead to more boilerplate code (delegation methods). Inheritance provides code reuse but creates tighter coupling. Composition is generally preferred for most use cases.

### Hard

1. **How do you design a component-based architecture for a large system?**
   Design small, focused components with clear interfaces. Use dependency injection to manage component relationships. Implement a component lifecycle for initialization and cleanup. Consider performance implications of component lookup and communication.

2. **How does composition affect testing and mocking?**
   Composition makes testing easier because components can be independently tested and mocked. You can create test doubles for individual components without affecting the entire system. This supports better test isolation and faster test execution.

## Common Pitfalls

### 1. Creating Too Many Delegation Methods

**Wrong**:
```java
class Engine {
    public void start() { /* ... */ }
    public void stop() { /* ... */ }
    public boolean isRunning() { /* ... */ }
    public int getHorsepower() { /* ... */ }
}

class Car {
    private Engine engine;

    // Too many delegation methods
    public void start() { engine.start(); }
    public void stop() { engine.stop(); }
    public boolean isRunning() { engine.isRunning(); }
    public int getHorsepower() { return engine.getHorsepower(); }
    // ... many more delegation methods
}
```

**Right**:
```java
class Engine {
    public void start() { /* ... */ }
    public void stop() { /* ... */ }
    public boolean isRunning() { /* ... */ }
    public int getHorsepower() { /* ... */ }
}

class Car {
    private Engine engine;

    // Expose component directly when appropriate
    public Engine getEngine() { return engine; }

    // Only delegate when you need to add behavior
    public void start() {
        System.out.println("Starting car...");
        engine.start();
    }
}
```

### 2. Tight Coupling Through Composition

**Wrong**:
```java
class ConcreteEngine {
    public void start() { /* ... */ }
}

class Car {
    private ConcreteEngine engine; // Tight coupling to concrete class

    public Car() {
        this.engine = new ConcreteEngine(); // Cannot change engine type
    }
}
```

**Right**:
```java
interface Engine {
    void start();
    void stop();
}

class ElectricEngine implements Engine {
    public void start() { System.out.println("Electric engine started"); }
    public void stop() { System.out.println("Electric engine stopped"); }
}

class GasEngine implements Engine {
    public void start() { System.out.println("Gas engine started"); }
    public void stop() { System.out.println("Gas engine stopped"); }
}

class Car {
    private Engine engine; // Depends on interface

    public Car(Engine engine) { // Dependency injection
        this.engine = engine;
    }
}
```

### 3. Not Managing Component Lifecycle

**Wrong**:
```java
class DatabaseConnection {
    private Connection connection;

    public DatabaseConnection() {
        this.connection = createConnection();
    }

    // Missing cleanup method
}

class Service {
    private DatabaseConnection db;

    public Service() {
        this.db = new DatabaseConnection(); // Connection never closed
    }
}
```

**Right**:
```java
class DatabaseConnection implements AutoCloseable {
    private Connection connection;

    public DatabaseConnection() {
        this.connection = createConnection();
    }

    @Override
    public void close() {
        if (connection != null) {
            connection.close();
        }
    }
}

class Service implements AutoCloseable {
    private DatabaseConnection db;

    public Service() {
        this.db = new DatabaseConnection();
    }

    @Override
    public void close() {
        db.close(); // Properly cleanup components
    }
}
```

## Best Practices

1. **Favor composition over inheritance**: Use composition to create flexible, maintainable designs. Inheritance should be used sparingly and only when there's a clear "is-a" relationship.

2. **Program to interfaces**: Depend on component interfaces rather than concrete implementations. This allows components to be swapped without changing the composite class.

3. **Keep components focused**: Each component should have a single responsibility. This makes components easier to understand, test, and reuse.

4. **Manage component lifecycle**: Ensure components are properly initialized and cleaned up. Implement AutoCloseable for components that hold resources.

5. **Document relationships**: Clearly document the relationships between components and any assumptions about their usage.

## Real World Usage

### How Spring Uses This

Spring Framework uses composition extensively:

- **ApplicationContext**: Composes BeanFactory, Environment, and EventPublisher
- **Transaction Management**: Composes TransactionManager, PlatformTransactionManager
- **Security**: Composes AuthenticationManager, AuthorizationManager

### How Hibernate Uses This

Hibernate ORM uses composition for:

- **SessionFactory**: Composes Configuration, Mapping, and Cache
- **Session**: Composes PersistenceContext, ActionQueue, and EventListenerGroup
- **Query**: Composes QueryParameters, ReturnParameters, and AutoDiscoverReturnType

### How JDK Uses This

The Java Development Kit uses composition in:

- **Collections**: ArrayList composes Object[], LinkedList composes Node elements
- **I/O Streams**: BufferedInputStream composes InputStream and byte buffer
- **Thread**: Thread composes Runnable and ThreadLocal variables

### Enterprise Usage

In enterprise applications, composition is used for:

- **Service Layer**: Services compose repositories, external clients, and utilities
- **REST Controllers**: Controllers compose request handlers, validators, and response mappers
- **Configuration**: Configuration objects compose property sources and converters

## References

1. **Effective Java** by Joshua Bloch - Item 18: Favor composition over inheritance
2. **Design Patterns** - Composite, Decorator, and Proxy patterns
3. **Head First Design Patterns** - Composition and delegation
4. **Clean Architecture** by Robert C. Martin - Component-based design
5. **Java Concurrency in Practice** - Thread composition patterns

## Summary

- Composition creates "has-a" relationships by combining objects as members
- It provides greater flexibility than inheritance for most use cases
- Delegation allows composite objects to forward method calls to components
- Composition supports runtime flexibility and component replacement
- Use interfaces for component types to enable loose coupling
- Manage component lifecycle properly to avoid resource leaks

**Next Steps**: [20-association](../20-association/README.md)
