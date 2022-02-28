package no.nav.arbeidsplassen.matchprofile.cv

import no.nav.arbeidsplassen.matchprofile.job.Location
import java.util.*
import kotlin.collections.List

data class WorkExperience (
    val uuid: String? = null,
    val employer: String? = null,
    val jobTitle: String? = null,
    val alternativeJobTitle: String? = null,
    val conceptId: String? = null,
    val location: Any? = null,
    val description: String? = null,
    val fromDate: Date? = null,
    val toDate: Date? = null,
    val ongoing:Boolean = false,
    val styrkkode: String? = null,
    val ikkeAktueltForFremtiden: Boolean = false,
    val updatedAt: Date? = null,
    val organisasjonsnummer: Any? = null
)

data class Education (
    val uuid: String? = null,
    val institution: String? = null,
    val field: String? = null,
    val nuskode: String? = null,
    val hasAuthorization: Boolean = false,
    val vocationalCollege: Any? = null,
    val startDate: Date? = null,
    val endDate: Date? = null,
    val description: String? = null,
    val ongoing: Any? = null,
    val updatedAt: Date? = null
)

data class OtherExperience (
    val uuid: String? = null,
    val description: String? = null,
    val role: String? = null,
    val fromDate: Date? = null,
    val toDate: Date? = null,
    val ongoing:Boolean = false,
    val updatedAt: Date? = null
)

data class Occupation (
    val id: String? = null,
    val title: String? = null,
    val conceptId: Long = 0,
    val styrk08: String? = null
)

data class Location (
    val id: String? = null,
    val location: String? = null,
    val code: String? = null,
    val conceptId:Long = 0
)

data class JobWishes (
    val active:Boolean = false,
    val startOption: String? = null,
    val occupations: List<Occupation> = listOf(),
    val occupationDrafts: List<Any> = listOf(),
    val locations: List<Location> = listOf(),
    val occupationTypes: List<String> = listOf(),
    val workLoadTypes: List<String> = listOf(),
    val workScheduleTypes: List<String> = listOf()
)

data class CvDTO (
    val hasCar:Boolean = false,
    val uuid: String,
    val workExperience: List<WorkExperience> = listOf(),
    val education: List<Education> = listOf(),
    val certificates: List<Certificates> = listOf(),
    val driversLicenses: List<DriversLicenses> = listOf(),
    val otherExperience: List<OtherExperience> = listOf(),
    val courses: List<Any> = listOf(),
    val languages: List<Languages> = listOf(),
    val authorizations: List<Any> = listOf(),
    val vocationalCertificates: List<Any> = listOf(),
    val skills: List<Skills> = listOf(),
    val summary: String? = null,
    val jobWishes: JobWishes? = null,
    val lastEditedByNav:Boolean = false,
    val updatedAt: Date? = null
)

data class Certificates (
    val uuid            : String? = null,
    val certificateName : String? = null,
    val alternativeName : String? = null,
    val conceptId       : String? = null,
    val issuer          : String? = null,
    val fromDate        : String? = null,
    val toDate          : String? = null,
    val updatedAt       : String? = null
)

data class DriversLicenses (

    val uuid         : String? = null,
    val type         : String? = null,
    val acquiredDate : String? = null,
    val expiryDate   : String? = null,
    val updatedAt    : String? = null

)

data class Skills (

    val uuid      : String? = null,
    val title     : String? = null,
    val conceptId : Int?    = null,
    val updatedAt : String? = null

)

data class Languages (

  val language           : String? = null,
  val uuid               : String? = null,
  val oralProficiency    : String? = null,
  val writtenProficiency : String? = null,
  val updatedAt          : String? = null

)
