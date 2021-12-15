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
import org.springframework.web.client.RestTemplate

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
        if (!validChain(remoteBlockchain)) return false
        if (!validChain(BLOCKCHAIN)) return false
        if (!isSameNonNullValue(remoteBlockchain, BLOCKCHAIN)) return false
        addBlockchain(from = remoteBlockchain, to = BLOCKCHAIN)
        return true
    }

    @GetMapping("/merge-blockchain")
    fun mergeBlockchain(): Boolean {
        val restTemplate = RestTemplate()
        for (server in SERVER_SET) {
            val remoteBlockchain = restTemplate.getForObject("http://$server/blockchain", Blockchain::class.java)
            if (remoteBlockchain==null) continue
            if (!validChain(remoteBlockchain)) continue
            if (!isSameNonNullValue(remoteBlockchain, BLOCKCHAIN)) continue
            addBlockchain(from = remoteBlockchain, to = BLOCKCHAIN)
        }

        SERVER_SET.forEach { server ->
            restTemplate.postForObject("http://$server/add-blockchain", BLOCKCHAIN, Boolean::class.java)
        }

        return true
    }
}