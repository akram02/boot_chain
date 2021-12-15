package be.xbd.chain.controller

import be.xbd.chain.ChainApplication
import be.xbd.chain.ChainApplication.Companion.BLOCKCHAIN
import be.xbd.chain.domain.Block
import be.xbd.chain.domain.Blockchain
import be.xbd.chain.service.addBlock
import be.xbd.chain.service.addRemoteBlockchain
import be.xbd.chain.service.allBlock
import be.xbd.chain.service.validChain
import org.springframework.web.bind.annotation.*

@RestController
class BlockchainController {

    @GetMapping("/validate")
    fun validateChain() : Boolean {
        return validChain(BLOCKCHAIN)
    }

    @GetMapping("/clean-blockchain")
    fun cleanData(): Boolean {
        ChainApplication.cleanData()
        return true
    }

    @GetMapping("/blockchain")
    fun blockchain(): Blockchain {
        return BLOCKCHAIN
    }

    @PostMapping("/add-blockchain")
    fun addBlockchain(@RequestBody remoteBlockchain: Blockchain): Boolean {
        return addRemoteBlockchain(remoteBlockchain, BLOCKCHAIN)
    }


    @GetMapping("/merge-blockchain")
    fun mergeBlockchain(): Boolean {
        return be.xbd.chain.service.mergeBlockchain(ChainApplication.SERVER_SET, BLOCKCHAIN)
    }
}