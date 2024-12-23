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
public class Waiter extends Thread {
    private int waiterId;

    public Waiter(int waiterId) {
        this.waiterId = waiterId;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Order order;

                RestaurantSimulation.mealsReadyQueueSemaphore.acquire();
                try {
                    if (RestaurantSimulation.mealsReadyQueue.isEmpty()) {
                        RestaurantSimulation.mealsReadyQueueSemaphore.release();
                        Thread.sleep(10);
                        continue;
                    }
                    order = RestaurantSimulation.mealsReadyQueue.poll();
                } finally {
                    RestaurantSimulation.mealsReadyQueueSemaphore.release();
                }

                if (order == null) continue;

                int currentSimTime = RestaurantSimulation.getCurrentSimulatedTime();
                RestaurantSimulation.updateSimulationTime(currentSimTime);
                
                RestaurantSimulation.eventTracker.addEvent(order.customerId,
                    RestaurantSimulation.formatTime(currentSimTime),
                    "Waiter " + waiterId + " serves " + order.mealName + " to Customer " + order.customerId +
                    " at Table " + order.customer.getTableAssigned(), currentSimTime);

                order.customer.notifyMealServed();
            }
        } catch (InterruptedException e) {
            // Waiter thread interrupted when simulation ends
        }
    }
}