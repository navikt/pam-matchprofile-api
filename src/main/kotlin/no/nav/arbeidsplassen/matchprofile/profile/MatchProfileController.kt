package no.nav.arbeidsplassen.matchprofile.profile

import io.micronaut.http.annotation.*
import org.slf4j.LoggerFactory
import java.util.*

//TODO authentication later
// Authorization by verifying true owner, before retrieving from the database.
@Controller("/api/v1/matchprofile")
class MatchProfileController(private val matchProfileService: MatchProfileService) {

    companion object {
        private val LOG = LoggerFactory.getLogger(MatchProfileController::class.java)
    }

    @Get("/{id}")
    fun findById(@PathVariable id: String): MatchProfileDTO? {
        return matchProfileService.findById(id)
    }

    @Get("/source/{sourceId}")
    fun findBySourceId(@PathVariable sourceId: String): MatchProfileDTO? {
        return matchProfileService.findBySourceId(sourceId)
    }

    @Post("/")
    fun create(@Body matchProfile: MatchProfileDTO) : MatchProfileDTO {
        TODO()
    }


}
