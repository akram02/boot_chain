package be.xbd.chain.controller

import be.xbd.chain.ChainApplication
import be.xbd.chain.domain.Block
import be.xbd.chain.service.addBlock
import be.xbd.chain.service.allBlock
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class BlockController {

    @GetMapping("/all-data")
    fun getAllBlock(): Set<Block> {
        return allBlock(ChainApplication.BLOCKCHAIN)
    }

    @GetMapping("/add-data")
    fun addData(@RequestParam data: String): Block {
        return addBlock(ChainApplication.BLOCKCHAIN, data)
    }
}