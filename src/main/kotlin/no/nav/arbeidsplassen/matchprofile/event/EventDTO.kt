package no.nav.arbeidsplassen.matchprofile.event

import java.time.OffsetDateTime

data class EventDTO(val id: String, val title: String, val organizationNumber: String, val organizationName: String, val startTime: OffsetDateTime,
               val endTime: OffsetDateTime, val description: String, val published: Boolean, val promoted: Boolean)


