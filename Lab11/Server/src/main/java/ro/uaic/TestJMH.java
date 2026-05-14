package ro.uaic;

import ro.uaic.repositories.ResultService;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Mode;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.Benchmark;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.openjdk.jmh.annotations.TearDown;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * TestJMH
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class TestJMH {
    private ConfigurableApplicationContext context;
    private ResultService service;

    @Setup
    public void setup() {
        this.context = SpringApplication.run(App.class);
        this.service = context.getBean(ResultService.class);
    }

    @TearDown
    public void tearDown() {
        this.context.close();
    }

    @Benchmark
    public void testDynamicSearchPerformance() {
        service.resultQuery("Emy", "50.0", "2026-05-13");
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
            .include(TestJMH.class.getSimpleName())
            .forks(0)
            .warmupIterations(3)
            .measurementIterations(10)
            .build();

        new Runner(opt).run();
    }
}
