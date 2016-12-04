package all.util;

import org.apache.spark.api.java.JavaSparkContext;

/**
 * Created by Ajay on 11/27/16.
 */
 public  class SparkUtil {
    private static JavaSparkContext sparkContext ;

    static{
         //sparkContext = new JavaSparkContext("local[50]", "Spark On Genetic Algorithm"); // 50 threads
         sparkContext = new JavaSparkContext("local[*]", "Spark On Genetic Algorithm");  // based on logical cores it will automatically creates the thread

        //sparkContext.setLogLevel("WARN");  // to turn off the spark

    }

    public static JavaSparkContext getSparkContext() {
        return sparkContext;
    }

    public static void setSparkContext(JavaSparkContext sparkContext) {
        SparkUtil.sparkContext = sparkContext;
    }
}
