package com.csw.aidltest.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.csw.aidltest.IService;
import com.csw.aidltest.R;
import com.csw.aidltest.entities.Book;

import java.util.List;

/**
 * Created by caisw on 2017/11/7.
 */

public class MainActivity extends AppCompatActivity {
    private TextView tv_log;
    private StringBuilder stringBuilder;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            addLog("connect IService success");
            IService iService = IService.Stub.asInterface(service);
            String result = null;
            try {
                result = iService.hello("MainActivity");
                addLog("IService.hello() return " + result);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            try {
                Book book = iService.newBook("aidl test", "caishuwei");
                Log.e("MainActivity", book.toString());//打印出的内存地址与服务中打印的内存地址不同，说明这是两个对象，只是内容相同
                addLog("IService.hello() return " + book.getName() + "|" + book.getAuthor());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            addLog("end");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_log = (TextView) this.findViewById(R.id.tv_log);
        getSupportActionBar().setTitle("MainActivity");

        stringBuilder = new StringBuilder();
        addLog("start");
        addLog("connect remote service");
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.csw.aidltest", "com.csw.aidltest.service.AIDLService"));
        intent.setAction("android.intent.action.AIDLService");
        bindService(intent, conn, Context.BIND_AUTO_CREATE);

    }

    public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
//        这是Android 5.0 (Lollipop) 之后的规定。 不能用包名的方式定义Service Intent, 而要用显性声明:
//        new Intent(context, xxxService.class);

        // 检查所有匹配该意图的服务
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);

        // 未找到或存在多个匹配选项，则返回空
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        // 获取服务的包名和类名，组成新的ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);

        // 为Intent设置ComponentName
        explicitIntent.setComponent(component);

        return explicitIntent;
    }

    private void addLog(String logInfo) {
        int index = stringBuilder.lastIndexOf("...");
        if (index >= 0) {
            stringBuilder.deleteCharAt(index);
        }
        stringBuilder.append(logInfo);
        stringBuilder.append("\n");
        stringBuilder.append("...");
        tv_log.setText(stringBuilder.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }
}
