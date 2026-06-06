package ru.netology.monitoring_hw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class MemoryConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(MemoryConsumerService.class);
    private final List<byte[]> memoryHog = new ArrayList<>();

    @PostConstruct
    public void startConsuming() {
        Thread consumer = new Thread(() -> {
            while (true) {
                try {
                    byte[] chunk = new byte[10 * 1024 * 1024];
                    memoryHog.add(chunk);
                    logger.info("Allocated 10 MB, total chunks: {}", memoryHog.size());
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (OutOfMemoryError e) {
                    logger.error("Out of memory, stopping allocation");
                    break;
                }
            }
        });
        consumer.setDaemon(true);
        consumer.start();
    }
}