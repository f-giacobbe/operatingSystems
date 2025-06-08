package Simulazioni.nov23;

public class Impiegato extends Thread {
    private UfficioPostale up;
    private String tipo;    //A, B o C

    public Impiegato(UfficioPostale u, String t) {
        up = u;
        tipo = t;

        this.setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            try {
                up.eseguiOperazione();
                up.prossimoCliente();
            } catch (InterruptedException _) {

            }
        }
    }

    public String getTipo() {
        return tipo;
    }
}
