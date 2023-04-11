package wasteservice.raspberry.sonar

import it.unibo.kactor.ActorBasic
import it.unibo.kactor.sysUtil

object sonarBuilder {
    lateinit var firstActorInPipe: ActorBasic

    fun createSonar()
    {
        val sonarActor = sysUtil.getActor("sonardatasource_concrete")
        if(sonarActor == null)
        {
            println("ERROR")
            return
        }
        else firstActorInPipe = sonarActor


        firstActorInPipe
            .subscribeLocalActor("filterDistanceChanged")
            .subscribeLocalActor("filterDistanceBounds")
            .subscribeLocalActor("filterAlarm")
    }
}