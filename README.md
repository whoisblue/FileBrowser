# FileBrowser
FileBrowser lib for Android.

1. Intent to open FileBrowser:
Intent intent = new Intent(getApplicationContext(), FileListActivity.class);
intent.setType("application/vnd.android.package-archive");
startActivityForResult(intent, REQUEST_CODE_GET_APK);

2. onActivityResult gets file path:
String path = data.getStringExtra(FileListActivity.FILE_PATH);
