package Simulazioni.giu24;

public class Paziente extends Thread {
    private ProntoSoccorso ps;
    private int codice;   //0 se codice rosso, 1 se giallo, 2 se verde

    public Paziente(ProntoSoccorso ps, int codice) {
        if (codice != 0 && codice != 1 && codice != 2) {
            throw new IllegalArgumentException("Codice triage non valido.");
        }
        this.ps = ps;
        this.codice = codice;
    }

    @Override
    public void run() {
        try {
            ps.accediPaziente(codice);
            ps.esciPaziente();
        } catch (InterruptedException _) {

        }
    }

    public int getCodice() {
        return codice;
    }
}
