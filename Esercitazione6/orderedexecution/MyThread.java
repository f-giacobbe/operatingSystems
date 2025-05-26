package Esercitazione6.orderedexecution;

public class MyThread extends Thread {
    private OrderedExecution od;
    private int id;

    public MyThread(OrderedExecution od, int id) {
        this.od = od;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            od.execute(this);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public int getID() {
        return id;
    }
}
