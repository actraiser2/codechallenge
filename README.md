# Instructions for running the amazing codechallenge

Application developed according the hexagonal architecture principles and DDD methodology using
the Spring Boot framework

Next steps for the future: 
1. Publish events for converting the application into event driven. 
2. Create the image for the aplication in order to it can run in a container, such as Docker
3. Create a pipeline based in Jenkins for CI/CD


For running the application (**supposing that you have installed jdk 11 or later and maven in your laptop**)

1. Clone the repository
2. From the command prompt go to the root project
3. Launch the following command **mvn test spring-boot:run**
4. If the application compiles succesfully and  all the test pass (i've created one for each business flow), the application should start successfully listening in the port 8080)
5. The contract of the service (OpenApi) can be viewed in the following endpoint: **http://localhost:8080/swagger-ui/index.html**