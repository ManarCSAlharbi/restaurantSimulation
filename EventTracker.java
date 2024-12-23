package RestaurantSimulation;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
public class EventTracker {
    private List<CustomerEvent> events = new ArrayList<>();
    private static final Object lock = new Object();

    public void addEvent(int customerId, String eventTime, String eventDescription, int timeInMinutes) {
        synchronized (lock) {
            events.add(new CustomerEvent(customerId, eventTime, eventDescription, timeInMinutes));
        }
    }

    public void printEvents() {
        synchronized (lock) {
            Collections.sort(events);
            
            int currentCustomer = -1;
            for (CustomerEvent event : events) {
                if (currentCustomer != event.customerId) {
                    if (currentCustomer != -1) {
                        System.out.println();
                    }
                    currentCustomer = event.customerId;
                }
                System.out.println("[" + event.eventTime + "] " + event.eventDescription);
            }
        }
    }

    public void reset() {
        synchronized (lock) {
            events.clear();
        }
    }
}
