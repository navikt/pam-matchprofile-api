package no.nav.arbeidsplassen.matchprofile.janzz

data class ConceptResultsDTO(val results: List<ConceptLabelDTO> = listOf())

data class ConceptLabelDTO(val label: String, val cid: Long?, val count: Long?, val rel: String?)
