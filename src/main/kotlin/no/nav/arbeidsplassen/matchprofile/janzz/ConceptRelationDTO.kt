package no.nav.arbeidsplassen.matchprofile.janzz

data class ConceptResultsDTO(val results: List<ConceptLabelDTO> = listOf())

data class ConceptLabelDTO(val label: String, val cid: Long?, val count: Long?, val rel: String?)

object Branch {
    const val occupation = "occupation"
    const val function  = "function"
    const val specialization = "specialization"
    const val skill     = "skill"
    const val softskill = "softskill"
    const val industry  = "industry"
    const val education = "education"
    const val authorization = "authorization"
    const val experience = "experience"
}
