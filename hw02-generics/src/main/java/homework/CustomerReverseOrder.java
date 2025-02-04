package homework;

import java.util.Deque;
import java.util.LinkedList;

public class CustomerReverseOrder {

    private Deque<Customer> stack = new LinkedList<>();

    public void add(Customer customer) {
        stack.addLast(customer);
    }

    public Customer take() {
        return stack.removeLast();
    }
}
