package no.nav.arbeidsplassen.matchprofile.profile

import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.TypeDef
import io.micronaut.data.model.DataType
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@MappedEntity
data class MatchProfile(
    @field:Id
    var id: String? = null,
    val owner: String,
    val sourceId: String = UUID.randomUUID().toString(),
    @field:TypeDef(type = DataType.STRING)
    val type: MatchProfileType = MatchProfileType.JOB,
    @field:TypeDef(type = DataType.STRING)
    val status: MatchProfileStatus = MatchProfileStatus.ACTIVE,
    val title: String? = null,
    val description: String? = null,
    @field:TypeDef(type = DataType.JSON)
    val profile: Profile,
    val createdBy: String = "matchprofile-api",
    val updatedBy: String = "matchprofile-api",
    val expires: Instant = Instant.now().plus(30, ChronoUnit.DAYS),
    val created: Instant = Instant.now(),
    val updated: Instant = Instant.now()
)

fun MatchProfile.isNew(): Boolean = id == null


data class Concept(val label: String, val cid: Long? = null, val branch: String?,
                   val expandedConcept: String? = null, val lang: String = "no")

enum class MatchProfileType {
    USER,
    JOB,
    EVENT
}

enum class MatchProfileStatus {
    ACTIVE, INACTIVE
}

data class Profile(val keywords: Set<Concept> = hashSetOf())
