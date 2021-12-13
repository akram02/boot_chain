package be.xbd.chain.domain

import be.xbd.chain.ChainApplication.Companion.BLOCKCHAIN

class Blockchain{
    var block: Block? = null
    var blockchainArray = arrayOfNulls<Blockchain>(36)




    fun new(): Blockchain {
        return this.addGenesis()
    }

    fun addBlock(data: Any, lastBlock: Block? = null): Block {
        var previousBlock: Block? = lastBlock
        if (previousBlock==null) {
            previousBlock = BLOCKCHAIN.block
        }
        val currentBlock = Block().mineBlock(previousBlock!!, data)
        addToBlockchain(BLOCKCHAIN, currentBlock)
        return currentBlock
    }

    fun addToBlockchain(blockchain: Blockchain, block: Block) {
        addToBlockchain(blockchain, block, block.uuid.replace("-", ""), 0)
    }

    private fun addToBlockchain(blockchain: Blockchain, block: Block, uuid: String, index: Int) {
        if (index==uuid.length) {
            blockchain.block = block
        }
        else {
            val chainIndex: Int = findChainArrayIndex(uuid, index)
            var chain =  blockchain.blockchainArray[chainIndex]
            if (chain == null) {
                chain = Blockchain()
                blockchain.blockchainArray[chainIndex] = chain
            }
            addToBlockchain(chain, block, uuid, index+1)
        }
    }

    private fun findBlockByUuid(blockchain: Blockchain, uuid: String, index: Int): Block? {
        return if (index==uuid.length) {
            blockchain.block
        } else {
            val chainIndex = findChainArrayIndex(uuid, index)
            val chain = blockchain.blockchainArray[chainIndex]
            if (chain==null) null
            else {
                findBlockByUuid(chain, uuid, index+1)
            }
        }
    }

    private fun findChainArrayIndex(uuid: String, index: Int): Int {
        val char = uuid[index]
        var chainIndex: Int
        if (char.isDigit()) {
            chainIndex = char.code - '0'.code
        } else {
            chainIndex = char.code - 'a'.code
            chainIndex += 10
        }
        return chainIndex
    }

    private fun addGenesis(): Blockchain {
        block = Block().genesis()
        addToBlockchain(this, block!!)
        return this
    }

    fun validChain(): Boolean {
        return validChain(BLOCKCHAIN, true)
    }

    fun allBlock(): HashSet<Block> {
        val blockSet = HashSet<Block>()
        BLOCKCHAIN.allBlock(BLOCKCHAIN, blockSet)
        return blockSet
    }

    fun allBlock(blockchain: Blockchain, blockSet: HashSet<Block>) {
        for (chain in blockchain.blockchainArray) {
            if (chain?.block != null) {
                blockSet.add(chain.block!!)
            }
            else if (chain!=null){
                allBlock(chain, blockSet)
            }
        }
    }

    private fun validChain(blockchain: Blockchain, start: Boolean = false): Boolean {
        if (blockchain.block!=null && !start) {
            val previousUuid = blockchain.block!!.previousUuid
            if (previousUuid == "") {
                return blockchain.block == BLOCKCHAIN.block
            }

            val previousBlock = findBlockByUuid(BLOCKCHAIN, previousUuid.replace("-", ""), 0)

            if (previousBlock == null) {
                return false
            }

            val currentBlock: Block = blockchain.block!!

            return if (validLastHash(previousBlock, currentBlock) && validBlockHash(currentBlock)) {
                return true
            } else {
                false
            }
        }

        for (chain in blockchain.blockchainArray) {
            if (chain != null) {
                if (!validChain(chain)) return false
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