package no.nav.arbeidsplassen.matchprofile.profile

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@MicronautTest
class MatchProfileRepositoryTest(private val repository: MatchProfileRepository, private val objectMapper: ObjectMapper) {

    private val concept = Concept(label =  "Taktekker", type ="occupation")
    private val concept2 = Concept(label =  "Utvikler", type ="occupation")
    private val concept3 = Concept(label = "Barnehageassistent", type="occupation")
    private val concept4 = Concept(label =  "Taktekker", type ="occupation")

    @Test
    fun duplicateConceptsShouldNotBeAllowed() {
        val match = MatchProfile(profile = Profile(keywords = hashSetOf(concept,concept2,concept4)))
        Assertions.assertEquals(match.profile.keywords.size, 2)
    }


    @Test
    fun saveMatchProfile() {
        val matchProfile = MatchProfile(profile = Profile(keywords = hashSetOf(concept,concept2)))
        val saved = repository.save(matchProfile)
        val found = repository.findBySourceId(saved.sourceId)
        val foundUpdated = found?.copy(profile = Profile(hashSetOf(concept3)), status = MatchProfileStatus.INACTIVE)
        repository.save(foundUpdated!!)
        val updated = repository.findById(found.id!!).get()
        Assertions.assertEquals(updated.sourceId, saved.sourceId)
        Assertions.assertTrue(updated.updated.isAfter(found.updated))
        Assertions.assertEquals(updated.profile.keywords.size, 1)
        Assertions.assertEquals(updated.status, MatchProfileStatus.INACTIVE)
        println(objectMapper.writeValueAsString(updated))
    }
}
