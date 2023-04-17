package wasteservice.raspberry.led

import it.unibo.radarSystem22.domainBCR.DeviceFactory
import it.unibo.radarSystem22.domainBCR.interfaces.ILed
import it.unibo.radarSystem22.domainBCR.utils.DomainSystemConfig
import wasteservice.TransportTrolleyState

object ledSupportBCR {
    private lateinit var led: ILed

    fun createLed()
    {
        DomainSystemConfig.setTheConfiguration("RaspberryDomainConfig.json")
        led = DeviceFactory.createLed()
    }

    fun doLed(state: TransportTrolleyState)
    {
        if(!this::led.isInitialized)
            return

        when (state)
        {
            TransportTrolleyState.HOME -> led.turnOff()
            TransportTrolleyState.MOVING -> led.blink()
            TransportTrolleyState.PICKUP -> led.turnOn()
            TransportTrolleyState.DUMP -> led.turnOn()
            TransportTrolleyState.STOPPED -> led.turnOn()
        }
    }
}