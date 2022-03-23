package no.nav.arbeidsplassen.matchprofile.typeahead

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import no.nav.pam.geography.*

@Controller("/api/v1/typeahead")
class TypeaheadController(private val geographyService: GeographyService) {


    @Get("/geografi/kommune")
    fun typeaheadKommune(@QueryValue q: String): List<Municipality> {
        return geographyService.getTypeaheadMunicipality(q)
    }

    @Get("/geografi/fylke")
    fun typeaheadFylke(@QueryValue q: String): List<County> {
        return geographyService.getTypeaheadCounty(q)
    }

}
