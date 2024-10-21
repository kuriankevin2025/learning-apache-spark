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

## IntelliJ Setup
* Runtime: Java17
* VM options: `--add-exports=java.base/sun.nio.ch=ALL-UNNAMED`
* SparkCode-Simple
  * Program arguments: `--mode simple`
  * Run: `SparkCode-Simple`
* SparkCode-Advanced
  * [Create Log Data](#create-log-data)
  * Program arguments: `--mode advanced --input C:\spark_operator\data\app_data.log`
  * Run: `SparkCode-Advanced`

## Local Setup
* Build: `mvn clean install` Path: `.`
* SparkCode-Simple
  * Run: `java --add-exports=java.base/sun.nio.ch=ALL-UNNAMED -cp "target/spark-template-java-17.jar;target/libs/*" learning.apache.spark.template.SparkCode --mode simple` Path: `./spark-template`
* SparkCode-Advanced
  * [Create Log Data](#create-log-data)
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
  * [Create Log Data](#create-log-data)
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

### Create Log Data
* Copy `./spark-template/spark_operator` to `C:\`
* Run `log_gen.py` Path: `C:\spark_operator\data`
* Log Data: `C:\spark_operator\data\app_data.log`

### Minikube Teardown
* Run: `helm uninstall spark-operator`
* Run: `minikube -p sparkoperator stop`
* Run: `minikube -p sparkoperator delete`

> NOTES:</br>
> * To use driver and executor pods, remove `.setMaster("local[*]")` from sparkConfig
> * To stop driver and executor from exiting, add thread.sleep() before javaSparkContext.close()
