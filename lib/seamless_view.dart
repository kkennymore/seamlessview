import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:flutter/material.dart';

class SeamlessView extends StatefulWidget {
  final String? url;
  final bool? javaScriptEnabled;
  final bool? domStorageEnabled;
  final bool? databaseEnabled;
  final int? cacheMode;
  final String? textStyle;
  final List<String>? urlPatterns;
  final Function? errorHandler;
  const SeamlessView({
    super.key,
    required this.url,
    this.javaScriptEnabled = true,
    this.domStorageEnabled = true,
    this.databaseEnabled = true,
    this.cacheMode = 1,
    this.textStyle = 'normal',
    this.urlPatterns = const [],
    this.errorHandler,
  });

  @override
  State<SeamlessView> createState() => _SeamlessViewState();
}

class _SeamlessViewState extends State<SeamlessView> {

  @override
void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
        initializePlatform();
    });
}
  @override
  Widget build(BuildContext context) {
    return const Scaffold(
      body: Center(
        child: Text('Loading...'),
      ),
    );
  }

  Future<void> initializePlatform() async {
    
    if (Platform.isIOS) {
      // Initialize iOS-specific code
      await _initializeIOS();
    } else if (Platform.isAndroid) {
      // Initialize Android-specific code
        await _initializeAndroid();
    }
  }

  Future<void> _initializeIOS() async {
    // Similar iOS-specific initialization can be placed here
    print('iOS Initialization Placeholder');
  }

  Future<void> _initializeAndroid() async {
    const platform = MethodChannel('com.hitek/seamlessview');
    try {
      final result = await platform.invokeMethod('seamlessInit', <String, dynamic>{
        'url': widget.url,
        'javaScriptEnabled': widget.javaScriptEnabled,
        'domStorageEnabled': widget.domStorageEnabled,
        'databaseEnabled': widget.databaseEnabled,
        'cacheMode': widget.cacheMode,
        'textStyle': widget.textStyle,
        'urlPatterns': widget.urlPatterns?.join(','), // Join patterns to a string if list is not null
      });
      print('Initialization successful KKKKKKKKKKKK: $result');
    } on PlatformException catch (e) {
      print('Failed to initialize: ${e.message}');
      if (widget.errorHandler != null) {
        widget.errorHandler!();
      }
    }
  }
}
