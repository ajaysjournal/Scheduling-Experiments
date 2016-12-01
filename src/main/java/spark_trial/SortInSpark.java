package spark_trial;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Ajay on 11/27/16.
 */
public class SortInSpark {

    public static void main(String[] args) {
        List<Person> individuals =new  ArrayList<Person>(10000);
        for(int i = 1 ; i < 100000;i++){
            if(i% 200 == 0 )
                individuals.add(new Person("smart"+Math.random()*100,""+Math.random()*1000+""));
            else
                individuals.add(new Person("ajay"+Math.random()*100,""+Math.random()*1000+""));

        }
        JavaSparkContext sc = new JavaSparkContext("local[4]", "IndividualChromosomeInSpark");


        JavaRDD<Person> dataSet = sc.parallelize(individuals); // this.Individual todo : this mush with respect the population

        Person i = dataSet.map(individual -> {
            return individual;
        }).reduce((i1, i2) -> { return Double.parseDouble(i1.getId()) > Double.parseDouble(i2.getId()) ? i1:i2;});

        System.out.print("highest == "+i.toString());

        i = dataSet.map(individual -> {
            return individual;
        }).reduce((i1, i2) -> { return Double.parseDouble(i1.getId()) < Double.parseDouble(i2.getId()) ? i1:i2;});


        List<Person> nPersons = dataSet.filter(
                individual -> {
                         if (Double.parseDouble(individual.getId()) > 500)
                             return true;
                        else return false;
                }
        ).collect();

        System.out.println("id > 500 counts "+nPersons.size());


        // Sort and take the 3rd largest --
        Person n2Persons = (Person) dataSet.top(3, new Comparator<Person>() {
            public int compare(Person o1, Person o2) {
                if (Double.parseDouble(o1.getId()) > Double.parseDouble(o2.getId())) {
                    return -1;
                } else if (Double.parseDouble(o1.getId()) < Double.parseDouble(o2.getId())) {
                    return 1;
                }
                return 0;
            }
        } );



        nPersons = dataSet.filter(individual -> {
            if(individual.getName().contains("smart"))
                return true;
            else return false;
        }).collect();
        System.out.println("Smart count "+nPersons.size());

        nPersons = dataSet.filter(individual -> {
            if(individual.getName().contains("ajay"))
                return true;
            else return false;
        }).collect();

        System.out.println("ajay word  count "+nPersons.size());


        System.out.print("lowest ==  "+i.toString());

        sc.stop();



    }

}
