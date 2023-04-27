%====================================================================================
% logical_architecture_waste_service description   
%====================================================================================
context(ctx_smartdevice, "localhost",  "TCP", "11799").
context(ctx_wasteservice, "localhost",  "TCP", "11800").
context(ctx_transporttrolley, "localhost",  "TCP", "11801").
context(ctx_robot, "localhost",  "TCP", "8020").
 qactor( basicrobot, ctx_robot, "external").
  qactor( smartdevice_simulator, ctx_smartdevice, "it.unibo.smartdevice_simulator.Smartdevice_simulator").
  qactor( typesprovider, ctx_wasteservice, "it.unibo.typesprovider.Typesprovider").
  qactor( wasteservice, ctx_wasteservice, "it.unibo.wasteservice.Wasteservice").
  qactor( transporttrolley, ctx_transporttrolley, "it.unibo.transporttrolley.Transporttrolley").
  qactor( pathexecutor, ctx_transporttrolley, "it.unibo.pathexecutor.Pathexecutor").
