import 'dart:io';

import 'package:flutter/material.dart';
import 'package:intl/intl.dart';

import 'view/view_home.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'SmartDevice Simulator',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const ViewHome(),
    );
  }
}
