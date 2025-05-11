package Esercitazione3;

public class ContoCorrenteTestSafe {
    public static void main(String[] args) throws InterruptedException {
        int depositoIniziale = 100_000;
        ContoCorrenteSafe cc = new ContoCorrenteSafe(depositoIniziale) {
            @Override
            public void deposita(int importo) {
                if (importo < 0) {
                    throw new RuntimeException("Importo non valido");
                }

                if (importo > 5000) {
                    System.err.println("Chiamo la finanza...");
                }

                deposito.addAndGet(importo);
                System.out.printf("Hai depositato %d\n", importo);
            }

            @Override
            public void preleva(int importo) {
                if (importo > deposito.get() || importo < 0) {
                    throw new RuntimeException("Importo non valido");
                }

                deposito.addAndGet(-importo);
                System.out.printf("Hai depositato %d\n", importo);
            }
        };
        int numCorrentisti = 10;
        int importo = 100;
        int numOperazioni = 2;
        testContoCorrente(cc, numCorrentisti, importo, numOperazioni);

        if (cc.getDeposito() == depositoIniziale) {
            System.out.printf("Corretto! Il deposito finale è %d%n", cc.getDeposito());
        } else {
            System.out.printf("Errore! Il deposito iniziale era di %d mentre il deposito finale è di %d%n", depositoIniziale, cc.getDeposito());
        }

    }


    private static void testContoCorrente(ContoCorrenteSafe cc, int numCorrentisti, int importo, int numOperazioni) throws InterruptedException {
        Correntista[] correntisti = new Correntista[numCorrentisti];

        for (int i = 0; i < numCorrentisti; i++) {
            correntisti[i] = new Correntista(cc, importo, numOperazioni);
        }

        Thread[] threadCorrentisti = new Thread[numCorrentisti];

        for (int i = 0; i < numCorrentisti; i++) {
            threadCorrentisti[i] = new Thread(correntisti[i]);
            threadCorrentisti[i].start();
        }

        for (int i = 0; i < numCorrentisti; i++) {
            threadCorrentisti[i].join();
        }
    }
}
