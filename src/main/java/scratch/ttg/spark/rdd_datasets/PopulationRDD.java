package scratch.ttg.spark.rdd_datasets;

import org.apache.spark.api.java.JavaRDD;
import scratch.Individual;
import spark.util.SparkUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ajay on 11/28/16.
 */
public class PopulationRDD implements Serializable {

    private static JavaRDD<Individual> populationRDD;


    public static void setPopulationRDD(List<Individual> population) {
        populationRDD= SparkUtil.getSparkContext().parallelize(population);
    }

    public static JavaRDD<Individual> getPopulationRDD() throws NullPointerException {
        if (populationRDD != null) {
            return populationRDD;
        }

        return null;
    }
}
