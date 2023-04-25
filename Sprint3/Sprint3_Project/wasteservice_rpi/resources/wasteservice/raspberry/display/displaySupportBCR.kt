package wasteservice.raspberry.display

import it.unibo.radarSystem22.domainBCR.DeviceFactory
import it.unibo.radarSystem22.domainBCR.interfaces.ITextDisplay
import it.unibo.radarSystem22.domainBCR.utils.DomainSystemConfig

object displaySupportBCR {
    lateinit var display: ITextDisplay

    fun createTextDisplay()
    {
        DomainSystemConfig.setTheConfiguration("RaspberryDomainConfig.json")
        if(DomainSystemConfig.enableTextDisplay)
            display = DeviceFactory.createTextDisplay()
    }

    fun doDisplay(text1: String, text2: String)
    {
        if(!this::display.isInitialized)
            return

        display.setLines(text1, text2)
    }
}