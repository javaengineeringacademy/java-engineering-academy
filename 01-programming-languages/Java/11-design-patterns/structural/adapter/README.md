# Adapter Design Pattern

## Overview
Adapter pattern converts the interface of a class into another interface clients expect. It lets classes work together that couldn't otherwise because of incompatible interfaces.

## When to Use
- You want to use an existing class but its interface does not match the one you need
- You need to integrate third-party libraries with incompatible interfaces
- You want to create a reusable class that cooperates with unrelated classes

## Code Example

```java
public interface MediaPlayer {
    void play(String audioType, String fileName);
}

public class AudioPlayer implements MediaPlayer {
    private MediaAdapter mediaAdapter;

    @Override
    public void play(String audioType, String fileName) {
        if (audioType.equalsIgnoreCase("mp3")) {
            System.out.println("Playing mp3 file. Name: " + fileName);
        } else if (audioType.equalsIgnoreCase("vlc") || audioType.equalsIgnoreCase("mp4")) {
            mediaAdapter = new MediaAdapter(audioType);
            mediaAdapter.play(audioType, fileName);
        } else {
            System.out.println("Invalid media. " + audioType + " format not supported");
        }
    }
}
```

## Common Mistakes
- Overusing adapters when a simple interface redesign would work
- Creating too many adapter layers that hurt performance
- Not documenting why an adapter exists

## Interview Questions
1. What is the difference between Adapter and Facade patterns?
2. Can you implement Adapter using inheritance instead of composition?
3. How does the Adapter pattern relate to the Open/Closed Principle?
