package spark.util;

import org.apache.spark.api.java.JavaSparkContext;

/**
 * Created by Ajay on 11/27/16.
 */
 public  class SparkUtil {
    private static JavaSparkContext sparkContext ;

    static{
         sparkContext = new JavaSparkContext("local[1000]", "Spark On Genetic Algorithm");
        sparkContext.setLogLevel("ERROR");  // to turn off the spark

    }

    public static JavaSparkContext getSparkContext() {
        return sparkContext;
    }

    public static void setSparkContext(JavaSparkContext sparkContext) {
        SparkUtil.sparkContext = sparkContext;
    }
}
