package no.nav.arbeidsplassen.matchprofile.job

import io.micronaut.configuration.kafka.exceptions.DefaultKafkaListenerExceptionHandler
import io.micronaut.configuration.kafka.exceptions.KafkaListenerException
import io.micronaut.configuration.kafka.exceptions.KafkaListenerExceptionHandler
import io.micronaut.context.annotation.Replaces
import jakarta.inject.Singleton
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.errors.SerializationException
import org.slf4j.LoggerFactory
import java.util.regex.Pattern

@Singleton
@Replaces(bean = DefaultKafkaListenerExceptionHandler::class)
class KafkaExceptionHandler(private val kafkaRegistry: KafkaStateRegistry) : KafkaListenerExceptionHandler {

    companion object {
        private val LOG = LoggerFactory.getLogger(KafkaExceptionHandler::class.java)
        private val SERIALIZATION_EXCEPTION_MESSAGE_PATTERN = Pattern.compile(".+ partition (.+)-(\\d+) at offset (\\d+)\\.")
    }

    override fun handle(exception: KafkaListenerException) {
        val cause = exception.cause!!
        val consumerBean = exception.kafkaListener
        val kafkaConsumer = exception.kafkaConsumer
        if (cause is SerializationException)  {
            LOG.error("Kafka consumer [$consumerBean] failed to deserialize value: ${cause.message}", cause)

            seekPastRecord(cause, consumerBean, kafkaConsumer)
        } else {
            val consumerRecord = exception.consumerRecord
            if (consumerRecord.isPresent) {
                LOG.error("Error processing record [$consumerRecord] for Kafka consumer [$consumerBean], error: " +
                        "${cause.message}", cause)
            } else {
                LOG.error("Kafka consumer [$consumerBean] produced error: ${cause.message}", cause)
            }
            LOG.error("Pausing consumer [$consumerBean], needs further investigation. Error: ${cause.message}")
            //kafkaRegistry.setConsumerState(consumerBean.javaClass.simpleName, KafkaState.ERROR)
        }
    }


    private fun seekPastRecord(cause: SerializationException, consumerBean: Any, kafkaConsumer: Consumer<*, *>) {
        try {
            val message = cause.message
            val match = SERIALIZATION_EXCEPTION_MESSAGE_PATTERN.matcher(message)
            if (match.find()) {
                val topic = match.group(1)
                val partition = match.group(2).toInt()
                val offset = match.group(3).toLong()
                val topicPartition = TopicPartition(topic, partition)

                LOG.warn("Seeking past undeserializable consumer record for partition $topicPartition with offset $offset")
                kafkaConsumer.seek(topicPartition, offset + 1L)
            }
        } catch (e: Throwable) {
            LOG.error("Kafka consumer [$consumerBean] failed to seek past undeserializable consumer record: ${e
                .message}", e)
        }
    }

}
