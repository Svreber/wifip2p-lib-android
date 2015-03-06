package epfl.react.p2p.wifip2plib;

import android.net.wifi.p2p.WifiP2pDevice;

/**
 * Created by quarta on 3/6/15.
 */
public abstract class DataListener {
    public abstract void onDataReceived(WifiP2pDevice device, String data);
}
