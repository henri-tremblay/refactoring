# Refactoring fun

We want to refactor this code until we are happy with it. 
Everything turns around `ReportingService` which calculate an annualized return of investment for a security and cash
position since the beginning of the year.

Have fun and check how far you can go.
Don't hesitate to play with everything, test and production code.
The only important thing is that you should keep the current behavior.

## Usage

* Reporting (spotbugs and jacoco): `./mvnw verify site`
* Mutation testing: `./mvnw package org.pitest:pitest-maven:mutationCoverage -DwithHistory`

## Benchmark

To run: `mvn package -DskipTests && java -jar app/target/benchmarks.jar`

## Maintenance

* Upgrade license: `./mvnw validate license:format`
* Upgrade the wrapper: `./mvnw -N io.takari:maven:0.7.7:wrapper`
