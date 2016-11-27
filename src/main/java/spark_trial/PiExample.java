package spark_trial;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.ArrayList;
import java.util.List;

/**
 * SparkPi example used for testing.
 *
 * Based on https://github.com/apache/spark/blob/master/examples/src/main/java/org/apache/spark/examples/JavaSparkPi.java
 */
public class PiExample {
    public static void main(String... argv) {
        System.out.println("Solving Pi");

        JavaSparkContext sc = new JavaSparkContext("local[4]", "PiSample");

        int slices = sc.defaultParallelism();
        int n = 100000 * slices;
        List<Integer> l = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            l.add(i);
        }

        JavaRDD<Integer> dataSet = sc.parallelize(l, slices);

        int count = dataSet.map(i -> {
            double x = Math.random() * 2 - 1;
            double y = Math.random() * 2 - 1;
            return (x * x + y * y < 1) ? 1 : 0;
        }).reduce((i1, i2) -> i1 + i2);

        double pi = 4.0 * (double)count / (double)n;
        System.out.println("Pi is roughly " + pi);
        sc.stop();
    }
}
