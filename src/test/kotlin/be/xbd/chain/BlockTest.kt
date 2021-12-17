package be.xbd.chain

import be.xbd.chain.domain.Block
import be.xbd.chain.service.blockHash
import be.xbd.chain.service.genesisBlock
import be.xbd.chain.service.mineBlockFromLastBlockAndData
import be.xbd.chain.service.newBlock
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.Instant
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BlockTest {

    @Test
    fun `genesis is valid`() {
        val block = Block(
            data = "genesis data",
            timestamp = Instant.ofEpochMilli(1_599_909_623_805_627),
            lastHash = "-",
            hash = "e863d50b9727417be301dc8c3f615e3e6a31a7bb7f3eaafc4bb2f948b92841bc",
            uuid = "8b02fea6-aa9d-4e53-9ba1-2882a9c3579e",
            previousUuid = ""
        )

        val genesis = genesisBlock()

        assertTrue(block==genesis)
    }

    @Test
    fun `mine block return new block`() {
        val genesis = genesisBlock()

        val newBlock = Block()
        newBlock.data = "this is mined data"
        newBlock.lastHash = genesis.hash

        val mineBlock = mineBlockFromLastBlockAndData(genesis, "this is mined data")

        assertTrue(newBlock.data == mineBlock.data && newBlock.lastHash == mineBlock.lastHash)
    }

    @Test
    fun `new give a new block when we pass the parameters`() {
        val timestamp = Instant.now()
        val lastHash = "random_hash"
        val data = "this is new block data"
        val uuid = UUID.randomUUID().toString()
        val previousUuid = UUID.randomUUID().toString()
        val block = Block()
        block.timestamp = timestamp.toEpochMilli().toString()
        block.lastHash = lastHash
        block.data = data
        block.uuid = uuid
        block.previousUuid = previousUuid
        block.hash = blockHash(block)

        assertTrue(block == newBlock(timestamp, lastHash, data, uuid, previousUuid))
    }
}