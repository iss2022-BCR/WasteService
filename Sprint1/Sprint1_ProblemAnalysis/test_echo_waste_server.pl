%====================================================================================
% test_echo_waste_server description   
%====================================================================================
context(ctx_test_wasteservice, "localhost",  "TCP", "11800").
 qactor( test_wasteservice, ctx_test_wasteservice, "it.unibo.test_wasteservice.Test_wasteservice").
