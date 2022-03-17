package no.nav.arbeidsplassen.matchprofile.job

import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.context.annotation.Requires
import no.nav.arbeidsplassen.matchprofile.profile.MatchProfileDTO
import no.nav.arbeidsplassen.matchprofile.profile.MatchProfileMaker
import no.nav.arbeidsplassen.matchprofile.profile.MatchProfileService
import org.slf4j.LoggerFactory

@Requires(property = "ad.kafka.enabled", value = "true")
@KafkaListener(batch = true)
class StillingInternTopicListener(
    private val matchProfileMaker: MatchProfileMaker,
    private val matchProfileService: MatchProfileService,
    private val kafkaStateRegistry: KafkaStateRegistry
) {

    companion object {
        private val LOG = LoggerFactory.getLogger(StillingInternTopicListener::class.java)
    }

    @Topic("\${ad.kafka.topic}")
    fun syncAds(ads: List<AdDTO>, offsets: List<Long>): List<MatchProfileDTO> {
        LOG.info("Received ${ads.size} ads from Kafka")

        if (kafkaStateRegistry.hasError()) {
            LOG.error("Kafka is in an error state. Skipping batch to avoid message oss. Consumer should be paused.")
            return emptyList()
        }

        val storedProfiles = ads
            .filter { it.publishedByAdmin != null }
            .map { ad -> matchProfileMaker.jobMatchProfile(ad) }
            .map { matchProfile -> matchProfileService.save(matchProfile) }

        LOG.info("Stored ${storedProfiles.size} match profiles successfully")
        return storedProfiles
    }

}