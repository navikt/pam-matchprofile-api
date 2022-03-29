package no.nav.arbeidsplassen.matchprofile.health

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import no.nav.arbeidsplassen.matchprofile.profile.ConceptFinder
import org.slf4j.LoggerFactory

@Controller("/internal")
class StatusController(private val conceptFinder: ConceptFinder) {

    companion object {
        private val LOG = LoggerFactory.getLogger(StatusController::class.java)
    }

    @Get("/isReady")
    fun isReady(): HttpResponse<String> {
        return HttpResponse.ok("OK")
    }

    @Get("/isAlive")
    fun isAlive(): HttpResponse<String> {
        return HttpResponse.ok("OK")
    }

    @Get("/check")
    fun check(): HttpResponse<String> {
        LOG.info("Checking janzz")
        conceptFinder.findConceptsForText("Dette er en test")
        return HttpResponse.ok("OK")
    }

}
