package ro.uaic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ro.uaic")
public class Compulsory {

	public static void main(String[] args) {
		SpringApplication.run(Compulsory.class, args);

        System.out.println("Started");
	}
}
