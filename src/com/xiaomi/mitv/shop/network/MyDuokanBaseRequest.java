package com.xiaomi.mitv.shop.network;

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

//    protected DKResponse mResponse;


    @Override
    public void run() {
        Log.i(TAG, "MyScraperRequest run!");

        reportBeforeSendDone(beforeSend());
        reportRequestComplete(doRequest(System.currentTimeMillis()/1000));
    }

    private void reportBeforeSendDone(DKResponse response) {
        if (mObserver != null) {
            Log.i(TAG, "reportBeforeSendDone");
            mObserver.onBeforeSendDone(this, response);
        }
    }

    private void reportRequestComplete(DKResponse response) {
        if(mObserver != null) {
            Log.i(TAG, "onRequestCompleted");
            mObserver.onRequestCompleted(this, response);
        }
    }

    private DKResponse doRequest(long ts){
        mUrl = ApiConfig.getServerUrl();
        String deviceId = mitv.os.System.getDeviceID();
        //todo:
//        String sig = "/" + getPath() + "/" + deviceId + "/" + getPlatformID() + "/" +
//                Build.VERSION.SDK_INT + getLocaleString() + "?key=" + ApiConfig.API_KEY +
//                "&ts="+ts;

        String sig = "/" + getPath() + String.format("?key=%s&deviceId=%s&platformId=%s&&ts=%s", ApiConfig.API_KEY, deviceId, getPlatformID(), ts);

        String para = getParameters();

        if(para != null){
            sig = sig + para;
        }

        String opaque = null;
        try {
            byte[] key = Security.SECRET_KEY.getBytes();
            String data = sig;

            byte[] body = getInput();

            if(body != null){
                data +=  "&" + new String(body);
            }

            data += "&token=" + Security.SECRET_TOKEN;
            opaque = Security.signature(data.getBytes(), key);
            mUrl += sig + "&" + ApiConfig.PARAM_OPAQUE + "=" + opaque;

            Log.i(TAG, "murl: " + mUrl);

//            Hashtable<String, String> params = new Hashtable<String, String>();
//            HttpRequest request = new HttpRequest();
//            Log.i(TAG, "murl!!!: " + mUrl);
//            request.setUrl(mUrl);
//            request.setParams(params);
//
//            if(body != null){
//                request.setBody(body);
//            }

            String res = NetworkApi.getStringFromUrl(mUrl, ApiConfig.getServerHost(), getInput());
            DKResponse response = new DKResponse(DKResponse.STATUS_SUCCESS, res, true);

            return response;

        }catch (Exception e) {
            e.printStackTrace();
        }

        return new DKResponse(DKResponse.STATUS_SERVER_ERROR, "", false);
    }


//    private DKResponse doRequest(long ts){
//        mUrl = ApiConfig.getServerUrl();
//        String deviceId = mitv.os.System.getDeviceID();
//        //todo:
////        String sig = "/" + getPath() + "/" + deviceId + "/" + getPlatformID() + "/" +
////                Build.VERSION.SDK_INT + getLocaleString() + "?key=" + ApiConfig.API_KEY +
////                "&ts="+ts;
//
//        String sig = "/" + getPath() + String.format("?key=%s&deviceId=%s&platformId=%s&&ts=%s", ApiConfig.API_KEY, deviceId, getPlatformID(), ts);
//
//        String para = getParameters();
//
//        if(para != null){
//            sig = sig + para;
//        }
//
//        String opaque = null;
//        try {
//            byte[] key = Security.SECRET_KEY.getBytes();
//            String data = sig;
//
//            byte[] body = getInput();
//
//            if(body != null){
//                data +=  "&" + new String(body);
//            }
//
//            data += "&token=" + Security.SECRET_TOKEN;
//            opaque = Security.signature(data.getBytes(), key);
//            mUrl += sig + "&" + ApiConfig.PARAM_OPAQUE + "=" + opaque;
//
//            Hashtable<String, String> params = new Hashtable<String, String>();
//            HttpRequest request = new HttpRequest();
//            Log.i(TAG, "murl!!!: " + mUrl);
//            request.setUrl(mUrl);
//            request.setParams(params);
//
//            if(body != null){
//                request.setBody(body);
//            }
//
//            HttpClient httpClient = new HttpClient();
//
//            httpClient.addHeader("Accept-Encoding", "gzip,deflate");
//            httpClient.addHeader("host", ApiConfig.getServerHost());
//
//            HttpResponse httpResponse;
//
//            try {
//                httpResponse = httpClient.doRequest(request, body == null && params.size() == 0);
//                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
//
//                final int blockSize = 8192;
//                byte[] buffer = new byte[blockSize];
//
//                int count = 0;
//                while((count = httpResponse.getContentStream().read(buffer, 0 , blockSize)) > 0) {
//                    byteStream.write(buffer,0, count);
//                }
//
//                byte[] bytes = byteStream.toByteArray();
//
//                DKResponse response = new DKResponse(DKResponse.STATUS_SUCCESS,  new String(bytes, 0, bytes.length, "utf-8"), true);
//                httpResponse.getContentStream().close();
//
//                return response;
//            }catch(Exception e) {
//                e.printStackTrace();
//                Log.e(TAG, e.getMessage(), e);
//            }
//
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return new DKResponse(DKResponse.STATUS_SERVER_ERROR, "", false);
//    }

//    private DKResponse parseDKResponse(byte[] buf, Class<?> cls){
//
//        DKResponse res = null;
//
//        try {
//            String json = new String(buf, 0, buf.length, "utf-8");
//            Log.d(TAG, "got response in gzip: " + json);
//
//            res = (DKResponse)JsonSerializer.getInstance().deserialize(json, cls);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return res;
//    }

//    private byte[] getBody(){
//        Object input = getInput();
//        if(input == null)
//            return null;  //for get
//
//        String json = JsonSerializer.getInstance().serialize(input);
//
//        ByteArrayOutputStream os = null;
//        GZIPOutputStream gos = null;
//
//        try {
//            os = new ByteArrayOutputStream();
//            gos = new GZIPOutputStream(os);
//
//            gos.write(json.getBytes());
//
//            gos.finish();
//
//            BASE64Encoder encoder = new BASE64Encoder();
//            String body = encoder.encode(os.toByteArray());
//            return body.getBytes();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally{
//            Util.closeQuietly(os);
//            Util.closeQuietly(gos);
//        }
//
//        return null;
//    }

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

    protected DKResponse beforeSend(){
        return null;
    }

    protected abstract byte[] getInput();
    protected abstract String getPath();
    protected abstract String getParameters();
    protected abstract Locale getLocale();
}