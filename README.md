## Usage

* Reporting (spotbugs and jacoco): `./mvnw verify site`
* Mutation testing: `./mvnw package org.pitest:pitest-maven:mutationCoverage -DwithHistory`

## Benchmark

To run: `mvn package -DskipTests && java -jar app/target/benchmarks.jar`

## Maintenance

* Upgrade license: `./mvnw validate license:format`
* Upgrade the wrapper: `./mvnw -N io.takari:maven:0.7.7:wrapper`
