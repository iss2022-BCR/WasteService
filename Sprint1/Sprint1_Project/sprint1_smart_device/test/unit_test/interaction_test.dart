import 'package:sprint1_smart_device/model/appl_message.dart';
import 'package:sprint1_smart_device/model/networking/client_connection.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:sprint1_smart_device/model/networking/tcp_client_connection.dart';
import 'package:sprint1_smart_device/model/waste_service/store_request.dart';
import 'package:sprint1_smart_device/model/waste_service/types_request.dart';
import 'package:sprint1_smart_device/model/waste_service/waste_type.dart';

import '../mocks/mock_waste_server.dart';

const String ip = "127.0.0.1";
const int port = 11780; // test port

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
      ApplMessage req = ApplMessage.fromString(String.fromCharCodes(data));

      // TypesRequest
      if (req.msgId.toLowerCase() == ("typesrequest")) {
        print("[Mock_WasteServer] Received TypesRequest.");

        print("[Mock_WasteServer] Replied with waste types list: ");
        ApplMessage msg = ApplMessage.fromString(
            "msg(typesreply, reply, typesprovider, smartdevice, typesreply(${mockServer.getTypesListString("_")}), ${req.msgNum + 1})");
        sock.write(msg.toString());
      } else
      // StoreRequest
      if (req.msgId.toLowerCase() == ("storerequest")) {
        StoreRequest sr = StoreRequest.fromQAKString(req.toString());
        if (mockServer.canPreStore(sr.wasteType, sr.wasteWeight)) {
          mockServer.addToPreStorage(sr.wasteType, sr.wasteWeight);
          print("[Mock_WasteServer] Load accepted.");
          sock.write("LoadAccepted");
          mockServer.addToStorage(sr.wasteType, sr.wasteWeight);
        } else {
          print("[Mock_WasteServer] Load rejected.");
          sock.write("LoadRejected");
        }
      }

      /*
      // Old code
      StoreRequest receivedSR =
          StoreRequest.fromQAKString(String.fromCharCodes(data));

      print(mockServer.getFullStatusString());
      if (mockServer.canStore(receivedSR.wasteType, receivedSR.wasteWeight)) {
        print("[Mock_WasteServer] Load accepted.");
        mockServer.deposit(receivedSR.wasteWeight, receivedSR.wasteType);
        sock.write("LoadAccepted");
      } else {
        print("[Mock_WasteServer] Load rejected.");
        sock.write("LoadRejected");
      }*/
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

    test('Types Request Succeeds', () async {
      TypesRequest tr = TypesRequest();

      connection.sendMessage(tr.toQAKString("smartdevice", "typesprovider"));
      print(
          "[Mock_SmartDevice] Sent TypesRequest ${tr.toQAKString("smartdevice", "typesprovider")}");
      await Future.delayed(const Duration(seconds: 1));

      expect(reply.contains("typesreply"), true);
      for (WasteType ws in WasteType.values) {
        expect(reply.contains(ws.name), true);
      }
    });

    test('Store Request Succeeds', () async {
      StoreRequest sr = StoreRequest(WasteType.PLASTIC.name, 10.0);

      connection.sendMessage(sr.toQAKString("smartdevice", "wasteservice", 3));
      print(
          "[Mock_SmartDevice] Sent Store Request ${sr.toQAKString("smartdevice", "wasteservice", 3)}");
      await Future.delayed(const Duration(seconds: 1));

      expect(reply.toLowerCase().contains("loadaccepted"), true);
    });

    test('Store Request Fails', () async {
      StoreRequest sr = StoreRequest(WasteType.GLASS.name, 1000.0);

      connection.sendMessage(sr.toQAKString("smartdevice", "wasteservice", 5));
      print(
          "[Mock_SmartDevice] Sent Store Request ${sr.toQAKString("smartdevice", "wasteservice", 5)}");
      await Future.delayed(const Duration(seconds: 1));

      expect(reply.toLowerCase().contains("loadrejected"), true);
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
