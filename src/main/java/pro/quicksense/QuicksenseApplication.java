package pro.quicksense;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("pro.quicksense.modules.mapper")
public class QuicksenseApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuicksenseApplication.class, args);
		System.out.println("Hello World");
	}
}
