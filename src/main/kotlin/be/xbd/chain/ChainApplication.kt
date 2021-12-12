package be.xbd.chain

import be.xbd.chain.domain.Blockchain
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ChainApplication {
	init {
	    BLOCKCHAIN = Blockchain().new()
	}
	companion object {
		lateinit var BLOCKCHAIN: Blockchain
	}
}

fun main(args: Array<String>) {
	runApplication<ChainApplication>(*args)
}
