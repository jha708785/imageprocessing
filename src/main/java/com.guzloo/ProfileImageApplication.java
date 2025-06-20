package com.guzloo;

import com.guzloo.model.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProfileImageApplication  implements CommandLineRunner {

	String uploadDir = "uploads/";


	@Autowired
	private ImageUtils utils;

	public static void main(String[] args) {
		SpringApplication.run(ProfileImageApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

		/*File suman = utils.generateInitialAvatar("kamini", uploadDir);
		System.out.println(suman);
*/

	}
}
