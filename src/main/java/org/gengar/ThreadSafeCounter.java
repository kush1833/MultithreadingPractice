package org.gengar;

/**
 * Key takeaways:
 * <li> Synchronized keyword is used to make a method or block thread-safe.</li>
 * <li> The order of gaining the lock is random and managed by OS</li>
 * <li> Join at the end is important if you want to see the result of thread executions </li>
 */
public class ThreadSafeCounter {
    private int count = 0;
    private synchronized void increment(){
        ++count;
    }
    private int getCount(){
        return count;
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadSafeCounter threadSafeCounter = new ThreadSafeCounter();
        Thread t1 = new Thread(() -> {
            for(int i=1; i<=500; i++) {
                threadSafeCounter.increment();
            }
//            for(int i=1; i<=5; i++) {
//                System.out.println("Thread 1 subtract: " + threadSafeCounter.decrement());
//            }
        });
        Thread t2 = new Thread(() -> {
            for(int i=1; i<=500; i++) {
                threadSafeCounter.increment();
            }
//            for(int i=1; i<=5; i++) {
//                System.out.println("Thread 2 subtract: " + threadSafeCounter.decrement());
//            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Count: " + threadSafeCounter.getCount());
    }
}
