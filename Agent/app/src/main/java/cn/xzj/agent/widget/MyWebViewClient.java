package cn.xzj.agent.widget;

import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;


/**
 * Created by channey on 2018/3/9.
 */

public class MyWebViewClient extends WebChromeClient {
    private WebCall webCall;
    private ProgressChangeListener mProgressChangeListener;

    public void setWebCall(WebCall webCall) {
        this.webCall = webCall;
    }

    public void setProgressChangeListener(ProgressChangeListener listener) {
        this.mProgressChangeListener = listener;
    }

    // For Android 3.0+
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        if (webCall != null)
            webCall.fileChose(uploadMsg);
    }

    // For Android < 3.0
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        openFileChooser(uploadMsg, "");
    }

    // For Android > 4.1.1
    public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                String acceptType, String capture) {
        openFileChooser(uploadMsg, acceptType);
    }

    // For Android > 5.0
    @Override
    public boolean onShowFileChooser(WebView webView,
                                     ValueCallback<Uri[]> filePathCallback,
                                     FileChooserParams fileChooserParams) {
        if (webCall != null)
            webCall.fileChose5(filePathCallback);
        return true;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        mProgressChangeListener.onProgressChanged(view,newProgress);
    }

    public interface WebCall {
        void fileChose(ValueCallback<Uri> uploadMsg);

        void fileChose5(ValueCallback<Uri[]> uploadMsg);
    }

    public interface ProgressChangeListener{
        void onProgressChanged(WebView view, int newProgress);
    }
}
