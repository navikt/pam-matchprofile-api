package no.nav.arbeidsplassen.matchprofile.outbox

import io.micronaut.data.model.Pageable
import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton
import no.nav.arbeidsplassen.matchprofile.LeaderElection
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.temporal.ChronoUnit

@Singleton
class OutboxScheduler(private val repository: OutboxRepository,
                      private val kafkaSender: OutboxKafkaSender,
                      private val leaderElection: LeaderElection){

    private val daysOld: Long = 14
    private var kafkaHasError = false
    private var counter=0

    companion object {
        private val LOG = LoggerFactory.getLogger(OutboxScheduler::class.java)
    }

    @Scheduled(fixedDelay = "30s")
    fun outboxToKafka() {
        if (leaderElection.isLeader() && kafkaHasError.not()) {
            LOG.info("Running matchprofile outbox to kafka sender, we have previously sent $counter")
            repository.findByStatusOrderByUpdated(OutboxStatus.PENDING, Pageable.from(0, 100)).forEach { outbox ->
                kafkaSender.sendEventFromOutbox(outbox.keyId, outbox.payload).subscribe(
                    {
                        repository.save(outbox.copy(status = OutboxStatus.DONE))
                        LOG.info("sent successfully ${outbox.keyId}")
                        counter++
                    },
                    {
                        LOG.error("Got error while sending to kafka, will stop sending", it)
                        repository.save(outbox.copy(status = OutboxStatus.ERROR))
                        kafkaHasError = true
                    }
                )
            }
        }
        else if (kafkaHasError) LOG.error("Kafka is in error state, will not sending anything")
    }

    @Scheduled(cron = "0 0 8 * * *")
    fun cleanOldEvents() {
        val old = Instant.now().minus(daysOld,ChronoUnit.DAYS)
        val deleted = repository.deleteByStatusAndUpdatedBefore(OutboxStatus.DONE, old)
        LOG.info("total $deleted old events from outbox was deleted")
    }

}
