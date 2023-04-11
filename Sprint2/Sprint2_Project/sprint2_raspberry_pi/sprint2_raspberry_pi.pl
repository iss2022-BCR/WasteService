%====================================================================================
% sprint2_raspberry_pi description   
%====================================================================================
context(ctx_raspberrypi, "localhost",  "TCP", "11802").
 qactor( sonardatasource_concrete, ctx_raspberrypi, "wasteservice.raspberry.sonar.sonarDataSourceHCSR04").
  qactor( sonardatasource_mock, ctx_raspberrypi, "wasteservice.raspberry.sonar.sonarDataSourceMock").
  qactor( filterdistancechanged, ctx_raspberrypi, "wasteservice.raspberry.sonar.filterDistanceChanged").
  qactor( filterdistancebounds, ctx_raspberrypi, "wasteservice.raspberry.sonar.filterDistanceBounds").
  qactor( filteralarm, ctx_raspberrypi, "wasteservice.raspberry.sonar.filterAlarm").
  qactor( sonar_bcr, ctx_raspberrypi, "it.unibo.sonar_bcr.Sonar_bcr").
  qactor( testalarmreceiver, ctx_raspberrypi, "it.unibo.testalarmreceiver.Testalarmreceiver").
