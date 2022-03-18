package no.nav.arbeidsplassen.matchprofile.job

import jakarta.inject.Singleton

@Singleton
class KafkaStateRegistry {

    private val stateRegistry = hashMapOf<String, KafkaState>()

    fun setConsumerState(consumer: String, state: KafkaState) {
        stateRegistry[consumer] = state
    }

    fun hasError(): Boolean = stateRegistry.isNotEmpty() && stateRegistry.values.contains(KafkaState.ERROR)
}

enum class KafkaState {
    RUNNING, PAUSED, ERROR
}
