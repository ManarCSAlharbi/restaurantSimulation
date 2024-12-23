package finalosprojectfrrrrrr;
/*

Group Members:
1- Manar Abdullah Alharbi - 2206712
2- Lamar Haitham Fatani - 2210455
3- Dana Khaled Alotaibi - 2129391
Group Number: 16
Section: 03


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
