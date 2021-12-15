package be.xbd.chain.controller

import be.xbd.chain.ChainApplication
import be.xbd.chain.domain.Block
import be.xbd.chain.service.addDataToBlockchain
import be.xbd.chain.service.getBlockSetFromBlockchain
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class BlockController {

    @GetMapping("/all-data")
    fun getAllBlock(): Set<Block> {
        return getBlockSetFromBlockchain(ChainApplication.BLOCKCHAIN)
    }

    @GetMapping("/add-data")
    fun addData(@RequestParam data: String): Block {
        return addDataToBlockchain(ChainApplication.BLOCKCHAIN, data)
    }
}