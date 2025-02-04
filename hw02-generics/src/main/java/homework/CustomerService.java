package homework;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    private TreeMap<Customer, String> customers = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        var entry = customers.firstEntry();
        return copyOfEntry(entry);
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        var entry = customers.higherEntry(customer);
        return copyOfEntry(entry);
    }

    private Map.Entry<Customer, String> copyOfEntry(Map.Entry<Customer, String> entry) {
        if (entry == null) {
            return null;
        }
        var copyOfKey = new Customer(entry.getKey());
        return Map.entry(copyOfKey, entry.getValue());
    }

    public void add(Customer customer, String data) {
        customers.put(customer, data);
    }
}
