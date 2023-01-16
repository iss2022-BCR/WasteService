%====================================================================================
% demo0 description   
%====================================================================================
context(ctxdemo0, "localhost",  "TCP", "8095").
 qactor( demo0, ctxdemo0, "it.unibo.demo0.Demo0").
  qactor( observer, ctxdemo0, "it.unibo.observer.Observer").
