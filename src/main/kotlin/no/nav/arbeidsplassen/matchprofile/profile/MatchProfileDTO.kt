package no.nav.arbeidsplassen.matchprofile.profile

import io.micronaut.core.annotation.Introspected
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Introspected
data class MatchProfileDTO (
    var id: String? = null,
    val sourceId: String = UUID.randomUUID().toString(),
    val type: MatchProfileType = MatchProfileType.JOB,
    val status: MatchProfileStatus = MatchProfileStatus.ACTIVE,
    val title: String? = null,
    val description: String? = null,
    val profile: ProfileDTO,
    val createdBy: String = "matchprofile-api",
    val updatedBy: String = "matchprofile-api",
    val expires: Instant = Instant.now().plus(30, ChronoUnit.DAYS),
    val created: Instant = Instant.now(),
    val updated: Instant = Instant.now()
)

@Introspected
data class ProfileDTO(val keywords: Set<ConceptDTO> = hashSetOf())

@Introspected
data class ConceptDTO(val label: String, val cid: Long? = null,  val type: String, val known: Boolean = true,
                   val expandedConcept: Boolean = false, val createdByUser: Boolean=false, val lang: String = "no")
