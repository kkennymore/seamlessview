package com.hitek.seamlessview;
import android.os.Bundle;
import android.widget.FrameLayout;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "com.hitek/seamlessview";
    private SeamlessView seamlessView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT, 
            FrameLayout.LayoutParams.MATCH_PARENT
        );
        seamlessView = new SeamlessView(this, getFlutterEngine());
        addContentView(seamlessView, layoutParams);
    }

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
            .setMethodCallHandler(
                (call, result) -> {
                    if (call.method.equals("initialize")) {
                        seamlessView.onMethodCall(call, result);
                        result.success("Initialized");
                    } else {
                        result.notImplemented();
                    }
                }
            );
    }
}
