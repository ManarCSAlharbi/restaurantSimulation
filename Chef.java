package RestaurantSimulation;

public class Chef extends Thread {
 private int chefId;

 public Chef(int chefId) {
     this.chefId = chefId;
 }

 @Override
 public void run() {
     try {
         while (true) {
             Order order;

             RestaurantSimulation.orderQueueSemaphore.acquire();
             try {
                 if (RestaurantSimulation.orderQueue.isEmpty()) {
                     RestaurantSimulation.orderQueueSemaphore.release();
                     Thread.sleep(10);
                     continue;
                 }
                 order = RestaurantSimulation.orderQueue.poll();
             } finally {
                 RestaurantSimulation.orderQueueSemaphore.release();
             }

             if (order == null) continue;

             int startPrepSimTime = RestaurantSimulation.getCurrentSimulatedTime();
             RestaurantSimulation.updateSimulationTime(startPrepSimTime);
             
             RestaurantSimulation.eventTracker.addEvent(order.customerId,
                 RestaurantSimulation.formatTime(startPrepSimTime),
                 "Chef " + chefId + " starts preparing " + order.mealName + " for Customer " + order.customerId, 
                 startPrepSimTime);

             int prepTime = RestaurantSimulation.mealPreparationTimes.getOrDefault(order.mealName, 5);
             Thread.sleep(prepTime * RestaurantSimulation.SCALE_FACTOR);

             int finishPrepSimTime = RestaurantSimulation.getCurrentSimulatedTime();
             RestaurantSimulation.updateSimulationTime(finishPrepSimTime);
             
             RestaurantSimulation.eventTracker.addEvent(order.customerId,
                 RestaurantSimulation.formatTime(finishPrepSimTime),
                 "Chef " + chefId + " finishes preparing " + order.mealName + " for Customer " + order.customerId,
                 finishPrepSimTime);

             synchronized (RestaurantSimulation.class) {
                 RestaurantSimulation.totalPreparationTime += prepTime;
             }

             RestaurantSimulation.mealsReadyQueueSemaphore.acquire();
             try {
                 RestaurantSimulation.mealsReadyQueue.add(order);
             } finally {
                 RestaurantSimulation.mealsReadyQueueSemaphore.release();
             }

             synchronized (RestaurantSimulation.mealsReadyQueue) {
                 RestaurantSimulation.mealsReadyQueue.notifyAll();
             }
         }
     } catch (InterruptedException e) {
         // Chef thread interrupted when simulation ends
     }
 }
}
