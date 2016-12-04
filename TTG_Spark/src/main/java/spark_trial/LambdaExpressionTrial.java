package spark_trial;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ajay on 11/27/16.
 */
public class LambdaExpressionTrial {

    class SomeJunkData{

        public int id ;


        public SomeJunkData() {
            this.id = (int)Math.random()*100;
        }



    }


    public static void main(String[] args) {
        Runnable t = () -> System.out.println("Hello World "+Math.random()*10);
        t.run();
        t.run();
        t.run();

        List<SomeJunkData> data = new ArrayList<>(100);
        t.run();





    }
}
