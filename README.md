# Learning Apache Spark - Java 17

## Run SparkCode Locally
* Run: `java --add-exports=java.base/sun.nio.ch=ALL-UNNAMED -Dlog4j.configurationFile=classpath:log4j2.xml -jar target/spark-template-java-17.jar`

## Run SparkCode in Kubernetes
* Run: `minikube -p sparkoperator start --driver docker --cpus 4 --memory 8192 --mount=true --mount-string=/mnt/c/sparkoperator:/minikube-host`
* Run: `helm install spark-operator https://github.com/kubeflow/spark-operator/releases/download/v2.0.2/spark-operator-2.0.2.tgz --set sparkJobNamespace=default --set webhook.enable=true --set webhook.port=443 --set webhook.namespaceSelector=""`
* Run: `watch kubectl get all -A`
* Run: `minikube -p sparkoperator addons enable dashboard`
* Run: `minikube -p sparkoperator addons enable metrics-server`
* Run: `minikube -p sparkoperator dashboard --url`
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
