package net.intelie.challenges.challenge;

import net.intelie.challenges.Event;
import net.intelie.challenges.EventIterator;
import org.junit.Test;
import java.util.Random;
import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;

public class ThreadSafeTest {
    private final Random rand;

    String[] types = {"type_one", "type_two", "type_tree", "type_four"};
    int[] number_by_type = {0, 0, 0, 0};
    Integer NUM_TEST = 2000;
    Integer NUM_THREAD = 20;

    public ThreadSafeTest() {
        this.rand = new Random();
    }

    @Test
    public void ThreadTest() {
        EventStore eventStore = new EventStore();
        for (int i = 0; i < NUM_THREAD; i++) {
            this.makeThread(eventStore);
        }
    }

    public void makeThread(EventStore eventStore) {
        new Thread(() -> {
            for (int i = 0; i < NUM_TEST; i++) {
                this.processEachFor(eventStore);
                try {
                    sleep(rand.nextInt(15));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void processEachFor(EventStore eventStore) {
        int type_number;
        int func = rand.nextInt(10);
        switch (func) {
            case 0:
                type_number = this.rand.nextInt(4);
                EventIterator eventIterator = eventStore.query(types[type_number], 0L, Long.MAX_VALUE);
                assertEquals(number_by_type[type_number], eventIterator.size());
                return;
            case 1:
                type_number = this.rand.nextInt(4);
                eventStore.removeAll(types[type_number]);
                number_by_type[type_number] = 0;
                return;
            default:
                type_number = this.rand.nextInt(4);
                Event event = this.makeEvent(type_number);
                eventStore.insert(event);
                number_by_type[type_number]++;
        }
    }

    private Event makeEvent(int type_number) {
        return new Event(types[type_number], System.currentTimeMillis());
    }
}