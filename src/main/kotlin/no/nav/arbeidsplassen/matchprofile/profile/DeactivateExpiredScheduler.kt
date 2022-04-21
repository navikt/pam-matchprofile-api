package no.nav.arbeidsplassen.matchprofile.profile

import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton
import no.nav.arbeidsplassen.matchprofile.LeaderElection
import org.slf4j.LoggerFactory

@Singleton
class DeactivateExpiredScheduler(private val matchProfileService: MatchProfileService,
                                 private val leaderElection: LeaderElection){


    companion object {
        private val LOG = LoggerFactory.getLogger(DeactivateExpiredScheduler::class.java)
    }

    @Scheduled(cron = "0 0 18 * * *")
    fun outboxToKafka() {
        if (leaderElection.isLeader()) {
            LOG.info("Running deactivate expired scheduler")
            matchProfileService.deactivateExpired()
            LOG.info("Finished!")
        }
    }

}
