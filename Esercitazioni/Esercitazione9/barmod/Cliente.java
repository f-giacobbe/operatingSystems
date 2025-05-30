package Esercitazioni.Esercitazione9.barmod;

public class Cliente extends Thread {
    private Bar bar;

    public Cliente(Bar bar) {
        this.bar = bar;
    }

    @Override
    public void run() {
        try {
            int scelta = bar.scegli();      //o 0 o 1, a seconda se si sceglie di pagare prima o di consumare prima

            //prima azione
            fai(scelta);

            //cambio azione
            scelta = 1 - scelta;

            //seconda azione
            fai(scelta);
        } catch (InterruptedException _) {

        }
    }

    private void fai(int scelta) throws InterruptedException {
        bar.inizia(scelta);
        Bar.interagisci(scelta);
        bar.finisci(scelta);
    }
}
