import java.util.*;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Thread thread = new Thread(() -> {
                String result = generateRoute("RLRFR", 100);
                int repetitionsR = (int) result.chars().filter(ch -> ch == 'R').count();

                synchronized (sizeToFreq) {
                    sizeToFreq.merge(repetitionsR, 1, Integer::sum);
                }
            });
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        int maxR = 0;
        int count = 'R';
        for (Map.Entry<Integer, Integer> max : sizeToFreq.entrySet()) {
            if (maxR < max.getValue()) {
                maxR = max.getValue();
                count = max.getKey();
            }
        }
        System.out.println("Самое частое количество повторений " + count + " (встретилось " + maxR + " раз)");
        System.out.println("Другие размеры:");

        for (Map.Entry<Integer, Integer> e : sizeToFreq.entrySet()) {
            System.out.println(" - " + e.getKey() + " (" + e.getValue() + " раз)");

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
