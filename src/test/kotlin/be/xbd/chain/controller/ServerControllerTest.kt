package be.xbd.chain.controller

import be.xbd.chain.availableServer
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*
import org.springframework.web.client.RestTemplate
import java.util.*
import kotlin.collections.ArrayList

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class ServerControllerTest {

    private lateinit var restTemplate: RestTemplate

    @BeforeAll
    fun setup() {
        restTemplate = RestTemplate()
    }

    @Test
    @Order(0)
    fun cleanServer() {
        val uuid = UUID.randomUUID().toString()
        println(uuid)
        availableServer.forEach {
            val result = restTemplate.getForObject("http://$it/clean-server", Boolean::class.java)
            assertNotNull(result)
            assertTrue(result!!)
        }
    }

    @Test
    @Order(1)
    fun addServer() {
        for (i in 0 until availableServer.size) {
            val currentServer = availableServer[i]

            val targetServer = if (i != availableServer.size - 1) {
                availableServer[i+1]
            } else {
                availableServer[0]
            }

            val set: Set<*>? = restTemplate.getForObject("http://$currentServer/add-server?server=$targetServer", Set::class.java)
            assertTrue(set!=null && set.isNotEmpty() && set.contains(targetServer))
        }
    }

    @Test
    @Order(2)
    fun allServer() {
        assertAllServerWithSize(2)
    }

    @Test
    @Order(3)
    fun addAllServerPost() {
    }

    @Test
    @Order(4)
    fun mergeServer0() {
        restTemplate.getForObject("http://localhost:8080/merge-server", Set::class.java)
        assertAllServerWithSize(availableServer.size)
    }


    private fun assertAllServerWithSize(size: Int) {
        availableServer.forEach {
            val set: Set<*>? = restTemplate.getForObject("http://$it/all-server", Set::class.java)
            assertTrue(set != null && set.size >= size)
        }
    }

}