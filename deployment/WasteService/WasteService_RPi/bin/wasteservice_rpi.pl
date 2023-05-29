%====================================================================================
% wasteservice_rpi description   
%====================================================================================
context(ctx_wasteservice, "localhost",  "TCP", "11800").
context(ctx_transporttrolley, "localhost",  "TCP", "11801").
context(ctx_robot, "localhost",  "TCP", "8020").
context(ctx_raspberrypi, "localhost",  "TCP", "11802").
 qactor( typesprovider, ctx_wasteservice, "external").
  qactor( wasteservice, ctx_wasteservice, "external").
  qactor( status_controller, ctx_wasteservice, "external").
  qactor( transporttrolley, ctx_transporttrolley, "external").
  qactor( trolleystateprovider, ctx_transporttrolley, "external").
  qactor( pathexecutorbcr, ctx_transporttrolley, "external").
  qactor( basicrobot, ctx_robot, "external").
  qactor( filter_distancechanged, ctx_raspberrypi, "wasteservice.raspberry.sonar.filterDistanceChanged").
  qactor( filter_distancebounds, ctx_raspberrypi, "wasteservice.raspberry.sonar.filterDistanceBounds").
  qactor( filter_textdisplay, ctx_raspberrypi, "wasteservice.raspberry.display.filterTextDisplay").
  qactor( filter_alarm, ctx_raspberrypi, "wasteservice.raspberry.sonar.filterAlarm").
  qactor( sonarinput, ctx_raspberrypi, "wasteservice.raspberry.sonar.sonarSupportBCR").
  qactor( sonar_bcr, ctx_raspberrypi, "it.unibo.sonar_bcr.Sonar_bcr").
  qactor( led_bcr, ctx_raspberrypi, "it.unibo.led_bcr.Led_bcr").
  qactor( textdisplay_bcr, ctx_raspberrypi, "it.unibo.textdisplay_bcr.Textdisplay_bcr").
  qactor( buzzer_bcr, ctx_raspberrypi, "it.unibo.buzzer_bcr.Buzzer_bcr").
