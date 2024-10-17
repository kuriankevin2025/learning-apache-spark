package learning.apache.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class SparkCode {

    private static final Logger LOGGER = LoggerFactory.getLogger(SparkCode.class);

    public static void main(String[] args) {

        System.setProperty("hadoop.home.dir", "c:/hadoop");

        List<Double> inputData = Arrays.asList(1.1, 2.2, 3.3, 4.4);

        SparkConf sparkConf = new SparkConf()
                .setAppName("reduce")
                .setMaster("local[*]");
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);

        JavaRDD<Double> inputDataRDD = javaSparkContext.parallelize(inputData);
        Double output = inputDataRDD.reduce(Double::sum);

        System.out.println();
        System.out.println("Result: " + output);
        LOGGER.info("Result: {}", output);

        javaSparkContext.close();
    }
}
