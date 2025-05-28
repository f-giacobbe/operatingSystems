package Simulazioni.giu24;

public class Medico extends Thread {
    private ProntoSoccorso ps;

    public Medico(ProntoSoccorso ps) {
        this.ps = ps;
        this.setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {  //il thread Medico è un thread daemon
            try {
                ps.iniziaVisita();
                ps.terminaVisita();
            } catch (InterruptedException _) {

            }
        }
    }
}
