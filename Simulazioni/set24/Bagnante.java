package Simulazioni.set24;

public class Bagnante extends Thread {
    private Stabilimento s;
    private int scelta = -1;     //per registrare se il bagnante sceglie il lettino (0) o l'ombrellone (1)

    public Bagnante(Stabilimento s) {
        this.s = s;
    }

    @Override
    public void run() {
        try {
            s.scegliAccesso();  //modifica il valore di scelta
            s.paga();
        } catch (InterruptedException _) {

        }
    }

    public int getScelta() {
        return scelta;
    }

    public void setScelta(int scelta) {
        this.scelta = scelta;
    }
}
