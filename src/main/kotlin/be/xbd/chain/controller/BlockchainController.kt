package be.xbd.chain.controller

import be.xbd.chain.ChainApplication.Companion.BLOCKCHAIN
import be.xbd.chain.domain.Block
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class BlockchainController {

    @GetMapping("/all-block")
    fun getAllBlock(): ArrayList<Block> {
        return BLOCKCHAIN.chain
    }

    @GetMapping("/add-data")
    fun addData(@RequestParam data: String): Block {
        BLOCKCHAIN.addBlock(data)
        return BLOCKCHAIN.chain[BLOCKCHAIN.chain.size-1]
    }

    @GetMapping("/validate")
    fun validateChain() : Boolean {
        return BLOCKCHAIN.validChain()
    }
}