package wasteservice

object testLed {
    @JvmStatic
    fun turnOnLed() {
        Runtime.getRuntime().exec("./ledBCR.py on")
    }

    @JvmStatic
    fun turnOffLed() {
        Runtime.getRuntime().exec("./ledBCR.py off")
    }
}