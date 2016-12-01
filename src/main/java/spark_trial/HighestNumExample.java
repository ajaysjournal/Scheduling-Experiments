package spark_trial;


import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ajay on 11/27/16.
 */
public class HighestNumExample {
    public static List<Double>   randIntegers =  new ArrayList<>(10000);

    public static void main(String[] args) {
         for(int i=0;i<10000;i++){
             randIntegers.add(Math.random());
         }

        System.out.println("Highest number out of 10000 digits");
        for(int i=0;i<1000;i++){
            System.out.print(" "+randIntegers.get(i)+ " ");

        }



        JavaSparkContext sc = new JavaSparkContext("local[4]", "HighestNum");

        int slices = sc.defaultParallelism();
        int n = 100000 * slices;
        List<Integer> l = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            l.add(i);
        }

        //JavaRDD<Integer> dataSet = sc.parallelize(l, slices);
        JavaRDD<Double> dataSet = sc.parallelize(randIntegers);

        List<Double> a = sc.parallelize(randIntegers).top(3);
        System.out.println("....thrid top "+sc.parallelize(randIntegers).top(3).get(2));

        double highest = dataSet.map(i -> {
            return i;
        }).reduce((i1, i2) -> {return i1>i2?i1:i2;} );

        double lowest = dataSet.map(i -> {
            return i;
        }).reduce((i1, i2) -> {return i1<i2?i1:i2;} );





        System.out.println("Highest " + highest);
        System.out.println("lowest - " + lowest);


        for(int i=0;i<1000;i++){
            System.out.print(" "+randIntegers.get(i)+ " ");

        }


        /*//JavaRDD<Integer> dataSet = sc.parallelize(l, slices);
        JavaRDD<Double> dataSet = sc.parallelize(randIntegers);

        double highest = dataSet.map(i -> {
            return i;
        }).reduce((i1, i2) -> {return i1>i2?i1:i2;} );*/


        sc.stop();
    }
}
