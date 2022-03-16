package no.nav.arbeidsplassen.matchprofile.job

import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.context.annotation.Requires
import no.nav.arbeidsplassen.matchprofile.profile.MatchProfileDTO
import no.nav.arbeidsplassen.matchprofile.profile.MatchProfileMaker
import no.nav.arbeidsplassen.matchprofile.profile.MatchProfileService
import org.slf4j.LoggerFactory

@Requires(property = "ad.kafka.enabled", value = "true")
@KafkaListener
class StillingInternTopicListener(
    private val matchProfileMaker: MatchProfileMaker,
    private val matchProfileService: MatchProfileService
) {

    companion object {
        private val LOG = LoggerFactory.getLogger(StillingInternTopicListener::class.java)
    }

    @Topic("\${ad.kafka.topic}")
    fun syncAds(adDtos: List<AdDTO>, offsets: List<Long>): List<MatchProfileDTO> {
        LOG.info("Received ${adDtos.size} ads from Kafka")

        val saved = adDtos
            .filter { it.publishedByAdmin != null }
            .map { ad -> matchProfileMaker.jobMatchProfile(ad) }
            .map { matchProfile -> matchProfileService.save(matchProfile) }

        LOG.info("Saved ${saved.size} match profiles successfully")
        return saved
    }

}
