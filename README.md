# Learning Apache Spark - Java 17

> To execute everything from driver pod or in local, add `.setMaster("local[*]")` to sparkConfig

> To stop driver and executor from exiting, add thread.sleep before sparkContext.close

## Run Spark Locally
> SparkCode
* Comment Out: `<excludeScope>provided</excludeScope>`
* Build: `mvn clean install`
* Run: `java --add-exports=java.base/sun.nio.ch=ALL-UNNAMED -cp "target/spark-template-java-17.jar;target/libs/*" learning.apache.spark.template.SparkCode`
> SparkCodeAdvanced
* Comment Out: `<excludeScope>provided</excludeScope>`
* Build: `mvn clean install`
* Add: `.setMaster("local[*]")`
* Run: `java --add-exports=java.base/sun.nio.ch=ALL-UNNAMED -cp "target/spark-template-java-17.jar;target/libs/*" learning.apache.spark.template.SparkCodeAdvanced --input=c:/sparkoperator/data/app_data.log`

## Run Spark in Kubernetes
* Run: `minikube -p sparkoperator start --driver docker --cpus 6 --memory 12288 --mount=true --mount-string=/mnt/c/sparkoperator:/minikube-host`
* Run: `helm install spark-operator https://github.com/kubeflow/spark-operator/releases/download/v2.0.2/spark-operator-2.0.2.tgz --set sparkJobNamespace=default --set webhook.enable=true --set webhook.port=443 --set webhook.namespaceSelector=""`
* Run: `minikube -p sparkoperator addons enable dashboard`
* Run: `minikube -p sparkoperator addons enable metrics-server`
* Run: `minikube -p sparkoperator dashboard --url`
* Run: `watch kubectl get all -A`
* Run: `kubectl apply -f deploysparkapp.yaml`
* Run: `kubectl delete -f deploysparkapp.yaml`
* Run: `helm uninstall spark-operator`
* Run: `minikube -p sparkoperator stop`
* Run: `minikube -p sparkoperator delete`

Kubernetes Spark Operator: https://github.com/kubeflow/spark-operator

Docker Repos:
* https://hub.docker.com/r/apache/spark
* https://hub.docker.com/_/spark

Github Spark Docker:
* https://github.com/apache/spark-docker

Spark Doc:
* https://spark.apache.org/docs/latest/
* https://spark.apache.org/docs/3.5.3/
