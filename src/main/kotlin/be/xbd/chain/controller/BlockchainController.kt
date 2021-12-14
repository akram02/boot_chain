package be.xbd.chain.controller

import be.xbd.chain.ChainApplication.Companion.BLOCKCHAIN
import be.xbd.chain.domain.Block
import be.xbd.chain.service.addBlock
import be.xbd.chain.service.allBlock
import be.xbd.chain.service.validChain
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class BlockchainController {

    @GetMapping("/all-data")
    fun getAllBlock(): Set<Block> {
        return allBlock(BLOCKCHAIN)
    }

    @GetMapping("/add-data")
    fun addData(@RequestParam data: String): Block {
        return addBlock(BLOCKCHAIN, data)
    }

    @GetMapping("/validate")
    fun validateChain() : Boolean {
        return validChain(BLOCKCHAIN)
    }
}