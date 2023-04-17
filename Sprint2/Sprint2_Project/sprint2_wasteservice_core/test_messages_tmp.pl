%====================================================================================
% test_messages_tmp description   
%====================================================================================
context(ctx_pc, "localhost",  "TCP", "11760").
context(ctx_rpi, "raspberrypi.local",  "TCP", "11761").
 qactor( pc, ctx_pc, "it.unibo.pc.Pc").
  qactor( rpi, ctx_rpi, "external").
