import 'package:flutter/material.dart';

import 'view/view_home.dart';

import '../model/constants.dart' as Constants;

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: Constants.appTitle,
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const ViewHome(),
    );
  }
}
