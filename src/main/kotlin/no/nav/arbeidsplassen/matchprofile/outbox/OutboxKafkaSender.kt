package no.nav.arbeidsplassen.puls.outbox

import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.KafkaKey
import io.micronaut.configuration.kafka.annotation.Topic
import org.apache.kafka.clients.producer.RecordMetadata
import reactor.core.publisher.Flux

@KafkaClient
interface OutboxKafkaSender {

    @KafkaClient(batch = true)
    @Topic("\${outbox.kafka.topic}")
    fun sendEventFromOutbox(@KafkaKey key: String, data: Any): Flux<RecordMetadata>

}
