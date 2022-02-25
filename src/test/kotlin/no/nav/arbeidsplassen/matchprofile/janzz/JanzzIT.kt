package no.nav.arbeidsplassen.matchprofile.janzz

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.test.extensions.junit5.annotation.MicronautTest

import no.nav.arbeidsplassen.matchprofile.job.AdDTO
import org.junit.jupiter.api.Test
import java.io.File

@MicronautTest
class JanzzIT(private val janzzClient: JanzzClient, private val objectMapper: ObjectMapper) {

    @Test
    fun janzzJobParser() {
        val path = JanzzIT::class.java.classLoader.getResource("jobs/").path
        File(path).walkTopDown().forEach {
            if (it.isFile) {
                println(it)
                val ad = objectMapper.readValue(it, AdDTO::class.java)
                val query = QueryBody(ad.title , ad.properties["adtext"]!!.toString().replace(Regex("<.*?>") , " "))
                val janzz = janzzClient.parseJob(query)
                println(objectMapper.writeValueAsString(janzz))
            }
        }
    }

    @Test
    fun occupationRelation() {
        objectMapper.writeValueAsString(janzzClient.occupationBranchRelation("Taktekker", "skill"))
    }


}

