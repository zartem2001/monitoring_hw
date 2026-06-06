package ru.netology.monitoring_hw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MemoryConsumer {

    private static final Logger log = LoggerFactory.getLogger(MemoryConsumer.class);
    private final List<byte[]> memoryBlocks = new ArrayList<>();

    @Value("${memory.block.size.mb:50}")      // размер блока в мегабайтах
    private int blockSizeMb;

    @Value("${memory.max.blocks:10}")          // максимальное количество блоков
    private int maxBlocks;

    @Scheduled(fixedDelay = 10000)             // каждые 10 секунд
    public void consumeMemory() {
        if (memoryBlocks.size() < maxBlocks) {
            byte[] block = new byte[blockSizeMb * 1024 * 1024];
            memoryBlocks.add(block);
            log.info("Выделен блок памяти, всего блоков: {}", memoryBlocks.size());
        } else {
            log.info("Достигнут лимит блоков ({}), больше не выделяем", maxBlocks);
        }
    }
}