package RestaurantSimulation;

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



