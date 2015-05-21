package com.xiaomi.mitv.shop.network;

import com.xiaomi.mitv.api.util.MILog;
import com.xiaomi.mitv.shop.util.Util;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by niuyi on 2015/5/20.
 */
public class NetworkApi {

    private static final String TAG = "NetworkApi";

    public static String getStringFromUrl(String url, String host, byte[] data) {
        MILog.i(TAG, "getStringFromUrl: " + url + " ,host: " + host);

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL u = new URL(url);
            connection = (HttpURLConnection)u.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            if(host != null){
                connection.addRequestProperty("host", host);
            }

            if(data == null){
                connection.setRequestMethod("GET");
            }else{
                connection.setRequestMethod("POST");
                connection.getOutputStream().write(data);
                connection.getOutputStream().flush();
                connection.getOutputStream().close();
            }

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));//设置编码,否则中文乱码
            String lines;
            StringBuffer sb = new StringBuffer();
            while ((lines = reader.readLine()) != null){
                sb.append(lines);
            }

            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(connection != null){
                connection.disconnect();
            }

            Util.closeQuietly(reader);
        }

        return null;
    }

    public static String getStringFromUrlByHttps(String url, String host, byte[] data) {
        MILog.i(TAG, "getStringFromUrlByHttps: " + url);

        HttpsURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL u = new URL(url);
            connection = (HttpsURLConnection)u.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            if(host != null){
                connection.addRequestProperty("host", host);
            }

            SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, new TrustManager[] {new X509TrustManager(){

                @Override
                public void checkClientTrusted(X509Certificate[] arg0,
                                               String arg1) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] arg0,
                                               String arg1) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }}}, new SecureRandom());


            SSLSocketFactory sslSocketFactory = context.getSocketFactory();
            connection.setSSLSocketFactory(sslSocketFactory);
            connection.setHostnameVerifier(new HostnameVerifier() {

                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            if(data == null){
                connection.setRequestMethod("GET");
            }else{
                connection.setRequestMethod("POST");
                connection.getOutputStream().write(data);
                connection.getOutputStream().flush();
                connection.getOutputStream().close();
            }

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));//设置编码,否则中文乱码

            String lines;
            StringBuffer sb = new StringBuffer();
            while ((lines = reader.readLine()) != null){
                sb.append(lines);
            }

            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(connection != null){
                connection.disconnect();
            }

            Util.closeQuietly(reader);
        }

        return null;
    }
}
