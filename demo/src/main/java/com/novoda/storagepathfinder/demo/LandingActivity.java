package com.novoda.storagepathfinder.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.novoda.storagepathfinder.AndroidDeviceStorageInspector;
import com.novoda.storagepathfinder.DeviceStorageRoot;

import java.util.List;

public class LandingActivity extends AppCompatActivity {

    private static final String ASSET_NAME = "10MB.zip";

    private AssetCloner assetCloner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        assetCloner = DemoDependenciesFactory.createAssetCloner(getApplicationContext());

        AndroidDeviceStorageInspector storageInspector = DemoDependenciesFactory.createStorageInspector(getApplicationContext());
        List<DeviceStorageRoot> deviceStorageRoots = storageInspector.getDeviceStorageRoots();

        RecyclerView recyclerView = findViewById(R.id.device_storage_roots);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        DeviceStorageRootsAdapter deviceStorageRootsAdapter = new DeviceStorageRootsAdapter(
                getLayoutInflater(),
                deviceStorageRoots,
                onAddFileClicked
        );
        recyclerView.setAdapter(deviceStorageRootsAdapter);
    }

    private final DeviceStorageRootViewHolder.Listener onAddFileClicked = deviceStorageRoot -> {
        String pathToCloneTo = deviceStorageRoot.getAbsolutePath() + "/" + ASSET_NAME;
        assetCloner.cloneAsset(ASSET_NAME, pathToCloneTo);
    };

}
