import java.util.ArrayList;

/**
 * Created by Ajay on 11/27/16.
 */
public class Main {
    public static void main(String[] args) {
        System.out.print("Hello Spark World ");
        ArrayList<Integer> ints = new ArrayList<>(10);

        for(int i=0;i<10; i++){
            ints.add(i*i*i);
        }
        for(int i=0;i<10; i++){
            System.out.println("Hello Spark World "+ints.get(i));
        }


    }
}
