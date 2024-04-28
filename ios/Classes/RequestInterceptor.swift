class RequestInterceptor: NSObject, WKUIDelegate {
    let urlPatterns: [String]

    init(urlPatterns: [String]) {
        self.urlPatterns = urlPatterns
    }

    func webView(_ webView: WKWebView, decidePolicyFor navigationAction: WKNavigationAction, decisionHandler: @escaping (WKNavigationActionPolicy) -> Void) {
        if let url = navigationAction.request.url, urlPatterns.contains(url.absoluteString) {
            decisionHandler(.cancel)
        } else {
            decisionHandler(.allow)
        }
    }
}