// ignore_for_file: constant_identifier_names

import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:integration_test/integration_test.dart';

import 'package:sprint1_smart_device/main.dart' as app;

const String HOME_VIEW_TITLE = "Smart Device Simulator";
const String REQUEST_VIEW_TITLE = "Store Request";

const String WASTE_SERVICE_IP = "192.168.1.4";
const String WASTE_SERVICE_PORT = "4001";

const String SR_WEIGHT_SUCCESS = "10.0";
const String SR_WEIGHT_FAIL = "10000.0";
const String SR_TYPE = "PLASTIC";

void main() {
  IntegrationTestWidgetsFlutterBinding.ensureInitialized();

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

    // Connection Failed =======================================================
    // Enter wrong IP and port
    await tester.enterText(
        find.byKey(const ValueKey('connectionIP')), "1.1.1.1");
    expect(find.text('1.1.1.1'), findsOneWidget);

    // Tap the 'Connect' button
    await tester.tap(find.byKey(const ValueKey('connect')));
    await tester.pumpAndSettle();

    // Verify that the connection failed and an Alert Dialog Box is being shown
    expect(find.text('Connection Failed'), findsOneWidget);
    await tester.tap(find.text('OK'));
    await tester.pump();

    // Connection Succeeded ====================================================
    // Type WASTE_SERVICE_IP in the IP field
    await tester.enterText(
        find.byKey(const ValueKey('connectionIP')), WASTE_SERVICE_IP);
    // Verify that the text in the IP input field is correct
    expect(find.text(WASTE_SERVICE_IP), findsOneWidget);

    // Type WASTE_SERVICE_PORT in the Port field
    await tester.enterText(
        find.byKey(const ValueKey('connectionPort')), WASTE_SERVICE_PORT);
    // Verify that the text in the port input field is correct
    expect(find.text(WASTE_SERVICE_PORT), findsOneWidget);

    // Tap the 'Connect' button
    await tester.tap(find.byKey(const ValueKey('connect')));
    await tester.pumpAndSettle();

    // Verify that the connection was successful
    expect(find.text('Store Request'), findsOneWidget);

    // Store Request ===========================================================
    expect(find.text('Store Request'), findsOneWidget);
    // Verify that there's a text input field for the Waste Weight parameter
    expect(find.byKey(const ValueKey('parameterWasteWeight')), findsOneWidget);
    expect(find.text('Waste Weight'), findsOneWidget);
    // Verify that there's a text input field for the port
    expect(find.byKey(const ValueKey('parameterWasteType')), findsOneWidget);
    expect(find.text('Waste Type'), findsOneWidget);

    // Send a StoreRequest and receive LoadAccepted ============================
    // Type SR_WEIGHT_SUCCESS in the Waste Weight field
    await tester.enterText(
        find.byKey(const ValueKey('parameterWasteWeight')), SR_WEIGHT_SUCCESS);
    expect(find.text(SR_WEIGHT_SUCCESS), findsOneWidget);
    expect(find.text(SR_TYPE), findsOneWidget);

    // Tap the 'Send Store Request' button
    await tester.tap(find.byKey(const ValueKey('sendStoreRequest')));
    await tester.pumpAndSettle();

    expect(find.text('loadaccepted'), findsOneWidget);

    // Send a StoreRequest and receive LoadRejected ============================
    // Type SR_WEIGHT_FAIL in the Waste Weight field
    await tester.enterText(
        find.byKey(const ValueKey('parameterWasteWeight')), SR_WEIGHT_FAIL);
    expect(find.text(SR_WEIGHT_FAIL), findsOneWidget);
    expect(find.text(SR_TYPE), findsOneWidget);

    // Tap the 'Send Store Request' button
    await tester.tap(find.byKey(const ValueKey('sendStoreRequest')));
    await tester.pumpAndSettle();

    expect(find.text('loadrejected'), findsOneWidget);
  });
}
