public class CombinedPizzaItem extends PizzaItem {

    private Pizza left;
    private Pizza right;

    public CombinedPizzaItem(Pizza left, Pizza right, Border border, PizzaSize size) {
        super(border, size);
        this.left = left;
        this.right = right;
    }


    public double calculateBasePrice() {
        return (left.getPrice() + right.getPrice()) / 2.0;
    }

    @Override
    public double calculatePrice() {
        double total = calculateBasePrice();
        if (border != null) {
            total += border.getPrice();
        }
        return total * size.getCoefficient();
    }

    public Pizza getLeft() {
        return left;
    }

    public Pizza getRight() {
        return right;
    }
}