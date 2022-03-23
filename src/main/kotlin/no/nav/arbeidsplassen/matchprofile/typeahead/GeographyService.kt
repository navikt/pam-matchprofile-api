package no.nav.arbeidsplassen.matchprofile.typeahead

import jakarta.inject.Singleton
import no.nav.pam.geography.County
import no.nav.pam.geography.Municipality
import no.nav.pam.geography.PostDataDAO

@Singleton
class GeographyService {

    companion object {
        private val postDataDAO = PostDataDAO()
    }

    fun getTypeaheadMunicipality(search: String): List<Municipality> {
        return postDataDAO.allMunicipalities
            .filter { it.name.contains(search, true) }
    }

    fun getTypeaheadCounty(search: String): List<County> {
        return postDataDAO.allPostData
            .map { it.county }
            .toSet()
            .toList()
            .filter { it.name.contains(search, true) }
    }

}
