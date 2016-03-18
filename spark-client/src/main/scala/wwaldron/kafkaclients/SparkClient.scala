package wwaldron.kafkaclients

object SparkClient extends App {

  import kafka.serializer.StringDecoder
  import org.apache.spark.streaming.kafka.KafkaUtils
  import org.apache.spark.streaming.{Seconds, StreamingContext}
  import org.apache.spark.{SparkConf, SparkContext}

  val checkpointDir = "/tmp/sparkcheckpoints"

  val sparkConfig = new SparkConf().setAppName("SparkDriver").setMaster("local[2]")
  val sparkContext = new SparkContext(sparkConfig)

  val kafkaConfig = Map(
    "metadata.broker.list" -> "localhost:9092"
  )

  def createStreamingContext() = {
    val ctx = new StreamingContext(sparkContext, Seconds(10))
    ctx.checkpoint(checkpointDir)

    val stream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      ctx,
      kafkaConfig,
      Set("messages")
    )

    stream.foreachRDD { rdd =>
      rdd.foreach { evt =>
        println(evt._2)
      }
    }

    ctx
  }

  val streamingContext = StreamingContext.getOrCreate(checkpointDir, createStreamingContext)

  streamingContext.start()
  streamingContext.awaitTerminationOrTimeout(30000)
  streamingContext.stop(stopSparkContext = true, stopGracefully = true)
}
