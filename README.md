# Learning Apache Spark - Java 11

## Run Locally
* Run: `java --add-exports=java.base/sun.nio.ch=ALL-UNNAMED -Dlog4j.configurationFile=classpath:log4j2.xml -jar target/spark-template-1.0-SNAPSHOT.jar` Path: `learning-apache-spark/spark-template/`

## Run Kubernetes Spark
* Run: `minikube start -p sparkoperator --driver docker --cpus 4 --memory 8192 --mount=true --mount-string=/mnt/c/sparkoperator:/minikube-host`
* Run: `helm install spark-operator https://github.com/kubeflow/spark-operator/releases/download/v2.0.2/spark-operator-2.0.2.tgz --set sparkJobNamespace=default --set webhook.enable=true --set webhook.port=443 --set webhook.namespaceSelector=""`
* Deployment File: `deploysparkapp.yaml`
```yaml
apiVersion: "sparkoperator.k8s.io/v1beta2"
kind: SparkApplication
metadata:
  name: spark-pi1
  namespace: default
spec:
  type: Scala
  mode: cluster
  image: "apache/spark:3.5.3-scala2.12-java11-ubuntu"
  imagePullPolicy: Always
  mainClass: learning.apache.spark.SparkCode
  mainApplicationFile: "local:///mnt/spark/work/spark-template-1.0-SNAPSHOT.jar"
  sparkVersion: "3.5.3"
  restartPolicy:
    type: Never
  volumes:
    - name: test-volume
      hostPath:
        path: "/minikube-host"
        type: Directory
  driver:
    cores: 1
    coreLimit: "1200m"
    memory: "512m"
    labels:
      app: spark
      version: 3.5.3
    serviceAccount: spark-operator-spark
    volumeMounts:
      - name: test-volume
        mountPath: /mnt/spark/work
  executor:
    cores: 1
    instances: 2
    memory: "512m"
    labels:
      app: spark
      version: 3.5.3
    serviceAccount: spark-operator-spark
    volumeMounts:
      - name: test-volume
        mountPath: /mnt/spark/work
```
* Run: `kubectl apply -f deploysparkapp.yaml`
* Run: `kubectl delete -f deploysparkapp.yaml`