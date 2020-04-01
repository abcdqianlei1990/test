package cn.xzj.agent.net.download;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.TimeUnit;

import cn.xzj.agent.entity.DownloadInfo;
import cn.xzj.agent.net.ApiManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/15
 * @Des
 */
public class DownloadTask {
    private CompositeDisposable mCompositeDisposable;
    private ApiManager mHttpService;
    private DownloadProgressSubscriber mDownloadProgressSubscriber;
    private DownloadInfo mDownloadInfo;

    DownloadTask(DownloadInfo downloadInfo) {
        mCompositeDisposable = new CompositeDisposable();
        this.mDownloadInfo = downloadInfo;
        initService();
    }

    private void initService() {
        mDownloadProgressSubscriber = new DownloadProgressSubscriber(mDownloadInfo);
        DownloadInterceptor interceptor = new DownloadInterceptor(mDownloadProgressSubscriber);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //手动创建一个OkHttpClient并设置超时时间
        builder.connectTimeout(60, TimeUnit.SECONDS);
        builder.addInterceptor(interceptor);
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://www.baidu.com")
                .build();
        mHttpService = retrofit.create(ApiManager.class);
    }

    public void start() {
        /*得到rx对象-上一次下載的位置開始下載*/
        mHttpService.downloadFile("bytes=" + mDownloadInfo.getReadLength() + "-", mDownloadInfo.getUrl())
                /*指定线程*/
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*失败后的retry配置*/
//                .retryWhen(new RetryWhenNetworkException())
                /*读取下载写入文件*/
                .map(new Function<ResponseBody, DownloadInfo>() {
                    @Override
                    public DownloadInfo apply(ResponseBody responseBody) throws Exception {
                        writeCache(responseBody, new File(mDownloadInfo.getSavePath()), mDownloadInfo.getReadLength(), mDownloadInfo.getCountLength());
                        return mDownloadInfo;
                    }
                })
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*数据回调*/
                .subscribe(mDownloadProgressSubscriber);
        mCompositeDisposable.add(mDownloadProgressSubscriber);
    }

    public void remove() {
        mCompositeDisposable.remove(mDownloadProgressSubscriber);
        mCompositeDisposable.clear();
    }


    /**
     * 写入文件
     */
    private void writeCache(ResponseBody responseBody, File file, long readLength, long contentLength) throws IOException {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        long allLength;
        if (contentLength == 0) {
            allLength = responseBody.contentLength();
        } else {
            allLength = contentLength;
        }
        FileChannel channelOut = null;
        RandomAccessFile randomAccessFile = null;
        randomAccessFile = new RandomAccessFile(file, "rwd");
        channelOut = randomAccessFile.getChannel();
        MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE,
                readLength, allLength - readLength);
        byte[] buffer = new byte[1024 * 8];
        int len;
        int record = 0;
        while ((len = responseBody.byteStream().read(buffer)) != -1) {
            mappedBuffer.put(buffer, 0, len);
            record += len;
        }
        responseBody.byteStream().close();
        if (channelOut != null) {
            channelOut.close();
        }
        if (randomAccessFile != null) {
            randomAccessFile.close();
        }
    }

}
