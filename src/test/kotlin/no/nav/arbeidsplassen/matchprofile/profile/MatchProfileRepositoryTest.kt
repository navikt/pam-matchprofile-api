package no.nav.arbeidsplassen.matchprofile.profile

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@MicronautTest
class MatchProfileRepositoryTest(private val repository: MatchProfileRepository, private val objectMapper: ObjectMapper) {

    private val concept = ConceptDTO(label =  "Taktekker", branch ="occupation")
    private val concept2 = ConceptDTO(label =  "Utvikler", branch ="occupation")
    private val concept3 = ConceptDTO(label = "Barnehageassistent", branch="occupation")
    private val concept4 = ConceptDTO(label =  "Taktekker", branch ="occupation")
    private val concept5 = ConceptDTO(label = "Butikkmedarbeider", branch="occpation", cid = 1L)

    @Test
    fun duplicateConceptsShouldNotBeAllowed() {
        val match = MatchProfile(owner ="owner", profile = Profile(concepts = hashSetOf(concept,concept2,concept4, concept5)))
        Assertions.assertEquals(match.profile.concepts.size, 3)
        println(objectMapper.writeValueAsString(match))
    }


    @Test
    fun saveMatchProfile() {
        val matchProfile = MatchProfile(owner ="owner", profile = Profile(concepts = hashSetOf(concept,concept2)))
        val saved = repository.save(matchProfile)
        val found = repository.findBySourceId(saved.sourceId)
        val foundUpdated = found?.copy(profile = Profile(hashSetOf(concept3)), status = MatchProfileStatus.INACTIVE)
        repository.save(foundUpdated!!)
        val updated = repository.findById(found.id!!).get()
        Assertions.assertEquals(updated.sourceId, saved.sourceId)
        Assertions.assertTrue(updated.updated.isAfter(found.updated))
        Assertions.assertEquals(updated.profile.concepts.size, 1)
        Assertions.assertEquals(updated.status, MatchProfileStatus.INACTIVE)
        println(objectMapper.writeValueAsString(updated))
    }
}
