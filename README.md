# Health Survey API
This project implements an application which allows patients update their situation every day. Each day, they can login to the system and fill a survey by answering two questions:

- How was your sleep last night? range: 0(bad) - 10(good)
- How good is your skin condition? range: 0(bad) - 10(good)

#Tech stack

+ Java 11
+ Spring Boot 2.5.2
+ Spring starter Data JPA 2.5.2
+ Spring starter Security 2.5.2
+ Spring starter test 2.5.2 (including Junit 5)
+ H2 embedded database
+ Swagger 2
+ Lombok

#Authentication

Api /survey is secured by HTTP basic authentication. You can use any users below to be authenticated:

- username/password: **bob/bob**
- username/password: **alice/alice**
- username/password: **duck/duck**
- username/password: **david/david**

# Getting Started
The application provides a restful api to fill survey as below:

1) url: "<server>:<port>/survey", method: POST.

	Add a survey for today, you have to be authenticated before using this api. Please understand that you can only add **one survey** each day.
	
**Note**: all the time shown in survey is written in UTC/GMT+0.

# Follow those below instructions to test and run application

1) Prerequisites


+ Java 11
+ Apache Maven 3.6.1

You also need a tool to create HTTP requests to the application as Postman.
 
2) Running the tests
To run the test cases of project, using the following command in project root:
```
mvn test
```

3) Running the application
To run the application, using the following command in project root:
```
mvn spring-boot:run
```

The application will run on port 8080 by default. If you want to change that (for example to port 8081), you can add the parameter "-Dserver.port=8080" as below:
```
mvn spring-boot:run -Dserver.port=8081
```

Now, you can use Postman or any tool to make requests to the application for testing.
