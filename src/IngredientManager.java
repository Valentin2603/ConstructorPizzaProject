import java.util.ArrayList;
import java.util.List;

public class IngredientManager implements Manager<Ingredient>{
    private List<Ingredient> ingredientManager;


    public IngredientManager(){
        ingredientManager = new ArrayList<>();

    }

    public void add(Ingredient ingredient){
        for (Ingredient ing : ingredientManager){
            if (ing.getName().equals(ingredient.getName())){
                System.out.println("Ошибка! Ингредиент с таким названием уже существует.");
                return;
            }
        }
        ingredientManager.add(ingredient);
    }


    public void changeIngredient(Ingredient ing, String name, int price) {
        ing.setName(name);
        ing.setPrice(price);
    }

    public void remove(Ingredient ing) {
        ingredientManager.remove(ing);
    }

    public int getSize(){
        return ingredientManager.size();
    }

    public Ingredient getIngredientIndex(int index){
        return ingredientManager.get(index);
    }

    public List<Ingredient> getAll() {
        return ingredientManager;
    }
}
