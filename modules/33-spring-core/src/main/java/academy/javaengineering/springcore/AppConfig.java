package academy.javaengineering.springcore;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Demonstrates Spring Core configuration.
 */
@Configuration
@ComponentScan(basePackages = "academy.javaengineering.springcore")
public class AppConfig {

    @Bean
    public Greeter greeter() {
        return new Greeter("Hello, Spring!");
    }

    @Bean
    public UserRepository userRepository() {
        return new InMemoryUserRepository();
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserService(userRepository);
    }
}
