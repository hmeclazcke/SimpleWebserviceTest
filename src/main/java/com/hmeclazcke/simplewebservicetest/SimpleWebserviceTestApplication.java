package com.hmeclazcke.simplewebservicetest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleWebserviceTestApplication {

    /*
     * Execution flow:
     *
     * main()
     *  -> SpringApplication.run(...)
     *  -> Spring Boot starts the embedded Tomcat server
     *
     * Example: POST /api/account
     *
     * Tomcat
     *  -> AccountController
     *  -> AccountService
     *  -> AccountRepository
     *  -> JPA
     *  -> Hibernate
     *  -> Database ( H2 database runs in memory ) using Account entity
     */
    public static void main(String[] args) {
        SpringApplication.run(SimpleWebserviceTestApplication.class, args);
    }

}
