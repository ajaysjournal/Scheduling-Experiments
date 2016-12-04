package pdp.project.parallel.ga.ttg.ttg.spark.rdd_datasets;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.broadcast.Broadcast;
import pdp.project.parallel.ga.ttg.ClassRoom;
import all.util.SparkUtil;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Ajay on 11/28/16.
 */
public class ClassRDD {

    private  static JavaRDD<ClassRoom> classesRDD ;

    private  static  Broadcast<ClassRoom[]> broadcastClassRDDVar ;

    public static void setParallelRDD(ClassRoom[] classRooms) {
        classesRDD=SparkUtil.getSparkContext().parallelize(new ArrayList<ClassRoom>(Arrays.asList(classRooms)));
        broadcastClassRDDVar = SparkUtil.getSparkContext().broadcast(classRooms);
    }

    public static JavaRDD<ClassRoom> getParallelRDD() throws NullPointerException {
        if (classesRDD != null) {
            return classesRDD;
        }

        return null;
    }

    public static ClassRoom[] getBroadcastClassRDDVar(){
        return  broadcastClassRDDVar.value();
    }
}