package be.xbd.chain

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.Instant

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BlockTest {

    @Test
    fun `genesis is valid`() {
        val block = Block(
            data = "genesis data",
            timestamp = Instant.ofEpochMilli(1_599_909_623_805_627),
            lastHash = "-",
            hash = "15cf4c2d49119d3250acf6024a63e7601e9686caf8bdc568d1940afc228e9a0e"
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
        val block = Block()
        block.timestamp = timestamp
        block.lastHash = lastHash
        block.data = data
        block.hash = Block().blockHash(block)

        assertTrue(block == Block().new(timestamp, lastHash, data))
    }
}