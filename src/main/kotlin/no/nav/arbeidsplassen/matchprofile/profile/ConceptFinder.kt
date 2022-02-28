package no.nav.arbeidsplassen.matchprofile.concept

import jakarta.inject.Singleton
import no.nav.arbeidsplassen.matchprofile.cv.CvDTO
import no.nav.arbeidsplassen.matchprofile.event.EventDTO
import no.nav.arbeidsplassen.matchprofile.janzz.*
import no.nav.arbeidsplassen.matchprofile.janzz.Branch.education
import no.nav.arbeidsplassen.matchprofile.janzz.Branch.experience
import no.nav.arbeidsplassen.matchprofile.janzz.Branch.function
import no.nav.arbeidsplassen.matchprofile.janzz.Branch.industry
import no.nav.arbeidsplassen.matchprofile.janzz.Branch.occupation
import no.nav.arbeidsplassen.matchprofile.job.AdDTO
import no.nav.arbeidsplassen.matchprofile.profile.ConceptDTO
import no.nav.arbeidsplassen.matchprofile.janzz.Branch.skill
import no.nav.arbeidsplassen.matchprofile.janzz.Branch.softskill
import no.nav.arbeidsplassen.matchprofile.janzz.Branch.specialization
import org.slf4j.LoggerFactory

@Singleton
class ConceptFinder(private val janzzClient: JanzzClient) {

    private val cSkills = 10
    private val cExpansions = 10

    companion object {
        private val LOG = LoggerFactory.getLogger(ConceptFinder::class.java)
    }

    fun findConceptsForJobAd(ad:AdDTO, related: Boolean = true) : Set<ConceptDTO> {
        val concepts = getParsedConcepts(parseJobAd(ad))
        LOG.info("parsed job ad: ${ad.uuid} found ${concepts.size} concepts")
        val combineConcepts = if (related ) concepts.plus(getRelatedConcepts(concepts)) else concepts
        return combineConcepts.associateBy { it.label }.values.toHashSet()
    }

    fun findConceptsForEvent(event: EventDTO, related: Boolean = true) : Set<ConceptDTO> {
        val concepts = getParsedConcepts(parseEvent(event))
        LOG.info("parsed event: ${event.id} found ${concepts.size} concepts")
        val combineConcepts = if (related ) concepts.plus(getRelatedConcepts(concepts)) else concepts
        return combineConcepts.associateBy { it.label }.values.toHashSet()
    }

    fun findConceptsForCv(cv: CvDTO, related: Boolean = true) : Set<ConceptDTO> {
        val concepts = getParsedConcepts(parseCV(cv))
        LOG.info("parsed event: ${cv.uuid} found ${concepts.size} concepts")
        val combineConcepts = if (related ) concepts.plus(getRelatedConcepts(concepts)) else concepts
        return combineConcepts.associateBy { it.label }.values.toHashSet()
    }

    fun findKnownConceptsForText(text: String, related: Boolean = true) : Set<ConceptDTO> {
        val concepts = getParsedConcepts(parseText(text))
        val combineConcepts = if (related ) concepts.plus(getRelatedConcepts(concepts)) else concepts
        return combineConcepts.associateBy { it.label }.values.toHashSet()
    }

    fun findBranchForKnownConcepts(knownConcepts: Set<ConceptDTO>, related: Boolean = true) : Set<ConceptDTO> {
        val concepts = getParsedConcepts(
            parseText(knownConcepts.filter { it.cid != null }.map { it.label }.joinToString("\n"))
        )
        val combineConcepts = if (related ) concepts.plus(getRelatedConcepts(concepts)) else concepts
        return combineConcepts.associateBy { it.label }.values.toHashSet()
    }

    private fun parseText(text: String): ParsedDTO {
        return janzzClient.parseJob(QueryBody("", body = text))
    }

    private fun parseJobAd(ad: AdDTO): ParsedDTO {
        val jobtitle = ad.properties["jobtitle"]?:""
        val text =  ad.properties["adtext"]?.toString()?.replace(Regex("<.*?>")," ") ?: " "
        return janzzClient.parseJob(QueryBody(title = "${ad.title} $jobtitle", body = text))
    }

    private fun parseJobAds(ads: List<AdDTO>): List<ParsedDTO> {
        return ads.map { parseJobAd(it)}
    }

    private fun parseCV(cv: CvDTO): ParsedDTO {
        val exp = cv.workExperience.map {"${it.jobTitle?:""} ${it.alternativeJobTitle?:""} ${it.description?:""}"}
        val edu = cv.education.map { "${it.institution?:""} ${it.field?:""} ${it.description?:""}" }
        val skills = cv.skills.map { it.title?:"" }
        val body = exp.joinToString(" ") + edu.joinToString(" ") + skills.joinToString(" ")
        val query = QueryBody("", body)
        return janzzClient.parseJob(query)
    }

    private fun parseCVs(cvs: List<CvDTO>): List<ParsedDTO> {
        return cvs.map { parseCV(it) }
    }

    private fun parseEvent(event: EventDTO): ParsedDTO {
        val query = QueryBody(event.title, event.description)
        return janzzClient.parseJob(query)
    }

    private fun parseEvents(events: List<EventDTO>): List<ParsedDTO> {
        return events.map { parseEvent(it) }
    }

    private fun getParsedConcepts(doc: ParsedDTO): List<ConceptDTO> {
        val concepts = termsToConcepts(doc.json.Occupation, occupation)
            .plus(termsToConcepts(doc.json.Skills, skill))
            .plus(termsToConcepts(doc.json.Education, education))
            .plus(termsToConcepts(doc.json.Experience, experience))
            .plus(termsToConcepts(doc.json.Softskills, softskill))
            .plus(termsToConcepts(doc.json.Specialization, specialization))
            .plus(termsToConcepts(doc.json.Function, function))
            .plus(termsToConcepts(doc.json.Industry, industry))
        return concepts
    }

    private fun getRelatedConcepts(concepts: List<ConceptDTO>): List<ConceptDTO> {
        val onlyOccupations = concepts
            .filter { it.branch == occupation }
            .filter { it.cid != null }

        val moreSkills = onlyOccupations
            .map { Pair(it.label, suggestBranchRelation(it.label, skill)) }
            .flatMap {(label, results) ->
                results
                    .take(cSkills)
                    .map { ConceptDTO(label=it.label, cid = it.cid, branch = skill, expandedConcept = label)}
             }

        val expansions = onlyOccupations
            .map { Pair(it.label, janzzClient.expandConceptByCid(it.cid!!).results ) }
            .flatMap {(label, results) -> results
                                            .take(cExpansions)
                                            .map{ ConceptDTO(label=it.label, cid=it.cid, branch=occupation, expandedConcept = label)}}

        return moreSkills.plus(expansions)
    }

    private fun termsToConcepts(terms:List<List<String>>, type: String): List<ConceptDTO> {
        return terms.map { toConcept(it,type) }
    }

    private fun toConcept(terms: List<String?>, type: String): ConceptDTO {
        println("$type:$terms")
        return ConceptDTO(
            label = terms[0]!!,
            cid = terms[1]?.toLong(),
            branch = type
        )
    }

    fun suggestBranchRelation(occupation: String, relation: String): List<ConceptLabelDTO> {

        return try {
            janzzClient.occupationBranchRelation(occupation, relation ).results
        }
        catch (e:Exception) {
            LOG.error("Got exception while calling suggestBranchRelation for $occupation", e)
            emptyList<ConceptLabelDTO>()
        }
    }

}
