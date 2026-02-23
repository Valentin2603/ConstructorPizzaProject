
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderManager implements Manager<Order>{

    private List<Order> orders = new ArrayList<>();


    public Order create() {
        return new Order();
    }

    public void add(Order order) {
        orders.add(order);
    }

    public List<Order> getAll() {
        return orders;
    }


    public void remove(Order item) {
        orders.remove(item);
    }

    @Override
    public int getSize() {
        return orders.size();
    }

    public List<Order> getOrdersByDate(LocalDate date) {
        return orders.stream()
                .filter(i -> i.getCreatedTime().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }
}