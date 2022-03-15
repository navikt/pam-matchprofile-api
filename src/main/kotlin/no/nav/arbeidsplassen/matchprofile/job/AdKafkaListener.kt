package no.nav.arbeidsplassen.matchprofile.job

import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.context.annotation.Requires
import no.nav.arbeidsplassen.matchprofile.profile.MatchProfileMaker
import no.nav.arbeidsplassen.matchprofile.profile.MatchProfileService
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory

@Requires(property = "ad.kafka.enabled", value = "true")
@KafkaListener
class AdKafkaListener(
    private val matchProfileMaker: MatchProfileMaker,
    private val matchProfileService: MatchProfileService
) {

    companion object {
        private val LOG = LoggerFactory.getLogger(AdKafkaListener::class.java)
    }

    @Topic("\${ad.kafka.topic}")
    fun receiveAd(records: List<AdDTO>, offsets: List<Long>) {
        LOG.info("Received $records from Kafka")

        val saved = records.map { handleRecord(it) }

        LOG.info("Handled ${saved.size} records successfully")
    }

    private fun handleRecord(ad: AdDTO): Boolean {
        return try {
            val matchProfileDTO = matchProfileMaker.jobMatchProfile(ad)
            matchProfileService.save(matchProfileDTO)
            true
        } catch (e: Exception) {
            false
        }
    }
}
