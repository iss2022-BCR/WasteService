import aima.core.agent.Action
import aima.core.search.framework.SearchAgent
import aima.core.search.framework.problem.GoalTest
import aima.core.search.framework.problem.Problem
import aima.core.search.framework.qsearch.GraphSearch
import aima.core.search.uninformed.BreadthFirstSearch
import java.io.ObjectInputStream
import java.io.FileInputStream
import unibo.planner22.model.*
import it.unibo.map_editor_bcr.model.map_config.CellType
import it.unibo.map_editor_bcr.model.room_map.RoomMapParser
import wasteservice.MapConfigSupport


object plannerBCR {
	private val name = "  [PlannerBCR]"
    private var robotState: RobotState? = null
	private var actions: List<Action>?    = null
 	
    private var curPos    : Pair<Int,Int> = Pair(0,0)
	private var curDir    : RobotState.Direction  = RobotState.Direction.DOWN
    private var curGoal: GoalTest = Functions()		 

	private var mapDims   : Pair<Int,Int> = Pair(0,0)
 	
	private var direction             = "downDir"
	private	var currentGoalApplicable = true;

	private var actionSequence : Iterator<Action>? = null

	private var storedactionSequence : Iterator<Action>? = null
    private var storedPos  : Pair<Int,Int> = Pair(0,0)

    private var search: BreadthFirstSearch? = null
    private var timeStart: Long = 0

	// PlannerBCR
	private var mapConfigSupport = MapConfigSupport()
	
/* 
 * ------------------------------------------------
 * CREATE AND MANAGE PLANS
 * ------------------------------------------------
 */
	/**
	 * Init AI Search algorithm and set the robot position at HOME (0,0), with direction down.
	 */
    @Throws(Exception::class)
    @JvmStatic fun initAI() {
    	robotState = RobotState(0, 0, RobotState.Direction.DOWN)
        search     = BreadthFirstSearch(GraphSearch())

		println("$name initAI done")
    }

	/**
	 * Set the goal position (x,y) of the Search Algorithm
	 * @param x X coordinate (horizontal axis)
	 * @param y Y coordinate (vertical axis)
	 */
    @JvmStatic
	fun setGoal(x: Int, y: Int) {
        try {
             println("$name setGoal $x,$y while robot in cell: ${getPosX()},${getPosY()} direction=${getDirection()} ") //canMove=$canMove

			if( RoomMap.getRoomMap().isObstacle(x,y) ) {
				println("$name ATTEMPT TO GO INTO AN OBSTACLE ")
				currentGoalApplicable = false
 				resetActions()
				return
			} else currentGoalApplicable = true

			RoomMap.getRoomMap().put(x, y, Box(false, true, false))  //set dirty

			curGoal = GoalTest { state  : Any ->
                val robotState = state as RobotState
				(robotState.x == x && robotState.y == y)
            }
			showMap()
         } catch (e: Exception) {
             //e.printStackTrace()
     		}
    }
 
    @Throws(Exception::class)
    @JvmStatic
	fun doPlan(): List<Action>? {
		//println("Planner22Util doPlan curGoal=$curGoal" )
		
		if( ! currentGoalApplicable ){
			println("$name doPlan cannot go into an obstacle")
			actions = listOf<Action>()
			return actions		//empty list
		} 
		
        val searchAgent: SearchAgent
        //println("Planner22Util doPlan newProblem (A) $curGoal" );
		val problem = Problem(robotState, Functions(), Functions(), curGoal, Functions())
        //println("Planner22Util doPlan newProblem (A) search " );
        searchAgent = SearchAgent(problem, search!!)
        actions     = searchAgent.actions
		
		println("$name doPlan actions=$actions")
		
        if (actions == null || actions!!.isEmpty()) {
            println("$name doPlan NO MOVES !!!!!!!!!!!! $actions!!"   )
            if (!RoomMap.getRoomMap().isClean) RoomMap.getRoomMap().setObstacles()
            //actions = ArrayList()
            return null
        } else if (actions!![0].isNoOp) {
            println("$name doPlan NoOp")
            return null
        }
		
        //println("Planner22Util doPlan actions=$actions")
		setActionMoveSequence()
        return actions
    }
	
	@JvmStatic fun planForGoal( x: String, y: String) {
		val vx = Integer.parseInt(x)
		val vy = Integer.parseInt(y)
		setGoal(vx,vy)		
		doPlan()   
 	}	
  	
	@JvmStatic fun planForNextDirty() {
		val rmap = RoomMap.getRoomMap()
		for( i in 0..getMapDimX( )-1 ){
			for( j in 0..getMapDimY( )-1 ){
				//println( ""+ i + "," + j + " -> " + rmap.isDirty(i,j)   );
				if( rmap.isDirty(i,j)  ){
					setGoal( i,j )
					doPlan() 
					return
				}
			}
		}
 	}	


	@JvmStatic fun memoCurentPlan(){
		storedPos            = curPos
		storedactionSequence = actionSequence;
	}
	
	@JvmStatic fun restorePlan(){
		//Goto storedcurPos
		actionSequence = storedactionSequence;
	}
	
/*
 * ------------------------------------------------
 * MANAGE PLANS AS ACTION SEQUENCES
 * ------------------------------------------------
*/	
	@JvmStatic fun setActionMoveSequence(){
		if( actions != null ) {
			 actionSequence = actions!!.iterator()
		}
	}
	
	@JvmStatic fun getNextPlannedMove() : String{
		if( actionSequence == null ) return ""
		else if( actionSequence!!.hasNext()) return actionSequence!!.next().toString()
				else return ""
	}

	@JvmStatic fun getActions() : List<Action>{
        return actions!!
    }
	@JvmStatic fun existActions() : Boolean{
		//println("existActions ${actions!!.size}")
		return actions!!.size>0   
	}
	@JvmStatic fun resetActions(){
		actions = listOf<Action>()
	}
	
	@JvmStatic fun get_actionSequence() : Iterator<Action>?{
		return actionSequence
	}

	@JvmStatic
	fun getActionsString() : String {
		var result = ""

		for(action in actions!!) {
			result += action
		}

		return result
	}
 	
/*
 * ------------------------------------------------
 * INSPECTING ROBOT POSITION AND DIRECTION
 * ------------------------------------------------
*/		
	@JvmStatic fun get_curPos() : Pair<Int,Int>{
		return curPos
	}

	@JvmStatic fun getPosX() : Int{ return robotState!!.getX() }
    @JvmStatic fun getPosY() : Int{ return robotState!!.getY() }
	
	@JvmStatic fun getDir()  : RobotState.Direction{
		return robotState!!.getDirection()
	}

	@JvmStatic fun getDirection() : String{
 		val direction = getDir()
		when( direction ){
			RobotState.Direction.UP    -> return "upDir"
			RobotState.Direction.RIGHT -> return "rightDir"
			RobotState.Direction.LEFT  -> return "leftDir"
			RobotState.Direction.DOWN  -> return "downDir"
			else            -> return "unknownDir"
 		}
  	}
	
	@JvmStatic fun atHome() : Boolean{
		return curPos.first == 0 && curPos.second == 0
	}
	
	@JvmStatic fun atPos( x: Int, y: Int ) : Boolean{
		return curPos.first == x && curPos.second == y
	}
	
	@JvmStatic fun showCurrentRobotState(){
		println("===================================================")
		showMap()
		direction = getDirection()
		println("RobotPos=(${curPos.first}, ${curPos.second})  direction=$direction  " )  
		println("===================================================")
	}

/*
* ------------------------------------------------
* MANAGING THE ROOM MAP
* ------------------------------------------------
*/	
 	@JvmStatic fun getMapDimX( ) 	: Int{ return mapDims.first }
	@JvmStatic fun getMapDimY( ) 	: Int{ return mapDims.second }
	@JvmStatic fun mapIsEmpty() : Boolean{return (getMapDimX( )==0 &&  getMapDimY( )==0 ) }

	@JvmStatic fun showMap() {
        println(RoomMap.getRoomMap().toString())
    }
	@JvmStatic fun showFancyMap() {
		println(RoomMapParser(RoomMap.getRoomMap()).toFancyString())
	}
	@JvmStatic fun getMap() : String{
		return RoomMap.getRoomMap().toString() 
	}
	@JvmStatic fun getMapOneLine() : String{ 
		return  "'"+RoomMap.getRoomMap().toString().replace("\n","@").replace("|","").replace(",","") +"'" 
	}

	@JvmStatic fun getMapDims() : Pair<Int,Int> {
		if( RoomMap.getRoomMap() == null ){
			return Pair(0,0)
		}
	    val dimMapx = RoomMap.getRoomMap().getDimX()
	    val dimMapy = RoomMap.getRoomMap().getDimY()
	    //println("getMapDims dimMapx = $dimMapx, dimMapy=$dimMapy")
		return Pair(dimMapx,dimMapy)	
	}

	/**
	 * Load a Room Map object from a binary file.
	 * @param fname the name of the binary file.
	 */
	@JvmStatic fun loadRoomMap(fname: String) {
 		try{
 			val inps = ObjectInputStream(FileInputStream("${fname}.bin"))
			val map  = inps.readObject() as RoomMap;
 			println("$name loadRoomMap = $fname DONE")
			RoomMap.setRoomMap( map )
		} catch(e:Exception){
			println("$name loadRoomMap = $fname FAILURE ${e.message}")
		}
		mapDims = getMapDims()//Pair(dimMapx,dimMapy)
	}

/*
* ------------------------------------------------
* UPDATING THE ROOM MAP
* ------------------------------------------------
*/		
 	@JvmStatic fun updateMap( move: String, msg: String="" ){
		doMove( move )
		setPositionOnMap( )
		if( msg.length > 0 ) println(msg) 
 	}
	
	@JvmStatic fun updateMapObstacleOnCurrentDirection(   ){
		doMove( direction )
		setPositionOnMap( )
	}

	@JvmStatic fun updateRobotPosition(pos: String) {

	}
	
	@JvmStatic fun setPositionOnMap(){
		direction     =  getDirection()
		val posx      =  getPosX()
		val posy      =  getPosY()
		curPos        =  Pair( posx,posy )
	}
 	
    @JvmStatic fun doMove(move: String) {
        val x   = getPosX()  
        val y   = getPosY()  
		val map = RoomMap.getRoomMap()
       // println("Planner22Util: doMove move=$move  dir=$dir x=$x y=$y dimMapX=$dimMapx dimMapY=$dimMapy")
       try {
            when (move) {
                "w" -> {
                    //map.put(x, y, Box(false, false, false)) //clean the cell
					map.cleanCell(x,y)
                    robotState = Functions().result(robotState!!, RobotAction.wAction) as RobotState
                    //map.put(robotState!!.x, robotState!!.y, Box(false, false, true))
					moveRobotInTheMap()
                }
                "s" -> {
                    robotState = Functions().result(robotState!!, RobotAction.sAction) as RobotState
                    //map.put(robotState!!.x, robotState!!.y, Box(false, false, true))
					moveRobotInTheMap()
                }
                "a"  -> {
                    robotState = Functions().result(robotState!!, RobotAction.lAction) as RobotState
                    //map.put(robotState!!.x, robotState!!.y, Box(false, false, true))
					moveRobotInTheMap()
                }
                "l" -> {
                    robotState = Functions().result(robotState!!, RobotAction.lAction) as RobotState
                    //map.put(robotState!!.x, robotState!!.y, Box(false, false, true))
					moveRobotInTheMap()
                }
                "d" -> {
                    robotState = Functions().result(robotState!!, RobotAction.rAction) as RobotState
                    //map.put(robotState!!.x, robotState!!.y, Box(false, false, true))
					moveRobotInTheMap()
                }
                "r" -> {
                    robotState = Functions().result(robotState!!, RobotAction.rAction) as RobotState
                    //map.put(robotState!!.x, robotState!!.y, Box(false, false, true))
					moveRobotInTheMap()
                }
				//Used by WALL-UPDATING
				//Box(boolean isObstacle, boolean isDirty, boolean isRobot)
                "rightDir" -> map.put(x + 1, y, Box(true, false, false)) 
                "leftDir"  -> map.put(x - 1, y, Box(true, false, false))
                "upDir"    -> map.put(x, y - 1, Box(true, false, false))
                "downDir"  -> map.put(x, y + 1, Box(true, false, false))
		   }//when
        } catch (e: Exception) {
            println("$name doMove: ERROR:" + e.message)
        }
    }
	
	@JvmStatic fun moveRobotInTheMap(){
		RoomMap.getRoomMap().put(robotState!!.x, robotState!!.y, Box(false, false, true))
	}
	
/*
* ------------------------------------------------
* UPDATING THE ROOM MAP FOR WALLS
* ------------------------------------------------
*/		
 	@JvmStatic fun setObstacleWall(  dir: RobotState.Direction, x:Int, y:Int){
		if( dir == RobotState.Direction.DOWN  ){ RoomMap.getRoomMap().put(x, y + 1, Box(true, false, false)) }
		if( dir == RobotState.Direction.RIGHT ){ RoomMap.getRoomMap().put(x + 1, y, Box(true, false, false)) }
	}
	@JvmStatic fun wallFound(){
 		 val dimMapx = RoomMap.getRoomMap().getDimX()
		 val dimMapy = RoomMap.getRoomMap().getDimY()
		 val dir     = getDir()
		 val x       = getPosX()
		 val y       = getPosY()
		 setObstacleWall( dir,x,y )
 		 println("$name wallFound dir=$dir  x=$x  y=$y dimMapX=$dimMapx dimMapY=$dimMapy");
		 doMove( dir.toString() )  //set cell
  		 if( dir == RobotState.Direction.RIGHT) setWallDown(dimMapx,y)
		 if( dir == RobotState.Direction.UP)    setWallRight(dimMapy,x)
 	}
	@JvmStatic fun setWallDown(dimMapx: Int, y: Int ){
		 var k   = 0
		 while( k < dimMapx ) {
			RoomMap.getRoomMap().put(k, y+1, Box(true, false, false))
			k++
		 }		
	}	
	@JvmStatic fun setWallRight(dimMapy: Int, x: Int){
 		 var k   = 0
		 while( k < dimMapy ) {
			RoomMap.getRoomMap().put(x+1, k, Box(true, false, false))
			k++
		 }		
	}

	
/*
* ------------------------------------------------
* TIME UTILITIES
* ------------------------------------------------
*/		
    @JvmStatic fun startTimer() {
        timeStart = System.currentTimeMillis()
    }
	
    @JvmStatic fun getDuration() : Int{
        val duration = (System.currentTimeMillis() - timeStart).toInt()
		println("$name DURATION = $duration")
		return duration
    }

	// BCR utilities
	@JvmStatic
	fun test() {
		println("DimX: ${RoomMap.getRoomMap().dimX}")
		println("DimY: ${RoomMap.getRoomMap().dimY}")
		println("CanMoveLeft (0,1): ${RoomMap.getRoomMap().canMoveLeft(0, 1)}")
		println("CanMoveLeft (1,1): ${RoomMap.getRoomMap().canMoveLeft(1, 1)}")
		println("isDirty: ${RoomMap.getRoomMap().isDirty(0,0)}")
	}

/*
* ------------------------------------------------
* PLANNER BCR METHODS
* ------------------------------------------------
*/
	@JvmStatic
	fun loadMapConfig(filename: String) {
		mapConfigSupport.loadMapConfig(filename)
	}

	@JvmStatic
	fun showMapConfig() {
		println(mapConfigSupport.mapToString())
	}

	@JvmStatic
	fun showFancyMapConfig() {
		println(mapConfigSupport.mapToFancyString())
	}

	fun getCell(coord: Pair<Int, Int>): CellType {
		return mapConfigSupport.getCell(coord)
	}

	@JvmStatic
	fun getMapConfigCoordinates(): List<Pair<Int, Int>> {
		return mapConfigSupport.getMapConfigCoordinates()
	}

	@JvmStatic
	fun getNearestPositionToCellType(startCoords: Pair<Int, Int>, cellType: String): Pair<Int, Int> {
		return mapConfigSupport.getNearestPositionToCellType(startCoords, cellType)
	}
	@JvmStatic
	fun getRandomPositionFromCellType(cellType: String): Pair<Int, Int> {
		return mapConfigSupport.getRandomPositionFromCellType(CellType.valueOf(cellType))
	}
}
