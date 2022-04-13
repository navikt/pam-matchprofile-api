package no.nav.arbeidsplassen.matchprofile.profile

import jakarta.inject.Singleton
import no.nav.arbeidsplassen.matchprofile.cv.CvDTO
import no.nav.arbeidsplassen.matchprofile.event.EventDTO
import no.nav.arbeidsplassen.matchprofile.job.AdDTO
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.ZoneOffset

@Singleton
class MatchProfileMaker(private val conceptFinder: ConceptFinder, private val matchProfileService: MatchProfileService) {

    companion object {
        private val LOG = LoggerFactory.getLogger(MatchProfileMaker::class.java)
    }
    fun jobMatchProfile(ad: AdDTO): MatchProfileDTO {
        // to reduce calls to janzz, we only run it once.
        return matchProfileService.findBySourceId(ad.uuid)?.
        takeIf {it.profile.concepts.isNotEmpty()}?.
        let { LOG.debug("Matchprofile already exists, and has concepts. Skipping janzz")
            it.copy(profile = it.profile.copy(locations = mapLocations(ad)), orgnr = ad.employer?.orgnr, title = ad.title,
            description = ad.businessName, status = mapStatus(ad), expires = ad.expires.toInstant(ZoneOffset.UTC))}?:
        run {
            val concepts = conceptFinder.findConceptsForJobAd(ad)
            MatchProfileDTO(profile = ProfileDTO(concepts = concepts, locations = mapLocations(ad)), type = MatchProfileType.JOB,
                orgnr = ad.employer?.orgnr, sourceId = ad.uuid, title = ad.title, description = ad.businessName, status = mapStatus(ad),
                expires=ad.expires.toInstant(ZoneOffset.UTC))
        }
    }

    private fun mapStatus(ad: AdDTO): MatchProfileStatus {
       return if (ad.status == "ACTIVE") MatchProfileStatus.ACTIVE else
           MatchProfileStatus.INACTIVE
    }

    private fun mapLocations(ad: AdDTO) =
        ad.locationList.map {
            LocationDTO(
                country = it.country,
                county = it.county,
                municipality = it.municipal,
                city = it.city
            )
        }

    fun eventMatchProfile(event: EventDTO): MatchProfileDTO {
        val concepts = conceptFinder.findConceptsForEvent(event)
        return MatchProfileDTO(profile = ProfileDTO(concepts = concepts), type = MatchProfileType.EVENT,
            orgnr = event.organizationNumber, sourceId = event.id, title = event.title, description = event.organizationName)
    }

    fun cvMatchProfile(cv: CvDTO): MatchProfileDTO {
        val concepts = conceptFinder.findConceptsForCv(cv)
        return MatchProfileDTO(profile = ProfileDTO(concepts = concepts), type = MatchProfileType.USER,
            sourceId = cv.uuid, title = "Autogenerert", description = "Autogenerert")
    }

}
