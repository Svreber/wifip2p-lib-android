package epfl.react.p2p.wifip2plib;

/**
 * Created by quarta on 3/4/15.
 */

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;

/**
 * Created by quarta on 3/3/15.
 */
public abstract class Wifip2pListener {
    public WifiP2pManager.ActionListener discoveryListener = null;
    public WifiP2pManager.PeerListListener peersListener = null;
    public WifiP2pManager.ConnectionInfoListener connectionInfoListener = null;

    public abstract void onWifiEnabled();
    public abstract void onWifiDisabled();
    public abstract void onDeviceStateChanged(WifiP2pDevice device);
    public abstract void onConnectionBroken();
}