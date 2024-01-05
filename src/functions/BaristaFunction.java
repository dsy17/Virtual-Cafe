package functions;

import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BaristaFunction {
    // setting up all the queues for areas of order processing
    public ConcurrentLinkedQueue<String[]> waiting_coffee = new ConcurrentLinkedQueue();
    public ConcurrentLinkedQueue<String[]> waiting_tea = new ConcurrentLinkedQueue();
    public BlockingQueue<String[]> brewing_coffee = new LinkedBlockingQueue<>(2);
    public BlockingQueue<String[]> brewing_tea = new LinkedBlockingQueue<>(2);
    public ConcurrentLinkedQueue<String[]> tray = new ConcurrentLinkedQueue();
    public static int customerCount = 0;  // counts new customers

    // takes user's order command, passes output to waitToBrew
    public synchronized void waitOrder(String[] order, String name) throws InterruptedException {
        for (String o : order) {
            int order_num = Integer.parseInt(o.split(" ")[1]);
            if (o.split(" ")[0].equals("coffee")) {
                for (int i = 0; i < order_num; i++) {
                    waiting_coffee.add(new String[]{"coffee", name});
                }
            }
            if (o.split(" ")[0].equals("tea")) {
                for (int i = 0; i < order_num; i++) {
                    waiting_tea.add(new String[]{"tea", name});

                }
            }
        }
        getWait( waiting_coffee.size() + waiting_tea.size());
        waitToBrew();
    }

    // passes all items in waiting areas to brewing areas if there is space, passes output to brewOrder
    public synchronized void waitToBrew() throws InterruptedException {
        try {
            if (!waiting_coffee.isEmpty()) {
                brewing_coffee.put(waiting_coffee.poll());
            }

            if (!waiting_tea.isEmpty()) {
                brewing_tea.put(waiting_tea.poll());
            }

        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        getBrew(brewing_coffee.size() + brewing_tea.size());
        brewOrder();
    }

    // brews orders when not empty, passes output to trayOrder
    public synchronized void brewOrder() throws InterruptedException {
        while (!brewing_coffee.isEmpty() || !brewing_tea.isEmpty()) {
            if (!brewing_coffee.isEmpty()) {
                Thread.sleep(45000); // takes 45 seconds for coffee to brew
                tray.add(brewing_coffee.take()); // passes order to tray once done
            }
            if (!brewing_tea.isEmpty()) {
                Thread.sleep(30000); // takes 30 seconds for tea to brew
                tray.add(brewing_tea.take());
            }
        }
        getTray(tray.size());
        trayOrder();
    }

    // outputs all orders in tray once the queue receives input
    public synchronized void trayOrder() {
        while (tray.size() > 0) {
            String[] output_order = tray.poll();
            System.out.println(output_order[1] + "'s " + output_order[0] + " is ready.");
        }
    }

    // executes when customer requests order status
    public void getOrderStatus(String name) {
        String[] containsCoffee = new String[]{"coffee", name};
        String[] containsTea = new String[]{"tea", name};
        boolean order_status = false;

        if (!waiting_coffee.isEmpty() || !waiting_tea.isEmpty()) {
            System.out.println(waiting_coffee.size() + " coffees and " + waiting_tea.size() + " teas in waiting area.\n");
            order_status = true;
        }


        if (brewing_coffee.contains(containsCoffee) || brewing_tea.contains(containsTea)) {
            int occur_coffee = Collections.frequency(brewing_coffee, containsCoffee);
            int occur_tea = Collections.frequency(brewing_tea, containsTea);

            System.out.println(occur_coffee + "coffees and " + occur_tea + " currently being prepared.\n");
            order_status = true;
        }

        if (tray.contains(containsCoffee) || tray.contains(containsTea)) {
            int occur_coffee = Collections.frequency(tray, containsCoffee);
            int occur_tea = Collections.frequency(tray, containsTea);

            System.out.println(occur_coffee + "coffees and " + occur_tea + " currently in the tray.\n");
            order_status = true;
        }

        if (!order_status) {
            System.out.println("No order found for " + name);
        }

    }

    // gets number of customers in cafe
    public static void getCustomerCount(int customers) {
        customerCount += customers;
        System.out.println("Number of customers in cafe: " + customerCount);
    }

    // gets number of customers waiting for orders
    public static void getCustomerWaiting(int numCustomers) {
        System.out.println("Number of customers waiting for orders: " + numCustomers);
    }

    // gets number of coffees and teas in waiting area
    public static void getWait(int waitArea) {
        System.out.println("Number of items in waiting area: " + waitArea);
    }

    // gets number of coffees and teas brewing
    public static void getBrew(int brewArea) {
        System.out.println("Number of items in brewing area: " + brewArea);
    }

    // gets number of coffees and teas ready to be collected
    public static void getTray(int trayArea) {
        System.out.println("Number of items in tray area: " + trayArea);
    }

}
