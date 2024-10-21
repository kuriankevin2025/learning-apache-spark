# Spark Template

* Root Dir: `learning-apache-spark`

## CodeBase
* SparkCode -> `./spark-template/src/main/java/learning/apache/spark/template/SparkCode.java`
* Dev Log Config -> `./spark-template/src/main/resources/log4j2.xml`
* Kubernetes Deployment -> `./spark-template/kubernetes/spark-code.yaml`
* Mount Files Path -> `./spark-template/spark_operator`
  * Log Generation Script -> `./spark-template/spark_operator/data/log_gen.py`
  * Log Config Driver -> `./spark-template/spark_operator/logs/driver/log4j2.xml`
  * Log Config Executor -> `./spark-template/spark_operator/logs/executor/log4j2.xml`

## Prerequisites
* Spark Operator Setup
  * Copy `./spark-template/spark_operator` to `C:\`
* Create Log Data
  * Run `log_gen.py` Path: `C:\spark_operator\data`

## IntelliJ Setup
* SparkCode-Simple
  * Runtime: Java17
  * VM options: `--add-exports=java.base/sun.nio.ch=ALL-UNNAMED`
  * Program arguments: `--mode simple`
  * Run: `SparkCode-Simple`
* SparkCode-Advanced
  * Runtime: Java17
  * VM options: `--add-exports=java.base/sun.nio.ch=ALL-UNNAMED`
  * Program arguments: `--mode advanced --input C:\spark_operator\data\app_data.log`
  * Run: `SparkCode-Advanced`

## Local Setup
* Build: `mvn clean install` Path: `.`
* SparkCode-Simple
  * Run: `java --add-exports=java.base/sun.nio.ch=ALL-UNNAMED -cp "target/spark-template-java-17.jar;target/libs/*" learning.apache.spark.template.SparkCode --mode simple` Path: `./spark-template`
* SparkCode-Advanced
  * Run(CMD): `java --add-exports=java.base/sun.nio.ch=ALL-UNNAMED -cp "target/spark-template-java-17.jar;target/libs/*" learning.apache.spark.template.SparkCode --mode advanced --input C:\spark_operator\data\app_data.log` Path: `./spark-template`
  * Run(Git Bash): `java --add-exports=java.base/sun.nio.ch=ALL-UNNAMED -cp "target/spark-template-java-17.jar;target/libs/*" learning.apache.spark.template.SparkCode --mode advanced --input C:/spark_operator/data/app_data.log` Path: `./spark-template`

## Minikube Setup
* Create minikube cluster
  * Run: `minikube -p spark-operator start --driver docker --cpus 6 --memory 12288 --mount=true --mount-string=/mnt/c/spark_operator:/minikube-host`
* Install spark operator
  * Run: `helm install spark-operator https://github.com/kubeflow/spark-operator/releases/download/v2.0.2/spark-operator-2.0.2.tgz --set sparkJobNamespace=default --set webhook.enable=true --set webhook.port=443 --set webhook.namespaceSelector=""`
* Install minikube plugins
  * Run: `minikube -p spark-operator addons enable dashboard`
  * Run: `minikube -p spark-operator addons enable metrics-server`
* Wait till all services are live
  * Run: `watch kubectl get all -A`
* Open Kubernetes Dashboard
  * Run: `minikube -p spark-operator dashboard --url`

## Kubernetes Setup
* SparkCode-Simple
  * Add `<scope>provided</scope>` under `spark-core_2.12`, `log4j-slf4j2-impl` and `commons-cli` Path: `./spark-template/pom.xml`
  * Build: `mvn clean install` Path: `.`
  * Copy `./spark-template/target/spark-template-java-17.jar` to `C:\spark_operator`
  * Copy `./spark-template/target/libs` to `C:\spark_operator`
  * Add Arguments. Path: `./spark-template/kubernetes/spark-code.yaml`
    ```
    arguments:
      - "--mode=simple"
    ```
  * Run: `kubectl apply -f spark-code.yaml` Path: `./spark-template/kubernetes`
* SparkCode-Advanced
  * Comment: `.setMaster("local[*]")`
  * Build: `mvn clean install` Path: `.`
  * Copy `./spark-template/target/spark-template-java-17.jar` to `C:\spark_operator`
  * Add Arguments. Path: `./spark-template/kubernetes/spark-code.yaml`
    ```
    arguments:
      - "--mode=advanced"
      - "--input=/mnt/spark/work/data/app_data.log"
    ```
  * Run: `kubectl apply -f spark-code.yaml` Path: `./spark-template/kubernetes`

## Minikube Teardown
* Run: `helm uninstall spark-operator`
* Run: `minikube -p spark-operator stop`
* Run: `minikube -p spark-operator delete`

> NOTES:</br>
> * To use driver and executor pods, remove `.setMaster("local[*]")` from sparkConfig
> * To stop driver and executor from exiting, add thread.sleep() before javaSparkContext.close()

## Knowledge Summery
* Spark kubernetes deployments
  * Provide command line arguments to spark runtime
  * Update driver and executors class path
  * Update driver and executors java options
* Package sparkCode and dependencies separately
* Run sparkCode in driver and executors
* Read command line arguments using Options
* Read data from input file
* Provide custom log configurations for driver and executors
  * Write driver logs to console and file
  * Write executor logs into console and separate files
