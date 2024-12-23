package finalosprojectfrrrrrr;
/*
Compiler:
java version "1.8.0_371"
NetBeans IDE 8.2

Hardware Configuration:
Processor	11th Gen Intel(R) Core(TM) i7-1165G7 @ 2.80GHz   2.80 GHz
Installed RAM	16.0 GB (15.8 GB usable)

Operating System: 
Edition	Windows 11 Home
Version	23H2


*/
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
