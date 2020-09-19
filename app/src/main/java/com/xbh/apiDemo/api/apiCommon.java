package com.xbh.apiDemo.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.xbh.apiDemo.R;
import com.xbh.apiDemo.ShellUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import androidx.annotation.RequiresApi;

public class apiCommon {
    private static final String TAG = "apiCommon";

    public static void setEthAuto(Context mContext) {
        Intent intent = new Intent("com.xbh.action.SET_ETHERNET_MODE");
        intent.putExtra("mode", "auto");
        mContext.sendBroadcast(intent);
    }

    public static void setEthStaticIp(Context mContext, String ip, String gateWay, String netMask, String Dns1, String Dns2) {
        Intent ipIntent = new Intent("com.xbh.action.SET_ETHERNET_MODE");
        ipIntent.putExtra("mode", "static");
        ipIntent.putExtra("ip", ip);
        ipIntent.putExtra("gateway", gateWay);
        ipIntent.putExtra("netMask", netMask);
        ipIntent.putExtra("dns1", Dns1);
        ipIntent.putExtra("dns2", Dns2);
        mContext.sendBroadcast(ipIntent);
    }

    public static void setAppKeepLive(String pkg) {
        Process process = null;
        Log.i(TAG, "setAppKeepLive pkg = " + pkg);
        String cmd1 = "setprop persist.lgo.nooperateStartPkg xxx";
        String cmd2 =  "setprop persist.lgo.keepNopAliveTimeSec xxx";
        try {
            if (TextUtils.isEmpty(pkg)) {
                cmd1 = cmd1.replace("xxx", " ");
                cmd2 = cmd2.replace("xxx", "-1");
            } else {
                cmd1 = cmd1.replace("xxx", pkg);
                cmd2 = cmd2.replace("xxx", "15");
            }
            process = Runtime.getRuntime().exec(cmd1);
            process = Runtime.getRuntime().exec(cmd2);
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    public static void installApp(Context mContext, String appPath) {
        if (TextUtils.isEmpty(appPath)) {
            Log.e(TAG, "请输入需要安装的Apk！");
            return;
        }
        Intent intent = new Intent("com.android.lango.installapp");
        intent.putExtra("apppath", appPath);
        mContext.sendBroadcast(intent);
    }

    public static String getSystemDisplayVersion(){
        return android.os.Build.DISPLAY;
    }

    public static void setConsoleLevel(Context mContext, String level) {
        Intent intent = new Intent("com.xbh.action.SET_CONSOLE_LOGLEVEL");
        intent.putExtra("console_loglevel", level);
        mContext.sendBroadcast(intent);
    }

    public static void setBackLight(Context mContext, int brightness) {
        Intent intent = new Intent("com.xbh.action.SET_BACKLIGHT");
        intent.putExtra("brightness", brightness);
        mContext.sendBroadcast(intent);
    }

    public static String getProperty(String key, String defaultValue) {
        String value = defaultValue;
        try {

            @SuppressLint("PrivateApi") Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            value = (String)(get.invoke(c, key, defaultValue ));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }


    public static String getFormattedCpuSerial(){
        String str = "";
        String strCpu = "";
        String cpuSerial = "0000000000000000";
        try{
            //read cpu serial
            Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for(; true;){
                str = input.readLine();
                if (str != null){
                    if (str.contains("Serial")) { // find cpu serial where a line
                        strCpu = str.substring(str.indexOf(":") + 1,str.length()); //extract cpu serial
                        cpuSerial = strCpu.trim(); // remove the blank
                        break;
                    }
                }else{
                    break;// end of file
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getFormattedCpuSerial: " + cpuSerial);
        return cpuSerial;
    }

    public static String getWifiMAC(Context mContext) {
        WifiManager wifi = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        @SuppressLint("HardwareIds") String macAddress = info.getMacAddress();
        if (macAddress != null) {
            Log.d(TAG, "getWifiMAC[" + macAddress + "]");
        }
        return macAddress;
    }

    public static void GetNetInfo(Context mContext) {
        //首先发送以下广播:
        Intent intent = new Intent("com.xbh.action.GET_NET_INFO");
        mContext.sendBroadcast(intent);
        //系统返回以下广播，请在APP中接收"com.xbh.action.NET_INFO"
        //注意,如网络未链接,返回type为"null"，并且其他信息为空
    }

    @SuppressLint("PrivateApi")
    public static String getEthMacAddress2(){
        String macString = "";
        Class<?> class1;
        try {
            class1 = Class.forName("android.app.XbhManager");
            Method getTipMethod3 = class1.getDeclaredMethod( "getDefault") ;
            Object result_3 =  getTipMethod3.invoke(null) ;
            Method method = result_3.getClass().getDeclaredMethod("getEthMac");
            macString = (String) method.invoke(result_3);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getEthMacAddress2: " + macString);
        return macString;
    }

    public static void shutDown(Context mContext) {
        Intent intent = new Intent("shine.intent.action.SHUTDOWN");
        mContext.sendBroadcast(intent);
    }

    public static void exitSleep(Context mContext) {
        Intent intent = new Intent("android.intent.action.exitsleep");
        mContext.sendBroadcast(intent);
    }

    public static void gotoSleep(Context mContext) {
        Intent intent = new Intent("android.intent.action.gotosleep");
        mContext.sendBroadcast(intent);
    }

    public static void forbidInstallApp(Context mContext, Boolean forbid) {
        Intent intent = new Intent("com.xbh.action.FORBID_INSTALL_APP");
        intent.putExtra("flag", forbid);//true则禁止，false则允许
        mContext.sendBroadcast(intent);
    }

    public static void forbidUninstallApp(Context mContext, Boolean forbid) {
        Intent intent = new Intent("com.xbh.action.FORBID_UNINSTALL_APP");
        intent.putExtra("flag", forbid);//true则禁止，false则允许
        mContext.sendBroadcast(intent);
    }

    public static void KeyHomeEn(Context mContext, Boolean en) {
        Intent intent = new Intent("com.xbh.action.KEYCODE_HOME_EN");
        intent.putExtra("flag", en);//true则允许，false则禁止
        mContext.sendBroadcast(intent);
    }

    public static void KeyBackEn(Context mContext, Boolean en) {
        Intent intent = new Intent("com.xbh.action.KEYCODE_BACK_EN");
        intent.putExtra("flag", en);//true则允许，false则禁止
        mContext.sendBroadcast(intent);
    }

    public static void KeyTouchScreenEn(Context mContext, Boolean en) {
        Intent intent = new Intent("com.xbh.action.TOUCHSCREEN_EN");
        intent.putExtra("flag", en);//true则允许，false则禁止
        mContext.sendBroadcast(intent);
    }

   /* public static void SaveLog(Boolean on) {
        Process process = null;
        Log.d(TAG, "SaveLog on = " + on);
        String cmd = "setprop persist.sys.saveSystemLog xxx";
        try {
            if (on) {
                cmd = cmd.replace("xxx", "1");
            } else {
                cmd = cmd.replace("xxx", "0");
            }
            process = Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }*/

    public static void SaveLog(Boolean on) {
        Process process = null;
        Log.d(TAG, "SaveLog on = " + on);
        String cmd = "setprop persist.sys.saveSystemLog xxx";

        if (on) {
            cmd = cmd.replace("xxx", "1");
        } else {
            cmd = cmd.replace("xxx", "0");
        }
        ShellUtils.execCommand(cmd, false);
    }

    public static void EnableEth(Context mContext, boolean enable) {
        Intent intent = new Intent("com.xbh.action.ENABLE_ETHERNET");
        intent.putExtra("flag", enable);//true则打开，false则关闭
        mContext.sendBroadcast(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void setSystemTimer(Context mContext, int year, int mouth, int date, int hour, int minute, int second) {
        Calendar ca = Calendar.getInstance();
        ca.set(year, mouth-1, date, hour, minute, second); //设置系统时间2018/06/18 16:21:15(注意月份要减1)
        ca.getTimeInMillis();
        Log.d("time", "" + ca.getTimeInMillis());
        Intent intent = new Intent("com.android.lango.setsystemtime");
        intent.putExtra("time", ""+ca.getTimeInMillis()); //String 类型
        mContext.sendBroadcast(intent);
    }

    public static void setPowerOnOff(Context mContext, int year, int mouth, int date, int hour, int minute) {
        final String SET_POWER_ON_OFF ="android.intent.action.setpoweronoff";
        //携带的数据格式为：
        int[] timeOffArray = {year, mouth, date, hour, minute};//下次开关机具体日期时间，year.mouth.date hour:minute开机
        int[] timeOnArray = {year, mouth, date, hour, minute+3}; //默认设置关机后3分钟自动开机
        Log.d(TAG, "setPowerOnOff: " + Arrays.toString(timeOnArray));
        Intent intent = new Intent();
        intent.setAction(SET_POWER_ON_OFF);
        intent.putExtra("timeon", timeOnArray);
        intent.putExtra("timeoff", timeOffArray);

        intent.putExtra("enable", true); //使能开关机，true为打开，false为关闭
        mContext.sendBroadcast(intent);
    }

    public static void setRebootDaily(Context mContext, boolean enable, int hour, int min) {
        final String ACTION_XBH_REBOOT_DAILY = "com.xbh.action.REBOOT_DAILY";
        Intent intent = new Intent();
        intent.setAction(ACTION_XBH_REBOOT_DAILY);
        if (enable) {
            intent.putExtra("enable", true);
        } else {
            intent.putExtra("enable", false);
        }
        intent.putExtra("hour", hour);
        intent.putExtra("min", min);
        mContext.sendBroadcast(intent);
    }

}