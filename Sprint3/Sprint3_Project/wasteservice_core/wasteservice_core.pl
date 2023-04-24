%====================================================================================
% wasteservice_core description   
%====================================================================================
context(ctx_statusgui, "localhost",  "TCP", "11803").
 qactor( gui_updater, ctx_statusgui, "it.unibo.gui_updater.Gui_updater").
