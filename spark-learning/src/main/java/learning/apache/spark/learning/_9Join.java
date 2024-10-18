package learning.apache.spark.learning;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.Optional;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

public class _9Join {

    public static void main(String[] args) {

        List<Tuple2<Integer, String>> inputData1 = List.of(
                new Tuple2<>(1, "ID1_1"),
                new Tuple2<>(2, "ID1_2"),
                new Tuple2<>(3, "ID1_3"),
                new Tuple2<>(4, "ID1_4"),
                new Tuple2<>(5, "ID1_5")
        );

        List<Tuple2<Integer, String>> inputData2 = List.of(
                new Tuple2<>(3, "ID2_3"),
                new Tuple2<>(4, "ID2_4"),
                new Tuple2<>(5, "ID2_5"),
                new Tuple2<>(6, "ID2_6"),
                new Tuple2<>(7, "ID2_7")
        );

        Logger.getLogger("org.apache").setLevel(Level.WARN);

        SparkConf sparkConf = new SparkConf()
                .setAppName("join")
                .setMaster("local[*]");
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);

        JavaPairRDD<Integer, String> inputData1PairRDD = javaSparkContext.parallelizePairs(inputData1);
        JavaPairRDD<Integer, String> inputData2PairRDD = javaSparkContext.parallelizePairs(inputData2);

        JavaPairRDD<Integer, Tuple2<String, String>> innerJoinPairRDD = inputData1PairRDD.join(inputData2PairRDD);
        System.out.println("Inner Join:");
        innerJoinPairRDD.collect().forEach(System.out::println);

        JavaPairRDD<Integer, Tuple2<String, Optional<String>>> leftOuterJoinPairRDD = inputData1PairRDD.leftOuterJoin(inputData2PairRDD);
        System.out.println("Left Outer Join:");
        leftOuterJoinPairRDD.collect().forEach(System.out::println);

        JavaPairRDD<Integer, Tuple2<Optional<String>, String>> rightOuterJoinPairRDD = inputData1PairRDD.rightOuterJoin(inputData2PairRDD);
        System.out.println("Right Outer Join:");
        rightOuterJoinPairRDD.collect().forEach(System.out::println);

        JavaPairRDD<Integer, Tuple2<Optional<String>, Optional<String>>> fullOuterJoinPairRDD = inputData1PairRDD.fullOuterJoin(inputData2PairRDD);
        System.out.println("Full Outer Join:");
        fullOuterJoinPairRDD.collect().forEach(System.out::println);

        JavaPairRDD<Tuple2<Integer, String>, Tuple2<Integer, String>> cartesianJoinPairRDD = inputData1PairRDD.cartesian(inputData2PairRDD);
        System.out.println("Cartesian Join:");
        cartesianJoinPairRDD.collect().forEach(System.out::println);

        javaSparkContext.close();
    }
}
