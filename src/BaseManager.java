import java.util.ArrayList;
import java.util.List;

public class BaseManager implements Manager<BasePizza> {
    private List<BasePizza> baseManager;
    private BasePizza myBase = new BasePizza("Классическая", 300);

    public BaseManager() {
        baseManager = new ArrayList<>();
        baseManager.add(myBase);
    }

    public BasePizza getMyBase() {
        return myBase;
    }


    public void add(BasePizza base) {
        for (BasePizza p : baseManager){
            if (p.getName().equals(base.getName())){
                System.out.println("Ошибка! Основа с таким названием уже существует.");
                return;
            }
        }
        baseManager.add(base);

    }

    public void change(BasePizza base, String name, int price){
        base.setName(name);
        base.setPrice(price);
    }

    public void remove(BasePizza baseForDelete){
        baseManager.remove(baseForDelete);
    }

    public int getSize(){
        return baseManager.size();
    }

    public BasePizza getByIndex(int index){
        return baseManager.get(index);
    }

    public List<BasePizza> getAll(){
        return baseManager;
    }


}
