package be.xbd.chain.controller

import be.xbd.chain.ChainApplication.Companion.BLOCKCHAIN
import be.xbd.chain.domain.Block
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class BlockchainController {

    @GetMapping("/all-data")
    fun getAllBlock(): Set<Block> {
        return BLOCKCHAIN.allBlock()
    }

    @GetMapping("/add-data")
    fun addData(@RequestParam data: String): Block {
        return BLOCKCHAIN.addBlock(data)
    }

    @GetMapping("/validate")
    fun validateChain() : Boolean {
        return BLOCKCHAIN.validChain()
    }
}