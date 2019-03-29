
import java.util.concurrent.Semaphore;
import java.util.Scanner;

public class Parking {
    private static String arr[][];
    private static Scanner scan;
    private static int t;
    private static long n;

    public void parkCar(int n) {
        final Semaphore semaphore = new Semaphore(2);
        Thread[] entry = new Thread[n];
        for (int i = 0; i < n; i++) {
            long l=(long)(i+1);
            String threadName = Integer.toString(i);

            entry[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.acquire();
                        try {
                            if(countsEmpty()>0){
                                Car newcar = new Car();
                                carToMass(l, newcar);
                                System.out.println("Машина №"+newcar.getCarId()+" припаркована");
                            }else{
                                System.out.println("На парковке нет свободных мест ");
                            }
                            Thread.sleep(t);
                        } finally {
                            semaphore.release();
                        }
                    } catch (Exception e) {
                        System.out.println("Возникла ошибка, попробуйте снова");
                    }
                }
            }, threadName);
            entry[i].start();

        }
    }

    public void unparkCar(int n){
        if(arr[Math.toIntExact(n-1)][1]==null){
            System.out.println(arr[Math.toIntExact(n-1)][0]+" возвращен, машина №"+arr[Math.toIntExact(n-1)][1]+" выехала с парковки");
            arr[Math.toIntExact(n-1)][1]=null;
        }else{
            System.out.println(arr[Math.toIntExact(n-1)][0]+" свободен");
        }
    }

    public static void creatureTickets(Long n){
        arr = new String[Math.toIntExact(n)][2];
        for (long i = 1; i <= n; i++) {
            Ticket newticket = new Ticket(i);
            arr[Math.toIntExact(i-1)][0]=newticket.getTicketNumber();
        }
    }

    public void carToMass (Long i,Car car){
        if (arr[Math.toIntExact(i-1)][1]==null){
            arr[Math.toIntExact(i-1)][1]=Long.toString(car.getCarId());
        }else{
            i++;
            carToMass(i, car);
        }
    }

    public void informationList (){
        for (long i = 0; i < arr.length; i++) {
            if(arr[Math.toIntExact(i)][1]==null) {
            }else{
                System.out.println(arr[Math.toIntExact(i)][0]+" Номер машины: "+arr[Math.toIntExact(i)][1]);
            }
        }
        if(countsEmpty()==n) {
            System.out.println("На парковке нет машин");
        }
    }

    public Long countsEmpty (){
        long count=0L;
        for (int i = 0; i < arr.length; i++) {
            if (arr[Math.toIntExact(i)][1]==null)
                count++;
        }
        return count;
    }

    public static void main(String[] args) {
        scan= new Scanner(System.in);

        do {
            System.out.println("Введите количество машиномест на парковке:");
            while (!scan.hasNextLong()) {
                System.out.println("Вы ввели не целочисленное значение");
                scan.next();
            }
            n = scan.nextLong();
            System.out.println("Вы зарегистрировали "+n+" мест на парковке");
        } while (n == 0L);
        do {
            System.out.println("Введите время на заезд машины(от 1 до 5 сек):");
            while (!scan.hasNextInt()) {
                System.out.println("Вы ввели не целочисленное значение");
                scan.next();
            }
            t=scan.nextInt();
        } while ((t<1)||(t>5));
        t*=1000;

        Parking parking = new Parking();
        creatureTickets(n);
        System.out.println("p:N - (park) чтобы припарковать машину, в командной строке вводится, где N - количество машин на въезд\n" +
                "u:N - (unpark) чтобы выехать с парковки. N - номер парковочного билета\n" +
                "u:[1..n] - (unpark) чтобы выехать с парковки нескольким машинам, где в квадратных скобках, через запятую передаются номера парковочных билетов\n" +
                "l - (list) список машин, находящихся на парковке. Для каждой машины выводится ее порядковый номер и номер билета\n" +
                "c - (count) количество оставшихся мест на парковке\n" +
                "e - (exit) выход из приложения");

        while (true) {

            String answer = scan.nextLine();

            if (answer.startsWith("p:")) {
                answer = answer.replaceAll(" ", "");
                String[] token1 = answer.split(":");
                try{
                    parking.parkCar(Integer.parseInt(token1[1]));
                }catch(Exception e){
                    System.out.println("Вы неправильно ввели команду, попробуйте снова");
                }

            } else if (answer.startsWith("u")) {
                if (answer.contains("[")) {
                    String rep0 = answer.replaceAll(" ", "");
                    String rep1 = rep0.replaceAll("u:\\[", "");
                    String rep2 = rep1.replaceAll("]", "");
                    String[] token2 = rep2.split(",");
                    Integer[] ticketsToUnpark = new Integer[token2.length];
                    final Semaphore semaphore = new Semaphore(2);
                    Thread[] departure = new Thread[token2.length];
                    try{
                        for (int i = 0; i < token2.length; i++) {
                            int it=i;
                            String threadName = Integer.toString(i);

                            departure[i] = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        semaphore.acquire();
                                        try {
                                            ticketsToUnpark[it] = Integer.parseInt(token2[it]);
                                            parking.unparkCar(ticketsToUnpark[it]);
                                            Thread.sleep(t);
                                        } finally {
                                            semaphore.release();
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Возникла ошибка, попробуйте снова");
                                    }
                                }
                            }, threadName);
                            departure[i].start();

                        }
                    }catch(Exception e){
                        System.out.println("Вы неправильно ввели команду, попробуйте снова");
                    }
                } else {
                    String rep0 = answer.replaceAll(" ", "");
                    String[] token3 = rep0.split(":");
                    try{
                        parking.unparkCar(Integer.parseInt(token3[1]));
                    }catch(Exception e){
                        System.out.println("Вы неправильно ввели команду, попробуйте снова");
                    }
                }
            } else if (answer.startsWith("l")) {
                parking.informationList();

            } else if (answer.startsWith("c")) {
                System.out.println("Количество свободных мест на парковке: "+parking.countsEmpty());

            } else if (answer.startsWith("e")) {
                System.exit(0);
                System.out.println("Парковка закрыта");
            }
        }
    }
}
