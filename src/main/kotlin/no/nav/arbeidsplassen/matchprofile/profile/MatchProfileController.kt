package no.nav.arbeidsplassen.matchprofile.profile

import io.micronaut.http.annotation.*
import org.slf4j.LoggerFactory

//TODO authentication later
@Controller("/api/v1/matchprofile")
class MatchProfileController(private val matchProfileService: MatchProfileService) {

    companion object {
        private val LOG = LoggerFactory.getLogger(MatchProfileController::class.java)
    }

    @Get("/{id}")
    fun findById(@PathVariable id: String): MatchProfileDTO? {
        return matchProfileService.findById(id)
    }

    @Post("/")
    fun create(@Body matchProfile: MatchProfileDTO) : MatchProfileDTO {
        LOG.info("saving matchprofile with sourceId: ${matchProfile.sourceId}")
        return matchProfileService.save(matchProfile)
    }

}
