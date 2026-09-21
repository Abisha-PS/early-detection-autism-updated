package com.autismscreening;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AutismScreeningApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutismScreeningApplication.class, args);
        System.out.println("=================================================================");
        System.out.println("   Early Detection of Autism Screening System Started!");
        System.out.println("   Access Web Application at: http://localhost:8080/");
        System.out.println("   Parent / Clinician Dashboard: http://localhost:8080/parent-dashboard.html");
        System.out.println("   Interactive Child Game Arena: http://localhost:8080/child-session.html");
        System.out.println("   H2 Embedded Database Console: http://localhost:8080/h2-console");
        System.out.println("=================================================================");
    }
}

