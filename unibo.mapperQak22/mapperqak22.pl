%====================================================================================
% mapperqak22 description   
%====================================================================================
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
context(ctxmapperqak22, "localhost",  "TCP", "8032").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( mapperqak22, ctxmapperqak22, "it.unibo.mapperqak22.Mapperqak22").
msglogging.
