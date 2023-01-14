%====================================================================================
% waste_service description   
%====================================================================================
context(ctx_wasteservice, "localhost",  "TCP", "11800").
context(ctx_transporttrolley, "localhost",  "TCP", "11801").
context(ctx_robot, "localhost",  "TCP", "8020").
 qactor( typesprovider, ctx_wasteservice, "it.unibo.typesprovider.Typesprovider").
  qactor( wasteservice, ctx_wasteservice, "it.unibo.wasteservice.Wasteservice").
  qactor( transporttrolley, ctx_transporttrolley, "it.unibo.transporttrolley.Transporttrolley").
  qactor( pathexecutor, ctx_transporttrolley, "it.unibo.pathexecutor.Pathexecutor").
  qactor( basicrobot, ctx_robot, "it.unibo.basicrobot.Basicrobot").
