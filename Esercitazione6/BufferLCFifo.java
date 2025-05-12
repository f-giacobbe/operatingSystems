package Esercitazione6;

import java.util.LinkedList;

public class BufferLCFifo extends BufferLC {
    private final LinkedList<Thread> codaProduttori = new LinkedList<>();
    private final LinkedList<Thread> codaConsumatori = new LinkedList<>();

    public BufferLCFifo(int dimensione) {
        super(dimensione);
    }

    @Override
    public void put(int i) {
        l.lock();

        try {
            codaProduttori.add(Thread.currentThread());

            while (!possoInserire()) {
                bufferPieno.await();
            }

            codaProduttori.removeFirst();
            buffer[in] = i;
            in = (in + 1) % buffer.length;
            numElementi++;
            bufferVuoto.signalAll();    //Qui è importante fare la signalAll, altrimenti potrei svegliare un Thread non in testa alla coda
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
            codaConsumatori.add(Thread.currentThread());

            while (!possoPrelevare()) {
                bufferVuoto.await();
            }

            codaConsumatori.removeFirst();
            i = buffer[out];
            out = (out + 1) % buffer.length;
            numElementi--;
            bufferPieno.signalAll();
            return i;
        } catch (InterruptedException _) {

        } finally {
            l.unlock();
        }

        throw new IllegalStateException();
    }

    private boolean possoInserire() {
        return numElementi < buffer.length && Thread.currentThread() == codaProduttori.getFirst();
    }

    private boolean possoPrelevare() {
        return numElementi > 0 && Thread.currentThread() == codaConsumatori.getFirst();
    }
}
