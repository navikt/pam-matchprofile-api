package no.nav.arbeidsplassen.matchprofile.janzz

data class SimilarityBody(
    val term: String,
    val branch: String,
    val lang: String
)

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
