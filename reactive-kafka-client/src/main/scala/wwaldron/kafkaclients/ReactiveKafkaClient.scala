package wwaldron.kafkaclients

object ReactiveKafkaClient extends App {
  def waitForCompletion() = {
    Thread.sleep(30000)
  }

  import akka.actor.ActorSystem
  import akka.stream.ActorMaterializer
  import akka.stream.scaladsl.Source
  import com.softwaremill.react.kafka.{ConsumerProperties, ReactiveKafka}
  import org.apache.kafka.common.serialization.StringDeserializer

  import scala.concurrent.duration._

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val kafka = new ReactiveKafka()

  val properties = ConsumerProperties(
    bootstrapServers = "localhost:9092",
    topic = "messages",
    groupId = "ReactiveKafka",
    keyDeserializer = new StringDeserializer(),
    valueDeserializer = new StringDeserializer()
  ).commitInterval(5.seconds)

  val consumer = kafka.consumeWithOffsetSink(properties)

  Source.fromPublisher(consumer.publisher)
    .map { record =>
      println(record.value())
      record
    }.runWith(consumer.offsetCommitSink)

  waitForCompletion()

  system.terminate()
}
