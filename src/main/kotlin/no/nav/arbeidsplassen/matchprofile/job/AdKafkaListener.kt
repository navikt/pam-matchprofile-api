package no.nav.arbeidsplassen.matchprofile.job

import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.context.annotation.Requires
import org.slf4j.LoggerFactory

@Requires(property = "ad.kafka.enabled", value = "true")
@KafkaListener
class AdKafkaListener {

    companion object {
        private val LOG = LoggerFactory.getLogger(AdKafkaListener::class.java)
    }

    @Topic("\${ad.kafka.topic}")
    fun receiveAd(records: List<AdDTO>, offsets: List<Long>) {
        LOG.info("Received ${records.size} from Kafka")

    }
}
