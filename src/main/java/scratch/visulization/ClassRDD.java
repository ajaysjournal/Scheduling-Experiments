package scratch.visulization;

import org.apache.spark.api.java.JavaRDD;
import scratch.Class;
import spark.util.SparkUtil;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Ajay on 11/28/16.
 */
public class ClassRDD {

    private  static JavaRDD<Class> classesRDD ;

    public static JavaRDD<Class> setParallelRDD(Class [] classes) {
        return SparkUtil.getSparkContext().parallelize(new ArrayList<Class>(Arrays.asList(classes)));
    }

    public static JavaRDD<Class> getParallelRDD() throws NullPointerException {
        if (classesRDD != null) {
            return this.classesRDD;
        }

        return null;
    }
}