package Simulazioni.set24;

public class Gestore extends Thread {
    private Stabilimento s;

    public Gestore(Stabilimento s) {
        this.s = s;
    }

    @Override
    public void run() {
        try {
            s.preparaPostazioni();
            s.chiusura();
        } catch (InterruptedException _) {

        }
    }
}
