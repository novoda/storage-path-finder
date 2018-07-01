package com.novoda.storagepathfinder.demo;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.novoda.storagepathfinder.StoragePath;

import java.io.File;

import org.apache.commons.io.FileUtils;

import static android.os.Build.VERSION.SDK_INT;
import static com.novoda.storagepathfinder.StoragePath.Type.PRIMARY;
import static com.novoda.storagepathfinder.StoragePath.Type.SECONDARY;
import static org.apache.commons.io.FileUtils.byteCountToDisplaySize;

class StoragePathViewHolder extends RecyclerView.ViewHolder {

    private final TextView absolutePathView;
    private final TextView pathTypeView;
    private final View addFile;
    private final TextView pathUsedSizeView;
    private final TextView pathAvailableSizeView;
    private final TextView pathTotalSizeView;
    private final TextView permissionsView;
    private final TextView stateView;

    StoragePathViewHolder(View root) {
        super(root);
        pathTypeView = root.findViewById(R.id.device_storage_root_type);
        absolutePathView = root.findViewById(R.id.device_storage_root);
        pathUsedSizeView = root.findViewById(R.id.device_storage_root_used_size);
        pathAvailableSizeView = root.findViewById(R.id.device_storage_root_available_size);
        pathTotalSizeView = root.findViewById(R.id.device_storage_root_total_size);
        permissionsView = root.findViewById(R.id.device_storage_root_permissions);
        stateView = root.findViewById(R.id.device_storage_root_state);
        addFile = root.findViewById(R.id.add_file);
    }

    void bind(StoragePath deviceStoragePath, Listener listener) {
        String typeText = "Type: " + deviceStoragePath.getType();
        pathTypeView.setText(typeText);

        String pathText = "Path: " + deviceStoragePath.getPathAsString();
        absolutePathView.setText(pathText);

        String sizeUsedText = "Storage Used: " + getSizeOf(deviceStoragePath);
        pathUsedSizeView.setText(sizeUsedText);

        long freeSpaceOfPath = deviceStoragePath.getPathAsFile().getFreeSpace();
        String sizeAvailableText = "Storage Available: " + byteCountToDisplaySize(freeSpaceOfPath);
        pathAvailableSizeView.setText(sizeAvailableText);

        long totalSpaceOfPath = deviceStoragePath.getPathAsFile().getTotalSpace();
        String sizeTotalText = "Storage Total: " + byteCountToDisplaySize(totalSpaceOfPath);
        pathTotalSizeView.setText(sizeTotalText);

        String canRead = String.valueOf(deviceStoragePath.getPathAsFile().canRead());
        String canWrite = String.valueOf(deviceStoragePath.getPathAsFile().canWrite());
        String permissionsText = "Permissions: Read:" + canRead + " | Write:" + canWrite;
        permissionsView.setText(permissionsText);

        String stateText = getStateFor(deviceStoragePath);
        stateView.setText(stateText);

        addFile.setOnClickListener(v -> listener.onAddFileClickedFor(deviceStoragePath));
    }

    private String getStateFor(StoragePath deviceStoragePath) {
        StoragePath.Type type = deviceStoragePath.getType();

        String state = "";
        String removable = "unknown";
        String emulated = "unknown";

        if (type.equals(PRIMARY)) {
            state = Environment.getExternalStorageState();
            removable = String.valueOf(Environment.isExternalStorageRemovable());
            emulated = String.valueOf(Environment.isExternalStorageEmulated());
        }

        if (parameterisedExternalStorageEnvironmentCallsAreAvailable() && type.equals(SECONDARY)) {
            File pathAsFile = deviceStoragePath.getPathAsFile();
            state = Environment.getExternalStorageState(pathAsFile);
            removable = String.valueOf(Environment.isExternalStorageRemovable(pathAsFile));
            emulated = String.valueOf(Environment.isExternalStorageEmulated(pathAsFile));
        }

        return "State: " + state + " | Removable:" + removable + " | Emulated:" + emulated;
    }

    private String getSizeOf(StoragePath deviceStoragePath) {
        File pathAsFile = deviceStoragePath.getPathAsFile();

        long sizeOfDirectory = FileUtils.sizeOfDirectory(pathAsFile);
        if (blockSizeCalculationsAreAvailable()) {
            if (deviceStoragePath.getType() == PRIMARY && sizeOfDirectory == 0) {
                sizeOfDirectory = getUsedSizeOf(deviceStoragePath);
            }
        }

        return byteCountToDisplaySize(sizeOfDirectory);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private long getUsedSizeOf(StoragePath deviceStoragePath) {
        long totalSize = getTotalSizeOf(deviceStoragePath);
        long availableSize = getAvailableSizeOf(deviceStoragePath);
        return totalSize - availableSize;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private long getTotalSizeOf(StoragePath deviceStoragePath) {
        StatFs stat = new StatFs(deviceStoragePath.getPathAsString());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return totalBlocks * blockSize;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private long getAvailableSizeOf(StoragePath deviceStoragePath) {
        StatFs stat = new StatFs(deviceStoragePath.getPathAsString());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return availableBlocks * blockSize;
    }

    private boolean blockSizeCalculationsAreAvailable() {
        return SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    private boolean parameterisedExternalStorageEnvironmentCallsAreAvailable() {
        return SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    interface Listener {
        void onAddFileClickedFor(StoragePath deviceStoragePath);
    }
}
