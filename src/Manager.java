import java.util.List;

public interface Manager<T> {
    int getSize();

    void add(T item);

    void remove(T item);

    List<T> getAll();

}
