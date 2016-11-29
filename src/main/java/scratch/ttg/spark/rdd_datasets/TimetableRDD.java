package scratch.ttg.spark.rdd_datasets;

import org.apache.spark.broadcast.Broadcast;
import scratch.Timetable;
import spark.util.SparkUtil;

/**
 * Created by Ajay on 11/28/16.
 */
public class TimetableRDD {

    private  static Broadcast<Timetable > timetableBroadcast ;

    public static Timetable getTimetable() {
        return timetableBroadcast.value();
    }

    public static void setTimetableBroadcast(Timetable timetable) {
        TimetableRDD.timetableBroadcast  = SparkUtil.getSparkContext().broadcast(timetable);
    }
}
