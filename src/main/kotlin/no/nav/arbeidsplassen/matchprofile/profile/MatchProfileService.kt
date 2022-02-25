package no.nav.arbeidsplassen.matchprofile.profile

import jakarta.inject.Singleton

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

    fun findByOwner(owner: String): List<MatchProfileDTO> {
        return repository.findByOwner(owner).map { it.toDTO() }
    }

    private fun MatchProfileDTO.toEntity(): MatchProfile {
        return MatchProfile(id = id, owner = owner, sourceId = sourceId, type = type, status = status, title = title, description = description,
            profile = profile.toEntity(), createdBy = createdBy, updatedBy = updatedBy, expires = expires, created = created,
        updated = updated)
    }

    private fun ProfileDTO.toEntity(): Profile {
        return Profile(keywords = keywords.map { it.toEntity() }.toSet())
    }

    private fun ConceptDTO.toEntity(): Concept {
        return Concept(label = label, cid = cid, branch = branch, expandedConcept = expandedConcept, lang = lang)
    }

    private fun Profile.toDTO(): ProfileDTO {
        return ProfileDTO(keywords = keywords.map { it.toDTO() }.toSet() )
    }

    private fun Concept.toDTO() : ConceptDTO {
        return ConceptDTO(label = label, cid = cid, branch = branch, expandedConcept = expandedConcept, lang = lang)
    }

    private fun MatchProfile.toDTO(): MatchProfileDTO {
       return MatchProfileDTO(id = id, owner = owner,  sourceId = sourceId, type = type, status = status, title = title, description = description,
           profile = profile.toDTO(), createdBy = createdBy, updatedBy = updatedBy, expires = expires, created = created,
           updated = updated)
    }

}
