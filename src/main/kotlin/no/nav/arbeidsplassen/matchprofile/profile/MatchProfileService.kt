package no.nav.arbeidsplassen.matchprofile.profile

import io.micronaut.aop.Around
import jakarta.inject.Singleton
import no.nav.arbeidsplassen.matchprofile.outbox.Outbox
import no.nav.arbeidsplassen.puls.outbox.OutboxRepository
import javax.transaction.Transactional

@Singleton
@Around
class MatchProfileService(private val repository: MatchProfileRepository, private val outboxRepository: OutboxRepository) {

    @Transactional
    fun save(matchProfile: MatchProfileDTO) : MatchProfileDTO {
       val entity = matchProfile.id?.let { repository.findById(it).orElseThrow()
           .mergeCopy(matchProfile)} ?: repository.findBySourceId(matchProfile.sourceId)
           // We only allow one matchprofile per sourceId for now
           ?.mergeCopy(matchProfile) ?: matchProfile.toEntity()
        val saved = repository.save(entity).toDTO()
        outboxRepository.save(Outbox(keyId = saved.id!!, type = saved.type.toString(), payload = saved))
        return saved
    }

    @Transactional
    fun saveWithUser(matchProfile: MatchProfileDTO, pId: String) : MatchProfileDTO {
        val entity = matchProfile.id?.let { repository.findById(it).orElseThrow()
            .takeIf {m -> m.pId == pId && m.sourceId == matchProfile.sourceId}?.mergeCopy(matchProfile) ?: throw IllegalArgumentException("Wrong user!")
        } ?: matchProfile.toEntity(pId)
        val saved = repository.save(entity).toDTO()
        outboxRepository.save(Outbox(keyId = saved.id!!, type = saved.type.toString(), payload = saved))
        return saved
    }

    fun findBySourceId(sourceId: String): MatchProfileDTO? {
        return repository.findBySourceId(sourceId)?.toDTO()
    }

    fun findById(id:String): MatchProfileDTO? {
        return repository.findById(id).orElse(null).toDTO()
    }

    fun findByOrgnr(orgnr: String): List<MatchProfileDTO> {
        return repository.findByOrgnr(orgnr).map { it.toDTO() }
    }

    fun findByPId(pId: String): MatchProfileDTO? {
        return repository.findByPId(pId)?.toDTO()
    }

    private fun MatchProfileDTO.toEntity(): MatchProfile {
        return MatchProfile(id = id, orgnr = orgnr, sourceId = sourceId, type = type, status = status, title = title, description = description,
            profile = profile.toEntity(), createdBy = createdBy, updatedBy = updatedBy, expires = expires, created = created,
        updated = updated)
    }

    private fun MatchProfileDTO.toEntity(pId: String): MatchProfile {
        return MatchProfile(id = id, pId = pId,  orgnr = orgnr, sourceId = sourceId, type = type, status = status, title = title, description = description,
            profile = profile.toEntity(), createdBy = createdBy, updatedBy = updatedBy, expires = expires, created = created,
            updated = updated)
    }

    private fun ProfileDTO.toEntity(): Profile {
        return Profile(concepts = concepts.map { it.toEntity() }.toSet())
    }

    private fun ConceptDTO.toEntity(): ConceptDTO {
        return ConceptDTO(label = label, cid = cid, branch = branch, expandedConcept = expandedConcept, lang = lang)
    }

    private fun Profile.toDTO(): ProfileDTO {
        return ProfileDTO(concepts = concepts.map { it.toDTO() }.toSet() )
    }

    private fun ConceptDTO.toDTO() : ConceptDTO {
        return ConceptDTO(label = label, cid = cid, branch = branch, expandedConcept = expandedConcept, lang = lang)
    }

    private fun MatchProfile.toDTO(): MatchProfileDTO {
       return MatchProfileDTO(id = id, orgnr = orgnr, sourceId = sourceId, type = type, status = status, title = title, description = description,
           profile = profile.toDTO(), createdBy = createdBy, updatedBy = updatedBy, expires = expires, created = created,
           updated = updated)
    }

    private fun MatchProfile.mergeCopy(dto: MatchProfileDTO): MatchProfile {
        return this.copy(orgnr = dto.orgnr, status = dto.status, title = dto.title, description = dto.description,
            profile = dto.profile.toEntity(), updatedBy = dto.updatedBy, expires = dto.expires)
    }

}
