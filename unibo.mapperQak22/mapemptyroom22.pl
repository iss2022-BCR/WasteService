%====================================================================================
% mapemptyroom22 description   
%====================================================================================
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
context(ctxmapemptyroom22, "localhost",  "TCP", "8032").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( mapemptyroom22, ctxmapemptyroom22, "it.unibo.mapemptyroom22.Mapemptyroom22").
msglogging.
