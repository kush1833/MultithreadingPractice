package org.gengar;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is an example of multiple producers and consumers using wait and notifyAll.
 */
class SharedBufferForMultiSetup {
    private final int capacity = 5;
    private final Queue<String> buffer = new LinkedList<>();
    public synchronized void producer(String value, String producerName) {
        while(buffer.size() == capacity) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        buffer.add(value);
        System.out.println("Produced: " + value + " by " + producerName);
        notifyAll();
    }
    public synchronized void consumer(String consumerName) {
        while(buffer.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        String value = buffer.poll();
        System.out.println("Consumed: " + value + " by " + consumerName);
        notifyAll();
    }
}
class MessageProducer implements Runnable {
    private final SharedBufferForMultiSetup buffer;
    private final int processingTime;
    private final AtomicInteger counter = new AtomicInteger(-1);
    public MessageProducer(SharedBufferForMultiSetup buffer, int processingTime) {
        this.buffer = buffer;
        this.processingTime = processingTime;
    }
    public int incrementCounter() {
        return counter.incrementAndGet();
    }
    @Override
    public void run() {
        while(true) {
            buffer.producer("Message " + incrementCounter(), Thread.currentThread().getName());

            try {
                Thread.sleep(processingTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
class MessageConsumer implements Runnable {
    private final SharedBufferForMultiSetup buffer;
    private final int processingTime;
    public MessageConsumer(SharedBufferForMultiSetup buffer, int processingTime) {
        this.buffer = buffer;
        this.processingTime = processingTime;
    }
    @Override
    public void run() {
        while (true) {
            buffer.consumer(Thread.currentThread().getName());
            try {
                Thread.sleep(processingTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
public class MultipleProducerAndConsumerExample {
    public static void main(String[] args) {
        SharedBufferForMultiSetup buffer = new SharedBufferForMultiSetup();
        Thread producer1 = new Thread(new MessageProducer(buffer, 200), "Producer1");
        Thread producer2 = new Thread(new MessageProducer(buffer, 600), "Producer2");
        Thread consumer1 = new Thread(new MessageConsumer(buffer, 2000), "Consumer1");
        Thread consumer2 = new Thread(new MessageConsumer(buffer, 5000), "Consumer2");
        producer1.start();
        producer2.start();
        consumer1.start();
        consumer2.start();
    }
}
