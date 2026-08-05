package academy.javaengineering.patterns.behavioral.mediator;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete Mediator implementation - Chat Room.
 * Coordinates message passing between users.
 */
public class ChatRoom implements Mediator {

    private final List<User> users = new ArrayList<>();

    @Override
    public void register(User colleague) {
        users.add(colleague);
        colleague.setMediator(this);
        System.out.println(colleague.getName() + " joined the chat room");
    }

    @Override
    public void sendMessage(String message, User sender) {
        for (User user : users) {
            if (user != sender) {
                user.receive(message, sender.getName());
            }
        }
    }

    public List<User> getUsers() {
        return new ArrayList<>(users);
    }
}
