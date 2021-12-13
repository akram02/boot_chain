package be.xbd.chain

import be.xbd.chain.domain.Blockchain
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.env.Environment
import java.net.InetAddress




@SpringBootApplication
class ChainApplication(environment: Environment) {

	init {
	    BLOCKCHAIN = Blockchain().new()
		cleanServerSet(environment)
	}

	companion object {
		lateinit var BLOCKCHAIN: Blockchain
		var SERVER_SET: HashSet<String> = HashSet()

		fun cleanServerSet(environment: Environment) {
			val port = environment.getProperty("server.port")
			SERVER_SET.clear()
			SERVER_SET.add("localhost:$port")
		}
	}
}

fun main(args: Array<String>) {
	runApplication<ChainApplication>(*args)
}
