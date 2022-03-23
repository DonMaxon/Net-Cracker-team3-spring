package com.example.demo;

import com.example.demo.basicClasses.api.SpecificationAPI;
import com.example.demo.basicClasses.entity.Location;
import com.example.demo.basicClasses.entity.Specification;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpecificationAPI specificationAPI = new SpecificationAPI();
		Specification spec = specificationAPI.createSpecification("SMR");
		try {
			String str = spec.serialize();
			System.out.println(str);
			spec = Specification.deserialize(str);
		}
		catch (IOException e){

		}


		SpringApplication.run(DemoApplication.class, args);
	}

}
