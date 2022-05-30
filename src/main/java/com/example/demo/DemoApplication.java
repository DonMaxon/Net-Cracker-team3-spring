package com.example.demo;

import com.example.demo.basicClasses.Test;
import com.example.demo.basicClasses.api.CustomerAPI;
import com.example.demo.basicClasses.api.LocationAPI;
import com.example.demo.basicClasses.api.OrderServiceAPI;
import com.example.demo.basicClasses.api.SpecificationAPI;
import com.example.demo.basicClasses.api.exceptions.WrongCountryException;
import com.example.demo.basicClasses.entity.*;
import com.example.demo.basicClasses.serializing.CustomerSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class DemoApplication {

	@Autowired
	CustomerSerializer customerSerializer;

	public static void main(String[] args) throws WrongCountryException {
		ApplicationContext applicationContext = SpringApplication.run(DemoApplication.class, args);
		Test service = applicationContext.getBean(Test.class);
		service.test();
		//SpringApplication.run(DemoApplication.class, args);
	}

}
