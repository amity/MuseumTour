package edu.dartmouthcs65.museumtour.RoomKit;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import edu.dartmouthcs65.museumtour.R;

public class RoomKit {
    private static Integer PERMISSIONS_REQUEST_CODE = 10;
    private static RoomKit instance;
    protected Boolean permissionsGranted = false;
    protected BeaconTracker beaconTracker;

    protected static RoomKit getInstance() {
        if (instance == null) {
            instance = new RoomKit();
        }
        return instance;
    }

    public static Boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != PERMISSIONS_REQUEST_CODE) {
            return false;
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            RoomKit.getInstance().permissionsGranted = true;
            if (RoomKit.getInstance().beaconTracker == null) {
                RoomKit.getInstance().beaconTracker.start();
            }
        }

        return true;
    }

    public static void checkPermissions(final Activity activity) {
        int status = activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        switch (status) {
            case PackageManager.PERMISSION_GRANTED:
                RoomKit.getInstance().permissionsGranted = true;
                break;
            case PackageManager.PERMISSION_DENIED:
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Location services needed");
                builder.setMessage("For the app to be able to determine room please grant location access");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        dialogInterface.dismiss();
                        activity.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_CODE);
                    }
                });
                builder.show();
                break;
            default:
                break;
        }
    }
}
