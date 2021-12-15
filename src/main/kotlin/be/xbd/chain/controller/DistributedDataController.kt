package be.xbd.chain.controller

import be.xbd.chain.ChainApplication
import be.xbd.chain.ChainApplication.Companion.BLOCKCHAIN
import be.xbd.chain.ChainApplication.Companion.SERVER_SET
import be.xbd.chain.domain.Blockchain
import be.xbd.chain.service.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class DistributedDataController {

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
        return mergeBlockchain(SERVER_SET, BLOCKCHAIN)
    }
}