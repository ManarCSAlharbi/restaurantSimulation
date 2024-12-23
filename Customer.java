package finalosprojectfrrrrrr;
/*

Group Members:
1- Manar Abdullah Alharbi - 2206712
2- Lamar Haitham Fatani 
- 2210455
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
import java.util.concurrent.Semaphore;

public class Customer extends Thread {
 private int customerId;
 private int arrivalTime;
 private String arrivalTimeStr;
 private String order;
 private Semaphore mealServed = new Semaphore(0);
 private int tableAssigned = -1;
 private int waitStartTime;
 private int waitEndTime;

 public Customer(int customerId, String arrivalTimeStr, String order) {
     this.customerId = customerId;
     this.arrivalTimeStr = arrivalTimeStr;
     this.arrivalTime = RestaurantSimulation.parseArrivalTimeToMinutes(arrivalTimeStr);
     this.order = order;
 }

 public int getTableAssigned() {
     return tableAssigned;
 }

 public String getArrivalTime() {
     return arrivalTimeStr;
 }

 @Override
 public void run() {
     try {
         // Sleep until arrival time
         int sleepTime = arrivalTime * RestaurantSimulation.SCALE_FACTOR;
         if (sleepTime > 0) {
             Thread.sleep(sleepTime);
         }

         int currentSimTime = RestaurantSimulation.getCurrentSimulatedTime();
         RestaurantSimulation.updateSimulationTime(currentSimTime);
         RestaurantSimulation.eventTracker.addEvent(customerId, 
             RestaurantSimulation.formatTime(currentSimTime),
             "Customer " + customerId + " arrives.", currentSimTime);

         waitStartTime = currentSimTime;
         RestaurantSimulation.tablesSemaphore.acquire();

         RestaurantSimulation.availableTablesSemaphore.acquire();
         try {
             if (!RestaurantSimulation.availableTables.isEmpty()) {
                 tableAssigned = RestaurantSimulation.availableTables.remove(0);
             }
         } finally {
             RestaurantSimulation.availableTablesSemaphore.release();
         }

         currentSimTime = RestaurantSimulation.getCurrentSimulatedTime();
         RestaurantSimulation.updateSimulationTime(currentSimTime);
         waitEndTime = currentSimTime;
         
         synchronized (RestaurantSimulation.class) {
             RestaurantSimulation.totalWaitTime += (waitEndTime - waitStartTime);
         }

         RestaurantSimulation.eventTracker.addEvent(customerId,
             RestaurantSimulation.formatTime(currentSimTime),
             "Customer " + customerId + " is seated at Table " + tableAssigned, currentSimTime);

         RestaurantSimulation.eventTracker.addEvent(customerId,
             RestaurantSimulation.formatTime(currentSimTime),
             "Customer " + customerId + " places an order: " + order, currentSimTime);

         RestaurantSimulation.orderQueueSemaphore.acquire();
         try {
             RestaurantSimulation.orderQueue.add(new Order(customerId, order, this));
         } finally {
             RestaurantSimulation.orderQueueSemaphore.release();
         }

         synchronized (RestaurantSimulation.orderQueue) {
             RestaurantSimulation.orderQueue.notifyAll();
         }

         mealServed.acquire();
         Thread.sleep(15 * RestaurantSimulation.SCALE_FACTOR);

         currentSimTime = RestaurantSimulation.getCurrentSimulatedTime();
         RestaurantSimulation.updateSimulationTime(currentSimTime);
         
         RestaurantSimulation.eventTracker.addEvent(customerId,
             RestaurantSimulation.formatTime(currentSimTime),
             "Customer " + customerId + " finishes eating and leaves the restaurant.", currentSimTime);
             
         RestaurantSimulation.eventTracker.addEvent(customerId,
             RestaurantSimulation.formatTime(currentSimTime),
             "Table " + tableAssigned + " is now available.", currentSimTime);

         RestaurantSimulation.availableTablesSemaphore.acquire();
         try {
             RestaurantSimulation.availableTables.add(tableAssigned);
         } finally {
             RestaurantSimulation.availableTablesSemaphore.release();
         }
         RestaurantSimulation.tablesSemaphore.release();

         synchronized (RestaurantSimulation.class) {
             RestaurantSimulation.totalCustomersServed++;
         }

     } catch (InterruptedException e) {
         e.printStackTrace();
     }
 }

 public void notifyMealServed() {
     mealServed.release();
 }
}