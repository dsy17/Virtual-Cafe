package functions;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class CustomerToBarista implements Runnable {

    private final Socket customerSocket;
    private Scanner input;
    private PrintWriter output;

    // setting up customer socket
    public CustomerToBarista(Socket customerSocket) throws IOException {
        this.customerSocket = customerSocket;
    }

    @Override
    public void run() {
        try {
            // setting up input/output
            input = new Scanner(customerSocket.getInputStream());
            output = new PrintWriter(customerSocket.getOutputStream());

            while (true) { // while customer is in cafe
                String customerInput = input.nextLine().toLowerCase();
                if (customerInput.equals("exit")) {
                    output.println("Customer is leaving the cafe.");
                    customerSocket.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
