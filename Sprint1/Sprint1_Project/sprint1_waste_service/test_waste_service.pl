%====================================================================================
% test_waste_service description   
%====================================================================================
context(ctx_wasteservice_test, "localhost",  "TCP", "11800").
 qactor( smartdevice_simulator, ctx_wasteservice_test, "it.unibo.smartdevice_simulator.Smartdevice_simulator").
  qactor( wasteservice, ctx_wasteservice_test, "it.unibo.wasteservice.Wasteservice").
  qactor( transporttrolley, ctx_wasteservice_test, "it.unibo.transporttrolley.Transporttrolley").
  qactor( pathexecutor, ctx_wasteservice_test, "it.unibo.pathexecutor.Pathexecutor").
  qactor( basicrobot, ctx_wasteservice_test, "it.unibo.basicrobot.Basicrobot").
