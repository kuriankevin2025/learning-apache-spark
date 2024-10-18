package learning.apache.spark.learning;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.List;

public class _2Reduce {

    public static void main(String[] args) {

        System.setProperty("hadoop.home.dir", "c:/hadoop");

        List<Double> inputData = List.of(1.1, 2.2, 3.3, 4.4);

        SparkConf sparkConf = new SparkConf()
                .setAppName("reduce")
                .setMaster("local[*]");
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);

        JavaRDD<Double> inputDataRDD = javaSparkContext.parallelize(inputData);
        Double output = inputDataRDD.reduce(Double::sum);

        System.out.println();
        System.out.println("Result: " + output);

        javaSparkContext.close();
    }
}
