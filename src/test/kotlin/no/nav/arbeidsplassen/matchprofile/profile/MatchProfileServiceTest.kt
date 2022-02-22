package no.nav.arbeidsplassen.matchprofile.profile

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@MicronautTest
class MatchProfileServiceTest(private val matchProfileService: MatchProfileService) {

    @Test
    fun crudMatchProfile() {
        val matchprofile = MatchProfileDTO(title = "This is a title", description = "this is a description",
            profile = ProfileDTO(keywords = hashSetOf(ConceptDTO(label = "taktekker", type = "occupation")))
        )
        val created = matchProfileService.save(matchprofile)
        val find = matchProfileService.findById(created.id!!)
        val updated = matchProfileService.save(find!!.copy(title = "Changed title"))
        val inactive = matchProfileService.save(updated.copy(status = MatchProfileStatus.INACTIVE))
        Assertions.assertEquals(inactive.status, MatchProfileStatus.INACTIVE)
        Assertions.assertEquals(inactive.title, "Changed title")
    }
}
