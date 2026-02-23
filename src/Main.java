import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        Menu menu = new Menu(sc);
        menu.start();
        sc.close();
    }
}