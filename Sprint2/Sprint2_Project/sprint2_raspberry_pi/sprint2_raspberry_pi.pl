%====================================================================================
% sprint2_raspberry_pi description   
%====================================================================================
context(ctx_wasteservice, "192.168.1.4",  "TCP", "11800").
context(ctx_transporttrolley, "192.168.1.4",  "TCP", "11801").
context(ctx_robot, "192.168.1.4",  "TCP", "8020").
context(ctx_raspberrypi, "localhost",  "TCP", "11802").
 qactor( typesprovider, ctx_wasteservice, "external").
  qactor( wasteservice, ctx_wasteservice, "external").
  qactor( transporttrolley, ctx_transporttrolley, "external").
  qactor( trolleystateprovider, ctx_transporttrolley, "external").
  qactor( pathexecutorbcr, ctx_transporttrolley, "external").
  qactor( basicrobot, ctx_robot, "external").
  qactor( alarmcontroller, ctx_raspberrypi, "it.unibo.alarmcontroller.Alarmcontroller").
  qactor( ledcontroller, ctx_raspberrypi, "it.unibo.ledcontroller.Ledcontroller").
