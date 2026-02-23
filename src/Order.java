import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {

    private UUID id;

    private LocalDateTime createdTime;

    private LocalDateTime scheduledTime;

    private String comment;

    private List<PizzaItem> items;

    public Order() {
        id = UUID.randomUUID();
        this.createdTime = LocalDateTime.now();
        this.items = new ArrayList<>();
    }

    public void addItem(PizzaItem item) {
        items.add(item);
    }

    public double getTotalPrice() {
        return items.stream()
                .mapToDouble(PizzaItem::calculatePrice)
                .sum();
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<PizzaItem> getItems() {
        return items;
    }
}