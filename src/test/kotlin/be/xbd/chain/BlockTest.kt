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
            hash = "89834407fba9135994bf09f5d15f73aed05efc9f4d0836337c7a53d54ee6e23f",
            uuid = "6fc316a1-b89f-4feb-9044-0288246ad738"
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
        val timestamp = Instant.now();
        val lastHash = "random_hash"
        val data = "this is new block data"
        val uuid = UUID.randomUUID().toString()
        val block = Block()
        block.timestamp = timestamp
        block.lastHash = lastHash
        block.data = data
        block.uuid = uuid
        block.hash = Block().blockHash(block)

        assertTrue(block == Block().new(timestamp, lastHash, data, uuid))
    }
}