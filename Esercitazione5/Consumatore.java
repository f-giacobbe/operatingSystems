package Esercitazione5;

public class Consumatore extends Thread {
    private Buffer buffer;

    public Consumatore(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            int prodotto = buffer.get();
            System.out.println(prodotto);   //simula una consumazione
        }
    }
}
