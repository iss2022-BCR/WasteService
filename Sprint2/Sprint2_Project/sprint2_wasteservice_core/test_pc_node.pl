%====================================================================================
% test_pc_node description   
%====================================================================================
context(ctx_pc, "localhost",  "TCP", "11750").
context(ctx_rpi, "192.168.1.8",  "TCP", "11751").
 qactor( pc, ctx_pc, "it.unibo.pc.Pc").
  qactor( rpi, ctx_rpi, "external").
