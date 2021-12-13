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
    lateinit var previousUuid: String

    constructor()

    constructor(timestamp: Instant, lastHash: String, hash: String, data: Any, uuid: String, previousUuid: String) {
        this.timestamp = timestamp
        this.lastHash = lastHash
        this.hash = hash
        this.data = data
        this.uuid = uuid
        this.previousUuid = previousUuid
    }


    fun mineBlock(lastBlock: Block, data: Any): Block {
        return new(Instant.now(), lastBlock.hash, data, UUID.randomUUID().toString(), lastBlock.uuid)
    }

    fun blockHash(block: Block) = "${block.timestamp.toEpochMilli()}:${block.lastHash}:${block.uuid}:${block.data}".toSha256()

    fun genesis(): Block {
        val now = Instant.ofEpochMilli(1_599_909_623_805_627)
        return new(now, "-", "genesis data", "8b02fea6-aa9d-4e53-9ba1-2882a9c3579e", "")
    }

    fun new(timestamp: Instant, lastHash: String, data: Any, uuid: String, previousUuid: String): Block {
        this.timestamp = timestamp
        this.lastHash = lastHash
        this.data = data
        this.uuid = uuid
        this.previousUuid = previousUuid
        hash = blockHash(this)
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (other==null) return false
        other as Block

        return timestamp == other.timestamp && lastHash == other.lastHash && hash == other.hash && data == other.data && uuid == other.uuid && previousUuid == other.previousUuid
    }

    override fun hashCode(): Int {
        var result = timestamp.hashCode()
        result = 31 * result + lastHash.hashCode()
        result = 31 * result + hash.hashCode()
        result = 31 * result + data.hashCode()
        result = 31 * result + uuid.hashCode()
        result = 31 * result + previousUuid.hashCode()
        return result
    }
}