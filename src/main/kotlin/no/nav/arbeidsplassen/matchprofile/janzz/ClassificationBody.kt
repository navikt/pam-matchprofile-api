package no.nav.arbeidsplassen.matchprofile.janzz

data class ClassificationBody(
    val title: String,
    val text: String,
    val classifications: List<String> = listOf(),
    val lang: String
)
