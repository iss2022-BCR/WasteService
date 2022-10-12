import 'package:sprint1_smart_device/model/networking/client_connection.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:sprint1_smart_device/model/networking/tcp_client_connection.dart';
import 'package:sprint1_smart_device/model/waste_service/store_request.dart';
import 'package:sprint1_smart_device/model/waste_service/waste_type.dart';

import '../mocks/mock_waste_server.dart';

const String ip = "127.0.0.1";
const int port = 11800;

void main() {
  bool serverConnect = false,
      serverDisconnect = false,
      clientConnect = false,
      clientDisconnect = false;
  String reply = "";

  group('[TCP Interaction Test]', () {
    // start server
    print("[Mock_WasteServer] Starting...");
    MockWasteServer mockServer = MockWasteServer();
    mockServer.startServer(port, (data, sock, iSock) {
      StoreRequest receivedSR =
          StoreRequest.fromJsonString(String.fromCharCodes(data));

      print(mockServer.getCurrentStorage());
      if (mockServer.canStore(receivedSR.wasteType, receivedSR.wasteWeight)) {
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
      serverConnect = true;
    }, onDisconnect: (sock, iSock) {
      print(
          "[Mock_WasteServer] Client ${sock.remoteAddress.address}:${sock.port} disconnected.");
      serverDisconnect = true;
    }).then((_) {
      print("[Mock_WasteServer] Listening for connections...");
    });

    // Start SmartDevice (Client)
    ClientConnection connection = TcpClientConnection();
    print("[Mock_SmartDevice] Starting...");

    test('Connect', () async {
      print(
          "[Mock_SmartDevice] Attempting a connection to Mock_WasteService...");
      await connection.connect(ip, port).then((value) {
        print("[Mock_SmartDevice] Connected to $ip:$port.");
        clientConnect = true;
      });

      expect(clientConnect, true);

      await Future.delayed(const Duration(seconds: 1));

      expect(serverConnect, true);

      connection.listen((data) {
        print(String.fromCharCodes(data));
        reply = String.fromCharCodes(data);
      });
    });

    test('Store Request Succeeds', () async {
      StoreRequest sr = StoreRequest(WasteType.PLASTIC, 10.0);

      connection.sendMessage(sr.toJsonString());
      print("[Mock_SmartDevice] Sent Store Request ${sr.toJsonString()}");
      await Future.delayed(const Duration(seconds: 1));

      expect(reply.toLowerCase() == "loadaccepted", true);
    });

    test('Store Request Fails', () async {
      StoreRequest sr = StoreRequest(WasteType.GLASS, 1000.0);

      connection.sendMessage(sr.toJsonString());
      print("[Mock_SmartDevice] Sent Store Request ${sr.toJsonString()}");
      await Future.delayed(const Duration(seconds: 1));

      expect(reply.toLowerCase() == "loadrejected", true);
    });

    test('Disconnect', () async {
      await connection.close().then((value) {
        clientDisconnect = true;
      });

      expect(clientDisconnect, true);

      await Future.delayed(const Duration(seconds: 1));

      expect(serverDisconnect, true);
    });
  });
}
