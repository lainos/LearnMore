package learnmore.yzx.com.rxandroidbledemo;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.RxBleDeviceServices;
import com.polidea.rxandroidble.utils.ConnectionSharingAdapter;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;

import static com.trello.rxlifecycle.android.ActivityEvent.PAUSE;

public class InitDeviceActivity extends RxAppCompatActivity {

    @BindView(R.id.tvState)
    TextView tvState;
    @BindView(R.id.tvReadContent)
    TextView tvReadContent;

    private RxBleDevice bleDevice;
    private Observable<RxBleConnection> connectionObservable;
    private PublishSubject<Void> disconnectTriggerSubject = PublishSubject.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_device);
        ButterKnife.bind(this);

        String macAddress = getIntent().getStringExtra(DeviceActivity.EXTRA_MAC_ADDRESS);
        bleDevice = RxAndroidBleApplication.getRxBleClient(this).getBleDevice(macAddress);
        connectionObservable = prepareConnectionObservable();
        //noinspection ConstantConditions
        getSupportActionBar().setSubtitle(getString(R.string.mac_address, macAddress));

    }

    @OnClick(R.id.btnConnect)
    public void connectCorosDevice() {
        if (isConnected()) {
            triggerDisconnect();
        } else {
            connectionObservable.subscribe(rxBleConnection -> {
                Log.d(getClass().getSimpleName(), "Hey, connection has been established!");
                runOnUiThread(this::updateUI);
            }, this::onConnectionFailure);
        }
    }

    @OnClick(R.id.btnInit)
    public void initCorosDevice() {
        if (isConnected()) {
            UUID uuid = UUID.fromString("6e400002-b5a3-f393-e0a9-77656c6f6f70");

            byte[] bytes1 = HexString.hexToBytes("70020133353932353030353132393134353600AC");
            byte[] bytes2 = HexString.hexToBytes("70012F84062001B44C0000000000000003000000");
            byte[] bytes3 = HexString.hexToBytes("700000");
            ArrayList<byte[]> allData = new ArrayList<>();
            allData.add(bytes1);
            allData.add(bytes2);
            allData.add(bytes3);
            Log.i("weloop", "bytes1 = " + Arrays.toString(bytes1) + ", byte2 = " + Arrays.toString(bytes2));
            for (byte[] temp : allData) {
                connectionObservable
                        .flatMap(rxBleConnection -> rxBleConnection.writeCharacteristic(uuid, temp))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(bytes -> {
                            onWriteSuccess();
                        }, this::onWriteFailure);
            }
        } else {
            Snackbar.make(findViewById(R.id.main), "设备未连接", Snackbar.LENGTH_SHORT).show();
        }

    }


    @OnClick(R.id.btnReadBattery)
    public void readBattery() {
        if (isConnected()) {
            UUID batteryUUID = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb");
            connectionObservable
                    .flatMap(rxBleConnection -> rxBleConnection.readCharacteristic(batteryUUID))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bytes -> {
                        tvReadContent.setText(new String(bytes) + "\n" + HexString.bytesToHex(bytes));
                    }, this::onReadFailure);
        }
    }

    private void onReadFailure(Throwable throwable) {
        //noinspection ConstantConditions
        Log.i("weloop", "Read failed = " + throwable.toString());
        Snackbar.make(findViewById(R.id.main), "Read error: " + throwable, Snackbar.LENGTH_SHORT).show();
    }


    private void triggerDisconnect() {
        disconnectTriggerSubject.onNext(null);
    }

    private void onConnectionFailure(Throwable throwable) {
        //noinspection ConstantConditions
        Snackbar.make(findViewById(R.id.main), "Connection error: " + throwable, Snackbar.LENGTH_SHORT).show();
    }


    private Observable<RxBleConnection> prepareConnectionObservable() {
        return bleDevice
                .establishConnection(false)
                .takeUntil(disconnectTriggerSubject)
                .compose(bindUntilEvent(PAUSE))
                .doOnUnsubscribe(this::clearSubscription)
                .compose(new ConnectionSharingAdapter());
    }

    private void clearSubscription() {
        updateUI();
    }


    private void onWriteFailure(Throwable throwable) {
        //noinspection ConstantConditions
        Snackbar.make(findViewById(R.id.main), "Write error: " + throwable, Snackbar.LENGTH_SHORT).show();
    }


    private void onWriteSuccess() {
        //noinspection ConstantConditions
        Snackbar.make(findViewById(R.id.main), "Write success", Snackbar.LENGTH_SHORT).show();
    }

    private void updateUI() {
        tvState.setText(!isConnected() ? getString(R.string.disconnect) : getString(R.string.connect));

    }

    private boolean isConnected() {
        return bleDevice.getConnectionState() == RxBleConnection.RxBleConnectionState.CONNECTED;
    }

}
