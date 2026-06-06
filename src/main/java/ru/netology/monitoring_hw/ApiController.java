package ru.netology.monitoring_hw;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ApiController {

    private final Counter invocationCounter;
    private final AtomicLong customMetricValue = new AtomicLong(0);

    public ApiController(MeterRegistry registry) {
            this.invocationCounter = Counter.builder("controller.invocations")
                .description("Total number of controller method invocations")
                .register(registry);
    }

    @GetMapping("/success")
    public ResponseEntity<String> success() {
        invocationCounter.increment(); // инкремент кастомной метрики
        return ResponseEntity.ok("Success 200");
    }

    @GetMapping("/notfound")
    public ResponseEntity<String> notFound() {
        invocationCounter.increment();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found 404");
    }

    @GetMapping("/error")
    public ResponseEntity<String> error() {
        invocationCounter.increment();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error 500");
    }

    @GetMapping("/custom-metric")
    public ResponseEntity<String> customMetricEndpoint() {
        long current = customMetricValue.incrementAndGet();
        invocationCounter.increment();
        return ResponseEntity.ok("Custom metric incremented. Current value: " + current);
    }
}
