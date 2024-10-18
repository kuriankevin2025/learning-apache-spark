package learning.apache.spark.learning;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class _10LoadFile {

    public static void main(String[] args) {

        System.setProperty("hadoop.home.dir", "c:/hadoop");

        SparkConf sparkConf = new SparkConf()
                .setAppName("loadFile")
                .setMaster("local[*]");
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);

        JavaRDD<String> inputDataRDD =
                javaSparkContext.textFile(
                        "spark-learning/src/main/resources/spark/inputData.txt");

        inputDataRDD.collect().forEach(System.out::println);

        javaSparkContext.close();
    }
}
