package spark_trial;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;


/**
 * Hello Spark!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello Spark!" );
        String logFile="data.txt";
        SparkConf conf = new SparkConf().setMaster("local[4]").setAppName("wordCount");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> logData = sc.textFile("data.txt").cache();
        long numAs = logData.filter(new Function<String,Boolean>()
        	{public Boolean call (String s) {return s.contains("a");}}).count();
        
        System.out.println("a count "+numAs);
        
    }
    
    
}
