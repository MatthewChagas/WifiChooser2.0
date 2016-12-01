package sme.com.br.wifichooser;

/**
 * Created by Matthew on 19/09/2016.
 */
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.List;
public class WifiManipulado {
    WifiManager wifiManager;

    public List<ScanResult> getConections(Context context){
        wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();
        List<android.net.wifi.ScanResult> connections = wifiManager.getScanResults();
        return connections;
    }
}