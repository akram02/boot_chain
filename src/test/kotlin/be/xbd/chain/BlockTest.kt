package be.xbd.chain

import be.xbd.chain.domain.Block
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
            hash = "b4825e09608df0ab92468f26b8cdaa1f68f5791b64221624489ee5a9fbb3a1ca",
            uuid = "8b02fea6-aa9d-4e53-9ba1-2882a9c3579e",
            previousUuid = ""
        )

        val genesis = Block().genesis()

        assertTrue(block==genesis)
    }

    @Test
    fun `mine block return new block`() {
        val genesis = Block().genesis()

        val newBlock = Block()
        newBlock.data = "this is mined data"
        newBlock.lastHash = genesis.hash

        val mineBlock = Block().mineBlock(genesis, "this is mined data")

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
        block.timestamp = timestamp
        block.lastHash = lastHash
        block.data = data
        block.uuid = uuid
        block.previousUuid = previousUuid
        block.hash = Block().blockHash(block)

        assertTrue(block == Block().new(timestamp, lastHash, data, uuid, previousUuid))
    }
}