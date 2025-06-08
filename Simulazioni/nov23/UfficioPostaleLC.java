package Simulazioni.nov23;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UfficioPostaleLC extends UfficioPostale {
    private Lock l = new ReentrantLock();

    private Condition attendoTotem = l.newCondition();  //per l'attesa del turno al totem dei ticket
    private HashMap<String, Condition> attendoTurno = new HashMap<>();  //per l'attesa del turno agli sportelli
    private HashMap<String, Condition> attendoCliente = new HashMap<>();    //per l'attesa di un cliente da parte dell'impiegato

    private LinkedList<Cliente> codaTotem = new LinkedList<>();
    private HashMap<String, LinkedList<Cliente>> codeSportello = new HashMap<>();

    private boolean totemOccupato = false;
    private HashMap<String, Boolean> sportelloOccupato = new HashMap<>();

    public UfficioPostaleLC() {
        super();

        attendoTurno.put("A", l.newCondition());
        attendoTurno.put("B", l.newCondition());
        attendoTurno.put("C", l.newCondition());

        attendoCliente.put("A", l.newCondition());
        attendoCliente.put("B", l.newCondition());
        attendoCliente.put("C", l.newCondition());

        codeSportello.put("A", new LinkedList<>());
        codeSportello.put("B", new LinkedList<>());
        codeSportello.put("C", new LinkedList<>());

        sportelloOccupato.put("A", false);
        sportelloOccupato.put("B", false);
        sportelloOccupato.put("C", false);
    }

    @Override
    public boolean ritiraTicket(String operazione) throws InterruptedException {
        l.lock();
        try {
            Cliente c = (Cliente) Thread.currentThread();
            codaTotem.add(c);

            while (devoAspettareTotem()) {
                attendoTotem.await();
            }

            totemOccupato = true;
            codaTotem.remove();

            if (ticketStampati.get(operazione) >= TICKET_MAX) {
                return false;
            }

            ticketStampati.replace(operazione, ticketStampati.get(operazione) + 1);
            System.out.printf("Ticket per l'operazione %s ritirato!%n", operazione);
            return true;
        } finally {
            totemOccupato = false;
            attendoTotem.signalAll();
            l.unlock();
        }
    }

    private boolean devoAspettareTotem() {
        return totemOccupato || !Thread.currentThread().equals(codaTotem.getFirst());
    }

    @Override
    public void attendiSportello(String operazione) throws InterruptedException {
        l.lock();
        try {
            Cliente c = (Cliente) Thread.currentThread();
            codeSportello.get(operazione).add(c);

            while (!mioTurno(operazione)) {
                attendoTurno.get(operazione).await();
            }

            codeSportello.get(operazione).remove();
            sportelloOccupato.replace(operazione, true);
            attendoCliente.get(operazione).signal();
        } finally {
            l.unlock();
        }
    }

    private boolean mioTurno(String op) {
        return !sportelloOccupato.get(op) && Thread.currentThread().equals(codeSportello.get(op).getFirst());
    }

    @Override
    public void prossimoCliente() throws InterruptedException {
        l.lock();
        try {
            Impiegato imp = (Impiegato) Thread.currentThread();
            String op = imp.getTipo();

            System.out.printf("Può venire il prossimo con operazione %s%n", op);
            sportelloOccupato.replace(op, false);
            attendoTurno.get(op).signalAll();
        } finally {
            l.unlock();
        }
    }

    @Override
    public void eseguiOperazione() throws InterruptedException {
        l.lock();
        try {
            Impiegato imp = (Impiegato) Thread.currentThread();
            String op = imp.getTipo();

            while (!sportelloOccupato.get(op)) {
                attendoCliente.get(op).await();
            }

            svolgi(op);
            System.out.printf("Ho finito di svolgere l'operazione %s, te ne puoi andare%n", op);
        } finally {
            l.unlock();
        }
    }








    public static void main(String[] args) {
        int numClienti = 200;
        UfficioPostaleLC up = new UfficioPostaleLC();
        up.test(numClienti);
    }
}
