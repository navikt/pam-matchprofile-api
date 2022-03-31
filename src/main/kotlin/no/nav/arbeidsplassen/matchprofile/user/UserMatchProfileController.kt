package no.nav.arbeidsplassen.matchprofile.user

import io.micronaut.http.annotation.*
import no.nav.arbeidsplassen.matchprofile.profile.MatchProfileDTO
import no.nav.arbeidsplassen.matchprofile.profile.MatchProfileService
import org.slf4j.LoggerFactory

@Controller("/api/v1/user/matchprofile")
class UserMatchProfileController(private val matchProfileService: MatchProfileService) {


    companion object {
        private val LOG = LoggerFactory.getLogger(UserMatchProfileController::class.java)
    }

    @Get("/")
    fun get(): MatchProfileDTO? {
        val pId = "anonymous-1"
        return matchProfileService.findByPId(pId)
    }

    @Post("/")
    fun create(@Body matchProfile: MatchProfileDTO) : MatchProfileDTO {
        val pId = "anonymous-1" // Locked to this user until we get authentication/authorization
        return matchProfileService.saveWithUser(matchProfile, pId)
    }

    @Delete("/")
    fun delete(@Body matchProfile: MatchProfileDTO): Boolean {
        val pId = "anonymous-1"
        return matchProfileService.deleteWithUser(matchProfile, pId)
    }


}
