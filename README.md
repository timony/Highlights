# Highlights homework
[Assignment](https://github.com/agrpdev/homeworks/blob/master/algorithms.md)

## Commands
* Build  `./gradlew clean build`
* Run `./gradlew bootRun`
* Requests
  * http://localhost:8080/highlights/<username>/documents/<elementId>/highlights  
    * headers  
  Content-Type: application/json
    * body  
      {
      "startElementId": "1",
      "offset": 20,
      "length": 10
      }


## Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.4.5/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.4.5/gradle-plugin/reference/html/#build-image)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.4.5/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.4.5/reference/htmlsingle/#boot-features-developing-web-applications)
* [Validation](https://docs.spring.io/spring-boot/docs/2.4.5/reference/htmlsingle/#boot-features-validation)

## Guides
The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

## Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)