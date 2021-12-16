package be.xbd.chain

import be.xbd.chain.domain.Blockchain
import be.xbd.chain.service.newBlockchainWithGenesisBlock
import be.xbd.chain.config.host
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.env.Environment


@SpringBootApplication
class ChainApplication(environment: Environment) {

	init {
	    BLOCKCHAIN = newBlockchainWithGenesisBlock()
		cleanServerSet(environment)
	}

	companion object {
		lateinit var BLOCKCHAIN: Blockchain
		var SERVER_SET: HashSet<String> = HashSet()

		fun cleanServerSet(environment: Environment) {
			val port = environment.getProperty("server.port")
			SERVER_SET.clear()
			SERVER_SET.add("$host:$port")
		}

		fun cleanData() {
			BLOCKCHAIN = newBlockchainWithGenesisBlock()
		}
	}
}

fun main(args: Array<String>) {
	runApplication<ChainApplication>(*args)
}
