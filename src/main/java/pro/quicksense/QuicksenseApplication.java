package pro.quicksense;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class QuicksenseApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuicksenseApplication.class, args);
		log.info("User Management System started successfully...");
	}
}
