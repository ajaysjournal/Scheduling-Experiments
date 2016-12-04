package pdp.project.parallel.ga.ttg.ttg.spark.rdd_datasets;

import org.apache.spark.broadcast.Broadcast;
import pdp.project.parallel.ga.ttg.Timetable;
import all.util.SparkUtil;

/**
 * Created by Ajay on 11/28/16.
 */
public class TimetableRDD {

    private  static Broadcast<Timetable> timetableBroadcast ;

    public static Timetable getTimetable() {
        return timetableBroadcast.value();
    }

    public static void setTimetableBroadcast(Timetable timetable) {
        TimetableRDD.timetableBroadcast  = SparkUtil.getSparkContext().broadcast(timetable);
    }
}
