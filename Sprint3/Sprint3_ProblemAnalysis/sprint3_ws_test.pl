%====================================================================================
% sprint3_test_container_update description
%====================================================================================
context(ctx_wasteservice, "localhost",  "TCP", "11800").
context(ctx_transporttrolley, "localhost",  "TCP", "11801").
context(ctx_robot, "localhost",  "TCP", "8020").
context(ctx_statusgui, "localhost",  "TCP", "11803").
 qactor( typesprovider, ctx_wasteservice, "it.unibo.typesprovider.Typesprovider").
  qactor( wasteservice, ctx_wasteservice, "it.unibo.wasteservice.Wasteservice").
  qactor( status_controller, ctx_wasteservice, "it.unibo.status_controller.Status_controller").
  qactor( transporttrolley, ctx_transporttrolley, "it.unibo.transporttrolley.Transporttrolley").
  qactor( trolleystateprovider, ctx_transporttrolley, "it.unibo.trolleystateprovider.Trolleystateprovider").
  qactor( pathexecutorbcr, ctx_transporttrolley, "it.unibo.pathexecutorbcr.Pathexecutorbcr").
  qactor( basicrobot, ctx_robot, "it.unibo.basicrobot.Basicrobot").
  qactor( gui_simulator, ctx_statusgui, "it.unibo.gui_simulator.Gui_simulator").
