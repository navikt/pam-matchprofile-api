package no.nav.arbeidsplassen.matchprofile

import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import jakarta.inject.Singleton
import org.apache.http.HttpHost
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.client.CredentialsProvider
import org.apache.http.impl.client.BasicCredentialsProvider
import org.opensearch.client.RestClient
import org.opensearch.client.RestHighLevelClient
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Factory
class OpenSearchConfig(@Value("\${OPENSEARCH_USER:admin}") private val user: String,
                       @Value("\${OPENSEARCH_PASSWORD:admin}") private val password: String,
                       @Value("\${OPENSEARCH_URL:https://localhost:9200}") private val url: String) {

    @Singleton
    fun buildOpenSearchClient(): RestHighLevelClient {
        val credentialsProvider: CredentialsProvider = BasicCredentialsProvider()
        credentialsProvider.setCredentials(
            AuthScope.ANY,
            UsernamePasswordCredentials(user, password)
        )
        val builder = RestClient.builder(HttpHost.create(url))
            .setHttpClientConfigCallback {
                    httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                if ("https://localhost:9200" == url) {
                    httpClientBuilder.setSSLHostnameVerifier { _,_ -> true }
                    val context = SSLContext.getInstance("SSL")
                    context.init(null, arrayOf(object : X509TrustManager {
                        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                        }

                        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate>? {
                            return null
                        }

                    }), SecureRandom())
                    httpClientBuilder.setSSLContext(context)
                }
                httpClientBuilder
            }
        return RestHighLevelClient(builder)
    }
}