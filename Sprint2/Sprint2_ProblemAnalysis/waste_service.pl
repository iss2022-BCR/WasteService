%====================================================================================
% waste_service description   
%====================================================================================
context(ctx_wasteservice, "localhost",  "TCP", "11800").
context(ctx_transporttrolley, "localhost",  "TCP", "11801").
context(ctx_raspberrypi, "localhost",  "TCP", "11802").
context(ctx_robot, "localhost",  "TCP", "8020").
 qactor( typesprovider, ctx_wasteservice, "it.unibo.typesprovider.Typesprovider").
  qactor( wasteservice, ctx_wasteservice, "it.unibo.wasteservice.Wasteservice").
  qactor( transporttrolley, ctx_transporttrolley, "it.unibo.transporttrolley.Transporttrolley").
  qactor( trolleystateprovider, ctx_transporttrolley, "it.unibo.trolleystateprovider.Trolleystateprovider").
  qactor( pathexecutorbcr, ctx_transporttrolley, "it.unibo.pathexecutorbcr.Pathexecutorbcr").
  qactor( basicrobot, ctx_robot, "it.unibo.basicrobot.Basicrobot").
  qactor( alarmcontroller, ctx_raspberrypi, "it.unibo.alarmcontroller.Alarmcontroller").
  qactor( ledcontroller, ctx_raspberrypi, "it.unibo.ledcontroller.Ledcontroller").
