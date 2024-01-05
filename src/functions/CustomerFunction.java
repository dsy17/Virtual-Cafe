package functions;

import java.util.Arrays;

public class CustomerFunction {

    // processes customer's order, outputs a String[] used to pass to BaristaFunction.waitOrder
    public String[] processOrder(String order) {
        String[] order_tokens = order.split(" ");

        int coffee_count = 0;
        int tea_count = 0;
        for (String a : order_tokens) {
            if (a.equals("coffee") || a.equals("coffees")) {
                coffee_count += Integer.parseInt(order_tokens[Arrays.asList(order_tokens).indexOf(a)-1]);
            } else if (a.equals("tea") || a.equals("teas")) {
                tea_count += Integer.parseInt(order_tokens[Arrays.asList(order_tokens).indexOf(a)-1]);
            }
        }

        return new String[] {"coffee " + coffee_count, "tea " + tea_count};

    }

}
