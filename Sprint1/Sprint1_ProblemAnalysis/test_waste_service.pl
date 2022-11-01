%====================================================================================
% test_waste_service description   
%====================================================================================
context(ctx_wasteservice_test, "localhost",  "TCP", "11702").
 qactor( wasteservice_test, ctx_wasteservice_test, "it.unibo.wasteservice_test.Wasteservice_test").
  qactor( transporttrolley_test, ctx_wasteservice_test, "it.unibo.transporttrolley_test.Transporttrolley_test").
