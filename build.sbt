name := """KafkaClients"""

scalaVersion in ThisBuild := "2.11.7"

val javaClient = project.in(file("java-client"))
  .settings(
    libraryDependencies ++= Seq(
      "org.apache.kafka" % "kafka-clients" % "0.9.0.1"
    )
  )

val sparkClient = project.in(file("spark-client"))
  .settings(
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % "1.6.1",
      "org.apache.spark" %% "spark-streaming" % "1.6.1",
      "org.apache.spark" %% "spark-streaming-kafka" % "1.6.1"
    )
  )

val reactiveKafkaClient = project.in(file("reactive-kafka-client"))
  .settings(
    libraryDependencies ++= Seq(
      "com.softwaremill.reactivekafka" %% "reactive-kafka-core" % "0.10.0"
    )
  )
