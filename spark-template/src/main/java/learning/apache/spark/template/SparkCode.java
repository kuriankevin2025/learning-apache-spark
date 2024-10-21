package learning.apache.spark.template;

import learning.apache.spark.entity.Message;
import org.apache.commons.cli.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

public class SparkCode {

    private static final Logger LOGGER = LoggerFactory.getLogger(SparkCode.class);

    private static final String modeKey = "mode";
    private static final String inputKey = "input";

    public static void main(String[] args) {

        System.setProperty("hadoop.home.dir", "c:/hadoop");

        SparkConf sparkConf = new SparkConf()
                .setAppName("sparkCode")
                .setMaster("local[*]");
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);

        String mode = getOptionValue(args, modeKey);

        switch (Mode.valueOf(mode.toUpperCase())) {
            case SIMPLE -> simple(javaSparkContext);
            case ADVANCED -> {
                String input = getOptionValue(args, inputKey);
                advanced(javaSparkContext, input);
            }
        }

        javaSparkContext.close();
    }

    public static void simple(JavaSparkContext javaSparkContext) {

        List<Double> inputData = List.of(1.1, 2.2, 3.3, 4.4);

        JavaRDD<Double> inputDataRDD = javaSparkContext.parallelize(inputData);
        Double output = inputDataRDD.reduce(Double::sum);
        LOGGER.info("----- Result: {}", output);

        Message message = new Message();
        message.setId(1);
        message.setData("Message Data");
        LOGGER.info("----- Message: {}", message);
    }

    public static void advanced(JavaSparkContext javaSparkContext, String inputFile) {

        JavaRDD<String> inputDataRDD = javaSparkContext.textFile(inputFile);
        LOGGER.info("----- InputDataRDD Partition Size: {}", inputDataRDD.getNumPartitions());

        JavaPairRDD<String, Long> inputDataPairRDD =
                inputDataRDD.mapToPair(s -> new Tuple2<>(s.split(":")[0].trim(), 1L));
        JavaPairRDD<String, Long> outputDataPairRDD = inputDataPairRDD.reduceByKey(Long::sum);

        outputDataPairRDD.foreach(r -> LOGGER.info("----- Key: {} ValueCount: {}", r._1, r._2));
    }

    private static Options createOptions() {

        Options options = new Options();

        Option mode = Option.builder("m")
                .longOpt("mode")
                .desc("Spark template mode")
                .hasArg()
                .argName("mode")
                .required()
                .build();
        options.addOption(mode);

        Option input = Option.builder("i")
                .longOpt("input")
                .desc("Input file path")
                .hasArg()
                .argName("file")
                .build();
        options.addOption(input);

        return options;
    }

    private static void printHelp(Options options) {

        System.out.println("----- SparkCode Help...");
        String message = """
                java --add-exports=java.base/sun.nio.ch=ALL-UNNAMED \
                -cp "target/spark-template-java-17.jar;target/libs/*" \
                learning.apache.spark.template.SparkCode [options]
                """;
        String header = "----- SparkCode Arguments...";
        String footer = "----- ----- -----";
        HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp(message, header, options, footer, true);
    }

    private static String getOptionValue(String[] args, String optionKey) {

        Options option = createOptions();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(option, args);

            String optionValue = cmd.getOptionValue(optionKey);
            if (optionValue == null) {
                throw new NullPointerException(MessageFormat.format(
                        "OptionKey:{0} OptionValue:{1} ErrorMessage:{2}",
                        optionKey, optionValue, "OptionValue is null"));
            }
            LOGGER.info("----- OptionKey:{} OptionValue:{}" ,optionKey, optionValue);
            return optionValue;

        } catch (ParseException e) {
            printHelp(option);
            throw new RuntimeException(MessageFormat.format(
                    "Arguments:{0} OptionKey:{1}", Arrays.asList(args), optionKey), e);
        }
    }

    private enum Mode {
        SIMPLE, ADVANCED
    }
}
