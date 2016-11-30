package spark_trial;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.broadcast.Broadcast;
import all.util.SparkUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ajay on 11/28/16.
 */
public class SparkBroadCastEx {

    public static void main(String[] args) {

        List<Person> individuals =new ArrayList<Person>(10000);
        for(int i = 1 ; i < 100000;i++){
            if(i% 200 == 0 )
                individuals.add(new Person("smart"+Math.random()*100,""+Math.random()*1000+""));
            else
                individuals.add(new Person("ajay"+Math.random()*100,""+Math.random()*1000+""));

        }

        JavaRDD<Person> dataSet = SparkUtil.getSparkContext().parallelize(individuals);




        Broadcast<int[]> broadcastVar = SparkUtil.getSparkContext().broadcast(new int[] {1, 2, 3});
        broadcastVar.value();

        dataSet.map(individual -> {
            for (int i:broadcastVar.value() ){
                System.out.print("i"+i);
            }
            return individual;
        }).reduce((i1, i2) -> { return Double.parseDouble(i1.getId()) > Double.parseDouble(i2.getId()) ? i1:i2;});



    }
}
