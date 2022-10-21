%====================================================================================
% interaction_sd_ws description   
%====================================================================================
context(ctx_test_storerequest, "localhost",  "TCP", "11820").
 qactor( test_wasteservice, ctx_test_storerequest, "it.unibo.test_wasteservice.Test_wasteservice").
