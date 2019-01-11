package whoisblue.filebrowser;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Environment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileListActivity extends ListActivity {

    public static final String FILE_PATH = "file_path";
    private String typeMIME;
    private final String typeAPK = "application/vnd.android.package-archive";

    static Handler mHandle = new Handler(Looper.getMainLooper());
    static ListView mListView = null;

    private String SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private File mCurrentNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String type = getIntent().getType();
        typeMIME = (type != null)?type:"*/*";

        mListView = getListView();
        List<FileItem> items = getFileItems(new File(SDPATH));
        setListAdapter(new FileArrayAdapter(this, R.layout.file_item, items));
    }

    @Override
    public void onBackPressed() {
        if(!mCurrentNode.getAbsolutePath().equals(SDPATH))
        {
            List<FileItem> items = getFileItems(mCurrentNode.getParentFile());
            setListAdapter(new FileArrayAdapter(this, R.layout.file_item, items));
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        FileItem item = (FileItem) getListAdapter().getItem(position);
        mCurrentNode = new File(item.getPath());
        if(item.getType() == FileItem.TYPE_DIR) {
            List<FileItem> items = getFileItems(mCurrentNode);
            setListAdapter(new FileArrayAdapter(this, R.layout.file_item, items));
        } else {
            Intent intent = new Intent();
            intent.putExtra(FILE_PATH, mCurrentNode.getAbsolutePath());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private List<FileItem> getFileItems(File file) {
        mCurrentNode = file;
        this.setTitle(mCurrentNode.getAbsolutePath());
        File[] items = file.listFiles();
        List<FileItem> mDir = new ArrayList<>();
        List<FileItem> mFile = new ArrayList<>();
        for(File item:items) {
            if(item.isDirectory()) {
                mDir.add(new FileItem(FileItem.TYPE_DIR, item.getName(), item.getAbsolutePath()));
            } else {
                String suffixFilter = null;
                switch (typeMIME) {
                    case typeAPK:
                        suffixFilter = ".apk";
                        break;
                    default:
                        break;
                }
                if(suffixFilter != null && !item.getName().endsWith(suffixFilter)) {
                    continue;
                }
                mFile.add(new FileItem(FileItem.TYPE_FILE, item.getName(), item.getAbsolutePath()));
            }
        }
        mDir.addAll(mFile);
        if(!SDPATH.equals(file.getAbsolutePath())) {
            mDir.add(0, new FileItem(FileItem.TYPE_DIR, "..", file.getParent()));
        }
        return mDir;
    }
}
