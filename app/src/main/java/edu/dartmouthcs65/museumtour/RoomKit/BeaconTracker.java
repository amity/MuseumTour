package edu.dartmouthcs65.museumtour.RoomKit;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;


public class BeaconTracker extends JsonHttpResponseHandler implements BeaconConsumer, RangeNotifier {
    private static String IBEACON_LAYOUT = "m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24";
    private static String MAP_ID = "5a96f0692616a30009a23ea4";
    private static String UUID = "B113B38F-8502-4DDF-B466-3F687EF15867";
    private static BeaconTracker instance;
    private BeaconManager manager;
    private Context mContext;
    private Classifier.Listener listener;
    private List<List<Beacon>> last5 = new ArrayList<>();
    private ServiceConnection serviceConnection;
    private List<Pair<Integer, String>> classificationBuffer = new ArrayList<>();

    private BeaconTracker(Context context) {
        manager = BeaconManager.getInstanceForApplication(context);
        manager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_LAYOUT));
        manager.setForegroundScanPeriod(300);
        mContext = context;
    }

    public static BeaconTracker getInstance(Context context) {
        if (instance == null) {
            instance = new BeaconTracker(context);
        }
        return instance;
    }

    public void start() {
        if (RoomKit.getInstance().permissionsGranted) {
            if (serviceConnection == null)
                manager.bind(this);
        }else{
            RoomKit.getInstance().beaconTracker = this;
        }
    }

    public void stop() {
        manager.unbind(this);
    }

    public void setOnClassifyListener(Classifier.Listener listener) {
        this.listener = listener;
    }

    public void setRangeFrequency(Integer hz) {
        manager.setForegroundScanPeriod((long) (1/((float) hz) * 1000));
    }

    @Override
    public void onBeaconServiceConnect() {
        manager.addRangeNotifier(this);

        try {
            manager.startRangingBeaconsInRegion(new Region("main", null, null, null));
        } catch (RemoteException error) {
            Log.d("RoomKit", "Failed to start ranging beacons" + error.getLocalizedMessage());
        }
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
        List<Beacon> list = new ArrayList<>();
        for (Beacon beacon : collection) {
            String uuid = beacon.getId1().toString();
            if (uuid.toLowerCase().equals(UUID.toLowerCase())) {
                list.add(beacon);
            }
        }

        last5.add(list);

        if (last5.size() > 5) {
            Map<Pair<Integer, Integer>, Integer> counts = new HashMap<>();
            Map<Pair<Integer, Integer>, Double> averages = new HashMap<>();

            for (List<Beacon> beacons : last5) {
                for (Beacon beacon : beacons) {
                    Double distance = beacon.getDistance();
                    Pair<Integer, Integer> pair = Pair.create(beacon.getId2().toInt(), beacon.getId3().toInt());
                    if (!counts.containsKey(pair)) {
                        counts.put(pair, 1);
                        averages.put(pair, distance);
                    } else {
                        int count = counts.get(pair) + 1;
                        averages.put(pair, distance / count + averages.get(pair) * (count - 1) / count);
                        counts.put(pair, count);
                    }
                }
            }
            last5 = new ArrayList<>();

            classifiy(averages);
        }
    }

    public void classifiy(Map<Pair<Integer, Integer>, Double> beacons) {
        AsyncHttpClient client = new AsyncHttpClient();

        List<Map<String, Object>> data = new ArrayList<>();
        for (Pair<Integer, Integer> pair : beacons.keySet()) {
            int major = pair.first;
            int minor = pair.second;
            Double strength = beacons.get(pair);
            Map<String, Object> map = new HashMap<>();
            map.put("major", major);
            map.put("minor", minor);
            map.put("strength", strength);

            data.add(map);
        }
        Gson gson = new Gson();
        String string = gson.toJson(data);
        Log.d("posting with", string);
        StringEntity entity = null;
        try {
            entity = new StringEntity(string, ContentType.APPLICATION_JSON);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        client.addHeader("authorization", "29ece557ad5f54dc1b813d56d53e7ac60dc7da1417734079b0");
        client.addHeader("client_os", "android");

        client.post(mContext, "https://roomkit.herokuapp.com/maps/" + MAP_ID, entity, "application/json", this);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
            int roomIndex = (Integer) response.get("roomIndex");
            String room = (String) response.get("room");
            Pair<Integer, String> pair = Pair.create(roomIndex, room);
            classificationBuffer.add(pair);
            if (classificationBuffer.size() > 5) {
                Pair<Integer, String> bestGuess = null;
                Integer max = 3;
                Map<Pair<Integer, String>, Integer> dict = new HashMap<>();

                for (Pair<Integer, String> guess : classificationBuffer) {
                    if (dict.get(guess) == null) {
                        dict.put(guess, 1);
                    }else{
                        dict.put(guess, dict.get(guess) + 1);
                    }
                }

                for (Pair<Integer, String> guess : dict.keySet()) {
                    if (((int) dict.get(guess)) > max) {
                        bestGuess = guess;
                        max = ((int) dict.get(guess));
                    }
                }
                classificationBuffer.remove(0);

                if (bestGuess != null) {
                    listener.onClassify(bestGuess.first, bestGuess.second);
                }
            }
        } catch (JSONException exp) {

        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.d("classify failure", Integer.toString(statusCode));
        Log.d("classify failure", responseString);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        if (this.serviceConnection != null) {
            return false;
        }

        mContext.bindService(intent, serviceConnection, i);
        this.serviceConnection = serviceConnection;
        return true;
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        mContext.unbindService(serviceConnection);
    }

    @Override
    public Context getApplicationContext() {
        return mContext;
    }
}
