package pdp.project.parallel.ga.ttg.ttg.spark.rdd_datasets;

import all.util.SparkUtil;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.broadcast.Broadcast;
import pdp.project.parallel.ga.ttg.Individual;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ajay on 11/28/16.
 */
public class PopulationRDD implements Serializable {

    private static JavaRDD<Individual> populationRDD;
    private static JavaRDD<Individual> sortedPopulationRDD;
    private static Broadcast<List<Individual>> broadcastVar;

    public static void setPopulationRDD(List<Individual> population) {
        populationRDD= SparkUtil.getSparkContext().parallelize(population);
        sortedPopulationRDD = SparkUtil.getSparkContext().parallelize(population);
        PopulationRDD.setSortedData(population.size());
        populationRDD.cache();
    }

    public static JavaRDD<Individual> getPopulationRDD() throws NullPointerException {
        if (populationRDD != null)
            return populationRDD;
        return null;
    }

    public static JavaRDD<Individual> getSortedPopulationRDD() throws NullPointerException {
        if (populationRDD != null)
            return sortedPopulationRDD;
        return null;
    }

    public static JavaRDD<Individual> getSortedData2() throws NullPointerException {
        if (populationRDD != null) {
            sortedPopulationRDD = populationRDD.sortBy(  i -> {return i.getFitness(); }, true, 1 );
             return sortedPopulationRDD;
        }
        return null;
    }

    public static List<Individual> getSortedData() throws NullPointerException {
        if (broadcastVar != null) {
            return broadcastVar.value();
        }
        return null;
    }

    public static void setSortedData(int popSize) throws NullPointerException {
        if (sortedPopulationRDD != null) {
            sortedPopulationRDD = populationRDD.sortBy(  i -> {return i.getFitness(); }, true, 1 );
            broadcastVar = SparkUtil.getSparkContext().broadcast(sortedPopulationRDD.top(popSize));
            broadcastVar.value();
            //sortedPopulationRDD.cache();
        }
    }


}
