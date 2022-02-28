package no.nav.arbeidsplassen.matchprofile.profile

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import no.nav.arbeidsplassen.matchprofile.job.AdDTO
import org.junit.jupiter.api.Test

@MicronautTest
class ConceptFinderIT(private val conceptFinder: ConceptFinder, private val objectMapper: ObjectMapper) {

    //@Test
    fun findConceptsForJob() {
        val ad = objectMapper.readValue(ConceptFinder::class.java.classLoader.getResourceAsStream("jobs/bartender.json"), AdDTO::class.java)
        println(objectMapper.writeValueAsString(conceptFinder.findConceptsForJobAd(ad)))
    }

    @Test
    fun findConceptsForText() {
        val text = "Servitører\n Bartendere\n Servitør\n Serviceinnstilt\n Blid\n Omgjengelig\n Kommunikasjonsevner\n Øltapper"
        println(objectMapper.writeValueAsString(conceptFinder.findKnownConceptsForText(text, false)))
    }
}
