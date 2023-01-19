package net.intelie.challenges.challenge;

import net.intelie.challenges.Event;
import net.intelie.challenges.EventIterator;
import org.junit.Test;
import static org.junit.Assert.*;

public class EventStoreTest {

    @Test
    public void TestInsert(){
        EventStore eventStore=new EventStore();
        Event event1 = new Event("some_type", 1000L);
        eventStore.insert(event1);
        EventIterator eventIterator=eventStore.query(event1.type(),event1.timestamp(),event1.timestamp());
        Event event=eventIterator.current();
        assertEquals(event1.type(),event.type());
        assertEquals(event1.timestamp(),event.timestamp());
        assertFalse(eventIterator.moveNext());
    }

    @Test
    public void removeOneType(){
        EventStore eventStore=new EventStore();
        Event event1 = new Event("type_one", 1000L);
        Event event2 = new Event("type_two", 1000L);
        eventStore.insert(event1);
        eventStore.insert(event2);
        eventStore.removeAll("type_one");
        EventIterator eventInterator=eventStore.query(event1.type(),event1.timestamp(),event1.timestamp());
        assertEquals(0,eventInterator.size());

        boolean moveNext;
        eventInterator=eventStore.query(event2.type(),event1.timestamp(),event1.timestamp());
        Event current=eventInterator.current();
        assertEquals(current.type(),event2.type());
        assertEquals(current.timestamp(),event2.timestamp());
        moveNext=eventInterator.moveNext();
        assertFalse(moveNext);
    }
    @Test
    public void queryTestType(){
        EventStore eventStore=new EventStore();
        Event event1 = new Event("type_one", 1000L);
        eventStore.insert(event1);
        EventIterator eventInterator=eventStore.query(event1.type(),event1.timestamp(),event1.timestamp());
        assertEquals(net.intelie.challenges.v1.EventIterator.class, eventInterator.getClass());

        eventStore.insert(new Event("type_one", System.currentTimeMillis()));
        eventStore.insert(new Event("type_one", System.currentTimeMillis()));
        eventStore.insert(new Event("type_one", System.currentTimeMillis()));
        eventInterator=eventStore.query(event1.type(),event1.timestamp(),event1.timestamp());
        int sizeinitial= eventInterator.size();
        Event event=eventInterator.current();
        assertNotNull(event);
        eventInterator.remove();
        assertEquals(sizeinitial-1,eventInterator.size());
    }
}