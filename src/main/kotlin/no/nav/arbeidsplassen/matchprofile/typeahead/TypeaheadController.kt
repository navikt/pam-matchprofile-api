package no.nav.arbeidsplassen.matchprofile.typeahead

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import no.nav.pam.geography.*
import org.slf4j.LoggerFactory

@Controller("/api/v1/typeahead")
class TypeaheadController(private val geographyService: GeographyService) {

    companion object {
        private val LOG = LoggerFactory.getLogger(TypeaheadController::class.java)
    }

    @Get("/geografi/kommune")
    fun typeaheadKommune(@QueryValue q: String, @QueryValue numResults: Int?): List<Municipality> {
        LOG.info("Received request for typeahead kommune, query: $q")

        return geographyService.getTypeaheadMunicipalities(q, numResults)
    }

    @Get("/geografi/fylke")
    fun typeaheadFylke(@QueryValue q: String, @QueryValue numResults: Int?): List<County> {
        LOG.info("Received request for typeahead fylke, query: $q")

        return geographyService.getTypeaheadCounties(q, numResults)
    }

}
