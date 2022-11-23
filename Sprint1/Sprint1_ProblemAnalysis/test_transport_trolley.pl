%====================================================================================
% test_transport_trolley description   
%====================================================================================
context(ctx_transporttrolley_test, "localhost",  "TCP", "11703").
 qactor( wasteservice_tt_test, ctx_transporttrolley_test, "it.unibo.wasteservice_tt_test.Wasteservice_tt_test").
  qactor( transporttrolley_tt_test, ctx_transporttrolley_test, "it.unibo.transporttrolley_tt_test.Transporttrolley_tt_test").
