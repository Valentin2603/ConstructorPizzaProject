public abstract class PizzaItem {
    protected Border border;
    protected PizzaSize size;

    public PizzaItem( Border border, PizzaSize size){
        this.border = border;
        this.size = size;
    }


    public double calculatePrice() {
        double price = 0;
        if (border != null) {
            price += border.getPrice();
        }
        return price * size.getCoefficient();
    }


    public PizzaSize getSize() {
        return size;
    }

    public Border getBorder() {
        return border;
    }
}
