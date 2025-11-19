package Proyectodeaula.Workwise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableMethodSecurity
@SpringBootApplication
@EnableScheduling
public class WorkwiseApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkwiseApplication.class, args);
	}

}