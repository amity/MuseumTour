package edu.dartmouthcs65.museumtour.RoomKit;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BeaconTracker implements BeaconConsumer, RangeNotifier {
    private static String IBEACON_LAYOUT = "m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24";
    private static String MAP_ID = "5a96f0692616a30009a23ea4";
    private static String UUID = "B113B38F-8502-4DDF-B466-3F687EF15867";
    private static BeaconTracker instance;
    private BeaconManager manager;
    private Context mContext;
    private Classifier.Listener listener;
    private List<List<Beacon>> last5 = new ArrayList<>();

    private BeaconTracker(Context context) {
        manager = BeaconManager.getInstanceForApplication(context);
        manager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_LAYOUT));
        manager.setForegroundScanPeriod(16);
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
        Log.d("RoomKit", "did range!");
        List<Beacon> list = new ArrayList<>();
        for (Beacon beacon : collection) {
            String uuid = beacon.getId1().toString();
            if (uuid.toLowerCase().equals(UUID.toLowerCase())) {
                list.add(beacon);
            }
        }

        last5.add(list);

        if (last5.size() > 5) {
            Map<String, Integer> counts = new HashMap<>();
            Map<String, Double> averages = new HashMap<>();

            for (List<Beacon> beacons : last5) {
                for (Beacon beacon : beacons) {
                    String string = beacon.getId2().toString() + beacon.getId3().toString();
                    if (!counts.containsKey(string)) {
                        counts.put(string, 1);
                        averages.put(string, beacon.getDistance());
                    } else {
                        int count = counts.get(string) + 1;
                        averages.put(string, beacon.getDistance() * 1 / (count) + averages.get(string) * (count - 1) / count);
                        counts.put(string, count);
                    }
                }
            }
            last5 = new ArrayList<>();

            new Classifier(listener).classifiy(averages, MAP_ID);
        }
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        mContext.bindService(intent, serviceConnection, i);
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
