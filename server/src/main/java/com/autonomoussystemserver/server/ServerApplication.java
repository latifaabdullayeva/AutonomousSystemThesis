// The server side of the project is using Spring Boot Framework together with Spring Data JPA and Hibernate.
// The database management system that we use is PostgreSQL
package com.autonomoussystemserver.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ServerApplication {

    public static void main(String[] args) {
        System.out.println("Backend: " + "ServerApplication");
        SpringApplication.run(ServerApplication.class, args);
    }
}
// Write in terminal
// mvn spring-boot:run -e -X -Dspring-boot.run.arguments=--username=sFsM2OIr6DUWtWeyGFSIOwGtuIIFQBf14gtlxXFj,--ipAddress=192.168.0.100