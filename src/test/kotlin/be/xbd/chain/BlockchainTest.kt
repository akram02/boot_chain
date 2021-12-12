package be.xbd.chain

import org.junit.Before
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.Instant

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BlockchainTest {
    lateinit var blockchain: Blockchain

    @BeforeAll
    fun setup() {
        initializeBlockchain()
    }

    @BeforeEach
    fun clear() {
        blockchain = Blockchain().new()
    }

    private fun initializeBlockchain() {
        blockchain = Blockchain().new()
    }

    @Test
    fun `should start with the genesis block`() {
        val genesis = blockchain.chain[0]

        val block = Block(
            data = "genesis data",
            timestamp = Instant.ofEpochMilli(1_599_909_623_805_627),
            lastHash = "-",
            hash = "15cf4c2d49119d3250acf6024a63e7601e9686caf8bdc568d1940afc228e9a0e"
        )

        assertTrue(block == genesis)
    }

    @Test
    fun `adds a new block`() {
        val data = "foo"
        val newBlockchain = blockchain.addBlock(data)
        val block = newBlockchain.chain[newBlockchain.chain.size - 1]
        assertEquals(block.data, data)
    }

    @Test
    fun `validate a chain`() {
        blockchain.addBlock("some-block-data")
        assertTrue(blockchain.validChain())
    }

    @Test
    fun `when we temper data in existing chain`() {
        blockchain = blockchain
            .addBlock("blockchain-data-block-1")
            .addBlock("blockchain-data-block-2")
            .addBlock("blockchain-data-block-3")
        assertTrue(blockchain.validChain())

        val index = 2
        val temperedBlock = blockchain.chain[index]
        temperedBlock.data = "tempered_data"
        blockchain.chain[index]=temperedBlock

        assertFalse(blockchain.validChain())
    }

    @Test
    fun `when we temper hash in existing chain`() {
        blockchain = blockchain
            .addBlock("blockchain-data-block-1")
            .addBlock("blockchain-data-block-2")
            .addBlock("blockchain-data-block-3")
        assertTrue(blockchain.validChain())

        val index = 2
        val temperedBlock = blockchain.chain[index]
        temperedBlock.hash = "tempered_hash"
        blockchain.chain[index]=temperedBlock

        assertFalse(blockchain.validChain())
    }
}