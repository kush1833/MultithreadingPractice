package org.gengar;
class Counter {
    private int count = 0;
    private static int staticCount = 0;

    // Non-static synchronized method (instance-level lock)
    public synchronized void incrementInstance() {
        System.out.println(Thread.currentThread().getName() + " - Instance method start");
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        count++;
        System.out.println(Thread.currentThread().getName() + " - Instance method end");
    }

    // Static synchronized method (class-level lock)
    public static synchronized void incrementStatic() {
        System.out.println(Thread.currentThread().getName() + " - Static method start");
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        staticCount++;
        System.out.println(Thread.currentThread().getName() + " - Static method end");
    }
}

/**
 * Object vs Class level locking example. Here is a run's output:
 * <pre>
 *
 * {@code
 * === STARTING INSTANCE-LEVEL TEST ===
 * T2 - Instance method start
 * T1 - Instance method start
 * T2 - Instance method end
 * T1 - Instance method end
 * === STARTING CLASS-LEVEL TEST ===
 * T3 - Static method start
 * T3 - Static method end
 * T4 - Static method start
 * T4 - Static method end
 * === TEST COMPLETE ===
 *}
 * </pre>
 * Notice how the instance-level locking allows both threads to run in parallel (both started),
 * while the class-level locking blocks the second thread until the first one completes (T4 waited for T3 to end).
 */
public class ObjectAndClassLevelLockingExample {
    public static void main(String[] args) throws InterruptedException {
        Counter counter1 = new Counter();
        Counter counter2 = new Counter();

        // Instance-level locking (should execute in parallel)
        Thread t1 = new Thread(counter1::incrementInstance, "T1");
        Thread t2 = new Thread(counter2::incrementInstance, "T2");

        // Class-level locking (should block each other)
        Thread t3 = new Thread(Counter::incrementStatic, "T3");
        Thread t4 = new Thread(Counter::incrementStatic, "T4");

        System.out.println("=== STARTING INSTANCE-LEVEL TEST ===");
        t1.start(); // t1 and t2 can run in parallel
        t2.start();
        t1.join();
        t2.join();

        System.out.println("=== STARTING CLASS-LEVEL TEST ===");
        t3.start(); // t3 and t4 will block each other
        t4.start();
        t3.join();
        t4.join();

        System.out.println("=== TEST COMPLETE ===");
    }
}
