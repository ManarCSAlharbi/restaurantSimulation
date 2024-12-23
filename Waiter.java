package RestaurantSimulation;

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
