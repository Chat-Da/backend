package site.chatda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class ChatDaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatDaApplication.class, args);
	}

}
