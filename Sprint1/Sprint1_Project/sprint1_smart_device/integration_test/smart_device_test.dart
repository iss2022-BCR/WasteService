// ignore_for_file: constant_identifier_names

import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:integration_test/integration_test.dart';

import 'package:sprint1_smart_device/main.dart' as app;
import 'package:sprint1_smart_device/model/waste_service/store_request.dart';
import 'package:sprint1_smart_device/model/waste_service/waste_type.dart';

import '../test/mocks/mock_waste_server.dart';

const String HOME_VIEW_TITLE = "Smart Device Simulator";
const String REQUEST_VIEW_TITLE = "Store Request";

const String WASTE_SERVICE_IP = "127.0.0.1";
const int WASTE_SERVICE_PORT = 11800;

const double SR_WEIGHT = 10.0;
const WasteType SR_TYPE = WasteType.PLASTIC;

void main() {
  IntegrationTestWidgetsFlutterBinding.ensureInitialized();
  String wasteServiceIP = WASTE_SERVICE_IP;
  int wasteServicePort = WASTE_SERVICE_PORT;
  double storeRequestWeight = SR_WEIGHT;
  WasteType storeRequestType = SR_TYPE;

  setUpAll(() {
    wasteServiceIP =
        const String.fromEnvironment('IP', defaultValue: WASTE_SERVICE_IP);
    wasteServicePort =
        const int.fromEnvironment('PORT', defaultValue: WASTE_SERVICE_PORT);

    // test
    storeRequestWeight =
        double.tryParse(const String.fromEnvironment('WASTE_WEIGHT')) ??
            SR_WEIGHT;
    try {
      storeRequestType =
          WasteType.values.byName(const String.fromEnvironment('WASTE_TYPE'));
    } catch (e) {
      storeRequestType = SR_TYPE;
    }
  });

  print("[SmartDevice Test] Starting test with the following parameters:");
  print("\tIP = $wasteServiceIP");
  print("\tPort = $wasteServicePort");
  print("\tPortStoreRequest_Weight = $storeRequestWeight");
  print("\tStoreRequest_Type = $storeRequestType");

  // Default: run mock server on localhost
  if (wasteServiceIP == WASTE_SERVICE_IP) {
    MockWasteServer mockServer = MockWasteServer();
    mockServer.startServer(wasteServicePort, (data, sock, iSock) {
      StoreRequest receivedSR =
          StoreRequest.fromQAKString(String.fromCharCodes(data));

      print(mockServer.getCurrentStorage());
      if (mockServer.canStore(receivedSR.wasteWeight, receivedSR.wasteType)) {
        print("[Mock_WasteServer] Load accepted.");
        mockServer.deposit(receivedSR.wasteWeight, receivedSR.wasteType);
        sock.write("LoadAccepted");
      } else {
        print("[Mock_WasteServer] Load rejected.");
        sock.write("LoadRejected");
      }
    }, onConnect: (sock, iSock) {
      print(
          "[Mock_WasteServer] Accepted connection #$iSock from client ${sock.remoteAddress.address}:${sock.port}.");
    }, onDisconnect: (sock, iSock) {
      print(
          "[Mock_WasteServer] Client ${sock.remoteAddress.address}:${sock.port} disconnected.");
    }).then((_) {
      print("[Mock_WasteServer] Listening for connections...");
    });
  }

  testWidgets("Test Run", (WidgetTester tester) async {
    app.main();

    await tester.pumpAndSettle();

    // ViewHome ================================================================
    expect(find.text(HOME_VIEW_TITLE), findsOneWidget);
    // Verify that there's a text input field for the IP address
    expect(find.byKey(const ValueKey('connectionIP')), findsOneWidget);
    expect(find.text('IP address'), findsOneWidget);
    // Verify that there's a text input field for the port
    expect(find.byKey(const ValueKey('connectionPort')), findsOneWidget);
    expect(find.text('Port'), findsOneWidget);
    // Verify the two input fields are empty
    expect(find.text(''), findsNWidgets(2));
    // Verify that there's a button to connect
    expect(find.byKey(const ValueKey('connect')), findsOneWidget);
    await Future.delayed(const Duration(seconds: 1));

    // Connection Failed =======================================================
    // Enter wrong IP and port
    await tester.enterText(
        find.byKey(const ValueKey('connectionIP')), "1.1.1.1");
    expect(find.text('1.1.1.1'), findsOneWidget);
    await Future.delayed(const Duration(seconds: 1));

    // Tap the 'Connect' button
    await tester.tap(find.byKey(const ValueKey('connect')));
    await tester.pumpAndSettle();

    // Verify that the connection failed and an Alert Dialog Box is being shown
    expect(find.text('Connection Failed'), findsOneWidget);
    await Future.delayed(const Duration(seconds: 1));

    await tester.tap(find.text('OK'));
    await tester.pump();
    await Future.delayed(const Duration(seconds: 1));

    // Connection Succeeded ====================================================
    // Type WASTE_SERVICE_IP in the IP field
    await tester.enterText(
        find.byKey(const ValueKey('connectionIP')), wasteServiceIP);

    // Type WASTE_SERVICE_PORT in the Port field
    await tester.enterText(find.byKey(const ValueKey('connectionPort')),
        wasteServicePort.toString());
    await Future.delayed(const Duration(seconds: 1));

    // Tap the 'Connect' button
    await tester.tap(find.byKey(const ValueKey('connect')));
    await tester.pumpAndSettle();

    // Verify that the connection was successful
    expect(find.text('Store Request'), findsOneWidget);
    await Future.delayed(const Duration(seconds: 1));

    // Store Request ===========================================================
    expect(find.text('Store Request'), findsOneWidget);
    // Verify that there's a text input field for the Waste Weight parameter
    expect(find.byKey(const ValueKey('parameterWasteWeight')), findsOneWidget);
    expect(find.text('Waste Weight'), findsOneWidget);
    // Verify that there's a text input field for the port
    expect(find.byKey(const ValueKey('parameterWasteType')), findsOneWidget);
    expect(find.text('Waste Type'), findsOneWidget);
    await Future.delayed(const Duration(seconds: 1));

    // Send a StoreRequest and receive a Reply =================================
    // Type SR_WEIGHT in the Waste Weight field
    await tester.enterText(find.byKey(const ValueKey('parameterWasteWeight')),
        storeRequestWeight.toString());
    // Select SR_TYPE in the Waste Type field
    await tester.tap(find.byKey(const ValueKey('parameterWasteType')));
    await tester.pumpAndSettle();
    await tester.tap(find.text(storeRequestType.name).last);
    await tester.pumpAndSettle();
    // Verify the Store Request parameters
    expect(find.text(storeRequestWeight.toString()), findsOneWidget);
    expect(find.text(storeRequestType.name), findsOneWidget);
    await Future.delayed(const Duration(seconds: 1));

    // Tap the 'Send Store Request' button
    await tester.tap(find.byKey(const ValueKey('sendStoreRequest')));
    await tester.pumpAndSettle();

    // Verify that we received a reply (positive or negative)
    expect(
        find.textContaining(RegExp(r'Accepted|Rejected', caseSensitive: false)),
        findsOneWidget);

    await Future.delayed(const Duration(seconds: 3));
  });
}
