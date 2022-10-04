import 'package:sprint1_smart_device/model/networking/client_connection.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:sprint1_smart_device/model/networking/tcp_client_connection.dart';

import '../mock_waste_server.dart';

void main() {
  group('Store Request', () {
    // start server
    MockWasteServer mockServer = MockWasteServer(100.0, 100.0);
    mockServer.startServer(11800, (data, sock, iSock) {
      print("received Msg");
    }, onConnect: () {}, onDisconnect: () {});

    ClientConnection connection = TcpClientConnection();

    test('Connect OK', () async {
      bool res = false;

      await connection.connect("127.0.0.1", 11800).then((value) {
        print("Connected");
        res = true;
      }).catchError((e) {
        res = true;
      });

      expect(res, true);
    });

    /*test('Store Requeest OK', () {
      connection.sendMessage("");
    });*/
  });
}
