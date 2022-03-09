package no.nav.arbeidsplassen.matchprofile.profile

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.Timestamp
import java.util.*
import javax.transaction.Transactional

@JdbcRepository(dialect = Dialect.POSTGRES)
abstract class MatchProfileRepository(private val connection: Connection, private val objectMapper: ObjectMapper) : CrudRepository<MatchProfile, String> {

    private val tableName = "match_profile"
    val insertSQL = """insert into $tableName ("p_id", "orgnr", "source_id", "type", "status", "title", "description", "profile", "created_by", "updated_by", "expires", "created", "updated", "id") values (?,?,?,?,?,?,?,?::jsonb,?,?,?,?, clock_timestamp(),?)"""
    val updateSQL = """update $tableName set "p_id"=?, "orgnr"=?, "source_id"=?, "type"=?, "status"=?, "title"=?, "description"=?, "profile"=?::jsonb, "created_by"=?, "updated_by"=?, "expires"=?, "created"=?, "updated"=clock_timestamp() where "id"=?"""

    @Transactional
    override fun <S : MatchProfile> save(entity: S): S {
        if (entity.isNew()) {
            connection.prepareStatement(insertSQL).apply {
                val new = entity.copy(id = UUID.randomUUID().toString())
                prepareSQL(new)
                check(executeUpdate() == 1 )
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

    private fun PreparedStatement.prepareSQL(entity: MatchProfile) {
        var index=1
        setString(index,entity.pId)
        setString(++index,entity.orgnr)
        setString(++index, entity.sourceId)
        setString(++index, entity.type.name)
        setString(++index, entity.status.name)
        setString(++index, entity.title)
        setString(++index, entity.description)
        setString(++index, objectMapper.writeValueAsString(entity.profile))
        setString(++index, entity.createdBy)
        setString(++index, entity.updatedBy)
        setTimestamp(++index, Timestamp.from(entity.expires))
        setTimestamp(++index, Timestamp.from(entity.created))
        setString(++index, entity.id)
    }

    @Transactional
    abstract fun findBySourceId(sourceId: String): MatchProfile?

    @Transactional
    abstract fun findByOrgnr(orgnr: String): List<MatchProfile>

}

