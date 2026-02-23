import java.util.ArrayList;
import java.util.List;

public class PizzaManager implements Manager<Pizza>{
    private List<Pizza> pizzaManager;


    public PizzaManager(){
        pizzaManager = new ArrayList<>();
    }

    public int getSize(){
        return pizzaManager.size();
    }

    public Pizza getPizzaIndex(int index){
        return pizzaManager.get(index);
    }

    public void add(Pizza userPizza){

        for (Pizza p : pizzaManager){
            if (p.getName().equals(userPizza.getName())){
                System.out.println("Ошибка! Пицца с таким названием уже существует.\n\n");
                return;
            }
        }

        pizzaManager.add(userPizza);
    }

    public void changePizzaName(Pizza pizza, String name){
        pizza.setName(name);
    }

    public void changePizzaBase(Pizza pizza, BasePizza base) {
        pizza.setBase(base);
    }

    public void changePizzaIngredients(Pizza pizza, List<Ingredient> ingredientList){
        pizza.setIngredients(ingredientList);
    }

    public void remove(Pizza pizzaForDelete) {
        pizzaManager.remove(pizzaForDelete);
    }

    public List<Pizza> getAll() {
        return pizzaManager;
    }
}
