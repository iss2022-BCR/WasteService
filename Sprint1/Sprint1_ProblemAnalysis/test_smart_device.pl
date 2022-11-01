%====================================================================================
% test_smart_device description   
%====================================================================================
context(ctx_smartdevice, "localhost",  "TCP", "11799").
context(ctx_wasteservice, "localhost",  "TCP", "11800").
 qactor( typesprovider, ctx_wasteservice, "external").
  qactor( wasteservice, ctx_wasteservice, "external").
  qactor( smartdevice_simulator, ctx_smartdevice, "it.unibo.smartdevice_simulator.Smartdevice_simulator").
