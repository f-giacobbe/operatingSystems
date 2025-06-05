package Simulazioni.gen24;

public class Utente extends Thread {
    private Biblioteca biblioteca;
    private int tipo;  //0 se tesserato, 1 se esterno
    static final int TESSERATO = 0;
    static final int ESTERNO = 1;

    public Utente(Biblioteca b, int t) {
        biblioteca = b;
        tipo = t;
    }

    @Override
    public void run() {
        try {
            biblioteca.richiediPrestito();
            biblioteca.esci();
        } catch (InterruptedException _) {

        }
    }

    public int getTipo() {
        return tipo;
    }
}
