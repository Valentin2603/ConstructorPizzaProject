import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Menu {
    Scanner sc;
    BaseManager baseManager;
    IngredientManager ingredientManager;
    PizzaManager pizzaManager;
    BorderManager borderManager;
    MenuOrder menuOrder;


    public Menu(Scanner sc){
        baseManager = new BaseManager();
        ingredientManager = new IngredientManager();
        pizzaManager = new PizzaManager();
        borderManager = new BorderManager();
        this.sc = sc;

        initData();

        this.menuOrder = new MenuOrder(sc, pizzaManager, borderManager, baseManager, ingredientManager);
    }

    private void initData(){
        BasePizza base1 = new BasePizza("Тонкая", 285);
        BasePizza base2 = new BasePizza("Сырная", 350);

        baseManager.add(base1);
        baseManager.add(base2);

        Ingredient myIngredient1 = new Ingredient("Сыр", 60);
        Ingredient myIngredient2 = new Ingredient("Курица", 100);
        Ingredient myIngredient3 = new Ingredient("Лук", 30);
        Ingredient myIngredient4 = new Ingredient("Томатная паста", 40);
        Ingredient myIngredient5 = new Ingredient("Майонез", 40);
        Ingredient myIngredient6 = new Ingredient("Томаты", 35);
        Ingredient myIngredient7 = new Ingredient("Колбаски", 55);
        Ingredient myIngredient8 = new Ingredient("Чеснок", 40);
        Ingredient myIngredient9 = new Ingredient("Кунжут", 30);

        ingredientManager.add(myIngredient1);
        ingredientManager.add(myIngredient2);
        ingredientManager.add(myIngredient3);
        ingredientManager.add(myIngredient4);
        ingredientManager.add(myIngredient5);
        ingredientManager.add(myIngredient6);
        ingredientManager.add(myIngredient7);
        ingredientManager.add(myIngredient8);
        ingredientManager.add(myIngredient9);

        Pizza pizza1 = new Pizza("Маргарита", baseManager.getMyBase(),List.of(myIngredient1, myIngredient2, myIngredient4, myIngredient6));
        Pizza pizza2 = new Pizza("Пеперони", baseManager.getMyBase(),List.of(myIngredient1, myIngredient7, myIngredient4));
        Pizza pizza3 = new Pizza("Сырная", base2, List.of(myIngredient1, myIngredient6, myIngredient9));

        pizzaManager.add(pizza1);
        pizzaManager.add(pizza2);
        pizzaManager.add(pizza3);

        Border border1 = new Border("Сырный", List.of(myIngredient1), List.of());
        Border border2 = new Border("Чесночный", List.of(myIngredient8), List.of(pizza1));
        Border border3 = new Border("С кунжутом", List.of(myIngredient9), List.of(pizza3));

        borderManager.add(border1);
        borderManager.add(border2);
        borderManager.add(border3);


    }



    private void addBase(){
        System.out.println("Укажите название: ");
        String name = sc.nextLine();
        System.out.println("Укажите стоимость:");
        int price = sc.nextInt();
        sc.nextLine();

        int maxPrice = baseManager.getMyBase().getPrice();
        if (price > maxPrice){
            System.out.println("Стоимость основы не должна превышать " + maxPrice + " рублей.\n");
            return;
        }
        BasePizza userBase = new BasePizza(name, price);
        baseManager.add(userBase);
        System.out.println("Основа успешно добавлена!");
        System.out.println("\n\n");
    }

    private void addIngredient(){
        System.out.println("Укажите название: ");
        String name = sc.nextLine();
        System.out.println("Укажите стоимость:");
        int price = sc.nextInt();
        sc.nextLine();
        Ingredient userIngredient = new Ingredient(name, price);
        ingredientManager.add(userIngredient);
        System.out.println("Ингредиент успешно добавлен!");
        System.out.println("\n\n");
    }

    private void addBorder(){
        System.out.println("Укажите название: ");
        String name = sc.nextLine();

        System.out.println("Введите через пробел номера ингредиентов для добавления в бортик: ");
        MenuUtils.showIngredients(ingredientManager);

        List<Ingredient> selectedIngredients = Arrays.stream(sc.nextLine().split(" "))
                .map(Integer::parseInt)
                .map(i -> ingredientManager.getIngredientIndex(i-1))
                .toList();

        Border userBorder = new Border(name, selectedIngredients, List.of());

        System.out.println("Хотите добавить список запрещенных пицц к этому бортику?");
        System.out.println("1. Да!\n" +
                "2. Нет.");
        int choice = MenuUtils.readChoice(sc,2);

        if (choice == 1) {
            System.out.println("Введите через пробел номера запрещенных пицц для этого бортика:");
            MenuUtils.showPizzas(pizzaManager);

            List<Pizza> selectedPizzas = Arrays.stream(sc.nextLine().split(" "))
                    .map(Integer::parseInt)
                    .map(i -> pizzaManager.getPizzaIndex(i - 1))
                    .toList();

            userBorder.setListImpossiblePizzas(selectedPizzas);
        }
        borderManager.add(userBorder);
        userBorder.setPrice();
        System.out.println("Бортик " + userBorder.getName() + " за " + userBorder.getPrice() + " рублей добавлен в систему!\n\n");

    }

    private void addPizza() {
        System.out.println("Укажите название: ");
        String name = sc.nextLine();

        System.out.println("Укажите основу: ");
        MenuUtils.showBases(baseManager);
        int choice = MenuUtils.readChoice(sc, baseManager.getSize());

        BasePizza userBase = baseManager.getByIndex(choice-1);

        System.out.println("Введите через пробел номера ингредиентов для добавления: \n");
        MenuUtils.showIngredients(ingredientManager);

        List<Ingredient> selectedIngredients = Arrays.stream(sc.nextLine().split(" "))
                .map(Integer::parseInt)
                .map(i -> ingredientManager.getIngredientIndex(i-1))
                .toList();

        Pizza userPizza = new Pizza(name, userBase, selectedIngredients);
        pizzaManager.add(userPizza);

        System.out.println("Пицца " + userPizza.getName() + " за " + userPizza.getPrice() + " рублей добавлена в систему.\n");
    }

    private void removeBase() {
        System.out.println("Выберите основу для удаления:\n");
        MenuUtils.showBases(baseManager);
        int choice = MenuUtils.readChoice(sc, baseManager.getSize());
        BasePizza baseForDel = baseManager.getByIndex(choice-1);
        boolean canDel = true;
        for (Pizza p : pizzaManager.getAll()){
            if (p.getBase() == baseForDel){
                canDel = false;
            }
        }
        if (canDel) {
            baseManager.remove(baseForDel);
            System.out.println("Основа успешно удалена!\n");
        } else {
            System.out.println("Нельзя удалить основу, которая используется в созданной пицце!\n");
        }

    }

    private void removeIngredient() {
        System.out.println("Выберите ингредиент для удаления:\n");
        MenuUtils.showIngredients(ingredientManager);
        int choice = MenuUtils.readChoice(sc,ingredientManager.getSize());
        Ingredient ingredientForDel = ingredientManager.getIngredientIndex(choice-1);

        boolean canDel = true;
        for (Pizza p : pizzaManager.getAll()){
            for (Ingredient ing : p.getIngredients()){
                if (ing == ingredientForDel){
                    canDel = false;
                }
            }
        }
        if (canDel) {
            ingredientManager.remove(ingredientForDel);
            System.out.println("Ингредиент успешно удалён!\n");
        } else {
            System.out.println("Нельзя удалить ингредиент, который используется в созданной пицце!\n");
        }

    }

    private void removeBorder() {
        System.out.println("Выберите бортик для удаления:\n");
        MenuUtils.showBorders(borderManager);
        int choice = MenuUtils.readChoice(sc, borderManager.getSize());
        Border borderForRemove = borderManager.getBorderIndex(choice-1);

        borderManager.removeBorder(borderForRemove);
        System.out.println("Бортик успешно удалён!\n\n");

    }

    private void removePizza() {
        System.out.println("Выберите пиццу для удаления:\n");
        MenuUtils.showPizzas(pizzaManager);
        int choice = MenuUtils.readChoice(sc, pizzaManager.getSize());

        pizzaManager.remove(pizzaManager.getPizzaIndex(choice-1));
        System.out.println("Пицца успешно удалена!\n");
    }

    private void make() {
        System.out.println("1) добавить основу\n" +
                "2) добавить ингредиент\n" +
                "3) добавить бортик\n" +
                "4) добавить пиццу\n");
        int choice = MenuUtils.readChoice(sc, 4);

        switch (choice) {
            case 1 -> addBase();
            case 2 -> addIngredient();
            case 3 -> addBorder();
            case 4 -> addPizza();
        }
    }

    private void delete() {
        System.out.println("1) удалить основу\n" +
                "2) удалить ингредиент\n" +
                "3) удалить бортик\n" +
                "4) удалить пиццу\n");
        int choice = MenuUtils.readChoice(sc, 4);

        switch (choice) {
            case 1 -> removeBase();
            case 2 -> removeIngredient();
            case 3 -> removeBorder();
            case 4 -> removePizza();
        }
    }

    private void changeBase() {
        System.out.println("Выберите основу:\n");
        MenuUtils.showBases(baseManager);
        int choice = MenuUtils.readChoice(sc, baseManager.getSize());

        BasePizza base = baseManager.getByIndex(choice-1);
        System.out.println("Введите новое название:\n");
        String newName = sc.nextLine();
        System.out.println("Введите новую цену:\n");
        int newPrice = sc.nextInt();
        sc.nextLine();

        baseManager.change(base, newName, newPrice);
        System.out.println("Основа - " + newName + " успешно изменена!\n");

    }

    private void changeIngredient() {
        System.out.println("Выберите ингредиент:\n");
        MenuUtils.showIngredients(ingredientManager);
        int choice = MenuUtils.readChoice(sc, ingredientManager.getSize());
        Ingredient ingredient = ingredientManager.getIngredientIndex(choice-1);
        System.out.println("Введите новое название:\n");
        String newName = sc.nextLine();
        System.out.println("Введите новую цену:\n");
        int newPrice = sc.nextInt();
        sc.nextLine();

        ingredientManager.changeIngredient(ingredient, newName, newPrice);
        System.out.println("Ингредиент успешно изменён!\n");
    }

    private void changeBorder() {
        System.out.println("Выберите бортик:\n");
        MenuUtils.showBorders(borderManager);
        int choice = MenuUtils.readChoice(sc, borderManager.getSize());
        Border border = borderManager.getBorderIndex(choice-1);

        System.out.println("1) изменить название\n" +
                "2) изменить ингредиенты\n" +
                "3) изменить список запрещенных пицц\n");
        int choice1 = sc.nextByte();
        sc.nextLine();

        switch (choice1) {
            case 1:{
                System.out.println("Введите новое название:\n");
                String newName = sc.nextLine();
                borderManager.changeName(border, newName);
                System.out.println("\nНазвание бортика изменено!\n");
                break;
            }
            case 2:{
                System.out.println("Выберите ингредиенты через пробел:\n");
                MenuUtils.showIngredients(ingredientManager);
                List<Ingredient> selectedIngredients = Arrays.stream(sc.nextLine().split(" "))
                        .map(Integer::parseInt)
                        .map(i -> ingredientManager.getIngredientIndex(i-1))
                        .toList();
                borderManager.changeIngredients(border, selectedIngredients);
                System.out.println("\nИнгредиенты изменены!\n");
                break;
            }
            case 3:{
                System.out.println("Хотите добавить список запрещенных пицц к этому бортику?");
                System.out.println("1. Да!" +
                        "2. Нет.");
                int choiceP = MenuUtils.readChoice(sc, 2);

                if (choiceP == 1) {
                    System.out.println("Введите через пробел номера запрещенных пицц для этого бортика:");
                    MenuUtils.showPizzas(pizzaManager);

                    List<Pizza> selectedPizzas = Arrays.stream(sc.nextLine().split(" "))
                            .map(Integer::parseInt)
                            .map(i -> pizzaManager.getPizzaIndex(i - 1))
                            .toList();

                    border.setListImpossiblePizzas(selectedPizzas);
                } else {
                    border.removeListImpossiblePizzas();
                }
                border.setPrice();
                break;
            }
        }
    }


    private void changePizza() {
        System.out.println("Выберите пиццу:\n");
        MenuUtils.showPizzas(pizzaManager);
        int choice = MenuUtils.readChoice(sc, pizzaManager.getSize());
        Pizza pizza = pizzaManager.getPizzaIndex(choice-1);

        System.out.println("1) изменить название\n " +
                "2) изменить основу\n" +
                "3) изменить ингредиенты\n");
        int choice1 = sc.nextByte();
        sc.nextLine();
        switch (choice1){
            case 1:{
                System.out.println("Введите новое название:\n");
                String newName = sc.nextLine();
                pizzaManager.changePizzaName(pizza, newName);
                System.out.println("\nНазвание пиццы изменено!");
                break;
            }
            case 2:{
                System.out.println("Выберите основу:\n");
                MenuUtils.showBases(baseManager);
                int choiceNewBase = sc.nextByte();
                sc.nextLine();
                BasePizza newBase = baseManager.getByIndex(choiceNewBase-1);
                pizzaManager.changePizzaBase(pizza, newBase);
                System.out.println("\nОснова изменена!\n");
                break;
            }
            case 3:{
                System.out.println("Выберите ингредиенты через пробел:\n");
                MenuUtils.showIngredients(ingredientManager);
                List<Ingredient> selectedIngredients = Arrays.stream(sc.nextLine().split(" "))
                        .map(Integer::parseInt)
                        .map(i -> ingredientManager.getIngredientIndex(i-1))
                        .toList();
                pizzaManager.changePizzaIngredients(pizza, selectedIngredients);
                System.out.println("\nИнгредиенты изменены!\n");
                break;
            }
        }




    }

    private void change() {
        System.out.println("1) редактировать основу\n" +
                "2) редактировать ингредиент\n" +
                "3) редактировать бортик\n" +
                "4) редактировать пиццу\n");
        int choice = MenuUtils.readChoice(sc, 4);

        switch (choice) {
            case 1 -> changeBase();
            case 2 -> changeIngredient();
            case 3 -> changeBorder();
            case 4 -> changePizza();
        }
    }

    private void see() {
        System.out.println("1) посмотреть существующие основы\n" +
                "2) посмотреть существующие ингредиенты\n" +
                "3) посмотреть существующие бортики\n" +
                "4) посмотреть существующие пиццы\n");
        int choice = MenuUtils.readChoice(sc, 4);

        switch (choice) {
            case 1 -> MenuUtils.showBases(baseManager);
            case 2 -> MenuUtils.showIngredients(ingredientManager);
            case 3 -> MenuUtils.showBorders(borderManager);
            case 4 -> MenuUtils.showPizzas(pizzaManager);
        }
    }

    public void start() {
        System.out.println("Введите ваше имя: \n");
        String name = sc.nextLine();
        System.out.println("Привет, " + name + ".\n\n  Меню: \n");

        while (true) {
            System.out.println("1) создание\n" +
                    "2) удаление\n" +
                    "3) редактирование\n" +
                    "4) просмотр\n" +
                    "5) заказы\n" +
                    "6) выход");

            int choice = MenuUtils.readChoice(sc, 6);

            switch (choice) {
                case 1 -> make();
                case 2 -> delete();
                case 3 -> change();
                case 4 -> see();
                case 5 -> menuOrder.start();
                case 6 -> {
                    return;
                }
            }

        }

    }
}
