package com.android.car_exam4;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;



import java.util.ArrayList;
import java.util.List;

public class LauncherActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_QUERY_ALL_PACKAGES = 1001;

    private ViewPager2 viewPager2;
    private List<AppInfo> appList;
    CarouselViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        viewPager2 = findViewById(R.id.viewPager2);





    // Request QUERY_ALL_PACKAGES permission if not granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                if (!hasQueryAllPackagesPermission()) {
                    requestQueryAllPackagesPermission();
                } else {
                    setupLauncher();
                }
            } catch (PackageManager.NameNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            setupLauncher();
        }
    }

    private void setupLauncher() {
        // Initialize the app list and set up the adapter

       // carouselRecyclerView.setAdapter(adapter);
        appList = getInstalledApps();

        adapter = new CarouselViewAdapter(appList);
        viewPager2.setPageTransformer(new CarouselPageTransformer());
        viewPager2.setAdapter(adapter);

        // Handle click events on app icons in the carousel
        adapter.setOnItemClickListener(new CarouselViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AppInfo appInfo = appList.get(position);
                launchApp(appInfo.getPackageName());
            }
        });

        // Handle click events on app icons
    }

    private List<AppInfo> getInstalledApps() {
        List<AppInfo> apps = new ArrayList<>();
        PackageManager packageManager = getPackageManager();
        List<ApplicationInfo> applicationInfos = packageManager.getInstalledApplications(0);

        for (ApplicationInfo appInfo : applicationInfos) {
            // Filter out system apps if needed
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                Drawable appIcon = appInfo.loadIcon(packageManager);
                String appName = appInfo.loadLabel(packageManager).toString();
                String packageName = appInfo.packageName;
                apps.add(new AppInfo(appIcon, appName, packageName));
            }
        }

        return apps;
    }

    private void launchApp(String packageName) {
        try {
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
            if (launchIntent != null) {
                startActivity(launchIntent);
            } else {
                Toast.makeText(this, "App cannot be launched.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error launching the app.", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private boolean hasQueryAllPackagesPermission() throws PackageManager.NameNotFoundException {
        return PackageManager.PERMISSION_GRANTED ==
                getPackageManager().getApplicationInfo(getPackageName(), 0).targetSdkVersion;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void requestQueryAllPackagesPermission() throws PackageManager.NameNotFoundException {
        if (!hasQueryAllPackagesPermission()) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.QUERY_ALL_PACKAGES)) {
                // Show an explanation to the user, if needed.
            }
            requestPermissions(new String[]{Manifest.permission.QUERY_ALL_PACKAGES},
                    REQUEST_PERMISSION_QUERY_ALL_PACKAGES);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_QUERY_ALL_PACKAGES) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupLauncher();
            } else {
                // Permission denied. Show a message or take appropriate action.
                Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
                // You can also direct the user to the app settings to grant the permission manually
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.fromParts("package", getPackageName(), null));
                startActivity(intent);
            }
        }
    }
}
