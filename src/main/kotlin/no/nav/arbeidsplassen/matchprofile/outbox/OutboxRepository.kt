package no.nav.arbeidsplassen.matchprofile.outbox

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import java.sql.Connection
import java.sql.PreparedStatement
import java.time.Instant
import java.util.*
import javax.transaction.Transactional

@Suppress("UNCHECKED_CAST")
@JdbcRepository(dialect = Dialect.POSTGRES)
abstract class OutboxRepository(private val connection: Connection, private val objectMapper: ObjectMapper): CrudRepository<Outbox, String> {

    val insertSQL = """insert into "outbox" ("key_id", "type", "status", "payload", "updated", "id" ) values (?,?,?,?::jsonb,clock_timestamp(),?)"""
    val updateSQL = """update "outbox" set "key_id"=?, "type"=?, "status"=?, "payload"=?::jsonb, "updated"=clock_timestamp() where "id"=?"""

    @Transactional
    override fun <S : Outbox> save(entity: S): S  {
        if (entity.isNew()) {
            connection.prepareStatement(insertSQL).apply {
                val new = entity.copy(id = UUID.randomUUID().toString())
                prepareSQL(new)
                check(executeUpdate() == 1)
                return new as S
            }
        }
        else {
            connection.prepareStatement(updateSQL).apply {
                prepareSQL(entity)
                check(executeUpdate() == 1 )
                return entity
            }
        }
    }

    private fun PreparedStatement.prepareSQL(entity: Outbox) {
        var index=1
        setString(index, entity.keyId)
        setString(++index, entity.type)
        setString(++index, entity.status.toString())
        setString(++index, objectMapper.writeValueAsString(entity.payload))
        setString(++index, entity.id)
    }


    @Transactional
    abstract fun findByStatusOrderByUpdated(outboxStatus: OutboxStatus, pageable: Pageable): List<Outbox>

    @Transactional
    abstract fun deleteByStatusAndUpdatedBefore(outboxStatus: OutboxStatus, before: Instant): Int

}
