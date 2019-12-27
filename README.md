## Usage

* Reporting (spotbugs and jacoco): `mvn verify site`
* Mutation testing: `mvn package org.pitest:pitest-maven:mutationCoverage -DwithHistory`

## Benchmark

To run: `mvn package -DskipTests && java -jar app/target/benchmarks.jar`

### Baseline (before any refactoring)

```
Benchmark                             Mode  Cnt   Score   Error   Units
ReportingServiceBenchmark.calculate  thrpt   10  63.884 ± 0.571  ops/ms
```
