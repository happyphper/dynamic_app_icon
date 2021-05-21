import 'dart:async';

import 'package:flutter/services.dart';

class DynamicAppIcon {
  static const MethodChannel _channel = const MethodChannel('dynamic_app_icon');

  static Future<void> updateIcon(String name) async {
    Map<String, dynamic> data = {"name": name};

    await _channel.invokeMethod('updateIcon', data);
  }
}
