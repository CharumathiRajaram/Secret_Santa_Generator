package com.secretsanta.secret_santa_api_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecretSantaApiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecretSantaApiServiceApplication.class, args);
	}

}
//
//
//input->csv (emp_name,emp_email)
//parse csv and assign secret child to each emp
//emp cant choose themselves as child
//no emp cannot be child for an emp like prev
//each must have 1
//each child shoud assigned only once.class
//
//
//out->csv
//emp name,emp email,secret name,secret emp emial
//
//
//constrants..prev year will be given as input as csv emp like output we rae going to generate
//
//
//exp:
//modular and extensible-oop no monolithic no single class or func
//
//
//
//test-testcasezs
//error handling-invalid input file handling
//documentation-
//readme
//version cobntrol
//vc
