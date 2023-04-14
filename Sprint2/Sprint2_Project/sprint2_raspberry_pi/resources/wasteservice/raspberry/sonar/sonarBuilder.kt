package wasteservice.raspberry.sonar

import it.unibo.kactor.ActorBasic
import it.unibo.kactor.sysUtil

object sonarBuilder {
    const val firstActorName = "sonarinput"
    lateinit var firstActorInPipe: ActorBasic

    fun createSonar()
    {
        val sonarActor = sysUtil.getActor(firstActorName)
        if(sonarActor == null)
        {
            println("ERROR: actor '$firstActorName' not found.")
            return
        }
        else firstActorInPipe = sonarActor

        firstActorInPipe
            .subscribeLocalActor("filter_distancebounds")
            .subscribeLocalActor("filter_distancechanged")
            .subscribeLocalActor("filter_textdisplay")
            .subscribeLocalActor("filter_alarm")
    }
}