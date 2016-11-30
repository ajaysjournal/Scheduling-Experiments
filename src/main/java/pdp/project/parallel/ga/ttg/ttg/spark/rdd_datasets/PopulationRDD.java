package pdp.project.parallel.ga.ttg.ttg.spark.rdd_datasets;

import org.apache.spark.api.java.JavaRDD;
import pdp.project.parallel.ga.ttg.Individual;
import all.util.SparkUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ajay on 11/28/16.
 */
public class PopulationRDD implements Serializable {

    private static JavaRDD<Individual> populationRDD;


    public static void setPopulationRDD(List<Individual> population) {
        populationRDD= SparkUtil.getSparkContext().parallelize(population);
        populationRDD.cache();
    }

    public static JavaRDD<Individual> getPopulationRDD() throws NullPointerException {
        if (populationRDD != null) {
            return populationRDD;
        }

        return null;
    }
}
