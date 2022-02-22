package no.nav.arbeidsplassen.matchprofile.profile

import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
class MatchProfileService(private val repository: MatchProfileRepository) {

    fun save(matchProfile: MatchProfileDTO) : MatchProfileDTO {
       val entity = matchProfile.id?.let { repository.findById(it).orElseThrow()
           .copy(status = matchProfile.status, title = matchProfile.title, description = matchProfile.description,
               profile = matchProfile.profile.toEntity(), expires = matchProfile.expires) } ?: matchProfile.toEntity()
        return repository.save(entity).toDTO()
    }


    fun findBySourceId(sourceId: String): MatchProfileDTO? {
        return repository.findBySourceId(sourceId)?.toDTO()
    }

    fun findById(id:String): MatchProfileDTO? {
        return repository.findById(id).orElse(null).toDTO()
    }

    private fun MatchProfileDTO.toEntity(): MatchProfile {
        return MatchProfile(id = id, sourceId = sourceId, type = type, status = status, title = title, description = description,
            profile = profile.toEntity(), createdBy = createdBy, updatedBy = updatedBy, expires = expires, created = created,
        updated = updated)
    }

    private fun ProfileDTO.toEntity(): Profile {
        return Profile(keywords = keywords.map { it.toEntity() }.toSet())
    }

    private fun ConceptDTO.toEntity(): Concept {
        return Concept(label = label, cid = cid, type = type, known = known, expandedConcept = expandedConcept,
            createdByUser = createdByUser, lang = lang)
    }

    private fun Profile.toDTO(): ProfileDTO {
        return ProfileDTO(keywords = keywords.map { it.toDTO() }.toSet() )
    }

    private fun Concept.toDTO() : ConceptDTO {
        return ConceptDTO(label = label, cid = cid, type = type, known = known, expandedConcept = expandedConcept,
            createdByUser = createdByUser, lang = lang)
    }

    private fun MatchProfile.toDTO(): MatchProfileDTO {
       return MatchProfileDTO(id = id, sourceId = sourceId, type = type, status = status, title = title, description = description,
           profile = profile.toDTO(), createdBy = createdBy, updatedBy = updatedBy, expires = expires, created = created,
           updated = updated)
    }

}
