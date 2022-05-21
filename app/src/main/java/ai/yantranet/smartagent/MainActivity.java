package ai.yantranet.smartagent;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent myService = new Intent(this, MyService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, CAMERA},
                    100);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startService(myService);
        } else {
            startService(myService);
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:

                boolean external_storage_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean write_external_storage_accepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean camera = grantResults[2] == PackageManager.PERMISSION_GRANTED;


                if (external_storage_accepted && write_external_storage_accepted && camera) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(MainActivity.this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(MainActivity.this, CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling

                        return;
                    } else {
                        finish();

                    }
                    //code here
                } else {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE) && shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE) && shouldShowRequestPermissionRationale(CAMERA)) {
                            showMessageOKCancel("To access this app Give permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, CAMERA},
                                                        100);
                                            }
                                        }
                                    });
                            return;
                        } else {
                            finish();
//                            Toast.makeText(MainActivity.this, "Permissions Denied", Toast.LENGTH_LONG).show();

                        }
                    }
                }
                break;
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Confirm");

        builder.setMessage(message);
        builder.setPositiveButton("OK", okListener);
        final AlertDialog alert = builder.create();
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                alert.dismiss();
            }
        });
        alert.show();
    }


}