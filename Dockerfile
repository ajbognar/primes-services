FROM eclipse-temurin:17
WORKDIR /home
COPY ./target/primes-services-0.0.1-SNAPSHOT.jar primes-services.jar
ENTRYPOINT ["java", "-jar", "primes-services.jar"]