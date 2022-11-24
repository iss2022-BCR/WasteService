%====================================================================================
% demo_waste_service description   
%====================================================================================
context(ctx_demo_wasteservice, "localhost",  "TCP", "11800").
 qactor( typesprovider, ctx_demo_wasteservice, "it.unibo.typesprovider.Typesprovider").
  qactor( wasteservice, ctx_demo_wasteservice, "it.unibo.wasteservice.Wasteservice").
  qactor( transporttrolley, ctx_demo_wasteservice, "it.unibo.transporttrolley.Transporttrolley").
  qactor( pathexecutor, ctx_demo_wasteservice, "it.unibo.pathexecutor.Pathexecutor").
  qactor( basicrobot, ctx_demo_wasteservice, "it.unibo.basicrobot.Basicrobot").
