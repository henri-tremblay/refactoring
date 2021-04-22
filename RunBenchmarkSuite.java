/*
 * Copyright 2019-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.lang.System;

/**
 * Script to run the benchmark on multiple git versions of the project.
 * With Java < 10, do {@code javac RunBenchmarkSuite.java && java RunBenchmarkSuite commit1 commit2 ...}.
 * With Java >= 10 do {@code java RunBenchmarkSuite.java commit1 commit2 ...}, e.g. {@code java RunBenchmarkSuite.java master henri}.
 */
public class RunBenchmarkSuite {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: RunBenchmarkSuite.java commit1 commit2 ...");
            System.exit(1);
        }
        for (String commit : args) {
            System.out.println("################# " + commit + " #################");
            command("git", "checkout", commit);
            command("mvn", "clean", "package", "-DskipTests");
            command("java", "-jar", "benchmark/target/benchmarks.jar");
        }

        command("git", "checkout", "master");
    }

    private static void command(String... args) throws Exception {
        ProcessBuilder builder = new ProcessBuilder(args)
            .redirectErrorStream(true)
            .redirectOutput(ProcessBuilder.Redirect.INHERIT);
        Process process = builder.start();

        process.waitFor();
    }
}
