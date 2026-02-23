import java.util.ArrayList;
import java.util.List;

public abstract class ProductComposite {
    protected String name;
    protected int price;
    private List<Ingredient> ingredients;

    public ProductComposite(String name, List<Ingredient> ingredients){
        this.name = name;
        this.ingredients = ingredients;
    }

    public void setName(String pizzaName) {
        this.name = pizzaName;
    }


    public void setIngredients(List<Ingredient> userIngredients) {
        ingredients.clear();
        ingredients.addAll(userIngredients);
    }

    public void setPrice() {
        price = 0;
        for (Ingredient ing : ingredients) {
            price += ing.getPrice();
        }

    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

}
