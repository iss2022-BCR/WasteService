%====================================================================================
% test_defects_solution description   
%====================================================================================
context(ctx_defects_solution_test, "localhost",  "TCP", "11703").
 qactor( smartdevice_ds_test, ctx_defects_solution_test, "it.unibo.smartdevice_ds_test.Smartdevice_ds_test").
  qactor( wasteservice_ds_test, ctx_defects_solution_test, "it.unibo.wasteservice_ds_test.Wasteservice_ds_test").
  qactor( transporttrolley_ds_test, ctx_defects_solution_test, "it.unibo.transporttrolley_ds_test.Transporttrolley_ds_test").
