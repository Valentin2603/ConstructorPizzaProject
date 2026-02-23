import java.util.ArrayList;
import java.util.List;

public class Border extends ProductComposite{
    private List<Pizza> listImpossiblePizzas;

    public Border(String name, List<Ingredient> ingredientList, List<Pizza> listImpossiblePizzas){
        super(name, ingredientList);
        this.listImpossiblePizzas = listImpossiblePizzas;
        super.setPrice();
    }

    public List<Pizza> getListImpossiblePizzas() {
        return listImpossiblePizzas;
    }

    public void setListImpossiblePizzas(List<Pizza> listImpossiblePizzas) {
        this.listImpossiblePizzas = listImpossiblePizzas;
    }

    public void removeListImpossiblePizzas() {
        listImpossiblePizzas.clear();
    }

    public boolean isCompatibleWith(Pizza pizza) {
        if (listImpossiblePizzas == null)
            return true;
        for (Pizza forbidden : listImpossiblePizzas) {
            if (forbidden.getName().equals(pizza.getName())) {
                return false;
            }

        }

        return true;
    }


    public Border copy() {

        return new Border(
                this.name,
                new ArrayList<>(this.getIngredients()),
                new ArrayList<>(this.listImpossiblePizzas)
        );

    }
}
