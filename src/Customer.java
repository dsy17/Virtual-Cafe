import functions.BaristaFunction;
import functions.CustomerFunction;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

class Customer {

    private static final int port = 7777;
    private static Scanner input;


    public static void main(String[] args) throws IOException, InterruptedException {
        // sets boolean variable to false, indicates customer's no-exit status
        boolean leave = false;

        // setting up sockets and scanner
        Socket customerSocket = new Socket("localhost", port);
        input = new Scanner(System.in);

        // prompts user to enter name, used as an identifier for ordering coffees and teas
        System.out.println("Welcome to the Virtual Cafe. Enter name: ");
        String name = input.nextLine();
        CustomerFunction customer = new CustomerFunction();
        BaristaFunction barista = new BaristaFunction();


        System.out.println("Hello, "+ name +". What would you like to order?: ");
        while (!leave) {  // while user does not input "exit"
            String order = input.nextLine().toLowerCase();
            if (!order.equals("exit")) {
                if (order.equals("menu")) { // displays menu
                    System.out.println("Menu:\nTea\nCoffee");
                } else if (order.equals("order status")) { // gets user's order status
                    barista.getOrderStatus(name);
                } else if (order.substring(0, order.indexOf(" ")).equals("order")) { // if user orders a tea/coffee
                    System.out.println("New order being made");
                    String[] new_order = customer.processOrder(order);
                    barista.waitOrder(new_order, name);
                } else {
                    System.out.println("I don't understand your request, can you repeat that?");
                }
            } else { // user has input "exit", terminates program
                leave = true;
                System.out.println("Have a nice day, " + name);
                BaristaFunction.getCustomerCount(-1);
            }
        }
        customerSocket.close(); // closes user socket
    }


}
