package be.xbd.chain

import be.xbd.chain.ChainApplication.Companion.BLOCKCHAIN
import be.xbd.chain.domain.Block
import be.xbd.chain.domain.Blockchain
import be.xbd.chain.service.*
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
        blockchain = newBlockchainWithGenesisBlock()
        BLOCKCHAIN = blockchain
    }

    private fun initializeBlockchain() {
        blockchain = newBlockchainWithGenesisBlock()
        BLOCKCHAIN = blockchain
    }

    @Test
    fun `should start with the genesis block`() {
        val genesis = blockchain.block

        val block = Block(
            data = "genesis data",
            timestamp = Instant.ofEpochMilli(1_599_909_623_805_627),
            lastHash = "-",
            hash = "e863d50b9727417be301dc8c3f615e3e6a31a7bb7f3eaafc4bb2f948b92841bc",
            uuid = "8b02fea6-aa9d-4e53-9ba1-2882a9c3579e",
            previousUuid = ""
        )

        assertTrue(block == genesis)
    }

    @Test
    fun `adds a new block`() {
        val data = "foo"
        val block = addDataToBlockchain(blockchain, data)
        assertEquals(block.data, data)
        val allBlock = getBlockSetFromBlockchain(blockchain)
        assertTrue(allBlock.size==2)
    }

    @Test
    fun `validate a chain`() {
        addDataToBlockchain(blockchain, "some-block-data")
        assertTrue(validChain(blockchain))
    }

    @Test
    fun `when we temper data in existing chain`() {
        val firstBlock = addDataToBlockchain(blockchain, "blockchain-data-block-1")
        val secondBlock = addDataToBlockchain(blockchain, "blockchain-data-block-2", firstBlock)
        addDataToBlockchain(blockchain, "blockchain-data-block-3", secondBlock)
        assertTrue(validChain(blockchain))

        secondBlock.data = "tempered_data"

        addBlockToBlockchain(blockchain, secondBlock)

        assertFalse(validChain(blockchain))
    }

    @Test
    fun `when we temper hash in existing chain`() {
        val firstBlock = addDataToBlockchain(blockchain, "blockchain-data-block-1")
        val secondBlock = addDataToBlockchain(blockchain, "blockchain-data-block-2", firstBlock)
        addDataToBlockchain(blockchain, "blockchain-data-block-3", secondBlock)
        assertTrue(validChain(blockchain))

        secondBlock.hash = "tempered_hash"

        addBlockToBlockchain(blockchain, secondBlock)

        assertFalse(validChain(blockchain))
    }
}