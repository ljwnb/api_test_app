package com.xbh.apiDemo.api;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.xbh.apiDemo.R;
import com.xbh.apiDemo.ShellUtils;

public class api905aHw {
    private static final String TAG = "api905aHw";

    public static void setWiegandMode(String mode) {
        ShellUtils.execCommand("echo " + mode + " > /sys/devices/platform/wiegand_snd/wiegand_mode", false);
    }

    public static void sendWiegandCardNo(Context mContext, String cardNo) {
        Log.d(TAG, "Wiegand Snd CardNo :" + cardNo);
        if (cardNo.equals("")) {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.wieGandcardNoTip), Toast.LENGTH_LONG).show();
            return;
        }
        ShellUtils.execCommand("echo " + cardNo + " > /sys/devices/platform/wiegand_snd/wiegand_data", false);
    }

    public static void ledCtrl(Context mContext, int port, boolean on) {
        Intent intent = new Intent("com.xbh.action.LED_CTRL");
        intent.putExtra("port", port);//0xff为所有，0为led 12V供电控制(需要先打开供电)，然后1 2 3分别是r g b, 4为恒流IR接口
        if (on) {
            intent.putExtra("status", 1);//1打开，0关闭，port为恒流IR接口则值为0-100
        } else {
            intent.putExtra("status", 0);
        }
        mContext.sendBroadcast(intent);
    }

    public static void usbPowerCtrl(Context mContext, int port, boolean on) {
        Intent intent = new Intent("com.xbh.action.SET_USB_POWER");
        intent.putExtra("port" , port);//0xff为所有usb，0为OTG口，1 2 3普通usb，4为4G模块端口
        intent.putExtra("enable" , on);//true打开，false关闭
        mContext.sendBroadcast(intent);
    }

    public static void relayCtrl(Context mContext, boolean on) {
        Intent intent = new Intent("com.xbh.action.RELAY_CTRL");
        intent.putExtra("port" , 0);//0xff为所有，0 1为第0和第1个继电器,905D3.A内置为0
        intent.putExtra("enable" , on);//true打开，false关闭
        mContext.sendBroadcast(intent);
    }

    public static void rs485Ctrl(Context mContext, boolean mode) {
        //XM.A905D3.A的RS485端口为ttyS3，发送和接收模式切换如下:
        Intent intent = new Intent("com.xbh.action.RS485_SND_EN");
        intent.putExtra("enable" , mode);//true为发送模式，false为接收模式
        mContext.sendBroadcast(intent);
    }

}