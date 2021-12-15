package be.xbd.chain.service

import be.xbd.chain.domain.Block
import be.xbd.chain.domain.Blockchain
import org.springframework.web.client.RestTemplate

fun allBlock(blockchain: Blockchain): HashSet<Block> {
    val blockSet = HashSet<Block>()
    allBlock(blockchain, blockSet)
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

fun addBlock(blockchain: Blockchain, data: Any, lastBlock: Block? = null): Block {
    var previousBlock: Block? = lastBlock
    if (previousBlock==null) {
        previousBlock = blockchain.block
    }
    val currentBlock = mineBlock(previousBlock!!, data)
    addToBlockchain(blockchain, currentBlock)
    return currentBlock
}


fun addRemoteBlockchain(remote: Blockchain, local: Blockchain): Boolean {
    if (!validChain(remote)) return false
    if (!validChain(local)) return false
    if (!isSameNonNullValue(remote, local)) return false
    addBlockchain(from = remote, to = local)
    return true
}


fun mergeBlockchain(
    localServerSet: HashSet<String>,
    localBlockchain: Blockchain
): Boolean {
    val restTemplate = RestTemplate()
    for (server in localServerSet) {
        val remoteBlockchain = restTemplate.getForObject("http://$server/blockchain", Blockchain::class.java)
        if (remoteBlockchain == null) continue
        if (!validChain(remoteBlockchain)) continue
        if (!isSameNonNullValue(remoteBlockchain, localBlockchain)) continue
        addBlockchain(from = remoteBlockchain, to = localBlockchain)
    }

    localServerSet.forEach { server ->
        restTemplate.postForObject("http://$server/add-blockchain", localBlockchain, Boolean::class.java)
    }

    return true
}

fun addToBlockchain(blockchain: Blockchain, block: Block) {
    addToBlockchain(blockchain, block, block.uuid.replace("-", ""), 0)
}

fun addToBlockchain(blockchain: Blockchain, block: Block, uuid: String, index: Int) {
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

fun findBlockByUuid(blockchain: Blockchain, uuid: String, index: Int = 0): Block? {
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

fun validChain(rootBlockchain: Blockchain): Boolean {
    return validChain(rootBlockchain, rootBlockchain, true)
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

private fun validChain(rootBlockchain: Blockchain, blockchain: Blockchain, start: Boolean = false): Boolean {
    if (blockchain.block!=null && !start) {
        val previousUuid = blockchain.block!!.previousUuid
        if (previousUuid == "") {
            return blockchain.block == rootBlockchain.block
        }

        val previousBlock = findBlockByUuid(rootBlockchain, previousUuid.replace("-", ""), 0)

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
            if (!validChain(rootBlockchain, chain)) return false
        }
    }
    return true
}

private fun validBlockHash(block: Block): Boolean {
    return block.hash == blockHash(block)
}

private fun validLastHash(previousBlock: Block, currentBlock: Block): Boolean {
    return currentBlock.lastHash == previousBlock.hash
}
