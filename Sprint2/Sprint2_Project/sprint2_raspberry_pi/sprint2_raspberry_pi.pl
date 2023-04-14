%====================================================================================
% sprint2_raspberry_pi description   
%====================================================================================
context(ctx_raspberrypi, "localhost",  "TCP", "11802").
 qactor( filter_distancechanged, ctx_raspberrypi, "wasteservice.raspberry.sonar.filterDistanceChanged").
  qactor( filter_distancebounds, ctx_raspberrypi, "wasteservice.raspberry.sonar.filterDistanceBounds").
  qactor( filter_textdisplay, ctx_raspberrypi, "wasteservice.raspberry.display.filterTextDisplay").
  qactor( filter_alarm, ctx_raspberrypi, "wasteservice.raspberry.sonar.filterAlarm").
  qactor( sonarinput, ctx_raspberrypi, "wasteservice.raspberry.sonar.sonarSupportBCR").
  qactor( sonar_bcr, ctx_raspberrypi, "it.unibo.sonar_bcr.Sonar_bcr").
  qactor( led_test, ctx_raspberrypi, "it.unibo.led_test.Led_test").
  qactor( display_test, ctx_raspberrypi, "it.unibo.display_test.Display_test").
  qactor( testalarmreceiver, ctx_raspberrypi, "it.unibo.testalarmreceiver.Testalarmreceiver").
  qactor( faketrolley, ctx_raspberrypi, "it.unibo.faketrolley.Faketrolley").
