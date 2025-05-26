package Esercitazione6.buffer;

import java.util.PriorityQueue;

public class BufferLCID extends BufferLC {
    private final PriorityQueue<Thread> codaProduttori = new PriorityQueue<>(BufferLCID::compareThreadID);
    private final PriorityQueue<Thread> codaConsumatori = new PriorityQueue<>(BufferLCID::compareThreadID);

    public BufferLCID(int dimensione) {
        super(dimensione);
    }

    private static int compareThreadID(Thread x, Thread y) {
        return Long.compare(x.threadId(), y.threadId());
    }

    @Override
    public void put(int i) {
        l.lock();
        try {
            codaProduttori.add(Thread.currentThread());
            while (!possoInserire()) {
                bufferPieno.await();
            }
            codaProduttori.remove();
            buffer[in] = i;
            in = (in + 1) % buffer.length;
            numElementi++;
            bufferVuoto.signalAll();
        } catch (InterruptedException _) {

        } finally {
            l.unlock();
        }
    }

    private boolean possoInserire() {
        return numElementi < buffer.length && codaProduttori.peek() == Thread.currentThread();
    }

    @Override
    public int get() {
        int res;
        l.lock();
        try {
            codaConsumatori.add(Thread.currentThread());
            while (!possoPrelevare()) {
                bufferVuoto.await();
            }
            codaConsumatori.remove();
            res = buffer[out];
            out = (out + 1) % buffer.length;
            numElementi--;
            bufferPieno.signalAll();
            return res;
        } catch (InterruptedException _) {

        } finally {
            l.unlock();
        }
        throw new IllegalStateException();
    }

    private boolean possoPrelevare() {
        return numElementi > 0 && codaConsumatori.peek() == Thread.currentThread();
    }
}
