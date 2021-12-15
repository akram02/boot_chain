package be.xbd.chain.domain

import be.xbd.chain.service.addToBlockchain
import be.xbd.chain.service.genesis

class Blockchain{
    var block: Block? = null
    var blockchainArray = arrayOfNulls<Blockchain>(36)

    fun new(): Blockchain {
        return this.addGenesis()
    }

    private fun addGenesis(): Blockchain {
        block = genesis()
        addToBlockchain(this, block!!)
        return this
    }
}