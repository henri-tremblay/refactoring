/*
 * Copyright 2019-2023 the original author or authors.
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
package pro.tremblay.core.benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import pro.tremblay.core.Amount;
import pro.tremblay.core.Position;
import pro.tremblay.core.Preferences;
import pro.tremblay.core.Quantity;
import pro.tremblay.core.ReportingService;
import pro.tremblay.core.Security;
import pro.tremblay.core.SecurityPosition;
import pro.tremblay.core.SystemTimeSource;
import pro.tremblay.core.Transaction;
import pro.tremblay.core.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static pro.tremblay.core.Quantity.qty;
import static pro.tremblay.core.SecurityPosition.securityPosition;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 10, time = 1)
@Measurement(iterations = 5, time = 2)
@Fork(2)
@State(Scope.Benchmark)
public class ReportingServiceBenchmark {

    private final Preferences preferences = new Preferences();
    private final ReportingService service = new ReportingService(preferences, new SystemTimeSource());

    private Collection<Transaction> transactions;
    private Position position;

    @Setup
    public void setup() {
        preferences.put(ReportingService.LENGTH_OF_YEAR, "365");

        Security[] securities = Security.values();
        SecurityPosition[] securityPositions = Stream.of(securities)
            .map(sec -> securityPosition(sec, qty(1_000)))
            .toArray(SecurityPosition[]::new);

        position = new Position()
            .cash(Amount.amnt(1_000_000))
            .addSecurityPositions(securityPositions);

        LocalDate now = LocalDate.now();
        int dayOfYear = now.getDayOfYear();

        TransactionType[] transactionTypes = TransactionType.values();

        Random random = new Random();
        transactions = random.ints(100, 1, 100)
            .mapToObj(quantity -> {
                Transaction t = new Transaction();
                return t.date(now.minusDays(random.nextInt(dayOfYear)))
                    .cash(Amount.amnt(random.nextInt(1_000)))
                    .type(transactionTypes[random.nextInt(transactionTypes.length)])
                    .quantity(t.getType().hasQuantity() ? qty(quantity) : Quantity.zero())
                    .security(t.getType().hasQuantity() ? securities[random.nextInt(securities.length)] : null);
            })
            .collect(Collectors.toList());
    }

    @Benchmark
    public BigDecimal calculate() {
        return service.calculateReturnOnInvestmentYTD(position, transactions);
    }

    public static void main(String[] args) {
        ReportingServiceBenchmark benchmark = new ReportingServiceBenchmark();
        benchmark.setup();
        benchmark.calculate();
    }
}
