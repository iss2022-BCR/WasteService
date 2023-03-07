%====================================================================================
% test_messages description   
%====================================================================================
context(ctx_pc, "192.168.1.4",  "TCP", "11760").
context(ctx_rpi, "localhost",  "TCP", "11761").
 qactor( pc, ctx_pc, "external").
  qactor( rpi, ctx_rpi, "it.unibo.rpi.Rpi").
