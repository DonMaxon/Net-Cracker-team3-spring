package com.example.demo;

import com.example.demo.basicClasses.api.exceptions.WrongCountryException;
import com.example.demo.basicClasses.serializing.CustomerSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	@Autowired
	CustomerSerializer customerSerializer;

	public static void main(String[] args) throws WrongCountryException {
		SpringApplication.run(DemoApplication.class, args);
	}

}
