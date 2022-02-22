package no.nav.arbeidsplassen.matchprofile.janzz

data class SimilarityBody(
    val term: String,
    val branch: BranchType,
    val lang: String
)

enum class BranchType {
    occupation,
    function,
    specialization,
    skill,
    softskill,
    industry,
    education,
    authorization
}
