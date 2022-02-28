package no.nav.arbeidsplassen.matchprofile.profile

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@MicronautTest
class MatchProfileServiceTest(private val matchProfileService: MatchProfileService, private val objectMapper: ObjectMapper) {

    @Test
    fun crudMatchProfile() {
        val matchprofile = MatchProfileDTO(owner = "owner", title = "This is a title", description = "this is a description",
            profile = ProfileDTO(concepts = hashSetOf(ConceptDTO(label = "taktekker", branch = "occupation")))
        )
        val created = matchProfileService.save(matchprofile)
        val find = matchProfileService.findById(created.id!!)
        val updated = matchProfileService.save(find!!.copy(title = "Changed title"))
        val inactive = matchProfileService.save(updated.copy(status = MatchProfileStatus.INACTIVE))
        Assertions.assertEquals(inactive.status, MatchProfileStatus.INACTIVE)
        Assertions.assertEquals(inactive.title, "Changed title")
        println(objectMapper.writeValueAsString(inactive))
    }
}
