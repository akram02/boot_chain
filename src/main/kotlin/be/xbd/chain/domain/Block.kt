package be.xbd.chain.domain

import be.xbd.chain.toSha256
import java.time.Instant
import java.util.*

class Block {
    lateinit var  timestamp: Instant
    lateinit var  lastHash: String
    lateinit var  hash: String
    lateinit var  data: Any
    lateinit var uuid: String

    constructor()

    constructor(timestamp: Instant, lastHash: String, hash: String, data: Any, uuid: String) {
        this.timestamp = timestamp
        this.lastHash = lastHash
        this.hash = hash
        this.data = data
        this.uuid = uuid
    }


    fun mineBlock(lastBlock: Block, data: Any): Block {
        return new(Instant.now(), lastBlock.hash, data, UUID.randomUUID().toString())
    }

    fun blockHash(block: Block): String {
        return getHash(block)
    }

    fun genesis(): Block {
        val now = Instant.ofEpochMilli(1_599_909_623_805_627)
        return new(now, "-", "genesis data", "6fc316a1-b89f-4feb-9044-0288246ad738")
    }

    fun new(timestamp: Instant, lastHash: String, data: Any, uuid: String): Block {
        addTimestamp(timestamp)
        addLastHash(lastHash)
        addData(data)
        addUuid(uuid)
        addHash()
        return this
    }

    private fun addHash() {
        hash = getHash(this)
    }

    private fun getHash(block: Block) = "${block.timestamp.toEpochMilli()}:${block.lastHash}:${block.uuid}:${block.data}".toSha256()

    private fun addData(data: Any) {
        this.data = data
    }

    private fun addUuid(uuid: String) {
        this.uuid = uuid
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

        return timestamp == other.timestamp && lastHash == other.lastHash && hash == other.hash && data == other.data && uuid == other.uuid
    }

    override fun hashCode(): Int {
        var result = timestamp.hashCode()
        result = 31 * result + lastHash.hashCode()
        result = 31 * result + hash.hashCode()
        result = 31 * result + data.hashCode()
        result = 31 * result + uuid.hashCode()
        return result
    }
}