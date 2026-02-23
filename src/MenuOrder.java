import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MenuOrder {

    private Scanner sc;
    private PizzaManager pizzaManager;
    private BorderManager borderManager;
    private OrderManager orderManager;
    private BaseManager baseManager;
    private IngredientManager ingredientManager;

    public MenuOrder(Scanner sc, PizzaManager pizzaManager, BorderManager borderManager, BaseManager baseManager, IngredientManager ingredientManager) {
        this.sc = sc;
        this.pizzaManager = pizzaManager;
        this.borderManager = borderManager;
        this.orderManager = new OrderManager();
        this.baseManager = baseManager;
        this.ingredientManager = ingredientManager;
    }

    public void start() {
        while (true) {
            System.out.println("\n===== ЗАКАЗЫ =====");
            System.out.println("1) создать заказ");
            System.out.println("2) просмотреть заказы");
            System.out.println("3) удалить заказ");
            System.out.println("0) назад");

            int choice = MenuUtils.readChoiceWithZero(sc, 3);

            if (choice == 0) return;
            if (choice == 1) createOrder();
            if (choice == 2) showOrdersMenu();
            if (choice == 3) deleteOrder();
        }
    }

    public void createOrder() {

        Order order = orderManager.create();
        System.out.println("Создан заказ №" + order.getId());

        while (true) {

            System.out.println("\n1) добавить пиццу");
            System.out.println("2) завершить заказ");

            int choice = MenuUtils.readChoice(sc, 1, 2);
            if (choice == 2) break;

            PizzaItem item = createPizzaItem();
            if (item == null) {
                System.out.println("Пицца не добавлена");
                continue;
            }

            order.addItem(item);
            System.out.println("Пицца добавлена");
        }

        System.out.println("\nКомментарий?");
        System.out.println("1) да");
        System.out.println("2) нет");

        if (MenuUtils.readChoice(sc, 1, 2) == 1) {
            System.out.println("Введите комментарий:");
            order.setComment(sc.nextLine());
        }

        System.out.println("\nОтложенный заказ?");
        System.out.println("1) да");
        System.out.println("2) нет");

        if (MenuUtils.readChoice(sc, 1, 2) == 1) {
            order.setScheduledTime(readDateTime());
        }

        orderManager.add(order);
        System.out.println("Заказ создан");
    }

    private PizzaItem createPizzaItem() {

        System.out.println("\n1) пицца из системы");
        System.out.println("2) создать вручную");
        System.out.println("3) комбинированная");
        System.out.println("0) отмена");

        int choice = MenuUtils.readChoiceWithZero(sc, 3);

        if (choice == 0) return null;
        if (choice == 1) return createSimpleFromCatalog();
        if (choice == 2) return createManualPizzaItem();
        return createCombinedPizzaItem();
    }

    private PizzaItem createSimpleFromCatalog() {

        Pizza pizza = choosePizzaTemplate();
        if (pizza == null) return null;

        Pizza copy = pizza.clone();

        askDoubleIngredients(copy);

        PizzaSize size = chooseSize();
        Border border = chooseBorderOption(copy);

        return new SimplePizzaItem(copy, border, size);
    }

    private PizzaItem createManualPizzaItem() {

        System.out.println("\nВведите название:");
        String name = sc.nextLine();

        if (baseManager.getSize() == 0) {
            System.out.println("Нет основ в системе");
            return null;
        }

        System.out.println("Выберите основу:");
        MenuUtils.showBases(baseManager);
        int baseChoice = MenuUtils.readChoice(sc, 1, baseManager.getSize());
        BasePizza base = baseManager.getByIndex(baseChoice - 1);

        System.out.println("Введите через пробел номера ингредиентов (можно пусто):");
        MenuUtils.showIngredients(ingredientManager);
        List<Ingredient> selectedIngredients = readIngredientList();

        Pizza pizza = new Pizza(name, base, selectedIngredients);

        askDoubleIngredients(pizza);

        PizzaSize size = chooseSize();
        Border border = chooseBorderOption(pizza);

        return new SimplePizzaItem(pizza, border, size);
    }

    private PizzaItem createCombinedPizzaItem() {

        System.out.println("\nПервая половина:");
        Pizza left = choosePizzaTemplate();
        if (left == null) return null;
        Pizza leftCopy = left.clone();
        askDoubleIngredients(leftCopy);

        System.out.println("\nВторая половина:");
        Pizza right = choosePizzaTemplate();
        if (right == null) return null;
        Pizza rightCopy = right.clone();
        askDoubleIngredients(rightCopy);

        PizzaSize size = chooseSize();
        Border border = chooseBorderOption(leftCopy);

        return new CombinedPizzaItem(leftCopy, rightCopy, border, size);
    }

    private Pizza choosePizzaTemplate() {

        if (pizzaManager.getSize() == 0) {
            System.out.println("Нет пицц в системе");
            return null;
        }

        MenuUtils.showPizzas(pizzaManager);
        int choice = MenuUtils.readChoice(sc, 1, pizzaManager.getSize());
        return pizzaManager.getPizzaIndex(choice - 1);
    }

    private PizzaSize chooseSize() {

        System.out.println("\nРазмер:");
        System.out.println("1) SMALL");
        System.out.println("2) MEDIUM");
        System.out.println("3) LARGE");

        int choice = MenuUtils.readChoice(sc, 1, 3);

        if (choice == 1) return PizzaSize.SMALL;
        if (choice == 2) return PizzaSize.MEDIUM;
        return PizzaSize.LARGE;
    }

    private Border chooseBorderOption(Pizza pizza) {

        System.out.println("\nБортик?");
        System.out.println("1) нет");
        System.out.println("2) да");

        if (MenuUtils.readChoice(sc, 1, 2) == 1) return null;

        Border first = chooseBorder(pizza);
        if (first == null) return null;

        System.out.println("\nКомбинированный бортик?");
        System.out.println("1) нет");
        System.out.println("2) да");

        if (MenuUtils.readChoice(sc, 1, 2) == 1) return first;

        Border second = chooseBorder(pizza);
        if (second == null) return first;

        return new CombinedBorder(first, second);
    }

    private Border chooseBorder(Pizza pizza) {

        List<Border> allowed = new ArrayList<>();

        for (int i = 0; i < borderManager.getSize(); i++) {
            Border b = borderManager.getBorderIndex(i);
            if (b.isCompatibleWith(pizza)) allowed.add(b);
        }

        if (allowed.isEmpty()) {
            System.out.println("Нет доступных бортиков");
            return null;
        }

        System.out.println("\nБортики:");
        for (int i = 0; i < allowed.size(); i++) {
            Border b = allowed.get(i);
            System.out.println((i + 1) + ") " + b.getName() + " (" + b.getPrice() + ")");
        }

        int choice = MenuUtils.readChoice(sc, 1, allowed.size());
        return allowed.get(choice - 1).copy();
    }

    private void askDoubleIngredients(Pizza pizza) {

        if (pizza.getIngredients() == null || pizza.getIngredients().isEmpty()) return;

        System.out.println("\nУдвоить ингредиенты?");
        System.out.println("1) нет");
        System.out.println("2) да");

        if (MenuUtils.readChoice(sc, 1, 2) == 1) return;

       System.out.println("Выберите ингредиенты для удвоения:");
       MenuUtils.showIngredients(ingredientManager);

        List<Ingredient> selectedIngredients = Arrays.stream(sc.nextLine().split(" "))
                .map(Integer::parseInt)
                .map(i -> ingredientManager.getIngredientIndex(i - 1))
                .toList();

        for (Ingredient ing : selectedIngredients){
            pizza.doubleIngredient(ing);
        }
        System.out.println("Ингредиенты успешно удвоены!\n");

    }

    private List<Ingredient> readIngredientList() {

        String line = sc.nextLine();
        if (line.isEmpty()) return new ArrayList<>();

        return Arrays.stream(line.split(" "))
                .map(Integer::parseInt)
                .map(i -> ingredientManager.getIngredientIndex(i - 1))
                .toList();
    }

    private LocalDateTime readDateTime() {
        System.out.println("Введите дату и время (dd.MM.yyyy HH:mm)");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return LocalDateTime.parse(sc.nextLine(), formatter);
    }

    private void showOrdersMenu() {

        if (orderManager.getSize() == 0) {
            System.out.println("Заказов пока нет");
            return;
        }

        while (true) {

            System.out.println("\n===== ПРОСМОТР ЗАКАЗОВ =====");
            System.out.println("1) показать все");
            System.out.println("2) по дате создания");
            System.out.println("3) только отложенные");
            System.out.println("4) по диапазону суммы");
            System.out.println("5) сортировка по дате");
            System.out.println("0) назад");

            int choice = MenuUtils.readChoiceWithZero(sc, 5);
            if (choice == 0) return;

            List<Order> result = new ArrayList<>(orderManager.getAll());

            if (choice == 2) {
                LocalDate date = readDate();
                result = result.stream()
                        .filter(o -> o.getCreatedTime().toLocalDate().equals(date))
                        .collect(Collectors.toList());
            }

            if (choice == 3) {
                result = result.stream()
                        .filter(o -> o.getScheduledTime() != null)
                        .collect(Collectors.toList());
            }

            if (choice == 4) {
                System.out.println("min сумма:");
                double min = Double.parseDouble(sc.nextLine());
                System.out.println("max сумма:");
                double max = Double.parseDouble(sc.nextLine());

                result = result.stream()
                        .filter(o -> o.getTotalPrice() >= min && o.getTotalPrice() <= max)
                        .collect(Collectors.toList());
            }

            if (choice == 5) {
                result.sort(Comparator.comparing(Order::getCreatedTime));
            }

            printOrders(result);
        }
    }

    private void printOrders(List<Order> orders) {

        if (orders.isEmpty()) {
            System.out.println("Ничего не найдено");
            return;
        }

        for (Order order : orders) {
            System.out.println("\n----------------------------");
            System.out.println("Заказ: " + order.getId());
            System.out.println("Создан: " + order.getCreatedTime());

            if (order.getScheduledTime() != null) {
                System.out.println("Отложен: " + order.getScheduledTime());
            }

            if (order.getComment() != null && !order.getComment().isEmpty()) {
                System.out.println("Комментарий: " + order.getComment());
            }

            System.out.println("Пиццы:");
            for (PizzaItem item : order.getItems()) {
                System.out.println("- " + itemName(item) + " | " + item.calculatePrice());
            }

            System.out.println("Итого: " + order.getTotalPrice());
        }
    }

    private String itemName(PizzaItem item) {

        if (item instanceof SimplePizzaItem) {
            return ((SimplePizzaItem) item).getPizza().getName();
        }

        if (item instanceof CombinedPizzaItem) {
            CombinedPizzaItem c = (CombinedPizzaItem) item;
            return "Комбо: " + c.getLeft().getName() + " + " + c.getRight().getName();
        }

        return "Пицца";
    }

    private LocalDate readDate() {
        System.out.println("Введите дату (dd.MM.yyyy):");
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.parse(sc.nextLine(), f);
    }

    private void deleteOrder() {

        if (orderManager.getSize() == 0) {
            System.out.println("Заказов пока нет");
            return;
        }

        List<Order> orders = orderManager.getAll();

        System.out.println("\nВыберите заказ для удаления:");
        for (int i = 0; i < orders.size(); i++) {
            System.out.println((i + 1) + ") " + orders.get(i).getId());
        }

        int idx = MenuUtils.readChoice(sc, 1, orders.size());
        Order toRemove = orders.get(idx - 1);
        orderManager.remove(toRemove);
        System.out.println("Заказ удалён");
    }
}