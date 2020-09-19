package com.xbh.apiDemo.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.xbh.apiDemo.R;
import com.xbh.apiDemo.api.apiCommon;

import java.util.Objects;

/**
 * 创建时间： 2020/9/1 17:17
 */
public class ApiCommonPage extends Fragment implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, DatePicker.OnDateChangedListener{
    //private final String TAG = getTag();
    private final String TAG = "ApiCommonPage";
    //View
    private Button btGotoSleep, btExitSleep, btShutDown, btInstallApp, btSetAppKeepLive, btSetStaticIp, btSetEthAuto;
    private Button btSetSystemTime, btSetPowerOn;
    private Button btSetRebootDaily;
    private ToggleButton tBtForbidInstallApp, tBtForbidUninstallApp, tBtKeyHomeEn, tBtKeyBackEn, tBtKeyTouchScreenEn, tBtSaveLog, tBtEnableEth;
    private TextView tvClickCnt, tvCpuSerial, tvSerialNo, tvSystemDisplayVersion, tvBuildDate, tvEthMacAddress, tvWifiMAC;
    private TextView tvNetType, tvNetMode, tvNetIp, tvNetGateWay, tvNetMask, tvNetDns1, tvNetDns2;
    private Spinner spConLevel;
    private SeekBar backLightBar;
    private EditText etAppPath, etAppKeepLive, etSetEthIp, etSetEthGateWay, etSetEthNetMask, etSetEthDns1, etSetEthDns2;
    private TimePicker timePicker;
    private DatePicker datePicker;
    //Data
    private long click_cnt = 0;
    private Context mContext;
    private int year = 2020;
    private  int month = 1;
    private  int day = 1;
    private  int hour = 12;
    private  int min = 30;

    public static ApiCommonPage newInstance(String text) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("text", text);
        ApiCommonPage fragment = new ApiCommonPage();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.api_common_page_layout, container, false);
        mContext = getContext();

        initBroadcast();
        initView(view);
        updateInfo();
        return view;
    }

    private void initView(View view) {
        //Button
        btGotoSleep = (Button)view.findViewById(R.id.gotosleep);//获取按钮
        btGotoSleep.setOnClickListener(this);
        btExitSleep = (Button)view.findViewById(R.id.exitsleep);
        btExitSleep.setOnClickListener(this);
        btShutDown = (Button)view.findViewById(R.id.ShutDown);
        btShutDown.setOnClickListener(this);
        btInstallApp = (Button)view.findViewById(R.id.BtInstallApp);
        btInstallApp.setOnClickListener(this);
        btSetAppKeepLive = (Button)view.findViewById(R.id.BtsetAppKeepLive);
        btSetAppKeepLive.setOnClickListener(this);
        btSetStaticIp = (Button)view.findViewById(R.id.setEthStatic);
        btSetStaticIp.setOnClickListener(this);
        btSetEthAuto = (Button)view.findViewById(R.id.setEthAuto);
        btSetEthAuto.setOnClickListener(this);
        btSetSystemTime = (Button)view.findViewById(R.id.btSetSystemTime);
        btSetSystemTime.setOnClickListener(this);
        btSetPowerOn = (Button)view.findViewById(R.id.btSetPowerOn);
        btSetPowerOn.setOnClickListener(this);
        btSetRebootDaily = (Button)view.findViewById(R.id.btSetRebootDaily);
        btSetRebootDaily.setOnClickListener(this);

        //ToggleButton
        tBtForbidInstallApp = (ToggleButton)view.findViewById(R.id.forbid_install_app);
        tBtForbidUninstallApp = (ToggleButton)view.findViewById(R.id.forbid_uninstall_app);
        tBtKeyHomeEn = (ToggleButton)view.findViewById(R.id.key_home_en);
        tBtKeyBackEn = (ToggleButton)view.findViewById(R.id.key_back_en);
        tBtKeyTouchScreenEn = (ToggleButton)view.findViewById(R.id.key_touch_en);
        tBtSaveLog = (ToggleButton)view.findViewById(R.id.save_log);
        tBtEnableEth = (ToggleButton)view.findViewById(R.id.enable_eth);

        tBtForbidInstallApp.setOnCheckedChangeListener(this);
        tBtForbidUninstallApp.setOnCheckedChangeListener(this);
        tBtKeyHomeEn.setOnCheckedChangeListener(this);
        tBtKeyBackEn.setOnCheckedChangeListener(this);
        tBtKeyTouchScreenEn.setOnCheckedChangeListener(this);
        tBtSaveLog.setOnCheckedChangeListener(this);
        tBtEnableEth.setOnCheckedChangeListener(this);

        //EditText
        etAppPath = (EditText)view.findViewById(R.id.installApp);
        etAppKeepLive = (EditText)view.findViewById(R.id.appKeepLive);
        etSetEthIp = (EditText)view.findViewById(R.id.setEthIp);
        etSetEthGateWay = (EditText)view.findViewById(R.id.setEthGateway);
        etSetEthNetMask = (EditText)view.findViewById(R.id.setEthnetMask);
        etSetEthDns1 = (EditText)view.findViewById(R.id.setEthDns1);
        etSetEthDns2 = (EditText)view.findViewById(R.id.setEthDns2);

        //TextView
        tvClickCnt = (TextView)view.findViewById(R.id.click_cnt);
        tvCpuSerial = (TextView)view.findViewById(R.id.CpuSerial);
        tvSerialNo = (TextView)view.findViewById(R.id.SerialNo);
        tvSystemDisplayVersion = (TextView)view.findViewById(R.id.SystemDisplayVersion);
        tvBuildDate = (TextView)view.findViewById(R.id.buildDate);
        tvEthMacAddress = (TextView)view.findViewById(R.id.EthMacAddress);
        tvWifiMAC = (TextView)view.findViewById(R.id.WifiMAC);
        tvNetDns1 = (TextView)view.findViewById(R.id.netDns1);
        tvNetDns2 = (TextView)view.findViewById(R.id.netDns2);
        tvNetGateWay = (TextView)view.findViewById(R.id.netGateway);
        tvNetIp = (TextView)view.findViewById(R.id.netIp);
        tvNetMask = (TextView)view.findViewById(R.id.netMask);
        tvNetMode = (TextView)view.findViewById(R.id.netMode);
        tvNetType = (TextView)view.findViewById(R.id.netType);

        //Spinner
        spConLevel = (Spinner)view.findViewById(R.id.consoleLevel);
        final String []arr = getResources().getStringArray(R.array.logLevel);
        spConLevel.setSelection(7,true);
        spConLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean b = true;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                apiCommon.setConsoleLevel(mContext, arr[position]);

                Toast.makeText(mContext, "" + arr[position], Toast.LENGTH_LONG).show();
                Log.d(TAG, "onItemSelected" + arr[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //SeekBar
        backLightBar = (SeekBar)view.findViewById(R.id.backLight);
        backLightBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                apiCommon.setBackLight(mContext, progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //TimePicker
        timePicker = view.findViewById(R.id.coursdaytime);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay;
                min = minute;
                if (minute < 10){
                    hour = hourOfDay;
                    min = minute;
                    Log.i("time","小时"+hour+"分钟"+"0"+min);
                }else {
                    Log.i("time","小时"+hour+"分钟"+min);
                }
            }
        });

        //DatePicker
        datePicker = view.findViewById(R.id.coursdaydate);
        datePicker.init(year,month,day,this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ShutDown:
                Log.d(TAG, "ShutDown");
                apiCommon.shutDown(mContext);
                break;
            case R.id.gotosleep:
                Log.d(TAG, "gotosleep");
                doGotoSleep();
                break;
            case R.id.exitsleep:
                Log.d(TAG, "exitsleep");
                apiCommon.exitSleep(mContext);
                break;
            case R.id.BtInstallApp:
                Log.d(TAG, "BtInstallApp");
                apiCommon.installApp(mContext, "" + etAppPath.getText().toString());
                break;
            case R.id.BtsetAppKeepLive:
                Log.d(TAG, "BtsetAppKeepLive");
                apiCommon.setAppKeepLive(etAppKeepLive.getText().toString());
                break;
            case R.id.setEthStatic:
                Log.d(TAG, "setEthStatic");
                doSetEthStaticIp();
                break;
            case R.id.setEthAuto:
                Log.d(TAG, "setEthAuto");
                apiCommon.setEthAuto(mContext);
                break;
            case R.id.btSetSystemTime:
                Log.d(TAG, "btSetSystemTimer");
                apiCommon.setSystemTimer(mContext, year, month, day, hour, min, 0);
                Toast.makeText(mContext, "设置成功！", Toast.LENGTH_LONG).show();
                break;
            case R.id.btSetPowerOn:
                Log.d(TAG, "btSetPowerOn");
                apiCommon.setPowerOnOff(mContext, year, month, day, hour, min);
                Toast.makeText(mContext, "设置成功！，默认关机三分钟后开机", Toast.LENGTH_LONG).show();
                break;
            case R.id.btSetRebootDaily:
                Log.d(TAG, "btSetRebootDaily");
                apiCommon.setRebootDaily(mContext, true, hour, min);
                Toast.makeText(mContext, "设置成功！", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.forbid_install_app:
                Log.d(TAG, "forbid_install_app : " + compoundButton.isChecked());
                apiCommon.forbidInstallApp(mContext, compoundButton.isChecked());
                break;
            case R.id.forbid_uninstall_app:
                Log.d(TAG, "forbid_uninstall_app : " + compoundButton.isChecked());
                apiCommon.forbidUninstallApp(mContext, compoundButton.isChecked());
                break;
            case R.id.key_home_en:
                Log.d(TAG, "key_home_en : " + !compoundButton.isChecked());
                apiCommon.KeyHomeEn(mContext, !compoundButton.isChecked());
                break;
            case R.id.key_back_en:
                Log.d(TAG, "key_back_en : " + !compoundButton.isChecked());
                apiCommon.KeyBackEn(mContext, !compoundButton.isChecked());
                break;
            case R.id.key_touch_en:
                Log.d(TAG, "key_touch_en : " + !compoundButton.isChecked());
                doTouchScreenTest(!compoundButton.isChecked());
                break;
            case R.id.save_log:
                Log.d(TAG, "save_log : " + compoundButton.isChecked());
                apiCommon.SaveLog(compoundButton.isChecked());
                break;
            case R.id.enable_eth:
                Log.d(TAG, "enable_eth : " + compoundButton.isChecked());
                apiCommon.EnableEth(mContext, compoundButton.isChecked());
                break;
            default:
                break;
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        month = monthOfYear +1;
        this.year = year;
        day = dayOfMonth;
        Log.i("time", this.year +"年"+month+"月"+day+"日");
    }


    private void initBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("xbh.intent.action.start_ltvmanageservice");//触摸点击广播
        filter.addAction("com.xbh.action.NET_INFO");
        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onReceive(Context context, Intent intent) {
                if ("xbh.intent.action.start_ltvmanageservice".equals(intent.getAction())) {
                    tvClickCnt.setText("" + ++click_cnt);
                } else if (Objects.equals(intent.getAction(), "com.xbh.action.NET_INFO")) {
                    Log.d(TAG, "rcv com.xbh.action.NET_INFO");
                    tvNetType.setText(intent.getStringExtra("type"));//wifi,mobile,eth
                    tvNetMode.setText(intent.getStringExtra("mode"));//static, dhcp
                    tvNetIp.setText(intent.getStringExtra("ip"));
                    tvNetGateWay.setText(intent.getStringExtra("gateway"));
                    tvNetMask.setText(intent.getStringExtra("netmask"));
                    tvNetDns1.setText(intent.getStringExtra("dns1"));
                    tvNetDns2.setText(intent.getStringExtra("dns2"));
                }
            }
        };
        mContext.registerReceiver(mReceiver, filter);
    }

    private void doSetEthStaticIp() {
        String ip = "" + etSetEthIp.getText().toString();
        String gateWay = "" + etSetEthGateWay.getText().toString();
        String netMask = "" + etSetEthNetMask.getText().toString();
        String Dns1 = "" + etSetEthDns1.getText().toString();
        String Dns2 = "" + etSetEthDns2.getText().toString();
        Log.d(TAG, "IP:"+ip);
        Log.d(TAG, "gateWay:"+gateWay);
        Log.d(TAG, "netMask:"+netMask);
        Log.d(TAG, "Dns1:"+Dns1);
        Log.d(TAG, "Dns2:"+Dns2);
        if ((ip.equals("")) || (gateWay.equals("")) || (netMask.equals(""))) {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.setEthStaticTip), Toast.LENGTH_LONG).show();
            return;
        }
        apiCommon.setEthStaticIp(mContext, ip, gateWay, netMask, Dns1, Dns2);
    }

    @SuppressLint({"SetTextI18n", "HardwareIds"})
    private void updateInfo() {
        tvSerialNo.setText("" + apiCommon.getProperty("ro.serialno", "null"));
        tvCpuSerial.setText("" + apiCommon.getFormattedCpuSerial());
        tvSystemDisplayVersion.setText("" +  apiCommon.getSystemDisplayVersion());
        tvBuildDate.setText("" + apiCommon.getProperty("ro.build.date.ver", "null"));
        tvEthMacAddress.setText("" + apiCommon.getEthMacAddress2());
        tvWifiMAC.setText("" + apiCommon.getWifiMAC(mContext));
        apiCommon.GetNetInfo(mContext);
    }

    private void doTouchScreenTest(boolean en) {
        apiCommon.KeyTouchScreenEn(mContext, en);
        if (!en) {
            new CountDownTimer(10*1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Toast.makeText(mContext, "触摸已禁用，" + millisUntilFinished/1000 + "秒后自动开启", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onFinish() {
                    tBtKeyTouchScreenEn.setChecked(false);
                    apiCommon.KeyTouchScreenEn(mContext, true);
                }
            }.start();
        }
    }

    private void doGotoSleep() {

        Log.d(TAG, "doGotoSleep!");
        Toast.makeText(mContext, getResources().getString(R.string.doGotoSleepTip), Toast.LENGTH_LONG).show();

        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                    apiCommon.gotoSleep(mContext);
                    Log.d(TAG, "gotoSleep!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    sleep(30000);
                    Log.d(TAG, "exitSleep!");
                    apiCommon.exitSleep(mContext);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }

}
