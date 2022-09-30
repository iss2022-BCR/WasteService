import 'package:flutter/material.dart';

import 'view/view_server.dart';
//import 'view/view_test.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Echo Waste Server',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const ViewServer(),
    );
  }
}
