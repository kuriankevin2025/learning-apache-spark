package learning.apache.spark.learning;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.List;

public class _6PairRDD {

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
        JavaPairRDD<String, Long> inputDataPairRDD =
                inputDataRDD.mapToPair(s -> new Tuple2<>(s.split(":")[0].trim(), 1L));
        JavaPairRDD<String, Long> outputDataPairRDD = inputDataPairRDD.reduceByKey(Long::sum);

        System.out.println();
        outputDataPairRDD.collect().forEach(System.out::println);

        // Fluent API/Interface
        // groupByKey can lead to severe performance problems - use only if there r no other alternatives
        System.out.println();
        javaSparkContext
                .parallelize(inputData)
                .mapToPair(s -> {
                    String[] columns = s.split(":");
                    return new Tuple2<>(columns[0].trim(), columns[1]);
                })
                .groupByKey()
                .mapValues(il -> {
                    int count = 0;
                    for (String s : il) {
                        count++;
                    }
                    return count;
                })
                .collect()
                .forEach(System.out::println);

        javaSparkContext.close();
    }
}
