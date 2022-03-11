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
    val pId: String? = null,
    val orgnr: String? = null,
    val sourceId: String = UUID.randomUUID().toString(),
    @field:TypeDef(type = DataType.STRING)
    val type: MatchProfileType = MatchProfileType.JOB,
    @field:TypeDef(type = DataType.STRING)
    val status: MatchProfileStatus = MatchProfileStatus.ACTIVE,
    val title: String? = null,
    val description: String? = null,
    @field:TypeDef(type = DataType.JSON)
    val profile: ProfileDTO,
    val createdBy: String = "matchprofile-api",
    val updatedBy: String = "matchprofile-api",
    val expires: Instant = Instant.now().plus(30, ChronoUnit.DAYS),
    val created: Instant = Instant.now(),
    val updated: Instant = Instant.now()
)

fun MatchProfile.isNew(): Boolean = id == null

enum class MatchProfileType {
    USER,
    JOB,
    EVENT,
    CV
}

enum class MatchProfileStatus {
    ACTIVE, INACTIVE, DELETED
}
