package net.intelie.challenges.challenge;

import net.intelie.challenges.Event;
import net.intelie.challenges.EventIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventStore implements net.intelie.challenges.EventStore {
    private List<Event> events;

    public EventStore() {
        this.events = new ArrayList<>();
    }

    // Stores an event
    @Override
    public void insert(Event event) {
        synchronized (EventStore.class) {
            this.events.add(event);
        }
    }

    // Removes all events by type
    @Override
    public void removeAll(String type) {
        synchronized (EventStore.class) {
            this.events = this.events.stream()
                    .filter(event -> !event.type().equals(type))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
    }

    // Retrieves an iterator for events based on their type and timestamp.
    @Override
    public EventIterator query(String type, long startTime, long endTime) {
        if (type == null)
            return new net.intelie.challenges.v1.EventIterator(new ArrayList<>());
        synchronized (EventStore.class) {
            List<Event> eventsFiltered = events.stream().filter(event ->
                    event.type().equals(type) &&
                            event.timestamp() <= endTime &&
                            event.timestamp() >= startTime
            ).collect(Collectors.toCollection(ArrayList::new));
            return new net.intelie.challenges.v1.EventIterator(eventsFiltered);
        }
    }

    @Override
    public String toString() {
        synchronized (EventStore.class) {
            return "[events]: " + events;
        }
    }
}