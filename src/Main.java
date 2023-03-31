import java.util.*;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        LinkedList<Thread> threads = new LinkedList<>();
        for (int i = 0; i < 1000; i++) {
            Thread thread = new Thread(() -> {
                String result = generateRoute("RLRFR", 100);
                int repetitionsR = (int) result.chars().filter(ch -> ch == 'R').count();

                synchronized (sizeToFreq) {
                    sizeToFreq.merge(repetitionsR, 1, Integer::sum);
                    sizeToFreq.notify();
                }
            });
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        for (int i = 0; i < 1000; i++) {
            Thread tr = new Thread(() -> {
                synchronized (sizeToFreq) {
                    while (!Thread.interrupted()) {
                        try {
                            sizeToFreq.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                Map.Entry<Integer, Integer> max = sizeToFreq
                        .entrySet()
                        .stream()
                        .max(Map.Entry.comparingByValue())
                        .get();
                System.out.println("Самое частое количество повторений " + max.getKey() + " (встретилось " + max.getValue() + " раз)");
                System.out.println("Другие размеры:");
                for (Map.Entry<Integer, Integer> e : sizeToFreq.entrySet()) {
                    System.out.println("- " + e.getKey() + " (" + e.getValue() + " раз)");
                }
            });
            threads.add(tr);
            tr.start();
            tr.interrupt();
        }
    }


    private static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}
