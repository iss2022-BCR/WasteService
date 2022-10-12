// ignore_for_file: library_prefixes

import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:sprint1_smart_device/model/waste_service/store_request.dart';
import 'package:sprint1_smart_device/model/waste_service/waste_type.dart';

import '../mocks/views/view_home_mock.dart';
import '../mocks/views/view_request_mock.dart';

import 'package:sprint1_smart_device/model/constants.dart' as Constants;

const String correctIP = "127.0.0.1";
const int correctPort = 11800;

const double weightAccepted = 10.0;
const WasteType typeAccepted = WasteType.PLASTIC;
const double weightRejected = 5000.0;
const WasteType typeRejected = WasteType.GLASS;

Widget createMockViewHome() {
  return MaterialApp(
    title: 'SmartDevice Simulator',
    theme: ThemeData(
      primarySwatch: Colors.blue,
    ),
    home: const ViewHomeMock(
        correctIP: correctIP, correctPort: correctPort, timeoutSeconds: 5),
  );
}

Widget createMockViewRequest() {
  return MaterialApp(
    title: 'SmartDevice Simulator',
    theme: ThemeData(
      primarySwatch: Colors.blue,
    ),
    home: ViewRequestMock(
      storeRequestAccepted: StoreRequest(weightAccepted, typeAccepted),
      storeRequestRejected: StoreRequest(weightRejected, typeRejected),
      waitingTimeoutSeconds: 5,
    ),
  );
}

void main() async {
  // ViewHome Test =============================================================
  // Test #1: Validation connection parameters (IP and Port)
  testWidgets('ViewHome: IP address and Port validation',
      (WidgetTester tester) async {
    // Build our app and trigger a frame.
    await tester.pumpWidget(createMockViewHome());

    // Verify the page displayed
    expect(find.text(Constants.titleHome), findsOneWidget);

    // Verify that there are 2 input fields (IP address and port) and they're empty.
    expect(find.byKey(const ValueKey('connectionIP')), findsOneWidget);
    expect(find.byKey(const ValueKey('connectionPort')), findsOneWidget);
    expect(find.text(''), findsNWidgets(2));

    // Enter and invalid address in the IP input field.
    await tester.enterText(
        find.byKey(const ValueKey('connectionIP')), '1000.0.0.0');
    // Tap the Connect button.
    await tester.tap(find.byKey(const ValueKey('connect')));
    await tester.pump();

    // Verify that the IP address field is invalid.
    expect(find.text('Invalid IPv4 address'), findsOneWidget);

    // Enter an invalid value in the port input field.
    await tester.enterText(
        find.byKey(const ValueKey('connectionPort')), '123456');
    // Tap the Connect button.
    await tester.tap(find.byKey(const ValueKey('connect')));
    await tester.pump();

    // Verify that the port field is invalid.
    expect(find.text('Invalid port'), findsOneWidget);
  });

  // Test #2: Loading is displayed while attempting to connect
  testWidgets(
      'ViewHome: Display loading indicator while waiting for the connection attempt to succeed',
      (WidgetTester tester) async {
    // Build our app and trigger a frame.
    await tester.pumpWidget(createMockViewHome());

    // Verify the page displayed
    expect(find.text(Constants.titleHome), findsOneWidget);

    // Enter a wrong address in the IP input field, so the connection attempt takes some time
    await tester.enterText(
        find.byKey(const ValueKey('connectionIP')), '192.0.0.0');

    // Tap the Connect button.
    await tester.tap(find.byKey(const ValueKey('connect')));

    // Advance time of a single frame to show the waiting message
    await tester.pump();

    // Verify that the loading indicator is present.
    expect(find.byType(CircularProgressIndicator), findsOneWidget);
    expect(find.text('Connection Attempt...'), findsOneWidget);

    // Advance time for timer to end
    await tester.pumpAndSettle();
  });

  // Test #3: Connection failed
  testWidgets(
      'ViewHome: Connection fails if the Server is offline or unreachable',
      (WidgetTester tester) async {
    // Build our app and trigger a frame.
    await tester.pumpWidget(createMockViewHome());

    // Verify the page displayed
    expect(find.text(Constants.titleHome), findsOneWidget);

    // Enter a wrong address in the IP input field.
    await tester.enterText(
        find.byKey(const ValueKey('connectionIP')), '192.0.0.0');

    // Tap the Connect button.
    await tester.tap(find.byKey(const ValueKey('connect')));
    await tester.pumpAndSettle();

    // Verify that the connection failed.
    expect(find.text('Connection Failed'), findsOneWidget);
  });

  // Test #4: Connection succeeded
  testWidgets('ViewHome: Connection succeeds if the Server is online',
      (WidgetTester tester) async {
    // Build our app and trigger a frame.
    await tester.pumpWidget(createMockViewHome());

    // Verify the page displayed
    expect(find.text(Constants.titleHome), findsOneWidget);

    // Enter a correct address in the IP input field.
    await tester.enterText(
        find.byKey(const ValueKey('connectionIP')), correctIP);
    // Enter a correct value in the port input field.
    await tester.enterText(
        find.byKey(const ValueKey('connectionPort')), correctPort.toString());

    // Tap the Connect button.
    await tester.tap(find.byKey(const ValueKey('connect')));
    await tester.pumpAndSettle();

    // Verify that the connection succeeded.
    expect(find.text('Connection Succeeded'), findsOneWidget);
  });

  // ViewRequest ===============================================================
  // Test #5: Validation StoreRequest (wasteWeight and wasteType)
  testWidgets('ViewRequest: StoreRequest validation',
      (WidgetTester tester) async {
    // Build our app and trigger a frame.
    await tester.pumpWidget(createMockViewRequest());

    // Verify the page displayed
    expect(find.text(Constants.titleRequest), findsOneWidget);

    // Verify that there are 2 input fields (wasteWeight and wasteType).
    expect(find.byKey(const ValueKey('parameterWasteWeight')), findsOneWidget);
    expect(find.byKey(const ValueKey('parameterWasteType')), findsOneWidget);

    // Enter and invalid value in the weight input field.
    await tester.enterText(
        find.byKey(const ValueKey('parameterWasteWeight')), '0.0');
    // Tap the Send button.
    await tester.tap(find.byKey(const ValueKey('sendStoreRequest')));
    await tester.pump();

    // Verify that the weight field is invalid.
    expect(find.text('Invalid weight'), findsOneWidget);
  });

  // Test #6: Receive a LoadRejected
  testWidgets('ViewRequest: StoreRequest rejected',
      (WidgetTester tester) async {
    // Build our app and trigger a frame.
    await tester.pumpWidget(createMockViewRequest());

    // Verify the page displayed
    expect(find.text(Constants.titleRequest), findsOneWidget);

    // Enter too much weight (the container can NOT store) in the Waste Weight input field
    await tester.enterText(find.byKey(const ValueKey('parameterWasteWeight')),
        weightRejected.toString());

    // Tap the dropdown widget to open the menu
    await tester.tap(find.byKey(const ValueKey('parameterWasteType')));
    await tester.pump();
    // Choose a waste type
    await tester.tap(find.text(typeRejected.name).last);
    await tester.pump();

    // Tap the Send button.
    await tester.tap(find.byKey(const ValueKey('sendStoreRequest')));
    await tester.pumpAndSettle();

    // Verify that we've received a negative answer
    expect(find.text('LoadRejected'), findsOneWidget);
  });

  // Test #7: Waiting for a reply
  testWidgets('ViewRequest: Waiting for a reply', (WidgetTester tester) async {
    // Build our app and trigger a frame.
    await tester.pumpWidget(createMockViewRequest());

    // Verify the page displayed
    expect(find.text(Constants.titleRequest), findsOneWidget);

    // Enter the value in the Waste Weight input field
    await tester.enterText(find.byKey(const ValueKey('parameterWasteWeight')),
        (weightAccepted + 1.0).toString());

    // Tap the dropdown widget to open the menu
    await tester.tap(find.byKey(const ValueKey('parameterWasteType')));
    await tester.pump();
    // Choose a waste type
    await tester.tap(find.text(typeRejected.name).last);
    await tester.pump();

    // Tap the Send button.
    await tester.tap(find.byKey(const ValueKey('sendStoreRequest')));
    await tester.pump();

    // Verify that we're waiting for a reply
    expect(find.textContaining('Waiting.'), findsOneWidget);

    // Advance time for timer to end
    await tester.pumpAndSettle(const Duration(seconds: 6));
  });

  // Test #8: Receive a LoadAccepted
  testWidgets('ViewRequest: StoreRequest accepted',
      (WidgetTester tester) async {
    // Build our app and trigger a frame.
    await tester.pumpWidget(createMockViewRequest());

    // Verify the page displayed
    expect(find.text(Constants.titleRequest), findsOneWidget);

    // Enter an OK amount of weight (that the container can store) in the Waste Weight input field
    await tester.enterText(find.byKey(const ValueKey('parameterWasteWeight')),
        weightAccepted.toString());

    // Tap the dropdown widget to open the menu
    await tester.tap(find.byKey(const ValueKey('parameterWasteType')));
    await tester.pump();
    // Choose a waste type
    await tester.tap(find.text(typeAccepted.name).last);
    await tester.pump();

    // Tap the Send button.
    await tester.tap(find.byKey(const ValueKey('sendStoreRequest')));
    await tester.pump();

    // Verify that we've received a positive answer
    expect(find.textContaining('LoadAccepted'), findsOneWidget);
  });
}
