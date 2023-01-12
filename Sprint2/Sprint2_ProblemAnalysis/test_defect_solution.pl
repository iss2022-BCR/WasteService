%====================================================================================
% test_defect_solution description   
%====================================================================================
context(ctx_defect_solution_test, "localhost",  "TCP", "11705").
 qactor( typesprovider, ctx_defect_solution_test, "it.unibo.typesprovider.Typesprovider").
  qactor( wasteservice, ctx_defect_solution_test, "it.unibo.wasteservice.Wasteservice").
  qactor( transporttrolley_ds_test, ctx_defect_solution_test, "it.unibo.transporttrolley_ds_test.Transporttrolley_ds_test").
