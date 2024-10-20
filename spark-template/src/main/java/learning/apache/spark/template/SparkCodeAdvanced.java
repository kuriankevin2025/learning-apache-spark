package learning.apache.spark.template;

import org.apache.commons.cli.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sparkproject.guava.collect.Iterables;
import scala.Tuple2;

import java.util.Arrays;

public class SparkCodeAdvanced {

    private static final Logger LOGGER = LoggerFactory.getLogger(SparkCodeAdvanced.class);

    public static void main(String[] args) {

        System.setProperty("hadoop.home.dir", "c:/hadoop");

        String inputFile = getInputFilePath(args);

        SparkConf sparkConf = new SparkConf()
                .setAppName("loadFile");
                //.setMaster("local[*]");
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);

        JavaRDD<String> inputDataRDD = javaSparkContext.textFile(inputFile);
        LOGGER.info("----- InputDataRDD Partition Size: {}", inputDataRDD.getNumPartitions());

        JavaPairRDD<String, String> inputDataPairRDD = inputDataRDD.mapToPair(line -> {
            String[] columns = line.split(":");
            return new Tuple2<>(columns[0], columns[1]);
        });
        LOGGER.info("----- InputDataPairRDD Partition Size: {}", inputDataPairRDD.getNumPartitions());

        JavaPairRDD<String, Iterable<String>> outputDataPairRDD = inputDataPairRDD.groupByKey();
        LOGGER.info("----- InputDataPairRDD Partition Size: {}", outputDataPairRDD.getNumPartitions());

        outputDataPairRDD.foreach(r -> LOGGER.info("----- Key: {} ValueCount: {}", r._1, Iterables.size(r._2)));

        // can be used to view the executor logs
//        try {
//            Thread.sleep(600000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        javaSparkContext.close();
    }

    private static String getInputFilePath(String[] args) {

        Options options = new Options();

        Option input = Option.builder("i")
                .longOpt("input")
                .desc("Input file path")
                .hasArg()
                .argName("file")
                .required()
                .build();
        options.addOption(input);

        HelpFormatter formatter = new HelpFormatter();
        String message = """
                java --add-exports=java.base/sun.nio.ch=ALL-UNNAMED \
                -cp "target/spark-template-java-17.jar;target/libs/*" \
                learning.apache.spark.template.SparkCodeAdvanced [options]
                """;

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        String inputFile = "";
        try {
            cmd = parser.parse(options, args);

            inputFile = cmd.getOptionValue("input");
            LOGGER.info("----- Input File: {}" ,inputFile);

        } catch (ParseException e) {
            LOGGER.warn("Arguments: {}", Arrays.asList(args));
            LOGGER.error("Error: ", e);
            formatter.printHelp(message, options, true);
            System.exit(1);
        }
        return inputFile;
    }
}
