%====================================================================================
% mapwithobstqak22 description   
%====================================================================================
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
context(ctxmapwithobstqak22, "localhost",  "TCP", "8032").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( pathexec, ctxbasicrobot, "external").
  qactor( mapwithobstqak22, ctxmapwithobstqak22, "it.unibo.mapwithobstqak22.Mapwithobstqak22").
