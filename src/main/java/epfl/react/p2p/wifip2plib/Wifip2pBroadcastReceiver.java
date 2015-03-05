/**
 * Created by quarta on 3/4/15.
 */
package epfl.react.p2p.wifip2plib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;

/**
 * Created by quarta on 3/3/15.
 */
public class Wifip2pBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private Wifip2pListener p2pListener;

    /**
     * @param manager WifiP2pManager system service
     * @param channel Wifi p2p channel
     */
    public Wifip2pBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel, Wifip2pListener listener) {
        super();
        this.manager = manager;
        this.channel = channel;
        this.p2pListener = listener;
    }

    /*
     * (non-Javadoc)
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
     * android.content.Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

            // UI update to indicate wifi p2p status.
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                this.p2pListener.onWifiEnabled();
            } else {
                this.p2pListener.onWifiDisabled();
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            // request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()
            if (this.manager != null) {
                this.manager.requestPeers(this.channel, this.p2pListener.peersListener);
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            if (manager == null) {
                return;
            }

            NetworkInfo networkInfo = (NetworkInfo) intent
                    .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {
                this.manager.requestConnectionInfo(this.channel, this.p2pListener.connectionInfoListener);
            } else {
                this.p2pListener.onConnectionBroken();
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            this.p2pListener.onDeviceStateChanged ((WifiP2pDevice) intent.getParcelableExtra(
                    WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));
        }
    }

}
