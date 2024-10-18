package learning.apache.spark.learning;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.List;

public class _5Tuple {

    public static void main(String[] args) {

        System.setProperty("hadoop.home.dir", "c:/hadoop");

        List<Integer> inputData = List.of(1, 2, 3, 4);

        SparkConf sparkConf = new SparkConf()
                .setAppName("tuple")
                .setMaster("local[*]");
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);

        JavaRDD<Integer> inputDataRDD = javaSparkContext.parallelize(inputData);
        JavaRDD<Tuple2<Integer, Double>> outputDataRDD = inputDataRDD.map(n -> new Tuple2<>(n, Math.pow(n, 2)));

        System.out.println();
        outputDataRDD.collect().forEach(System.out::println);

        javaSparkContext.close();
    }
}
