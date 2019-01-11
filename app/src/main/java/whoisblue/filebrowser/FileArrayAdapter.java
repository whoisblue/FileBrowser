package whoisblue.filebrowser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;


public class FileArrayAdapter extends ArrayAdapter<FileItem> {

    private Context mContext;
    private int mResId;
    private List<FileItem> mItems;

    public FileArrayAdapter(Context context, int ResourceId, List<FileItem> items) {
        super(context, ResourceId, items);
        mContext = context;
        mResId = ResourceId;
        mItems = items;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView != null) {
            view = convertView;
        } else {
            view = LayoutInflater.from(mContext).inflate(mResId, null);
        }
        final FileItem currentItem = mItems.get(position);
        if(currentItem != null) {
            TextView fileName = view.findViewById(R.id.textView);
            fileName.setText(currentItem.getName());
            ImageView fileIcon = view.findViewById(R.id.imageView);
            fileIcon.setTag(position);

            if(currentItem.getType() == FileItem.TYPE_FILE) {
                //for image files
                if(currentItem.getName().endsWith(".jpg") || currentItem.getName().endsWith(".png")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final Bitmap thumbnail = getThumbnailImage(currentItem.getPath());
                            FileListActivity.mHandle.post(new Runnable() {
                                @Override
                                public void run() {
                                    ImageView icon = FileListActivity.mListView.findViewWithTag(position);
                                    if(icon != null)
                                        icon.setImageBitmap(thumbnail);
                                }
                            });
                        }
                    }).start();
                } else {
                    //common files
                    fileIcon.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.file_icon));
                }
            } else {
                fileIcon.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.folder_icon));
            }
        }
        return view;
    }

    private Bitmap getThumbnailImage(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int originalSize = options.outHeight > options.outWidth ? options.outHeight : options.outWidth;
        int targetSize = (int) mContext.getResources().getDimension(R.dimen.file_browser_thumbnail_image_size);
        options.inSampleSize = originalSize / targetSize;
        options.inJustDecodeBounds = false;
        Bitmap thumbnailImage = BitmapFactory.decodeFile(filePath, options);
        if(thumbnailImage != null)
            return thumbnailImage;
        else
            return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.file_icon);
    }

}
