%====================================================================================
% sprint1_demo_waste_service description   
%====================================================================================
context(ctx_test_wasteservice, "localhost",  "TCP", "11800").
 qactor( test_wasteservice, ctx_test_wasteservice, "it.unibo.test_wasteservice.Test_wasteservice").
