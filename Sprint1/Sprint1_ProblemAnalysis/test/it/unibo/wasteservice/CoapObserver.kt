import alice.tuprologx.pj.model.Bool
import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapResponse
import unibo.comm22.coap.CoapConnection
import unibo.comm22.utils.ColorsOut
import unibo.comm22.utils.CommUtils
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Class that provides utility methods to interact with a COAP server.
 */
class CoapObserver : CoapHandler {

    class CoapTuple(val hostAddress: String, val resourcePath: String) {}

    private val contextsMap = HashMap<String, Pair<String, Int>>()
    private val actorsMap = HashMap<String, String>()
    private val activeConnections = HashMap<String, CoapConnection>()
    private val coapHistory: MutableList<String> = ArrayList()

    private val lock = ReentrantLock()
    private val condition = lock.newCondition()

    private fun createCoapTuple(actor: String): CoapTuple {
        val context = actorsMap[actor] ?: throw Exception("Error")
        val pair = contextsMap[context] ?: throw Exception("Error")

        println(actorsMap[actor] + "/" + actor)

        return CoapTuple("${pair.first}:${pair.second}", actorsMap[actor] + "/" + actor)
    }

    /**
     * Add the context to the contexts map.
     * @param name the name of the context (map key).
     * @param socketAddress a Pair object which represents a connection (hostname, port).
     */
    fun addContext(name: String, socketAddress: Pair<String, Int>) {
        contextsMap[name] = socketAddress
    }

    /**
     * Add the actor to the actors map.
     * @param actor the name of the actor (map key).
     * @param context the context in which the actor is running.
     */
    fun addActor(actor: String, context: String) {
        actorsMap[actor] = context
    }

    /**
     * Create a COAP connection to the actor.
     * @param actor the actor to connect to.
     */
    fun createCoapConnection(actor: String) {
        ColorsOut.out("Creating connection to $actor Actor", ColorsOut.BLUE)
        val coapTuple = createCoapTuple(actor)
        val connection = CoapConnection(coapTuple.hostAddress, coapTuple.resourcePath)

        connection.observeResource(this)

        while (connection.request("") == "0") {
            ColorsOut.out("Wating for connection to $actor", ColorsOut.BLUE)
            CommUtils.delay(100)
        }

        activeConnections[actor] = connection
        ColorsOut.out("Created connection to $actor, $connection", ColorsOut.BLUE)
    }

    /**
     * Close a single COAP connection.
     * @param actor the actor whose connection is to be closed.
    */
    fun closeCoapConnection(actor: String) {
        val connection = activeConnections[actor] ?: throw Exception("Connection to $actor was already closed...")
        connection.close()
    }

    /**
     * Close all COAP connections.
    */
    fun closeAllCoapConnections() {
        for(connEntry in activeConnections) {
            ColorsOut.out("Closing connection to ${connEntry.key}...")
            connEntry.value.close()
        }

        ColorsOut.out("Closed all connections.")
    }

    /**
     * Obtain a history of the COAP resource messages.
     * @return a list of String objects representing each COAP message.
     */
    fun getCoapHistory(): List<String> {
        lock.withLock {
            return coapHistory
        }
    }

    /**
     * Check if the COAP history contains a specific resource message.
     * @param entry the message to be searched.
     * @return true if it contains the message, false otherwise.
     */
    fun checkIfHystoryContains(entry: String): Boolean {
        lock.withLock {
            return coapHistory.contains(entry)
        }
    }

    /**
     * Check if the COAP history contains a whole ordered list of resource messages.
     * @param list a list of messages.
     * @param strict specify if the list to be searched must have all the elements one after the other (strict == true),
     *      or they can be scattered throughout the list (default: strict == false).
     * @return true if it contains the message, false otherwise.
     */
    fun checkIfHystoryContainsOrdered(list: MutableList<String>, strict: Boolean = false): Boolean {
        if(strict) {
            var s1 = ""
            var s2 = ""

            for (e in list)
                s1 += e

            lock.withLock {
                for(e in coapHistory)
                    s2 += e
            }

            return s2.contains(s1)
        }
        else {
            lock.withLock {
                for(i in coapHistory.indices) {
                    val el = coapHistory[i]
                    if(list.contains(el))
                        if(list.indexOf(el) == 0)
                            list.remove(el)
                        else return false
                }
                if(list.isNotEmpty())
                    return false
                return true
            }
        }
    }

    /**
     * Wait until a specific resource message is added to the COAP history.
     * @param entry the message to be waited.
     * @param refreshRate the amount of time (in milliseconds) that passes between a search and the one after (default: 300ms).
     * @param timeout the amount of time (in millisecods) after which the search is interrupted.
     * @return true if the message was found, false otherwise.
     */
    fun waitForSpecificHistoryEntry(entry: String, refreshRate: Int = 300, timeout: Int = 1000): Boolean {
        ColorsOut.outappl("Waiting for $entry", ColorsOut.BLUE)
        var found = false

        var rRefreshRate = 300
        var timeoutCounter = 0
        var tTimeout = 0

        if(refreshRate > 300)
            rRefreshRate = refreshRate
        if(timeout > 0)
            tTimeout = timeout

        while (!found && timeoutCounter < tTimeout) {
            CommUtils.delay(rRefreshRate)
            if(tTimeout > 0)
                timeoutCounter += rRefreshRate

            for(e in getCoapHistory()) {
                if(e.contains(entry)) {
                    return true
                }
            }
        }

        return false
    }

    /**
     * Remove all the resource messages from the COAP history.
     */
    fun clearCoapHistory() {
        lock.withLock {
            coapHistory.clear()
        }
    }

    override fun onLoad(response: CoapResponse) {
        lock.withLock {
            ColorsOut.out(response.responseText, ColorsOut.ANSI_YELLOW)
            coapHistory.add(response.responseText)
            condition.signalAll()
        }
    }

    override fun onError() {
        ColorsOut.outerr("CoapObserver observe error!")
    }
}