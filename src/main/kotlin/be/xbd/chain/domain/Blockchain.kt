package be.xbd.chain.domain

data class Blockchain(
    var chain: ArrayList<Block> = ArrayList()
) {

    fun new(): Blockchain {
        return this.addGenesis()
    }

    fun addBlock(data: Any): Blockchain {
        val lastBlock = chain[chain.size-1]
        chain.add(Block().mineBlock(lastBlock, data))
        return this
    }

    private fun addGenesis(): Blockchain {
        this.chain.add(Block().genesis())
        return this
    }

    fun validChain(): Boolean {
        val chain = chain
        for(i in 1 until chain.size) {
            if (validLastHash(chain[i-1], chain[i]) && validBlockHash(chain[i])) {
                continue
            }
            else {
                return false
            }
        }
        return true
    }

    private fun validBlockHash(block: Block): Boolean {
        return block.hash == Block().blockHash(block)
    }

    private fun validLastHash(previousBlock: Block, currentBlock: Block): Boolean {
        return currentBlock.lastHash == previousBlock.hash
    }
}