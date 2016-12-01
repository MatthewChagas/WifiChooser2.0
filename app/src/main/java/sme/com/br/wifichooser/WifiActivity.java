package sme.com.br.wifichooser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import android.net.wifi.ScanResult;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class WifiActivity extends AppCompatActivity
    {
        String SSID="aaaaa";
        int Level=-200;
        String Crypto="";
        String PassWord="";
        WifiManipulado wifi = new WifiManipulado();

        Firebase bd;
        Firebase save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        Firebase.setAndroidContext(this);
    }
    public void onClick(View view){

        final TextView result = (TextView) findViewById(R.id.value);
        final TextView Mostrando = (TextView) findViewById(R.id.label);
        List<ScanResult> results = wifi.getConections(this);
        if(results!=null){
            final StringBuilder info = new StringBuilder();
            int c=1;

            for(ScanResult connection: results){
                result.setText(info.append(connection.SSID).append("\n").append("MAC: ").append(connection.BSSID).append("\n").append("Crypto: ").append(connection.capabilities).append("\n").append("Level: " ).append(String.valueOf(connection.level)).append("\n\n\n"));
                if(connection.level>Level)
                {
                    SSID=connection.SSID;
                    Level=connection.level;
                    //Crypto=connection.capabilities;
                }
            }


            bd=new Firebase("https://teste-6f245.firebaseio.com/"+SSID);
            bd.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    PassWord = dataSnapshot.getValue(String.class);
                    WifiManager wifimanager=(WifiManager) getSystemService(Context.WIFI_SERVICE);
                    WifiConfiguration wc = new WifiConfiguration();

                    wc.SSID ="\""+SSID+"\"";
                    wc.status = WifiConfiguration.Status.ENABLED;
                    wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                    wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                    wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                    wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                    wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                    wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                    wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                    wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                    wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                    wc.preSharedKey="\""+PassWord+"\"";
                    int netId = wifimanager.addNetwork(wc);
                    wifimanager.enableNetwork(netId, true);
                    wifimanager.reconnect();
                    wifimanager.saveConfiguration();
                    Mostrando.setText("Voce esta conectado em "+SSID);

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });



        } else {
            result.setText("Sorry, no connections");
        }
    }

}
