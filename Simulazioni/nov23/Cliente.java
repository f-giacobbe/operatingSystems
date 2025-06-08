package Simulazioni.nov23;

public class Cliente extends Thread {
    private UfficioPostale up;
    private String tipo;    //A, B o C

    public Cliente(UfficioPostale u, String t) {
        up = u;
        tipo = t;
    }

    @Override
    public void run() {
        try {
            boolean esito = up.ritiraTicket(tipo);

            if (esito) {
                up.attendiSportello(tipo);
            } else {
                System.out.printf("Sono finiti i ticket per l'operazione %s, me ne vado %n", tipo);
            }
        } catch (InterruptedException _) {

        }
    }

    public String getTipo() {
        return tipo;
    }
}
