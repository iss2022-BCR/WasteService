package wasteservice

enum class TransportTrolleyState {
    NONE, HOME, MOVING, PICKUP, DUMP, STOPPED;

    companion object {
        fun parseFromMessage(s: String): TransportTrolleyState {
            for (tts in TransportTrolleyState.values()) {
                if (s.contains(tts.name))
                    return tts
            }

            return NONE
        }
    }
}

/*
// Test
fun main() {
    println(TransportTrolleyState.parseFromMessage("transportlalal(asdad)").toString())
}
*/