%====================================================================================
% demo_system_overview_v0 description   
%====================================================================================
context(ctx_wasteservice, "localhost",  "TCP", "11800").
context(ctx_monitor, "localhost",  "TCP", "11801").
context(ctx_raspdevice, "localhost",  "TCP", "11802").
context(ctx_smartdevice_test, "localhost",  "TCP", "11803").
context(ctx_basicrobot, "localhost",  "TCP", "11810").
 qactor( basicrobot, ctx_basicrobot, "external").
  qactor( smartdevice_test, ctx_smartdevice_test, "it.unibo.smartdevice_test.Smartdevice_test").
  qactor( wasteservice, ctx_wasteservice, "it.unibo.wasteservice.Wasteservice").
  qactor( transporttrolley, ctx_wasteservice, "it.unibo.transporttrolley.Transporttrolley").
  qactor( alarmdevice, ctx_raspdevice, "it.unibo.alarmdevice.Alarmdevice").
  qactor( warningdevice, ctx_raspdevice, "it.unibo.warningdevice.Warningdevice").
  qactor( wasteservicestatusgui, ctx_monitor, "it.unibo.wasteservicestatusgui.Wasteservicestatusgui").
