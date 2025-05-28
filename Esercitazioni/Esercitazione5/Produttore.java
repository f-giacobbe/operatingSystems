package Esercitazioni.Esercitazione5;

import java.util.Random;

public class Produttore extends Thread {
    private Buffer buffer;
    private static final Random random = new Random();

    public Produttore(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            buffer.put(random.nextInt());
        }
    }
}
