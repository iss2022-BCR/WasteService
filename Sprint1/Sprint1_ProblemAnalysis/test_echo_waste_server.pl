%====================================================================================
% test_echo_waste_server description   
%====================================================================================
context(ctx_test_wasteservice, "localhost",  "TCP", "11800").
context(ctxbasicrobot, "localhost",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( test_wasteservice, ctx_test_wasteservice, "it.unibo.test_wasteservice.Test_wasteservice").
