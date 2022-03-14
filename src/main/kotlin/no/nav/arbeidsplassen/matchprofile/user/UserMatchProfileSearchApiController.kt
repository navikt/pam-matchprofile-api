package no.nav.arbeidsplassen.matchprofile.user

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import no.nav.arbeidsplassen.matchprofile.profile.MatchProfileService
import no.nav.arbeidsplassen.matchprofile.profile.MatchProfileType
import org.opensearch.action.search.SearchRequest
import org.opensearch.client.RequestOptions
import org.opensearch.client.RestHighLevelClient
import org.opensearch.index.query.MoreLikeThisQueryBuilder
import org.opensearch.index.query.QueryBuilders
import org.opensearch.search.builder.SearchSourceBuilder
import org.slf4j.LoggerFactory

@Controller("/api/v1/user/match")
class UserMatchProfileSearchApiController(private val client: RestHighLevelClient, private val matchProfileService: MatchProfileService) {

    companion object {
        private val LOG = LoggerFactory.getLogger(UserMatchProfileSearchApiController::class.java)
    }

    @Get("/events")
    fun matchUserForEvents() : String {
        TODO()
    }

    @Get("/jobs")
    fun matchUserForJobs() : String  {
        val pId = "anonymous-1"
        val matchprofile = matchProfileService.findByPId(pId)
        return getMatchedItemsByType(matchprofile!!.sourceId, MatchProfileType.JOB)
    }

    private fun getMatchedItemsByType(id: String, type: MatchProfileType): String {
        val boolQuery = QueryBuilders.boolQuery()
        boolQuery.filter(QueryBuilders.termQuery("type", type))

        val mltQuery = QueryBuilders.moreLikeThisQuery(
            arrayOf("concepts_keyword", "concepts", "municipal", "county"),
            null,
            arrayOf(MoreLikeThisQueryBuilder.Item("matchprofile", id))
        )
        mltQuery.minTermFreq(1)
        mltQuery.minDocFreq(1)
        mltQuery.minWordLength(3)
        mltQuery.maxQueryTerms(200)
        mltQuery.minimumShouldMatch("15%")
        boolQuery.must().add(mltQuery)
        val sourceBuilder = SearchSourceBuilder()
        sourceBuilder.query(boolQuery)
        LOG.debug(sourceBuilder.toString())
        val searchRequest = SearchRequest(arrayOf("matchprofile"), sourceBuilder)
        val response =  client.search(searchRequest, RequestOptions.DEFAULT)
        return response.toString()
    }


}