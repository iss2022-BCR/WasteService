%====================================================================================
% test_pc_node description   
%====================================================================================
context(ctx_pc, "192.168.1.4",  "TCP", "11750").
context(ctx_rpi, "localhost",  "TCP", "11751").
 qactor( pc, ctx_pc, "external").
  qactor( rpi, ctx_rpi, "it.unibo.rpi.Rpi").
