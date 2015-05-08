package com.xiaomi.mitv.shop.network;

import android.os.Build;
import android.util.Log;
import com.xiaomi.mitv.api.net.HttpClient;
import com.xiaomi.mitv.api.net.HttpRequest;
import com.xiaomi.mitv.api.net.HttpResponse;
import com.xiaomi.mitv.shop.util.Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.zip.GZIPOutputStream;

/**
 * Created by niuyi on 2015/5/8.
 */
public abstract class MyDuokanBaseRequest extends MyBaseRequest {

    private static final String TAG = "MyDuokanBaseRequest";

    private String mUrl;

    protected DKResponse mResponse;


    @Override
    public void run() {
        Log.i(TAG, "MyScraperRequest run!");

        try{
            doRequest(System.currentTimeMillis()/1000);
            if(mResponse.getStatus() == 105){
                // ts is invalid.
                doRequest(mResponse.getTs());
            }
        }finally {
            reportRequestComplete();
        }

    }

    private void reportRequestComplete() {
        if(mObserver != null) {
            if (mResponse == null) {
                mResponse = new DKResponse(DKResponse.STATUS_UNKOWN_ERROR);
            }

            Log.i(TAG, "onRequestCompleted");
            mObserver.onRequestCompleted(this, mResponse);
        }
    }

    private void doRequest(long ts){
        mUrl = ApiConfig.getServerUrl();
        String deviceId = mitv.os.System.getDeviceID();
        //todo:
        String sig = "/" + getPath() + "/" + deviceId + "/" + getPlatformID() + "/" +
                Build.VERSION.SDK_INT + getLocaleString() + "?key=" + ApiConfig.API_KEY +
                "&ts="+ts;

        String para = getParameters();

        if(para != null){
            sig = sig + para;
        }

        String opaque = null;
        try {
            byte[] key = Security.SECRET_KEY.getBytes();
            String data = sig;

            byte[] body = getBody();

            if(body != null){
                data +=  "&" + new String(body);
            }

            data += "&token=" + Security.SECRET_TOKEN;
            opaque = Security.signature(data.getBytes(), key);
            mUrl += sig + "&" + ApiConfig.PARAM_OPAQUE + "=" + opaque;

            Hashtable<String, String> params = new Hashtable<String, String>();
            HttpRequest request = new HttpRequest();
            Log.i(TAG, "murl!!!: " + mUrl);
            request.setUrl(mUrl);
            request.setParams(params);

            if(body != null){
                request.setBody(body);
            }

            HttpClient httpClient = new HttpClient();

            httpClient.addHeader("Accept-Encoding", "gzip,deflate");
            httpClient.addHeader("host", ApiConfig.getServerHost());

            HttpResponse response;

            try {
                response = httpClient.doRequest(request, body == null && params.size() == 0);
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                final int blockSize = 8192;
                byte[] buffer = new byte[blockSize];
                int count = 0;
                while((count = response.getContentStream().read(buffer, 0 , blockSize)) > 0) {
                    byteStream.write(buffer,0, count);
                }
                mResponse = parseDKResponse(byteStream.toByteArray(), getResponseType());
                response.getContentStream().close();

            }catch(Exception e) {
                mResponse.setStatus(DKResponse.STATUS_SERVER_ERROR);
                Log.e(TAG, e.getMessage(), e);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DKResponse parseDKResponse(byte[] buf, Class<?> cls){

        DKResponse res = null;

        try {
            String json = new String(buf, 0, buf.length, "utf-8");
            Log.d(TAG, "got response in gzip: " + json);

            res = (DKResponse)JsonSerializer.getInstance().deserialize(json, cls);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    private byte[] getBody(){
        Object input = getInput();
        if(input == null)
            return null;  //for get

        String json = JsonSerializer.getInstance().serialize(input);

        ByteArrayOutputStream os = null;
        GZIPOutputStream gos = null;

        try {
            os = new ByteArrayOutputStream();
            gos = new GZIPOutputStream(os);

            gos.write(json.getBytes());

            gos.finish();

            BASE64Encoder encoder = new BASE64Encoder();
            String body = encoder.encode(os.toByteArray());
            return body.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            Util.closeQuietly(os);
            Util.closeQuietly(gos);
        }

        return null;
    }

    private String  getPlatformID(){
        return String.valueOf(mitv.os.System.getPlatform());
    }

    private String getLocaleString(){
        Locale locale = getLocale();
        if(locale == null){
            return "/zh/CN";
        }

        return "/" + locale.toString().replace('_', '/');
    }

    protected abstract Class<?> getResponseType();
    protected abstract Object getInput();
    protected abstract String getPath();
    protected abstract String getParameters();
    protected abstract Locale getLocale();
}
