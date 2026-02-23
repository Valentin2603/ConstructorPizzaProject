import java.util.*;

public class MenuUtils {
    public static int readChoice(Scanner sc, int max) {
        return readChoice(sc, 1, max);
    }

    public static int readChoiceWithZero(Scanner sc, int max) {
        return readChoice(sc, 0, max);
    }

    public static int readChoice(Scanner sc, int min, int max) {
        while (true) {
            String raw = sc.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(raw);
            } catch (Exception e) {
                System.out.println("Неправильный ввод! Введите число от " + min + " до " + max + ".\n");
                continue;
            }

            if (choice < min || choice > max) {
                System.out.println("Неправильный ввод! Введите от " + min + " до " + max + ".\n");
                continue;
            }
            return choice;
        }
    }

    public static void showBases(BaseManager baseManager){
        for (int n = 1; n <= baseManager.getSize(); ++n){
            BasePizza base = baseManager.getByIndex(n-1);
            System.out.println(n + ". " + base.getName() + " - " + base.getPrice() + " рублей.");
        }
        System.out.println("\n\n");
    }

    public static void showIngredients(IngredientManager ingredientManager){
        for (int n = 1; n <= ingredientManager.getSize(); ++n){
            Ingredient ingredient = ingredientManager.getIngredientIndex(n-1);
            System.out.println(n + ". " + ingredient.getName() + " - " + ingredient.getPrice() + " рублей.");
        }
        System.out.println("\n\n");
    }

    public static void showBorders(BorderManager borderManager){
        for (int n = 1; n <= borderManager.getSize(); ++n){
            Border border = borderManager.getBorderIndex(n-1);
            List<Ingredient> ingredientsBorder = border.getIngredients();
            List<Pizza> impossiblePizzas = border.getListImpossiblePizzas();
            System.out.println(n + ". " + border.getName() + " - " + border.getPrice() + " рублей.");

            if (!border.getIngredients().isEmpty()) {
                System.out.print("Ингредиенты: ");
                for (int i = 0; i < ingredientsBorder.size() - 1; ++i) {
                    System.out.print(ingredientsBorder.get(i).getName() + ", ");
                }
                System.out.println(ingredientsBorder.get(ingredientsBorder.size() - 1).getName() + ".");
            }

            if (!border.getListImpossiblePizzas().isEmpty()){
                System.out.print("Список запрещенных пицц: ");
                for (int i = 0; i < impossiblePizzas.size() - 1; ++i){
                    System.out.print(impossiblePizzas.get(i).getName() + ", ");
                }
                System.out.println(impossiblePizzas.get(impossiblePizzas.size() - 1).getName() + ".");
            }
            System.out.println();
        }
        System.out.println("\n\n");
    }

    public static void showPizzas(PizzaManager pizzaManager){
        for (int n = 1; n <= pizzaManager.getSize(); ++n) {
            Pizza pizza = pizzaManager.getPizzaIndex(n-1);
            BasePizza base = pizza.getBase();
            List<Ingredient> ingredientsPizza = pizza.getIngredients();
            System.out.println(n + ". " + pizza.getName() + " - " + pizza.getPrice() + " рублей.");
            System.out.println("Основа: " + base.getName() + ".");
            System.out.print("Ингредиенты: ");
            for (int i = 0; i < ingredientsPizza.size() - 1; ++i) {
                System.out.print(ingredientsPizza.get(i).getName() + ", ");
            }
            System.out.println(ingredientsPizza.get(ingredientsPizza.size() - 1).getName() + ".\n");
        }
        System.out.println("\n\n");
    }
}
