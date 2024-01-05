import functions.BaristaFunction;
import functions.CustomerToBarista;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class Barista {
    private final static int port = 7777;
    private BaristaFunction barista;

    private static void openCafe() throws IOException {
        // opening the cafe
        System.out.println("Opening cafe...");

        Socket customerSocket = null;

        try {
            ServerSocket baristaSocket = new ServerSocket(port);
            while (true) { // opens while loop to continuously check for new connections
                System.out.println("Open");
                customerSocket = baristaSocket.accept();
                BaristaFunction.getCustomerCount(0);

                // creates new thread when user connects
                Thread newThread = new Thread(new CustomerToBarista(customerSocket));
                newThread.start();
                BaristaFunction.getCustomerCount(1); // displays number of customers in cafe
            }
        } catch(IOException e){
            System.out.println("Exception");
            customerSocket.close();
        }
    }

    public static void main(String[] args) throws IOException {
        openCafe();
    }
}