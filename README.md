# infor-integration-test
Integration Tests Suite include test cases from all the three services, talking to each other and returning proper response or error response based on the Request.

The Integration Testing framework is built using a Maven project with dependencies like RestAssured, Jackson API, TestNG for handling RestFul APIs CRUD operations.
Test Cases can be triggered individually or as Test Suite or using the mvn command line.

*Note: Before any Integration Testing is triggered, it must be ensured that all below services are UP and RUNNING for proper response or else Exception Message would be received.

Services to be switched sequentially:
- infor-eureka-server
- infor-config-server
- infor-user-microservice
- infor-car-microservice
- infor-booking-microservice

Exceptions will be logged in server logs. For any debugging, it can be used.

