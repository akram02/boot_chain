package be.xbd.chain.service

import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import java.net.NetworkInterface

fun cleanServerSet(serverSet: HashSet<String>, port: String) {
    val interfaceEnumeration = NetworkInterface.getNetworkInterfaces()
    serverSet.clear()
    while (interfaceEnumeration.hasMoreElements()) {
        val networkInterface = interfaceEnumeration.nextElement()
        val inetEnumeration = networkInterface.inetAddresses
        while (inetEnumeration.hasMoreElements()) {
            val inetAddress = inetEnumeration.nextElement()
            val host: String? = inetAddress.hostAddress
            if (host != null && host.length<=15 && host.length>9)
                serverSet.add("$host:$port")
        }
    }
    println(serverSet)
}

fun collectAndMergeServer(remoteServerSet: HashSet<String>, localServerSet: HashSet<String>): HashSet<String> {
    val restTemplate = RestTemplate()

    for (server in localServerSet) {
        val remoteServer: ResponseEntity<Collection<*>> = restTemplate.postForEntity("http://$server/add-all-server", remoteServerSet, Collection::class.java)
        if (remoteServer.body != null) {
            remoteServerSet.addAll(remoteServer.body as Collection<String>)
        }
    }
    if (remoteServerSet != localServerSet) {
        localServerSet.addAll(remoteServerSet)
        collectAndMergeServer(HashSet(localServerSet), localServerSet)
    }

    return localServerSet
}
