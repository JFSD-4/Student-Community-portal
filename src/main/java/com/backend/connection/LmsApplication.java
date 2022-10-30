package com.backend.connection;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.backend.connection.service.FilesStorageService;


@SpringBootApplication
public class LmsApplication implements CommandLineRunner{

	@Resource
	FilesStorageService storageService;

	public static void main(String[] args) {
		SpringApplication.run(LmsApplication.class, args);
	}

	@Override
	public void run(String... arg) throws Exception {
		storageService.deleteAll();
		storageService.init();
	}

}
