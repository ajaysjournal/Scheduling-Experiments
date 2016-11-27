package spark_trial;

import org.apache.spark.api.java.JavaRDD;
import spark.util.SparkUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ajay on 11/26/16.
 */

public class ListPersonExample {
    public static void main(String[] args) {
        // creation
        ArrayList<Person> population;
        population = new ArrayList<Person>();
        for(int i=0;i<10;i++){
            population.add(new Person("A-"+i,""+Math.random()*10));

        }

        for (Person person: population) {
            System.out.println(person.getName());

        }


        System.out.println("Displaying list");
        int slices = SparkUtil.getSparkContext().defaultParallelism();
        int n = 100000 * slices;
        List<Integer> l = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            l.add(i);
        }

        JavaRDD<Person> dataSet = SparkUtil.getSparkContext().parallelize(population);

        int count = dataSet.map(person -> {
            System.out.println(person.toString());
            return 1;
        }).reduce((i1, i2) -> i1 + i2);


        String string = dataSet.map(person -> {
            //System.out.println(person.getName());
            return person.toString();
        }).reduce((i1, i2) -> i1+"::::"+i2);

        System.out.print(string.toString());

        System.out.println("Pi is roughly " + count);


        SparkUtil.getSparkContext().stop();


    }
}
