package no.nav.arbeidsplassen.matchprofile.janzz

data class ParsedDTO(

    val id: String? = null,
    val title: String? = null,
    val lang: String? = null,
    val json: Json = Json()
)

data class Json(

    val title: String? = null,
    val text: String? = null,
    val Industry: List<List<String>> = listOf(),
    val Company: List<List<String>> = listOf(),
    val Occupation: List<List<String>> = listOf(),
    val Localization: List<List<String>> = listOf(),
    val ContractType: List<List<String>> = listOf(),
    val Availability: List<List<String>> = listOf(),
    val Function: List<List<String>> = listOf(),
    val Specialization: List<List<String>> = listOf(),
    val Skills: List<List<String>> = listOf(),
    val Softskills: List<List<String>> = listOf(),
    val Education: List<List<String>> = listOf(),
    val Languages: List<List<String>> = listOf(),
    val Experience: List<List<String>> = listOf(),
    val Supervisor: List<List<String>> = listOf(),
    val Authorizations: List<List<String>> = listOf(),
    val Salary: List<List<String>> = listOf(),
    val WorkingCondition: List<List<String>> = listOf()
)
