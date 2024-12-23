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
