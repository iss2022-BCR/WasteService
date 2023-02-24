%====================================================================================
% test_waste_service description   
%====================================================================================
context(ctx_stopresume_test, "localhost",  "TCP", "11704").
 qactor( transporttrolley_sr_test, ctx_stopresume_test, "it.unibo.transporttrolley_sr_test.Transporttrolley_sr_test").
  qactor( pathexecutorbcr_sr_test, ctx_stopresume_test, "it.unibo.pathexecutorbcr_sr_test.Pathexecutorbcr_sr_test").
  qactor( alarmcontroller_sr_test, ctx_stopresume_test, "it.unibo.alarmcontroller_sr_test.Alarmcontroller_sr_test").
