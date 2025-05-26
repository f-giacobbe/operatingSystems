package Esercitazione6.orderedexecution;

import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OrderedExecution {
    private final PriorityQueue<MyThread> threadQueue = new PriorityQueue<>(OrderedExecution::compareThreadID);
    private final Lock l = new ReentrantLock();
    private final Condition possoEseguire = l.newCondition();

    private static int compareThreadID(MyThread x, MyThread y) {
        return Integer.compare(x.getID(), y.getID());
    }

    public void push(MyThread thread) {
        threadQueue.add(thread);
    }

    public MyThread pop() {
        return threadQueue.remove();
    }

    public void execute(MyThread thread) throws InterruptedException {
        l.lock();
        try {
            push(thread);
            while (!eseguibile(thread)) {
                possoEseguire.await();
            }
            threadQueue.remove();
            System.out.printf("Thread with id %d now executing%n", thread.getID());
            possoEseguire.signalAll();
        } finally {
            l.unlock();
        }
    }

    public boolean eseguibile(MyThread thread) {
        return threadQueue.peek() == thread;
    }
}
