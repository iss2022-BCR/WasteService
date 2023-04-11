%====================================================================================
% sprint2_raspberry_pi description   
%====================================================================================
context(ctx_raspberrypi, "localhost",  "TCP", "11802").
 qactor( sonardatasource_concrete, ctx_raspberrypi, "wasteservice.raspberry.sonar.sonarDataSourceHCSR04").
  qactor( sonardatasource_mock, ctx_raspberrypi, "wasteservice.raspberry.sonar.sonarDataSourceMock").
  qactor( filter_distancechanged, ctx_raspberrypi, "wasteservice.raspberry.sonar.filterDistanceChanged").
  qactor( filter_distancebounds, ctx_raspberrypi, "wasteservice.raspberry.sonar.filterDistanceBounds").
  qactor( filter_alarm, ctx_raspberrypi, "wasteservice.raspberry.sonar.filterAlarm").
  qactor( sonar_bcr, ctx_raspberrypi, "it.unibo.sonar_bcr.Sonar_bcr").
  qactor( testalarmreceiver, ctx_raspberrypi, "it.unibo.testalarmreceiver.Testalarmreceiver").
  qactor( led_bcr, ctx_raspberrypi, "it.unibo.led_bcr.Led_bcr").
  qactor( led_test, ctx_raspberrypi, "it.unibo.led_test.Led_test").
