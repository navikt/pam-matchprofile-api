package no.nav.arbeidsplassen.matchprofile.profile

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import no.nav.arbeidsplassen.matchprofile.job.AdDTO
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

@MicronautTest
class MatchProfileMakerTest(private val matchProfileMaker: MatchProfileMaker,
                            private val matchProfileService: MatchProfileService,
                            private val objectMapper: ObjectMapper) {

    @Test
    fun jobMatchProfileMakeTest() {
        val uuid = UUID.randomUUID().toString()
        val ad = objectMapper.readValue(ConceptFinder::class.java.classLoader.getResourceAsStream("jobs/bartender.json"), AdDTO::class.java)
            .copy(uuid = uuid)
        val matchProfile = matchProfileMaker.jobMatchProfile(ad)
        println(objectMapper.writeValueAsString(matchProfile))
        Assertions.assertEquals(uuid, matchProfile.sourceId)
        Assertions.assertEquals(MatchProfileStatus.ACTIVE,matchProfile.status )
        Assertions.assertEquals("974201828", matchProfile.orgnr)
    }
}