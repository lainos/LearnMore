package learnmore.yzx.com.rxandroidbledemo;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.RxBleDevice;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static com.trello.rxlifecycle.android.ActivityEvent.DESTROY;
import static com.trello.rxlifecycle.android.ActivityEvent.PAUSE;

/**
 * Connect Activity
 * <p> Extends RxAppCompatActivity to use {@link RxAppCompatActivity#bindUntilEvent}</p>
 */
public class DeviceActivity extends RxAppCompatActivity {

    public static final String EXTRA_MAC_ADDRESS = "extra_mac_address";
    private String macAddress;

    @BindView(R.id.connection_state)
    TextView connectionStateView;
    @BindView(R.id.connect_toggle)
    Button connectButton;
    @BindView(R.id.newMtu)
    EditText textMtu;
    @BindView(R.id.set_mtu)
    Button setMtuButton;
    @BindView(R.id.autoconnect)
    SwitchCompat autoConnectToggleSwitch;

    private RxBleDevice bleDevice;
    private Subscription connectionSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        ButterKnife.bind(this);
        macAddress = getIntent().getStringExtra(EXTRA_MAC_ADDRESS);
        getSupportActionBar().setSubtitle(getString(R.string.mac_address, macAddress));

        bleDevice = RxAndroidBleApplication.getRxBleClient(this).getBleDevice(macAddress);
        // How to listen for connection state changes
        bleDevice.observeConnectionStateChanges()
                .compose(bindUntilEvent(DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onConnectionStateChange);

    }

    @OnClick(R.id.connect_toggle)
    public void onConnectToggleClick() {
        if (isConnected()) {
            triggerDisconnect();
        } else {
            connectionSubscription = bleDevice.establishConnection(autoConnectToggleSwitch.isChecked())
                    .compose(bindUntilEvent(PAUSE))
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnUnsubscribe(this::clearSubscription)
                    .subscribe(this::onConnectionReceived, this::onConnectionFailure);
        }
    }

    @OnClick(R.id.set_mtu)
    public void onSetMtu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bleDevice.establishConnection(false)
                    .flatMap(rxBleConnection -> rxBleConnection.requestMtu(72))
                    .first() // Disconnect automatically after discovery
                    .compose(bindUntilEvent(PAUSE))
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnUnsubscribe(this::updateUI)
                    .subscribe(this::onMtuReceived, this::onConnectionFailure);
        }
    }

    @OnClick(R.id.goToInit)
    public void goToInitDeice() {
        final Intent intent = new Intent(this, InitDeviceActivity.class);
        intent.putExtra(DeviceActivity.EXTRA_MAC_ADDRESS, macAddress);
        startActivity(intent);
    }

    private boolean isConnected() {
        return bleDevice.getConnectionState() == RxBleConnection.RxBleConnectionState.CONNECTED;
    }

    private void onConnectionFailure(Throwable throwable) {
        Snackbar.make(findViewById(android.R.id.content), "Connection error: " + throwable, Snackbar.LENGTH_SHORT).show();
    }

    private void onConnectionReceived(RxBleConnection connection) {
        Snackbar.make(findViewById(android.R.id.content), "Connection received", Snackbar.LENGTH_SHORT).show();
    }

    private void onConnectionStateChange(RxBleConnection.RxBleConnectionState newState) {
        connectionStateView.setText(newState.toString());
        updateUI();
    }

    private void onMtuReceived(Integer mtu) {
        Snackbar.make(findViewById(android.R.id.content), "MTU received: " + mtu, Snackbar.LENGTH_SHORT).show();
    }

    private void clearSubscription() {
        connectionSubscription = null;
        updateUI();
    }

    private void triggerDisconnect() {
        if (connectionSubscription != null) {
            connectionSubscription.unsubscribe();
        }
    }

    private void updateUI() {
        final boolean connected = isConnected();
        connectButton.setText(connected ? R.string.disconnect : R.string.connect);
        autoConnectToggleSwitch.setEnabled(!connected);
    }

    /**
     * 发现服务的代码
     */
    @BindView(R.id.rvServices)
    RecyclerView rvServices;

    private DiscoveryResultsAdapter adapter;

    @OnClick(R.id.btnServiceDiscovery)
    public void onServiceDiscoryClick() {
        configureResultList();

        bleDevice.establishConnection(false)
                .flatMap(RxBleConnection::discoverServices)
                .first() // Disconnect automatically after discovery
                .compose(bindUntilEvent(PAUSE))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnUnsubscribe(this::updateUI)
                .subscribe(adapter::swapScanResult, this::onConnectionFailure);


        updateUI();
    }

    private void configureResultList() {
        rvServices.setHasFixedSize(true);
        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(this);
        rvServices.setLayoutManager(recyclerLayoutManager);
        adapter = new DiscoveryResultsAdapter();
        rvServices.setAdapter(adapter);
        adapter.setOnAdapterItemClickListener(view -> {
            final int childAdapterPosition = rvServices.getChildAdapterPosition(view);
            final DiscoveryResultsAdapter.AdapterItem itemAtPosition = adapter.getItem(childAdapterPosition);
            onAdapterItemClick(itemAtPosition);
        });
    }

    private void onAdapterItemClick(DiscoveryResultsAdapter.AdapterItem item) {
        if (item.type == DiscoveryResultsAdapter.AdapterItem.CHARACTERISTIC) {
            final Intent intent = new Intent(this, InitDeviceActivity.class);
            intent.putExtra(DeviceActivity.EXTRA_MAC_ADDRESS, macAddress);
            startActivity(intent);
        } else {
            //noinspection ConstantConditions
            Snackbar.make(findViewById(android.R.id.content), R.string.not_clickable, Snackbar.LENGTH_SHORT).show();
        }
    }

}
