package no.nav.arbeidsplassen.matchprofile.profile

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import org.slf4j.LoggerFactory

@Controller("/api/v1/matchprofile")
class MatchProfileController(private val matchProfileService: MatchProfileService) {

    companion object {
        private val LOG = LoggerFactory.getLogger(MatchProfileController::class.java)
    }

    @Get("/{id}")
    fun get(@PathVariable id: String): MatchProfileDTO? {
        return matchProfileService.findById(id).takeIf { isEventOrJob(it) }
    }

    private fun isEventOrJob(it: MatchProfileDTO?) =
        (MatchProfileType.JOB == it?.type
                || MatchProfileType.EVENT == it?.type)

    @Get("/uuid/{sourceId}")
    fun getByUuid(@PathVariable sourceId: String): MatchProfileDTO? {
        return matchProfileService.findBySourceId(sourceId).takeIf { isEventOrJob(it) }
    }

}