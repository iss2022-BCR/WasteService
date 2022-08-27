%====================================================================================
% demo_system_overview description   
%====================================================================================
context(ctx_wasteservice, "localhost",  "TCP", "11800").
context(ctx_robot, "localhost",  "TCP", "11801").
context(ctx_monitor, "localhost",  "TCP", "11802").
context(ctx_raspdevice, "localhost",  "TCP", "11803").
context(ctx_smartdevice_test, "localhost",  "TCP", "11804").
 qactor( smartdevice_test, ctx_smartdevice_test, "it.unibo.smartdevice_test.Smartdevice_test").
  qactor( wasteservice, ctx_wasteservice, "it.unibo.wasteservice.Wasteservice").
  qactor( transporttrolley, ctx_robot, "it.unibo.transporttrolley.Transporttrolley").
  qactor( basicrobot, ctx_robot, "it.unibo.basicrobot.Basicrobot").
