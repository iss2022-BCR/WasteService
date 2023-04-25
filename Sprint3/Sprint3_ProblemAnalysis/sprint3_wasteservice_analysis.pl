%====================================================================================
% sprint3_wasteservice_analysis description   
%====================================================================================
context(ctx_wasteservice, "localhost",  "TCP", "11800").
context(ctx_transporttrolley, "localhost",  "TCP", "11801").
context(ctx_robot, "localhost",  "TCP", "8020").
context(ctx_raspberrypi, "localhost",  "TCP", "11802").
context(ctx_statusgui, "localhost",  "TCP", "11803").
 qactor( typesprovider, ctx_wasteservice, "it.unibo.typesprovider.Typesprovider").
  qactor( wasteservice, ctx_wasteservice, "it.unibo.wasteservice.Wasteservice").
  qactor( transporttrolley, ctx_transporttrolley, "it.unibo.transporttrolley.Transporttrolley").
  qactor( trolleystateprovider, ctx_transporttrolley, "it.unibo.trolleystateprovider.Trolleystateprovider").
  qactor( pathexecutorbcr, ctx_transporttrolley, "it.unibo.pathexecutorbcr.Pathexecutorbcr").
  qactor( basicrobot, ctx_robot, "it.unibo.basicrobot.Basicrobot").
  qactor( sonar_bcr, ctx_raspberrypi, "external").
  qactor( led_bcr, ctx_raspberrypi, "external").
  qactor( buzzer_bcr, ctx_raspberrypi, "external").
  qactor( textdisplay_bcr, ctx_raspberrypi, "external").
  qactor( gui_controller, ctx_statusgui, "it.unibo.gui_controller.Gui_controller").
  qactor( gui_simulator, ctx_statusgui, "it.unibo.gui_simulator.Gui_simulator").
