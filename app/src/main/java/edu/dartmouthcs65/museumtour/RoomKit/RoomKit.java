package edu.dartmouthcs65.museumtour.RoomKit;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

/**
 * Created by john on 2/28/18.
 */

public class RoomKit {
    public static Integer PERMISSIONS_REQUEST_CODE = 10;
    private static RoomKit instance;
    private Boolean permissionsGranted = false;

    private static RoomKit getInstance() {
        if (instance == null) {
            return new RoomKit();
        }
        return instance;
    }

    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            RoomKit.getInstance().permissionsGranted = true;
        }
    }

    public static void checkPermissions(Activity activity) {
        int status = activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        switch (status) {
            case PackageManager.PERMISSION_GRANTED:
                RoomKit.getInstance().permissionsGranted = true;
                break;
            case PackageManager.PERMISSION_DENIED:
                activity.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_CODE);
                break;
            default:
                break;
        }
    }
}
