package com.wdk.sports.packages;

import com.zhy.http.library.OkHttpUtils;
import com.zhy.http.library.cookie.CookieJarImpl;
import com.zhy.http.library.cookie.store.PersistentCookieStore;
import com.zhy.http.library.https.HttpsUtils;
import com.zhy.http.library.log.LoggerInterceptor;
import ohos.aafwk.ability.AbilityPackage;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class MyHapPackage extends AbilityPackage {

    @Override
    public void onInitialize() {
        super.onInitialize();
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .cookieJar( cookieJar )
                .addInterceptor(new LoggerInterceptor("TAG"))
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }
}
