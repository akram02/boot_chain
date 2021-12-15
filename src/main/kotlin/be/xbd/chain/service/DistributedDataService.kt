package be.xbd.chain.service

import be.xbd.chain.domain.Blockchain

fun isSameNonNullValue(firstBlockchain: Blockchain, secondBlockchain: Blockchain): Boolean {
    val firstBlockSet = allBlock(firstBlockchain)
    firstBlockSet.forEach { firstBlock ->
        val uuid = firstBlock.uuid
        val secondBlock = findBlockByUuid(secondBlockchain, uuid.replace("-", ""))
        if (secondBlock != null) {
            if (secondBlock != firstBlock) return false
        }
    }
    return true
}


fun addBlockchain(from: Blockchain, to: Blockchain) {
    val fromBlockSet = allBlock(from)
    fromBlockSet.forEach { fromBlock ->
        val uuid = fromBlock.uuid
        val toBlock = findBlockByUuid(to, uuid.replace("-", ""))
        if (toBlock == null) {
            addToBlockchain(to, fromBlock)
        }
    }
}