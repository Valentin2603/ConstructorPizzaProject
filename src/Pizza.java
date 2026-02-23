import java.util.ArrayList;
import java.util.List;

public class Pizza extends ProductComposite {
    private BasePizza basePizza;
    private List<Ingredient> doubleIngredients = new ArrayList<>();


    public Pizza(String name, BasePizza base, List<Ingredient> ingredients){
        super(name, ingredients);
        this.basePizza = base;
        this.setPrice();
    }


    public void setBase(BasePizza userBase){
        this.basePizza = userBase;
    }

    @Override
    public void setPrice() {
        int total = basePizza.getPrice();
        for (Ingredient ing : getIngredients()) {
            total += ing.getPrice();
        }
        for (Ingredient ing : doubleIngredients) {
            total += ing.getPrice();
        }
        super.price = total;
    }

    @Override
    public double getPrice() {
        int total = basePizza.getPrice();
        for (Ingredient ing : getIngredients()) {
            total += ing.getPrice();
        }
        for (Ingredient ing : doubleIngredients) {
            total += ing.getPrice();
        }
        super.price = total;
        return super.price;
    }

    public Pizza clone() {
        return new Pizza(getName(), getBase(), new ArrayList<>(getIngredients()));
    }

    public BasePizza getBase() {
        return basePizza;
    }


    public void doubleIngredient(Ingredient ingredient) {
        doubleIngredients.add(ingredient);
    }

    public List<Ingredient> getDoubleIngredients() {
        return doubleIngredients;
    }


}

