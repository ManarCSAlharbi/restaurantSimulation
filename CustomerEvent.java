package RestaurantSimulation;

public class CustomerEvent implements Comparable<CustomerEvent> {
    int customerId;
    String eventTime;
    String eventDescription;
    int timeInMinutes;

    public CustomerEvent(int customerId, String eventTime, String eventDescription, int timeInMinutes) {
        this.customerId = customerId;
        this.eventTime = eventTime;
        this.eventDescription = eventDescription;
        this.timeInMinutes = timeInMinutes;
    }

    @Override
    public int compareTo(CustomerEvent other) {
        if (this.customerId != other.customerId) {
            return Integer.compare(this.customerId, other.customerId);
        }
        return Integer.compare(this.timeInMinutes, other.timeInMinutes);
    }
}
