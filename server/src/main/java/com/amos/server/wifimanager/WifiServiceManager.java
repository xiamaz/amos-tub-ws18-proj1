package com.amos.server.wifimanager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import com.amos.server.P2PActivityServer;

public class WifiServiceManager extends BroadcastReceiver {
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private P2PActivityServer activity;
    private WifiConnectionService connectionService;
    private PeersListenerService servicePeers;
    public WifiServiceManager(WifiP2pManager manager, WifiP2pManager.Channel channel , Activity activity)
    {
        this.mManager = manager;
        this.mChannel = channel;
        this.activity = (P2PActivityServer)activity;
        this.connectionService = new WifiConnectionService(this.activity,mManager,mChannel);
        this.servicePeers = new PeersListenerService(this.activity);
    }



    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Determine if Wifi P2P mode is enabled or not, alert
            // the Activity.
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                activity.setEnableWifi(true);
            } else {
                activity.setEnableWifi(false);
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            // The peer list has changed! We should probably do something about
            // that.

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            // Connection state changed! We should probably do something about
            // that.

            if(mManager != null)
            {
                NetworkInfo infoNet = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

                if(infoNet.isConnected())
                {
                    mManager.requestConnectionInfo(mChannel,this.connectionService);
                }
            }

            Log.d("WifiReceiverP2P","Connection Changed Action");

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {

            Log.d("WifiReceiverP2P","This Device Changed Action");
        }
    }


}
