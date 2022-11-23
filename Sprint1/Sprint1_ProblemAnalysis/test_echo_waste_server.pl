%====================================================================================
% test_echo_waste_server description   
%====================================================================================
context(ctx_echo_wasteservice, "localhost",  "TCP", "11800").
context(ctxbasicrobot, "localhost",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( echo_wasteservice, ctx_echo_wasteservice, "it.unibo.echo_wasteservice.Echo_wasteservice").
