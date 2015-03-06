package epfl.react.p2p.wifip2plib;

import java.net.Socket;

public class SocketDataListener {

    private Socket socket;
    private DataListener dataListener;

    public SocketDataListener(Socket socket, DataListener dataListener) {
        this.socket = socket;
        this.dataListener = dataListener;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public DataListener getDataListener() {
        return dataListener;
    }

    public void setDataListener(DataListener dataListener) {
        this.dataListener = dataListener;
    }

}
