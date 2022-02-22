package no.nav.arbeidsplassen.matchprofile.janzz

import com.fasterxml.jackson.databind.JsonNode
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpHeaders.USER_AGENT
import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client

@Client("\${janzz.url}")
@Headers(
    Header(name="Authorization", value="\${janzz.token}"),
    Header(name = USER_AGENT, value = "Micronaut HTTP Client"),
    Header(name = HttpHeaders.ACCEPT, value= "application/json")
)
interface JanzzClient {

    @Post("/parser/parse_job/")
    @Header(name = HttpHeaders.ACCEPT, value = "application/json")
    fun parseJob(@Body QueryBody: QueryBody): ParsedDTO

    // NOT as good, we use parseJob
    @Post("/parser/parse_cv/")
    @Header(name = HttpHeaders.ACCEPT, value = "application/json")
    fun parseCV(@Body QueryBody: QueryBody): ParsedDTO

    @Post("/parser/similarity/")
    @Header(name = HttpHeaders.ACCEPT, value = "application/json")
    fun findSimilarity(@Body similarity: SimilarityBody): JsonNode


    @Get("/occupation_suggest")
    @Header(name = HttpHeaders.ACCEPT, value = "application/json")
    fun occupationBranchRelation(@QueryValue occupation: String, @QueryValue relation:String, @QueryValue lang: String = "no"): ConceptResultsDTO

    @Get("/expand_concept")
    fun conceptExpand(@QueryValue q: String, @QueryValue branch: String, @QueryValue output_lang: String="no"): ConceptResultsDTO

}
