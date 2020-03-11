# Refactoring fun

We want to refactor this code until we are happy with it. 
Everything turns around `ReportingService` which calculate an annualized return of investment for a security and cash
position since the beginning of the year.

Have fun and check how far you can go.
Don't hesitate to play with everything, test and production code.
The only important thing is that you should keep the current behavior.

## Recommended way to try it out

I recommend that you first create you branch from `master`.
My branch is named `henri`.
But please don't look at it before trying by yourself, it will spoil the fun.
If you notice new commits arriving to master, just rebase your branch over `master`.

You can check your test coverage, performance and even the mutation testing result along the way.

## Usage

* Reporting (spotbugs and jacoco): `./mvnw verify site`
* Mutation testing: `./mvnw package org.pitest:pitest-maven:mutationCoverage -DwithHistory`

## Benchmark

To run: `mvn package -DskipTests && java -jar app/target/benchmarks.jar`

If you want to run it against multiple commits, you can do `java RunBenchmarkSuite.java commit1, commit2, ...`.

## Maintenance

* Upgrade license: `./mvnw validate license:format`
* Upgrade the Maven wrapper: `./mvnw -N io.takari:maven:0.7.7:wrapper`
* Check plugins to upgrade: `./mvnw versions:display-dependency-updates versions:display-plugin-updates`
