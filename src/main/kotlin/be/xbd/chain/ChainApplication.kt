package be.xbd.chain

import be.xbd.chain.domain.Blockchain
import be.xbd.chain.service.newBlockchainWithGenesisBlock
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
		lateinit var PORT: String
		var SERVER_SET: HashSet<String> = HashSet()

		fun cleanServerSet(environment: Environment) {
			PORT = environment.getProperty("server.port")!!
			be.xbd.chain.service.cleanServerSet(SERVER_SET, PORT)
		}

		fun cleanData() {
			BLOCKCHAIN = newBlockchainWithGenesisBlock()
		}
	}
}

fun main(args: Array<String>) {
	runApplication<ChainApplication>(*args)
}
