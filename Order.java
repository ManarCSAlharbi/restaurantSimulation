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
public class Order {
    int customerId;
    String mealName;
    Customer customer;

    public Order(int customerId, String mealName, Customer customer) {
        this.customerId = customerId;
        this.mealName = mealName;
        this.customer = customer;
    }
}



