package no.nav.arbeidsplassen.matchprofile.outbox

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.TypeDef
import io.micronaut.data.model.DataType
import java.time.Instant


@MappedEntity
data class Outbox(
    @field:Id
    var id: String? = null,
    val keyId: String,
    val type: String,
    @field:TypeDef(type = DataType.STRING)
    val status: OutboxStatus = OutboxStatus.PENDING,
    @field:TypeDef(type = DataType.JSON)
    val payload: Any,
    val updated: Instant = Instant.now())

fun Outbox.isNew(): Boolean = id == null

enum class OutboxStatus {
    PENDING, DONE, ERROR
}
