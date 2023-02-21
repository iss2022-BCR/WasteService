%====================================================================================
% test_led_state description   
%====================================================================================
context(ctx_ledstate_test, "localhost",  "TCP", "11705").
 qactor( transporttrolley_ls_test, ctx_ledstate_test, "it.unibo.transporttrolley_ls_test.Transporttrolley_ls_test").
  qactor( pathexecutorbcr_ls_test, ctx_ledstate_test, "it.unibo.pathexecutorbcr_ls_test.Pathexecutorbcr_ls_test").
  qactor( alarmcontroller_ls_test, ctx_ledstate_test, "it.unibo.alarmcontroller_ls_test.Alarmcontroller_ls_test").
  qactor( ledcontroller_ls_test, ctx_ledstate_test, "it.unibo.ledcontroller_ls_test.Ledcontroller_ls_test").
