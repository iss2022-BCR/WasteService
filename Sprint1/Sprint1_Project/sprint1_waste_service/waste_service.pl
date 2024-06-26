%====================================================================================
% waste_service description   
%====================================================================================
context(ctx_smartdevice, "localhost",  "TCP", "11799").
context(ctx_wasteservice, "localhost",  "TCP", "11800").
context(ctx_basicrobot, "localhost",  "TCP", "8020").
 qactor( smartdevice_simulator, ctx_smartdevice, "it.unibo.smartdevice_simulator.Smartdevice_simulator").
  qactor( typesprovider, ctx_wasteservice, "it.unibo.typesprovider.Typesprovider").
  qactor( wasteservice, ctx_wasteservice, "it.unibo.wasteservice.Wasteservice").
  qactor( transporttrolley, ctx_wasteservice, "it.unibo.transporttrolley.Transporttrolley").
  qactor( pathexecutor, ctx_wasteservice, "it.unibo.pathexecutor.Pathexecutor").
  qactor( basicrobot, ctx_basicrobot, "it.unibo.basicrobot.Basicrobot").
