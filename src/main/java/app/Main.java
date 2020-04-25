package app;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {
    public static void main(String[] args) {
        Queue<Integer> queue = new ConcurrentLinkedQueue();
        for (int i = 0; i < 9; i++) {
            queue.add(Integer.valueOf(i));
        }
        Integer en = queue.peek();
        Integer an = Integer.valueOf(10);
        while (en.compareTo(an) <= 0) {
            System.out.println("PEEK = " + en);
            en = queue.poll();
            en = queue.peek();
            System.out.println("POLL = " + en);
        }
    }
}
