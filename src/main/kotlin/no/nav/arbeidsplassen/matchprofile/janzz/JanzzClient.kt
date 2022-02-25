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
    fun parseJob(@Body QueryBody: QueryBody, @QueryValue want_cids: Boolean = true): ParsedDTO

    // NOT as good, we use parseJob
    @Post("/parser/parse_cv/")
    fun parseCV(@Body QueryBody: QueryBody): ParsedDTO

    @Post("/parser/similarity/")
    fun findSimilarity(@Body similarity: SimilarityBody): JsonNode


    @Get("/occupation_suggest")
    fun occupationBranchRelation(@QueryValue occupation: String, @QueryValue relation:String, @QueryValue lang: String = "no"): ConceptResultsDTO

    @Get("/expand_concept")
    fun expandConcept(@QueryValue q: String, @QueryValue branch: String, @QueryValue output_lang: String="no", @QueryValue with_cids: Boolean = true, @QueryValue exclude_tags: Boolean = true): ConceptResultsDTO

    @Get("/expand_concept_id")
    fun expandConceptByCid(@QueryValue id: Long, @QueryValue output_lang: String="no", @QueryValue with_cids: Boolean = true, @QueryValue exclude_tags: Boolean = true): ConceptResultsDTO

}
