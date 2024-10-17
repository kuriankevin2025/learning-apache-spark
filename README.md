# Learning Apache Spark - Java 17

## Run SparkCode Locally
* Run: `java --add-exports=java.base/sun.nio.ch=ALL-UNNAMED -Dlog4j.configurationFile=classpath:log4j2.xml -jar target/spark-template-java-17.jar`

## Run SparkCode in Kubernetes
* Run: `minikube start -p sparkoperator --driver docker --cpus 4 --memory 8192 --mount=true --mount-string=/mnt/c/sparkoperator:/minikube-host`
* Run: `helm install spark-operator https://github.com/kubeflow/spark-operator/releases/download/v2.0.2/spark-operator-2.0.2.tgz --set sparkJobNamespace=default --set webhook.enable=true --set webhook.port=443 --set webhook.namespaceSelector=""`
* Run: `kubectl apply -f deploysparkapp.yaml`
* Run: `kubectl delete -f deploysparkapp.yaml`
