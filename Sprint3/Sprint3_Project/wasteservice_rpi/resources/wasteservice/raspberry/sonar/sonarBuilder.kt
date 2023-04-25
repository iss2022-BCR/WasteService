package wasteservice.raspberry.sonar

import it.unibo.kactor.ActorBasic
import it.unibo.kactor.sysUtil

object sonarBuilder {
    const val firstActorName = "sonarinput"
    lateinit var firstActorInPipe: ActorBasic
    lateinit var lastActorInPipe: ActorBasic

    fun createSonar(enableText: Boolean = true)
    {
        val sonarActor = sysUtil.getActor(firstActorName)
        if(sonarActor == null)
        {
            println("ERROR: actor '$firstActorName' not found.")
            return
        }

        firstActorInPipe = sonarActor
        lastActorInPipe = sonarActor

        lastActorInPipe = lastActorInPipe.subscribeLocalActor("filter_distancebounds")
        lastActorInPipe = lastActorInPipe.subscribeLocalActor("filter_distancechanged")
        if(enableText)
            lastActorInPipe = lastActorInPipe.subscribeLocalActor("filter_textdisplay")
        lastActorInPipe = lastActorInPipe.subscribeLocalActor("filter_alarm")
    }
}