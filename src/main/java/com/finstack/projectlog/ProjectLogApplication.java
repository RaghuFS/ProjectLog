package com.finstack.projectlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@ComponentScan(basePackages = "com.finstack_tech.projectpulse")
@EnableScheduling
public class ProjectLogApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectLogApplication.class, args);
	}

}
