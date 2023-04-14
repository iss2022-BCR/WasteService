package wasteservice.raspberry.buzzer

import it.unibo.radarSystem22.domainBCR.DeviceFactory
import it.unibo.radarSystem22.domainBCR.interfaces.IBuzzer
import it.unibo.radarSystem22.domainBCR.utils.DomainSystemConfig
import wasteservice.TransportTrolleyState

object buzzerSupportBCR {
    private lateinit var buzzer: IBuzzer

    fun createBuzzer()
    {
        DomainSystemConfig.setTheConfiguration("RaspberryDomainConfig.json")
        buzzer = DeviceFactory.createBuzzer()
    }

    fun doBuzzer(state: TransportTrolleyState)
    {
        if(!this::buzzer.isInitialized)
            return

        when (state)
        {
            TransportTrolleyState.HOME -> buzzer.turnOff()
            TransportTrolleyState.MOVING -> buzzer.turnIntermittent()
            TransportTrolleyState.PICKUP -> buzzer.turnOn()
            TransportTrolleyState.DUMP -> buzzer.turnOn()
            TransportTrolleyState.STOPPED -> buzzer.turnOn()
        }
    }
}