package wwaldron.kafkaclients

object JavaClient extends App {

  val startTime = System.currentTimeMillis()

  def notDone(): Boolean = {
    System.currentTimeMillis() - startTime < 30000
  }

  import java.util.Properties
  import org.apache.kafka.clients.consumer.KafkaConsumer
  import collection.JavaConverters._

  val pollingTimeoutInMS = 100

  val props = new Properties()

  props.put("bootstrap.servers", "localhost:9092")
  props.put("group.id", "JavaConsumer")
  props.put("enable.auto.commit", "false")
  props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")

  val consumer = new KafkaConsumer[String, String](props)

  consumer.subscribe(List("messages").asJava)

  while(notDone()) {
    consumer.poll(pollingTimeoutInMS).iterator().asScala.foreach { record =>
      println(record.value())
    }
    consumer.commitSync()
  }

  consumer.close()
}
