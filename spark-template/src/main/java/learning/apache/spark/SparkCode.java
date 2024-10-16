package learning.apache.spark;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.ArrayList;
import java.util.List;

public class SparkCode {

    public static void main(String[] args) {

        List<Double> inputData = new ArrayList<>();
        inputData.add(1.1);
        inputData.add(2.2);
        inputData.add(3.3);
        inputData.add(4.4);

        Logger.getLogger("org.apache").setLevel(Level.WARN);

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
