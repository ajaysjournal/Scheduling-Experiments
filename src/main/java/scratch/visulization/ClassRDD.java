package scratch.visulization;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.broadcast.Broadcast;
import scratch.Class;
import spark.util.SparkUtil;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Ajay on 11/28/16.
 */
public class ClassRDD {

    private  static JavaRDD<Class> classesRDD ;

    private  static  Broadcast<Class []> broadcastClassRDDVar ;

    public static void setParallelRDD(Class [] classes) {
        classesRDD=SparkUtil.getSparkContext().parallelize(new ArrayList<Class>(Arrays.asList(classes)));
        broadcastClassRDDVar = SparkUtil.getSparkContext().broadcast(classes);
    }

    public static JavaRDD<Class> getParallelRDD() throws NullPointerException {
        if (classesRDD != null) {
            return classesRDD;
        }

        return null;
    }

    public static Class[] getBroadcastClassRDDVar(){
        return  broadcastClassRDDVar.value();
    }
}