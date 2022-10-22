%====================================================================================
% boundaryqak22 description   
%====================================================================================
context(ctxboundaryqak22, "localhost",  "TCP", "8032").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( boundaryqak22, ctxboundaryqak22, "it.unibo.boundaryqak22.Boundaryqak22").
msglogging.
