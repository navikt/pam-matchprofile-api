package no.nav.arbeidsplassen.matchprofile.profile

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Test

@MicronautTest
class MatchProfileRepositoryTest(private val repository: MatchProfileRepository, private val objectMapper: ObjectMapper) {

    val concept = Concept(label =  "Taktekker", type ="occupation")
    val concept2 = Concept(label =  "Utvikler", type ="occupation")
    val concept3 = Concept(label = "Barnehageassistent", type="occupation")
    val matchProfile = MatchProfile(profile = Profile(keywords = hashSetOf(concept,concept2)))

    @Test
    fun saveMatchProfile() {
        val saved = repository.save(matchProfile)
        val found = repository.findById(saved.id).get()
        val foundUpdated = found.copy(profile = Profile(hashSetOf(concept, concept3)))
        val updated = repository.save(foundUpdated)
        println(objectMapper.writeValueAsString(found))
        println(objectMapper.writeValueAsString(updated))
    }
}
