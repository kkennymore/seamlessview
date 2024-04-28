import UIKit
import WebKit

class SeamlessView: WKWebView, WKNavigationDelegate {

    static let CHANNEL = "com.hitek/seamlessview"

    var methodChannel: FlutterMethodChannel

    init(frame: CGRect, configuration: WKWebViewConfiguration, flutterEngine: FlutterEngine) {
        methodChannel = FlutterMethodChannel(name: SeamlessView.CHANNEL, binaryMessenger: flutterEngine.binaryMessenger)
        methodChannel.setMethodCallHandler({ [weak self] (call, result) in
            self?.handleMethodCall(call, result)
        })
        super.init(frame: frame, configuration: configuration)
        navigationDelegate = self
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    func handleMethodCall(_ call: FlutterMethodCall, _ result: @escaping FlutterResult) {
        switch call.method {
        case "setUrl":
            if let url = call.arguments as? String {
                load(URLRequest(url: URL(string: url)!))
                result(true)
            } else {
                result(FlutterMethodNotImplemented)
            }
        case "setJavaScriptEnabled":
            if let enabled = call.arguments as? Bool {
                configuration.preferences.javaScriptEnabled = enabled
                result(true)
            } else {
                result(FlutterMethodNotImplemented)
            }
        case "setDomStorageEnabled":
            if let enabled = call.arguments as? Bool {
                configuration.preferences.domStorageEnabled = enabled
                result(true)
            } else {
                result(FlutterMethodNotImplemented)
            }
        case "setDatabaseEnabled":
            if let enabled = call.arguments as? Bool {
                configuration.preferences.databaseEnabled = enabled
                result(true)
            } else {
                result(FlutterMethodNotImplemented)
            }
        case "setCacheMode":
            if let mode = call.arguments as? Int {
                configuration.preferences.cacheModel = WKCacheModel(rawValue: mode)!
                result(true)
            } else {
                result(FlutterMethodNotImplemented)
            }
        case "setPageBackground":
            if let color = call.arguments as? UIColor {
                backgroundColor = color
                result(true)
            } else {
                result(FlutterMethodNotImplemented)
            }
        case "setTextStyle":
            if let textStyle = call.arguments as? String {
                switch textStyle {
                case "bold":
                    let font = webView.font
                    let boldFont = UIFont.boldSystemFont(ofSize: font.pointSize)
                    webView.font = boldFont
                case "italic":
                    let font = webView.font
                    let italicFont = UIFont.italicSystemFont(ofSize: font.pointSize)
                    webView.font = italicFont
                case "normal":
                    let font = webView.font
                    let normalFont = UIFont.systemFont(ofSize: font.pointSize)
                    webView.font = normalFont
                case "strikethrough":
                    webView.stringByEvaluatingJavaScript(from: "document.body.style.textDecoration = 'line-through';")
                case "underline":
                    webView.stringByEvaluatingJavaScript(from: "document.body.style.textDecoration = 'underline';")
                default:
                    result(FlutterMethodNotImplemented)
                    return
                }
                result(true)
            } else {
                result(FlutterMethodNotImplemented)
            }
        case "setRequestInterceptor":
            if let urlPatterns = call.arguments as? [String] {
                let requestInterceptor = RequestInterceptor(urlPatterns: urlPatterns)
                webView.uiDelegate = requestInterceptor
                result(true)
            } else {
                result(FlutterMethodNotImplemented)
            }
        case "setErrorHandler":
            if let errorHandler = call.arguments as? (String, String) -> Void {
                self.errorHandler = errorHandler
                result(true)
            } else {
                result(FlutterMethodNotImplemented)
            }
        default:
            result(FlutterMethodNotImplemented)
        }
    }
}