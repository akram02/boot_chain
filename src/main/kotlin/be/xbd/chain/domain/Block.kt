package be.xbd.chain.domain

import be.xbd.chain.toSha256
import java.time.Instant

class Block {
    lateinit var  timestamp: Instant
    lateinit var  lastHash: String
    lateinit var  hash: String
    lateinit var  data: Any

    constructor()

    constructor(timestamp: Instant, lastHash: String, hash: String, data: Any) {
        this.timestamp = timestamp
        this.lastHash = lastHash
        this.hash = hash
        this.data = data
    }


    fun mineBlock(lastBlock: Block, data: Any): Block {
        return new(Instant.now(), lastBlock.hash, data)
    }

    fun blockHash(block: Block): String {
        return getHash(block)
    }

    fun genesis(): Block {
        val now = Instant.ofEpochMilli(1_599_909_623_805_627)
        return new(now, "-", "genesis data")
    }

    fun new(timestamp: Instant, lastHash: String, data: Any): Block {
        addTimestamp(timestamp)
        addLastHash(lastHash)
        addData(data)
        addHash()
        return this
    }

    private fun addHash() {
        hash = getHash(this)
    }

    private fun getHash(block: Block) = "${block.timestamp.toEpochMilli()}:${block.lastHash}:${block.data}".toSha256()

    private fun addData(data: Any) {
        this.data = data
    }

    private fun addLastHash(lastHash: String) {
        this.lastHash = lastHash
    }

    private fun addTimestamp(timestamp: Instant) {
        this.timestamp = timestamp
    }

    override fun equals(other: Any?): Boolean {
        if (other==null) return false
        other as Block

        return timestamp == other.timestamp && lastHash == other.lastHash && hash == other.hash && data == other.data
    }

    override fun hashCode(): Int {
        var result = timestamp.hashCode()
        result = 31 * result + lastHash.hashCode()
        result = 31 * result + hash.hashCode()
        result = 31 * result + data.hashCode()
        return result
    }
}