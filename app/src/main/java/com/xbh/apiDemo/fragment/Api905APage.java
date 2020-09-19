package com.xbh.apiDemo.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xbh.apiDemo.R;
import com.xbh.apiDemo.ShellUtils;
import com.xbh.apiDemo.api.api905aHw;

/**
 * 创建时间： 2020/9/1 17:17
 */
public class Api905APage extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    //final String TAG = getTag();
    private static final String TAG = "Api905APage";

    //View
    ToggleButton tBtLedRed, tBtLedGreen, tBtLedBlue, tBtLedAll, tBtLedPower;
    ToggleButton tBtRs485SndEn, tBtRelayCtrl;
    ToggleButton tBtUsbAll, tBtUsbOtg, tBtUsb1, tBtUsb2, tBtUsb3, tBtUsb4G;
    Button btWieGandSnd;
    Spinner spWieGandSndMode;
    TextView tvWieGandRcvCardNo;
    EditText etWieGandSndCardNo;

    //Data
    Context mContext;
    private final int LED_POWER = 0;
    private final int LED_RED = 1;
    private final int LED_GREEN = 2;
    private final int LED_BLUE = 3;
    private final int LED_ALL = 0xff;

    private final int USB_OTG = 0;
    private final int USB_1 = 1;
    private final int USB_2 = 2;
    private final int USB_3 = 3;
    private final int USB_4G = 4;
    private final int USB_ALL = 0xff;


    public static Api905APage newInstance(String text) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("text", text);
        Api905APage fragment = new Api905APage();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.api_905a_page_layout, container, false);
        mContext = getContext();
        initBroadcast();
        initView(view);
        return view;
    }

    private void initView(View view) {
        //ToggleButton
        tBtLedRed = view.findViewById(R.id.BT_LED_R);
        tBtLedRed.setOnCheckedChangeListener(this);
        tBtLedGreen = view.findViewById(R.id.BT_LED_G);
        tBtLedGreen.setOnCheckedChangeListener(this);
        tBtLedBlue = view.findViewById(R.id.BT_LED_B);
        tBtLedBlue.setOnCheckedChangeListener(this);
        tBtLedPower = view.findViewById(R.id.BT_LED_POWER);
        tBtLedPower.setOnCheckedChangeListener(this);
        tBtLedAll = view.findViewById(R.id.BT_LED_ALL);
        tBtLedAll.setOnCheckedChangeListener(this);

        tBtUsbAll = view.findViewById(R.id.BT_USB_ALL);
        tBtUsbAll.setOnCheckedChangeListener(this);
        tBtUsbOtg = view.findViewById(R.id.BT_USB_OTG);
        tBtUsbOtg.setOnCheckedChangeListener(this);
        tBtUsb1 = view.findViewById(R.id.BT_USB_1);
        tBtUsb1.setOnCheckedChangeListener(this);
        tBtUsb2 = view.findViewById(R.id.BT_USB_2);
        tBtUsb2.setOnCheckedChangeListener(this);
        tBtUsb3 = view.findViewById(R.id.BT_USB_3);
        tBtUsb3.setOnCheckedChangeListener(this);
        tBtUsb4G = view.findViewById(R.id.BT_USB_4G);
        tBtUsb4G.setOnCheckedChangeListener(this);

        tBtRelayCtrl = view.findViewById(R.id.RelayCtrl);
        tBtRelayCtrl.setOnCheckedChangeListener(this);
        tBtRs485SndEn = view.findViewById(R.id.Rs485SndEn);
        tBtRs485SndEn.setOnCheckedChangeListener(this);

        //Button
        btWieGandSnd = (Button) view.findViewById(R.id.BT_WG_SND);
        btWieGandSnd.setOnClickListener(this);

        //Spinner
        spWieGandSndMode = (Spinner)view.findViewById(R.id.wiegandSndMode);
        final String []arr = getResources().getStringArray(R.array.wieGandSndMode);
        spWieGandSndMode.setSelection(0, true);
        spWieGandSndMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: Wiegand send mode " + arr[position]);
                api905aHw.setWiegandMode(arr[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //TextView
        tvWieGandRcvCardNo = (TextView)view.findViewById(R.id.wieGandRcvCardNo);

        //EditText
        etWieGandSndCardNo = (EditText)view.findViewById(R.id.wieGandSndCardNo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.BT_WG_SND: {
                Log.d(TAG, "onClick: BT_WG_SND");
                api905aHw.sendWiegandCardNo(mContext, "" + etWieGandSndCardNo.getText().toString());
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.BT_LED_R: {
                Log.d(TAG, "onCheckedChanged: BT_LED_R " + compoundButton.isChecked());
                api905aHw.ledCtrl(mContext, LED_RED, compoundButton.isChecked());
            }
            break;
            case R.id.BT_LED_G: {
                Log.d(TAG, "onCheckedChanged: BT_LED_G " + compoundButton.isChecked());
                api905aHw.ledCtrl(mContext, LED_GREEN, compoundButton.isChecked());
            }
            break;
            case R.id.BT_LED_B: {
                Log.d(TAG, "onCheckedChanged: BT_LED_B " + compoundButton.isChecked());
                api905aHw.ledCtrl(mContext, LED_BLUE, compoundButton.isChecked());
            }
            break;
            case R.id.BT_LED_ALL: {
                Log.d(TAG, "onCheckedChanged: BT_LED_ALL " + compoundButton.isChecked());
                api905aHw.ledCtrl(mContext, LED_ALL, compoundButton.isChecked());
            }
            break;
            case R.id.BT_LED_POWER: {
                Log.d(TAG, "onCheckedChanged: BT_LED_POWER " + compoundButton.isChecked());
                api905aHw.ledCtrl(mContext, LED_POWER, compoundButton.isChecked());
            }
            break;
            case R.id.BT_USB_OTG: {
                Log.d(TAG, "onCheckedChanged: BT_USB_OTG " + compoundButton.isChecked());
                api905aHw.usbPowerCtrl(mContext, USB_OTG, compoundButton.isChecked());
            }
            break;
            case R.id.BT_USB_1: {
                Log.d(TAG, "onCheckedChanged: BT_USB_1 " + compoundButton.isChecked());
                api905aHw.usbPowerCtrl(mContext, USB_1, compoundButton.isChecked());
            }
            break;
            case R.id.BT_USB_2: {
                Log.d(TAG, "onCheckedChanged: BT_USB_2 " + compoundButton.isChecked());
                api905aHw.usbPowerCtrl(mContext, USB_2, compoundButton.isChecked());
            }
            break;
            case R.id.BT_USB_3: {
                Log.d(TAG, "onCheckedChanged: BT_USB_3 " + compoundButton.isChecked());
                api905aHw.usbPowerCtrl(mContext, USB_3, compoundButton.isChecked());
            }
            break;
            case R.id.BT_USB_4G: {
                Log.d(TAG, "onCheckedChanged: BT_USB_4G " + compoundButton.isChecked());
                api905aHw.usbPowerCtrl(mContext, USB_4G, compoundButton.isChecked());
            }
            break;
            case R.id.BT_USB_ALL: {
                Log.d(TAG, "onCheckedChanged: BT_USB_ALL " + compoundButton.isChecked());
                api905aHw.usbPowerCtrl(mContext, USB_ALL, compoundButton.isChecked());
            }
            break;
            case R.id.RelayCtrl: {
                Log.d(TAG, "onCheckedChanged: RelayCtrl " + compoundButton.isChecked());
                api905aHw.relayCtrl(mContext, compoundButton.isChecked());
            }
            break;
            case R.id.Rs485SndEn: {
                Log.d(TAG, "onCheckedChanged: Rs485SndEn " + compoundButton.isChecked());
                api905aHw.rs485Ctrl(mContext, compoundButton.isChecked());
            }
            break;
            default:
                break;
        }
    }

    private void initBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.xbh.action.WIEGAND_RECV");
        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if ("com.xbh.action.WIEGAND_RECV".equals(intent.getAction())) {
                    long cardno = intent.getLongExtra("cardno", -1);
                    tvWieGandRcvCardNo.setText(Long.toHexString(cardno));
                }
            }
        };
        mContext.registerReceiver(mReceiver, filter);
    }
}
