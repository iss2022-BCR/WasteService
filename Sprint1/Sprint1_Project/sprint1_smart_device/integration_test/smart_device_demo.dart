// ignore_for_file: constant_identifier_names

import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:integration_test/integration_test.dart';

import 'package:sprint1_smart_device/main.dart' as app;
import 'package:sprint1_smart_device/model/appl_message.dart';
import 'package:sprint1_smart_device/model/waste_service/store_request.dart';

import '../test/mocks/mock_waste_server.dart';

const String HOME_VIEW_TITLE = "Smart Device Simulator";
const String REQUEST_VIEW_TITLE = "Store Request";

const String WASTE_SERVICE_IP = "127.0.0.1";
const int WASTE_SERVICE_PORT = 11780;

const String SR_TYPE = "PLASTIC";
const double SR_WEIGHT = 10.0;

void main() {
  IntegrationTestWidgetsFlutterBinding.ensureInitialized();
  String wasteServiceIP = WASTE_SERVICE_IP;
  int wasteServicePort = WASTE_SERVICE_PORT;
  String storeRequestType = SR_TYPE;
  double storeRequestWeight = SR_WEIGHT;

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
      storeRequestType = const String.fromEnvironment('WASTE_TYPE');
    } catch (e) {
      storeRequestType = SR_TYPE;
    }
  });

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
    await tester.pumpAndSettle(const Duration(seconds: 1));

    // Verify that the connection was successful
    expect(find.text('Store Request'), findsOneWidget);
    await Future.delayed(const Duration(seconds: 3));

    // Store Request ===========================================================
    expect(find.text('Store Request'), findsOneWidget);
    // Verify that there's a text input field for the port
    expect(find.byKey(const ValueKey('parameterWasteType')), findsOneWidget);
    expect(find.text('Waste Type'), findsOneWidget);
    // Verify that there's a text input field for the Waste Weight parameter
    expect(find.byKey(const ValueKey('parameterWasteWeight')), findsOneWidget);
    expect(find.text('Waste Weight'), findsOneWidget);

    await Future.delayed(const Duration(seconds: 1));

    // Send a StoreRequest and receive a Reply =================================
    // Select SR_TYPE in the Waste Type field
    await tester.tap(find.byKey(const ValueKey('parameterWasteType')));
    await tester.pumpAndSettle();
    await tester.tap(find.text(storeRequestType).last);
    await tester.pumpAndSettle();
    // Type SR_WEIGHT in the Waste Weight field
    await tester.enterText(find.byKey(const ValueKey('parameterWasteWeight')),
        storeRequestWeight.toString());
    // Verify the Store Request parameters
    expect(find.text(storeRequestWeight.toString()), findsOneWidget);
    expect(find.text(storeRequestType), findsOneWidget);

    // Tap the 'Send Store Request' button
    await tester.tap(find.byKey(const ValueKey('sendStoreRequest')));

    //await Future.delayed(const Duration(seconds: 5));
    await tester.pumpAndSettle(const Duration(seconds: 5));

    // Verify that we received a reply (positive or negative)
    expect(
        find.textContaining(RegExp(r'Accepted|Rejected', caseSensitive: false)),
        findsOneWidget);

    await Future.delayed(const Duration(seconds: 3));
  });
}
