public class SimplePizzaItem extends PizzaItem {
    private Pizza pizza;


    public SimplePizzaItem(Pizza pizza, Border border, PizzaSize size){
        super(border, size);
        this.pizza = pizza;
    }

    @Override
    public double calculatePrice() {
        double total = pizza.getPrice();
        if (border != null) {
            total += border.getPrice();
        }
        return total * size.getCoefficient();
    }

    public Pizza getPizza() {
        return pizza;
    }
}
