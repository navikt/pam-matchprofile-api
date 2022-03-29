package no.nav.arbeidsplassen.matchprofile.typeahead

import jakarta.inject.Singleton
import no.nav.pam.geography.County
import no.nav.pam.geography.CountyDAO
import no.nav.pam.geography.Municipality
import no.nav.pam.geography.MunicipalityDAO

@Singleton
class GeographyService {

    fun getTypeaheadCounties(search: String, numResults: Int?): List<County> {
        return CountyDAO.getImmutableCountySet()
            .filter { it.name.contains(search, ignoreCase = true) }
            .take(numResults ?: 20)
    }

    fun getTypeaheadMunicipalities(search: String, numResults: Int?): List<Municipality> {
        return MunicipalityDAO().allMunicipalities
            .filter { it.name.contains(search, ignoreCase = true) }
            .take(numResults ?: 20)
    }

}
