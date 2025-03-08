package org.gengar;

/**
 * Question:
 * You are tasked with implementing a Counter class in Java that will be accessed concurrently by multiple threads.
 * The Counter should have an increment() method to increase the count and a getCount() method to retrieve the current value.
 * <p>
 * Write two versions of the increment() method:
 * <li>Using a synchronized method</li>
 * <li>Using a synchronized block</li>
 * Explain which approach is better and why.
 * </p>
 *
 * <p>
 * Key takeaways:
 * <li> synchronized method locks all the code within the method. Hence less flexible as lines that are not critical
 * might also be locked.</li>
 * <li> synchronized block locks only the critical section of the code. Hence more flexible as only the critical
 * section is locked.</li>
 * <li> Since synchronization slows a program, prefer blocks over methods so that only critical code piece is locked</li>
 * <li> If done on static block of code or method, lock is class level, else object level</li>
 * </p>
 */
class SynchronizedPracticeCounterMethod {
    private static int countForMethod = 0;
    // synchronized method
    synchronized public void increment(String threadName) { // whole method is locked by the thread that has the lock
        System.out.println("Thread: " + threadName + " is running method");
        ++countForMethod;
    }
    public int getCountForMethod() {
        return countForMethod;
    }
}
class SynchronizedPracticeCounterBlock {
    private static int countForBlock = 0 ;
    // synchronized block
    public static void incrementInBlock(String threadName) {
        System.out.println("Thread: " + threadName + " is running block"); //this line can be accessed by multiple threads without lock
        synchronized (SynchronizedPracticeCounterBlock.class) {
            ++countForBlock; //this line is locked by the thread that has the lock
        }
    }
    public int getCountForBlock() {
        return countForBlock;
    }
}
public class SynchronizedPractice {
    public static void main(String[] args) {
        SynchronizedPracticeCounterMethod counterMethod1 = new SynchronizedPracticeCounterMethod();
        SynchronizedPracticeCounterMethod counterMethod2 = new SynchronizedPracticeCounterMethod();
        SynchronizedPracticeCounterBlock counterBlock1 = new SynchronizedPracticeCounterBlock();
        SynchronizedPracticeCounterBlock counterBlock2 = new SynchronizedPracticeCounterBlock();
        Thread t1 = new Thread(() -> {
            for(int i=0; i<1000; i++) {
                counterMethod1.increment("Thread1");
                SynchronizedPracticeCounterBlock.incrementInBlock("Thread1");
            }
        });
        Thread t2 = new Thread(() -> {
            for(int i=0; i<1000; i++) {
                counterMethod2.increment("Thread2");
                SynchronizedPracticeCounterBlock.incrementInBlock("Thread2");
            }
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Count for method 1: " + counterMethod1.getCountForMethod());
        System.out.println("Count for method 2: " + counterMethod2.getCountForMethod());
        System.out.println("Count for block 1: " + counterBlock1.getCountForBlock());
        System.out.println("Count for block 2: " + counterBlock2.getCountForBlock());


    }
}
