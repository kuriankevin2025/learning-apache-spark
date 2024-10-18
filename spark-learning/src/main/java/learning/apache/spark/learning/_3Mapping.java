package learning.apache.spark.learning;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.List;

public class _3Mapping {

    public static void main(String[] args) {

        System.setProperty("hadoop.home.dir", "c:/hadoop");

        List<Integer> inputData = List.of(1, 2, 3, 4);

        SparkConf sparkConf = new SparkConf()
                .setAppName("mapping")
                .setMaster("local[*]");
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);

        JavaRDD<Integer> inputDataRDD = javaSparkContext.parallelize(inputData);
        JavaRDD<Double> outputDataRDD = inputDataRDD.map((i) -> Math.pow(i, 2));

        // below line doesn't work - println can't be serialized
        // outputDataRDD.foreach(e -> System.out.println(e));   // working
        // outputDataRDD.foreach(System.out::println);          // not working

        System.out.println();
        outputDataRDD.collect().forEach(System.out::println);

        long count = outputDataRDD.count();
        System.out.println();
        System.out.println("Count: " + count);

        javaSparkContext.close();
    }
}
