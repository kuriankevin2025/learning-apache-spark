package learning.apache.spark.learning;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.Serializable;
import java.util.List;
import java.util.StringJoiner;

public class _4Object {

    public static void main(String[] args) {

        System.setProperty("hadoop.home.dir", "c:/hadoop");

        List<Integer> inputData = List.of(1, 2, 3, 4);

        SparkConf sparkConf = new SparkConf()
                .setAppName("object")
                .setMaster("local[*]");
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);

        JavaRDD<Integer> inputDataRDD = javaSparkContext.parallelize(inputData);
        JavaRDD<IntegerWithSquareRoot> outputDataRDD = inputDataRDD.map(IntegerWithSquareRoot::new);

        System.out.println();
        outputDataRDD.collect().forEach(System.out::println);

        javaSparkContext.close();
    }

    // spark can only work with objects that are serializable
    private static class IntegerWithSquareRoot implements Serializable {
        private int num;
        private double square;

        public IntegerWithSquareRoot(int num) {
            this.num = num;
            this.square = Math.pow(num, 2);
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public double getSquare() {
            return square;
        }

        public void setSquare(double square) {
            this.square = square;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", IntegerWithSquareRoot.class.getSimpleName() + "[", "]")
                    .add("num=" + num)
                    .add("square=" + square)
                    .toString();
        }
    }
}
