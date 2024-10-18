package learning.apache.spark.learning;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

public class _7FlatMap {

    public static void main(String[] args) {

        System.setProperty("hadoop.home.dir", "c:/hadoop");

        List<String> inputData = List.of(
                "WARN  : Message1",
                "ERROR : Message2",
                "FATAL : Message3",
                "ERROR : Message4",
                "WARN  : Message5");

        SparkConf sparkConf = new SparkConf()
                .setAppName("reduce")
                .setMaster("local[*]");
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);

        JavaRDD<String> inputDataRDD = javaSparkContext.parallelize(inputData);
        JavaRDD<String> outputDataRDD =
                inputDataRDD
                        .flatMap(s -> Arrays.asList(s.split(":")).iterator())
                        .map(String::trim);

        System.out.println();
        outputDataRDD.collect().forEach(System.out::println);

        javaSparkContext.close();
    }
}
