package epfl.react.p2p.wifip2plib;

/**
 * Created by quarta on 3/4/15.
 */

import android.content.Context;
import android.content.IntentFilter;

import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.WpsInfo;

import android.util.Log;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.IOException;
import java.io.OutputStream;

import java.nio.charset.Charset;


/**
 * Created by quarta on 3/3/15.
 */
public class Wifip2pManager implements WifiP2pManager.ChannelListener {
    private static final int SOCKET_TIMEOUT = 5000;
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private Wifip2pListener p2pListener;


    public void init(Context context, Wifip2pListener listener) {
        if (listener.connectionInfoListener == null || listener.discoveryListener == null ||
                listener.peersListener == null) {
            throw new IllegalArgumentException("The Wifip2plistener misses some required attributes (listeners)");
        }


        IntentFilter intentFilter = new IntentFilter();
        //  Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        this.p2pListener = listener;
        this.manager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
        this.channel = this.manager.initialize(context, context.getMainLooper(), null);
        Wifip2pBroadcastReceiver broadcastManager = new Wifip2pBroadcastReceiver(this.manager, this.channel, listener);

        context.registerReceiver(broadcastManager, intentFilter);
    }

    public void discover() {
        this.manager.discoverPeers(this.channel, this.p2pListener.discoveryListener);
    }

    public void connect(WifiP2pDevice device, WifiP2pManager.ActionListener actionListener) {

        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;

        this.manager.connect(this.channel, config, actionListener);
    }

    public void sendData(WifiP2pDevice device, String data) {
        String host = device.deviceAddress;
        Socket socket = new Socket();
        int port = 0;//TODO: intent.getExtras().getInt(EXTRAS_GROUP_OWNER_PORT);

        try {
            socket.bind(null);
            socket.connect((new InetSocketAddress(host, port)), SOCKET_TIMEOUT);

            OutputStream stream = socket.getOutputStream();

            stream.write(data.getBytes(Charset.forName("UTF-8")));
            stream.flush();
        } catch (IOException e) {
            Log.e("Wifip2p Log", e.getMessage());
        } finally {
            if (socket != null) {
                if (socket.isConnected()) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // Give up
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onChannelDisconnected() {
        this.p2pListener.onConnectionBroken();
    }
}