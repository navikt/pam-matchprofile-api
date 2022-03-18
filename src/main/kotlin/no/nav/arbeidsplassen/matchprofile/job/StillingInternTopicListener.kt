package no.nav.arbeidsplassen.matchprofile.job

import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.OffsetStrategy
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.context.annotation.Requires
import no.nav.arbeidsplassen.matchprofile.profile.MatchProfileDTO
import no.nav.arbeidsplassen.matchprofile.profile.MatchProfileMaker
import no.nav.arbeidsplassen.matchprofile.profile.MatchProfileService
import org.apache.kafka.clients.consumer.Consumer
import org.slf4j.LoggerFactory

@Requires(property = "ad.kafka.enabled", value = "true")
@KafkaListener(batch = true, offsetStrategy = OffsetStrategy.DISABLED)
class StillingInternTopicListener(
    private val matchProfileMaker: MatchProfileMaker,
    private val matchProfileService: MatchProfileService,
    private val kafkaStateRegistry: KafkaStateRegistry
) {

    companion object {
        private val LOG = LoggerFactory.getLogger(StillingInternTopicListener::class.java)
    }

    @Topic("\${ad.kafka.topic}")
    fun syncAds(ads: List<AdDTO>, offsets: List<Long>, kafkaConsumer: Consumer<*, *>): List<MatchProfileDTO> {
        LOG.info("Received ${ads.size} ads from Kafka")

        if (kafkaStateRegistry.hasError()) {
            LOG.error("Kafka is in an error state. Skipping batch to avoid message loss. Consumer should be paused.")
            return emptyList()
        }

        val storedProfiles = ads
            .filter { it.publishedByAdmin != null }
            .map { ad -> matchProfileMaker.jobMatchProfile(ad) }
            .map { matchProfile -> matchProfileService.save(matchProfile) }

        kafkaConsumer.commitSync()

        LOG.info("Stored ${storedProfiles.size} match profiles successfully")
        return storedProfiles
    }

}
