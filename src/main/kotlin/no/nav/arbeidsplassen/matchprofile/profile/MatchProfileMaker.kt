package no.nav.arbeidsplassen.matchprofile.profile

import jakarta.inject.Singleton
import no.nav.arbeidsplassen.matchprofile.cv.CvDTO
import no.nav.arbeidsplassen.matchprofile.event.EventDTO
import no.nav.arbeidsplassen.matchprofile.job.AdDTO

@Singleton
class MatchProfileMaker(private val conceptFinder: ConceptFinder) {

    fun jobMatchProfile(ad: AdDTO): MatchProfileDTO {
        val concepts = conceptFinder.findConceptsForJobAd(ad)
        return MatchProfileDTO(profile = ProfileDTO(concepts = concepts), type = MatchProfileType.JOB,
            orgnr = ad.employer?.orgnr, sourceId = ad.uuid, title = ad.title, description = ad.businessName)
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
