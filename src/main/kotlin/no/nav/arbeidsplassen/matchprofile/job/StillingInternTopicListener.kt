package no.nav.arbeidsplassen.matchprofile.job

import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.OffsetReset
import io.micronaut.configuration.kafka.annotation.OffsetStrategy
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.context.annotation.Requires
import no.nav.arbeidsplassen.matchprofile.profile.MatchProfileDTO
import no.nav.arbeidsplassen.matchprofile.profile.MatchProfileMaker
import no.nav.arbeidsplassen.matchprofile.profile.MatchProfileService
import org.apache.kafka.clients.consumer.Consumer
import org.slf4j.LoggerFactory

@Requires(property = "ad.kafka.enabled", value = "true")
@KafkaListener(pollTimeout= "60000ms" , offsetStrategy = OffsetStrategy.DISABLED, offsetReset = OffsetReset.LATEST)
class StillingInternTopicListener(
    private val matchProfileMaker: MatchProfileMaker,
    private val matchProfileService: MatchProfileService,
    private val kafkaStateRegistry: KafkaStateRegistry
) {

    companion object {
        private val LOG = LoggerFactory.getLogger(StillingInternTopicListener::class.java)
    }

    @Topic("\${ad.kafka.topic}")
    fun syncAds(ad: AdDTO, offset: Long, kafkaConsumer: Consumer<*, *>) {
        LOG.info("Received ad ${ad.uuid} from Kafka")

        if (kafkaStateRegistry.hasError()) {
            LOG.error("Kafka is in an error state. Skipping to avoid message loss. Consumer should be paused.")
            return
        }
        if (ad.publishedByAdmin != null) {
            val matchProfile = matchProfileMaker.jobMatchProfile(ad)
            matchProfileService.save(matchProfile)
        }
        LOG.info("committing at offset $offset")
        kafkaConsumer.commitSync()
        kafkaStateRegistry.clearStateRegistry()
    }

}
