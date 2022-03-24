package no.nav.arbeidsplassen.matchprofile

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.context.annotation.Value
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import org.slf4j.LoggerFactory
import java.net.InetAddress
import java.time.LocalDateTime
import jakarta.inject.Singleton

@Singleton
class LeaderElection(@Client("LeaderElect") val client: HttpClient,
                     private val objectMapper: ObjectMapper) {

    private val hostname = InetAddress.getLocalHost().hostName
    private var leader =  InetAddress.getLocalHost().hostName;
    private var lastCalled = LocalDateTime.MIN

    init {
        LOG.info("leader election initialized this hostname is $hostname")
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(LeaderElection::class.java)
    }

    fun isLeader(): Boolean {
        return hostname == getLeader();
    }

    private fun getLeader(): String {
        return leader
    }
}

data class Elector(val name: String)
