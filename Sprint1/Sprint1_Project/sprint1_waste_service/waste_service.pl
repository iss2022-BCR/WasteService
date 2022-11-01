%====================================================================================
% waste_service description   
%====================================================================================
context(ctx_wasteservice, "localhost",  "TCP", "11800").
context(ctxbasicrobot, "localhost",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( typesprovider, ctx_wasteservice, "it.unibo.typesprovider.Typesprovider").
  qactor( wasteservice, ctx_wasteservice, "it.unibo.wasteservice.Wasteservice").
  qactor( transporttrolley, ctx_wasteservice, "it.unibo.transporttrolley.Transporttrolley").
  qactor( pathexecutor, ctx_wasteservice, "it.unibo.pathexecutor.Pathexecutor").
