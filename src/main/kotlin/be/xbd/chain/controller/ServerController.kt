package be.xbd.chain.controller

import be.xbd.chain.ChainApplication.Companion.SERVER_SET
import be.xbd.chain.ChainApplication.Companion.cleanServerSet
import org.springframework.core.env.Environment
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate

@RestController
class ServerController(val environment: Environment) {

    @GetMapping("/clean-server")
    fun cleanServer(): Boolean {
        cleanServerSet(environment)
        return true
    }

    @GetMapping("/add-server")
    fun addServer(@RequestParam server: String): Set<String> {
        SERVER_SET.add(server)
        return SERVER_SET
    }

    @GetMapping("/all-server")
    fun allServer(): Set<String> {
        return SERVER_SET
    }

    @PostMapping("/add-all-server")
    fun addAllServer(@RequestBody remoteServerSet: Set<String>): Set<String> {
        SERVER_SET.addAll(remoteServerSet)
        return SERVER_SET
    }

    @GetMapping("/merge-server")
    fun mergeServer(): Set<String> {
        collectAndMerge(HashSet(SERVER_SET))
        return SERVER_SET
    }

    private fun collectAndMerge(remoteServerSet: HashSet<String>) {
        val restTemplate = RestTemplate()

        for (server in SERVER_SET) {
            val remoteServer: ResponseEntity<Collection<*>> = restTemplate.postForEntity("http://$server/add-all-server", remoteServerSet, Collection::class.java)
            if (remoteServer.body != null) {
                remoteServerSet.addAll(remoteServer.body as Collection<String>)
            }
        }
        if (remoteServerSet != SERVER_SET) {
            SERVER_SET.addAll(remoteServerSet)
            collectAndMerge(HashSet(SERVER_SET))
        }
    }

}