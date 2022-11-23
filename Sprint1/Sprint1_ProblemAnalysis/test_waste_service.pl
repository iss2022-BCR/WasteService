%====================================================================================
% test_waste_service description   
%====================================================================================
context(ctx_wasteservice_test, "localhost",  "TCP", "11702").
 qactor( wasteservice_ws_test, ctx_wasteservice_test, "it.unibo.wasteservice_ws_test.Wasteservice_ws_test").
  qactor( transporttrolley_ws_test, ctx_wasteservice_test, "it.unibo.transporttrolley_ws_test.Transporttrolley_ws_test").
