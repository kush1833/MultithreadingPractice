package org.gengar;
class ThreadImpl1 implements Runnable {
    @Override
    public void run() {
        for(int i = 0; i < 10; i++) {
            System.out.println("ThreadImpl1 is running");
        }
    }
}
class ThreadImpl2 implements Runnable {
    @Override
    public void run() {
        for(int i = 0; i < 10; i++) {
            System.out.println(i+1);
        }
    }
}
/**
 * Key Takeaways:
 * <li> Prefer Runnable interface to Thread class for creating threads because it allows you to extend another class.
 * Also, it is a good practice to separate the task from the thread which is the executor of the task.
 * </li>
 * <li> Join() puts the calling thread to wait until the referenced thread completes its execution.
 * If you do Join() on a thread that has not started yet, it will have no effect(this is what I observed) or might error out.
 * </li>
 */
public class BasicThreadCreation {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new ThreadImpl1());
        Thread t2 = new Thread(new ThreadImpl2());
        t1.start();
        t2.join();
        t2.start();

    }
}

