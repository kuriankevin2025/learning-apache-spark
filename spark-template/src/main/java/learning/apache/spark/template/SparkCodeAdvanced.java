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
                .setAppName("sparkCodeAdvanced");
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);

        JavaRDD<String> inputDataRDD = javaSparkContext.textFile(inputFile);
        LOGGER.info("----- InputDataRDD Partition Size: {}", inputDataRDD.getNumPartitions());

        JavaPairRDD<String, Long> inputDataPairRDD =
                inputDataRDD.mapToPair(s -> new Tuple2<>(s.split(":")[0].trim(), 1L));
        JavaPairRDD<String, Long> outputDataPairRDD = inputDataPairRDD.reduceByKey(Long::sum);

        outputDataPairRDD.foreach(r -> LOGGER.info("----- Key: {} ValueCount: {}", r._1, r._2));

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
            LOGGER.warn("----- Arguments: {}", Arrays.asList(args));
            LOGGER.error("----- Error: ", e);
            formatter.printHelp(message, options, true);
            System.exit(1);
        }
        return inputFile;
    }
}
