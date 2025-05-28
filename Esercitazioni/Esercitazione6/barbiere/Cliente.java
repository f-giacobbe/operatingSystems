package Esercitazioni.Esercitazione6.barbiere;

public class Cliente extends Thread {
    private Sala sala;
    private int id;

    public Cliente(Sala sala, int id) {
        this.sala = sala;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            System.out.printf("Il cliente %s vuole tagliarsi i capelli%n", this);
            boolean res = sala.attendiTaglio();
            if (res) {
                System.out.printf("Il cliente %s si è seduto alla poltrona%n", this);
            } else {
                System.out.printf("La sala era piena e il cliente %s ha abbandonato la sala%n", this);
            }
        } catch (InterruptedException _) {

        }
    }

    @Override
    public String toString() {
        return "" + id;
    }
}
