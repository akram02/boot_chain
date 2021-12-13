package be.xbd.chain

import be.xbd.chain.ChainApplication.Companion.BLOCKCHAIN
import be.xbd.chain.domain.Block
import be.xbd.chain.domain.Blockchain
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
        BLOCKCHAIN = blockchain
    }

    private fun initializeBlockchain() {
        blockchain = Blockchain().new()
        BLOCKCHAIN = blockchain
    }

    @Test
    fun `should start with the genesis block`() {
        val genesis = blockchain.block

        val block = Block(
            data = "genesis data",
            timestamp = Instant.ofEpochMilli(1_599_909_623_805_627),
            lastHash = "-",
            hash = "b4825e09608df0ab92468f26b8cdaa1f68f5791b64221624489ee5a9fbb3a1ca",
            uuid = "8b02fea6-aa9d-4e53-9ba1-2882a9c3579e",
            previousUuid = ""
        )

        assertTrue(block == genesis)
    }

    @Test
    fun `adds a new block`() {
        val data = "foo"
        val block = blockchain.addBlock(data)
        assertEquals(block.data, data)
        val allBlock = blockchain.allBlock()
        assertTrue(allBlock.size==2)
    }

    @Test
    fun `validate a chain`() {
        blockchain.addBlock("some-block-data")
        assertTrue(blockchain.validChain())
    }

    @Test
    fun `when we temper data in existing chain`() {
        val firstBlock = blockchain.addBlock("blockchain-data-block-1")
        val secondBlock = blockchain.addBlock("blockchain-data-block-2", firstBlock)
        val thirdBlock = blockchain.addBlock("blockchain-data-block-3", secondBlock)
        assertTrue(blockchain.validChain())

        secondBlock.data = "tempered_data"

        blockchain.addToBlockchain(blockchain, secondBlock)

        assertFalse(blockchain.validChain())
    }

    @Test
    fun `when we temper hash in existing chain`() {
        val firstBlock = blockchain.addBlock("blockchain-data-block-1")
        val secondBlock = blockchain.addBlock("blockchain-data-block-2", firstBlock)
        val thirdBlock = blockchain.addBlock("blockchain-data-block-3", secondBlock)
        assertTrue(blockchain.validChain())

        secondBlock.hash = "tempered_hash"

        blockchain.addToBlockchain(blockchain, secondBlock)

        assertFalse(blockchain.validChain())
    }
}