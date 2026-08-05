package academy.javaengineering.springcore;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Greeter greeter() {
        return new Greeter("Hello");
    }

    @Bean
    public UserRepository userRepository() {
        return new InMemoryUserRepository();
    }

    @Bean
    public UserService userService() {
        return new UserService(userRepository());
    }

    @Bean
    public Greeter formalGreeter() {
        return new Greeter("Welcome");
    }
}
