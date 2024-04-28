package com.hitek.seamlessview;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.content.Context;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import android.widget.Toast;

public class SeamlessView extends WebView implements MethodCallHandler {
    private static final String CHANNEL = "com.hitek/seamlessview";
    private MethodChannel methodChannel;

    public SeamlessView(Context context, FlutterEngine flutterEngine) {
        super(context);
        methodChannel = new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL);
        methodChannel.setMethodCallHandler(this);
        initWebViewSettings();
    }

    private void initWebViewSettings() {
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true); // Default setting, adjust as necessary
        settings.setDomStorageEnabled(true); // Enable DOM storage by default
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        switch (call.method) {
            case "seamlessInit":
                String url = call.argument("url");
                loadUrl(url);
                result.success(true);
                break;
            case "setUrl":
                String url2 = call.argument("url");
                loadUrl(url2);
                result.success(true);
                break;
            case "setJavaScriptEnabled":
                boolean javaScriptEnabled = call.argument("enabled");
                getSettings().setJavaScriptEnabled(javaScriptEnabled);
                result.success(true);
                break;
            case "setDomStorageEnabled":
                boolean domStorageEnabled = call.argument("enabled");
                getSettings().setDomStorageEnabled(domStorageEnabled);
                result.success(true);
                break;
            case "setDatabaseEnabled":
                boolean databaseEnabled = call.argument("enabled");
                getSettings().setDatabaseEnabled(databaseEnabled);
                result.success(true);
                break;
            case "setCacheMode":
                int cacheMode = call.argument("mode");
                getSettings().setCacheMode(cacheMode);
                result.success(true);
                break;
            case "setPageBackground":
                int color = call.argument("color");
                setBackgroundColor(color);
                result.success(true);
                break;
            case "setTextStyle":
                String textStyle = call.argument("style");
                WebSettings settings = getSettings();
                settings.setDefaultFontSize(16);
                settings.setBoldText(false);
                settings.setItalicText(false);
                if (textStyle.equals("bold")) {
                    settings.setBoldText(true);
                } else if (textStyle.equals("italic")) {
                    settings.setItalicText(true);
                } else if (textStyle.equals("normal")) {
                    // Do nothing, default font is normal
                } else if (textStyle.equals("strikethrough")) {
                    loadUrl("javascript:document.body.style.textDecoration='line-through';");
                } else if (textStyle.equals("underline")) {
                    loadUrl("javascript:document.body.style.textDecoration='underline';");
                } else {
                    // Handle unknown text style
                    result.error("unknown_text_style", "Unknown text style: " + textStyle, null);
                }
                result.success(true);
                break;
            case "setRequestInterceptor":
                WebViewClient requestInterceptor = new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                        String url = request.getUrl().toString();
                        if (url.startsWith("(link unavailable)")) {
                            // Handle requests to (link unavailable)
                            return true;
                        } else if (url.startsWith("http://")) {
                            // Allow HTTP requests to proceed
                            return false;
                        } else if (url.startsWith("https://")) {
                            // Allow HTTPS requests to proceed
                            return false;
                        } else {
                            // Block unknown requests
                            return true;
                        }
                    }
                };
                setWebViewClient(requestInterceptor);
                result.success(true);
                break;
            case "setErrorHandler":
                WebViewClient errorHandler = new WebViewClient() {
                    @Override
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        // Implement error handler logic here
                        Toast.makeText(getContext(), "Error loading page: " + description, Toast.LENGTH_SHORT).show();
                        // Handle error further if needed
                    }
                };
                setWebViewClient(errorHandler);
                result.success(true);
                break;
            case "setUserAgent":
                String userAgent = call.argument("userAgent");
                getSettings().setUserAgentString(userAgent);
                result.success(true);
                break;
            case "setSupportZoom":
                boolean supportZoom = call.argument("supportZoom");
                getSettings().setSupportZoom(supportZoom);
                result.success(true);
                break;
            case "setBuiltInZoomControls":
                boolean builtInZoomControls = call.argument("builtInZoomControls");
                getSettings().setBuiltInZoomControls(builtInZoomControls);
                result.success(true);
                break;
            case "setDisplayZoomControls":
                boolean displayZoomControls = call.argument("displayZoomControls");
                getSettings().setDisplayZoomControls(displayZoomControls);
                result.success(true);
                break;
            case "setLoadWithOverviewMode":
                boolean loadWithOverviewMode = call.argument("loadWithOverviewMode");
                getSettings().setLoadWithOverviewMode(loadWithOverviewMode);
                result.success(true);
                break;
            case "setUseWideViewPort":
                boolean useWideViewPort = call.argument("useWideViewPort");
                getSettings().setUseWideViewPort(useWideViewPort);
                result.success(true);
                break;
            case "reload":
                reload();
                result.success(true);
                break;
            case "showUrlBar":
                boolean showUrlBar = call.argument("showUrlBar");
                if (showUrlBar) {
                    // Show the URL bar
                    ViewGroup.LayoutParams layoutParams = getLayoutParams();
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    setLayoutParams(layoutParams);
                } else {
                    // Hide the URL bar
                    ViewGroup.LayoutParams layoutParams = getLayoutParams();
                    layoutParams.height = 0;
                    setLayoutParams(layoutParams);
                }
                result.success(true);
                break;
            default:
                result.notImplemented();
                break;
        }
    }

    @Override
    public void onMethodCallWithContext(MethodCall call, Result result, MethodCallHandlerContext context) {
        onMethodCall(call, result);
    }
}
