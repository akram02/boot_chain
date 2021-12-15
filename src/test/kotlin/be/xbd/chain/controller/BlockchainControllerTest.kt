package be.xbd.chain.controller

import be.xbd.chain.availableServer
import be.xbd.chain.domain.Block
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import org.junit.jupiter.api.*
import org.springframework.web.client.RestTemplate

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class BlockchainControllerTest {

    private lateinit var restTemplate: RestTemplate

    @BeforeAll
    fun setup() {
        restTemplate = RestTemplate()
    }

    @Test
    fun getAllBlock() {
    }

    @Test
    @Order(0)
    fun cleanBlockchain() {
        availableServer.forEach {
            val result = restTemplate.getForObject("http://$it/clean-blockchain", Boolean::class.java)
            assertNotNull(result)
            assertTrue(result!!)
        }
    }

    @Test
    @Order(1)
    fun addData() {
        availableServer.forEach {
            val block = restTemplate.getForObject("http://$it/add-data?data=$it", Block::class.java)
            assertNotNull(block)
        }
    }

    @Test
    @Order(2)
    fun mergeBlockchain() {
        availableServer.forEach {
            val result = restTemplate.getForObject("http://$it/merge-blockchain", Boolean::class.java)
            assertNotNull(result)
            assertTrue(result!!)
        }
    }
    @Test
    @Order(3)
    fun validateChain() {
        availableServer.forEach {
            val result = restTemplate.getForObject("http://$it/validate", Boolean::class.java)
            assertNotNull(result)
            assertTrue(result!!)
        }
    }
}