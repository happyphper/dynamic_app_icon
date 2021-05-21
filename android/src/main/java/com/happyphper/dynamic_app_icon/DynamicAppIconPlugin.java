package com.happyphper.dynamic_app_icon;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import io.flutter.Log;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * DynamicAppIconPlugin
 */
public class DynamicAppIconPlugin implements FlutterPlugin, MethodCallHandler {
    static private String TAG = "DynamicAppIconPlugin";

    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;

    Context context;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "dynamic_app_icon");
        channel.setMethodCallHandler(this);
        context = flutterPluginBinding.getApplicationContext();
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("updateIcon")) {
            try {
                String data = call.argument("name");
                assert data != null;
                updateIcon(data);
                result.success(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    // 切换图标
    public void updateIcon(@NonNull String name) {
        String packageName = context.getPackageName();

        String className = String.format("%s.%s", packageName, name);

        ActivityInfo[] oldName = getActivities();

        PackageManager pm = context.getPackageManager();

        // 启用新入口
        pm.setComponentEnabledSetting(new ComponentName(packageName, className), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        String prefix = String.format("%s.icon", packageName);

        // 关闭已启用入口
        for (ActivityInfo activity : oldName) {
            // 是否为 icon 的 activity
            if (activity.name.contains(prefix) && !activity.name.equals(className)) {
                pm.setComponentEnabledSetting(
                        new ComponentName(packageName, activity.name),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                );
            }
        }
    }

    // 获取所有 activity
    public ActivityInfo[] getActivities() {
        ActivityInfo[] activityInfos;

        PackageManager pm = context.getPackageManager();
        String packageName = context.getPackageName();

        try {
            PackageInfo info = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES | PackageManager.GET_DISABLED_COMPONENTS);
            activityInfos = info.activities;

            Log.d(TAG, "Found this configured activities:");
            for (ActivityInfo activityInfo : activityInfos) {
                Log.d(TAG, activityInfo.name);
            }

            return activityInfos;

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }
}
