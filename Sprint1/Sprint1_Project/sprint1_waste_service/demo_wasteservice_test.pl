%====================================================================================
% demo_wasteservice_test description   
%====================================================================================
context(ctxwasteservice_test, "localhost",  "TCP", "8050").
 qactor( wasteservice, ctxwasteservice_test, "it.unibo.wasteservice.Wasteservice").
  qactor( transporttrolley, ctxwasteservice_test, "it.unibo.transporttrolley.Transporttrolley").
  qactor( trolleymover, ctxwasteservice_test, "it.unibo.trolleymover.Trolleymover").
  qactor( pather, ctxwasteservice_test, "it.unibo.pather.Pather").
  qactor( datacleaner, ctxwasteservice_test, "rx.dataCleaner").
  qactor( distancefilter, ctxwasteservice_test, "rx.distanceFilter").
  qactor( basicrobot, ctxwasteservice_test, "it.unibo.basicrobot.Basicrobot").
  qactor( envsonarhandler, ctxwasteservice_test, "it.unibo.envsonarhandler.Envsonarhandler").
