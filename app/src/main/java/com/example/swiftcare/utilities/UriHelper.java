package com.example.swiftcare.utilities;

import android.net.Uri;

public class UriHelper {
    private static Uri fileUri;

    public static void setFileUri(Uri uri) {
        fileUri = uri;
    }

    public static Uri getFileUri() {
        return fileUri;
    }

    public static void clearFileUri() {
        fileUri = null;
    }
}
