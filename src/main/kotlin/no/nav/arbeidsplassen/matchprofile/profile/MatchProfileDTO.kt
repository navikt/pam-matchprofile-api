package no.nav.arbeidsplassen.matchprofile.profile

import io.micronaut.core.annotation.Introspected
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Introspected
data class MatchProfileDTO (
    var id: String? = null,
    val orgnr: String? = null,
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
data class ProfileDTO(val concepts: Set<ConceptDTO> = hashSetOf(), val locations: List<LocationDTO> = listOf())

@Introspected
data class LocationDTO(val country: String? = "Norge", val county: String? = null, val countyCode: String? = null,
                       val municipality: String? = null, val municipalityCode: String? = null , val city: String? = null,
                       val postalCode: String? = null)

@Introspected
data class ConceptDTO(val label: String, val cid: Long? = null, val branch: String?, val expandedConcept: String? = null,
                      val lang: String = "no")
