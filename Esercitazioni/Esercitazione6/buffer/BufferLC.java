package Esercitazioni.Esercitazione6.buffer;

import Esercitazioni.Esercitazione5.Buffer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BufferLC extends Buffer {
    protected final int[] buffer;
    protected int in = 0;
    protected int out = 0;
    protected int numElementi = 0;
    protected Lock l = new ReentrantLock();
    protected Condition bufferVuoto = l.newCondition();
    protected Condition bufferPieno = l.newCondition();

    public BufferLC(int dimensione) {
        buffer = new int[dimensione];
    }

    @Override
    public void put(int i) {
        l.lock();
        try {
            while (numElementi == buffer.length) {
                bufferPieno.await();
            }

            buffer[in] = i;
            in = (in + 1) % buffer.length;
            numElementi++;
            bufferVuoto.signal();
        } catch (InterruptedException _) {

        } finally {
            l.unlock();
        }
    }

    @Override
    public int get() {
        int i;
        l.lock();
        try {
            while (numElementi == 0) {
                bufferVuoto.await();
            }

            i = buffer[out];
            out = (out + 1) % buffer.length;
            numElementi--;
            bufferPieno.signal();
            return i;
        } catch (InterruptedException _) {

        } finally {
            l.unlock();
        }

        throw new IllegalStateException();
    }
}
