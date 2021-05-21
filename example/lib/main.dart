import 'package:dynamic_app_icon/dynamic_app_icon.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: ListView.builder(
          itemCount: 31,
          itemBuilder: (BuildContext context, int i) {
            return ElevatedButton(
              onPressed: () => DynamicAppIcon.updateIcon("icon$i"),
              child: Text("$i"),
            );
          },
        ),
      ),
    );
  }
}
