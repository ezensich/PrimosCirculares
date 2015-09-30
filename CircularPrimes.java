
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author zensich ezequiel
 */
public class CircularPrimes implements Runnable {

    private int since, until;
    private static final List<Integer> listCircularPrimes = new LinkedList<>();

    public CircularPrimes(int since, int until) {
        this.since = since;
        this.until = until;
    }

    // Retorna la lista de primos circulares 
    public static List<Integer> getListCircularPrimes() {
        return listCircularPrimes;
    }

    @Override
    public void run() {
        calculateCircularPrimes(since, until);
    }

    /* Retorna una lista con números primos circulares, que se encuentran
    * entre un rango de números dados */
    public static List<Integer> calculateCircularPrimes(int since, int until) {
        if (since >= 2 && until >= 2) {
            //recorro todos los numeros en el rango dado
            for (int i = since; i <= until; i++) { 
                //si el numero es primo y no se encuentra en la lista de primos circulares ya calculados
                if (CircularPrimes.isPrime(i) && !listCircularPrimes.contains(i)) { 
                    List<Integer> calculateRotations = CircularPrimes.calculateRotations(i);
                    Iterator<Integer> itrRotations = calculateRotations.iterator();
                    boolean arePrimes = true;
                    //reviso si todas las rotaciones del numero son primos
                    while (arePrimes && itrRotations.hasNext()) {
                        if (!CircularPrimes.isPrime(itrRotations.next())) {
                            arePrimes = false;
                        }
                    }
                    //si lo son, los agrego a la lista de primos circulares
                    if (arePrimes) {
                        listCircularPrimes.addAll(calculateRotations);
                    }
                }

            }
        }
        return listCircularPrimes;
    }

    // Retorna una lista con las rotaciones del número 'n'
    public static List<Integer> calculateRotations(Integer n) {
        LinkedList<Integer> rotations = new LinkedList();
        int num = n;
        int digits = (int) (Math.log10(n)) + 1; //cantidad de digitos que tiene 'n'
        //calculo todas las rotaciones de 'n'
        for (int i = 0; i < digits; i++) {
            num = (int) ((num % 10) * (Math.pow(10, digits - 1)) + (num / 10));
            if (!rotations.contains(num)) {
                rotations.add(num);
            }
        }
        return rotations;
    }

    // Retorna true si un 'n' es número primo
    public static boolean isPrime(Integer n) {
        double sqrtN = Math.sqrt(n);
        boolean isPrime = true;

        if (n != 2 && n % 2 == 0) {
            isPrime = false;
        } else {
            if (n != 3 && n % 3 == 0) {
                isPrime = false;
            }
        }
        int i = 5;
        int s = 2;
        while (isPrime && i <= sqrtN) {
            if (n % i == 0) {
                isPrime = false;
            }
            i += s;
            s = 6 - s;
        }
        return isPrime;
    }

    public static void main(String[] args) throws InterruptedException {

        List<Thread> listThread = new LinkedList<>();
        int n = 1000000; //Número hasta el cual se van a buscar los primos circulares
        int since = 2, until = n / 2;
        int digits = (int) (Math.log10(n)) + 1; //Cantidad de digitos que tiene 'n'
	/*Largo un Thread por cada digito que tenga el numero dado.
	 *El algoritmo divide el numero en dos partes, y calcula los primos circulares de la primera parte,
	 *luego divide la mitad restante en dos partes y nuevamente calcula sobre la primera parte. Se repite
	 *esta forma de calculo tantas veces como digitos tenga el número */
        for (int i = 0; i < digits; i++) {
            if (i + 1 == digits) {
                until = n;
            }
            listThread.add(new Thread(new CircularPrimes(since,until)));
            since = until + 1;
            until += (n - until) / 2;

        }
        for (Thread t : listThread) {
            t.start();
        }
        for (Thread t : listThread) {
            t.join();
        }

        System.out.println("Números Primos Circulares encontrados en "+n+" : "+CircularPrimes.getListCircularPrimes().size());
        System.out.println(CircularPrimes.getListCircularPrimes().toString());

    }

}
