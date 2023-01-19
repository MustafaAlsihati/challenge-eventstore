package net.intelie.challenges.challenge;

import net.intelie.challenges.Event;
import java.io.Closeable;
import java.util.List;

public class EventIterator implements net.intelie.challenges.EventIterator {
    private final List<Event> list;
    int index=0;
    public EventIterator(List<Event> startQueue) {
        this.list =startQueue;
    }

    // Move to the next event
    @Override
    public boolean moveNext() {
        if(index + 1<this.list.size()) {
            index++;
            return true;

        }
        return false;
    }

    // Current event
    @Override
    public Event current() {
        return list.get(this.index);
    }

    // Remove current event
    @Override
    public void remove() {
        this.list.remove(index);
    }

    // Return the size
    @Override
    public int size() {
        return list.size();
    }

    @Override
    public void close() throws Exception {
       index=-1;
    }

    @Override
    public String toString() {
        return "[list]: " + list;
    }
}