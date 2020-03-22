package ru.azaychikov.exampleretrofit.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

import ru.azaychikov.exampleretrofit.R;
import ru.azaychikov.exampleretrofit.activity.FolderActivity;
import ru.azaychikov.exampleretrofit.model.File;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private List<File> dataList;
    private Context context;
    private OnFileClickListener onFileClickListener;
    private Map<String, List<File>> imageInFolders;

    public CustomAdapter(Context context, List<File> dataList, OnFileClickListener onFileClickListener, Map<String, List<File>> imageInFolders){
        this.context = context;
        this.dataList = dataList;
        this.onFileClickListener = onFileClickListener;
        this.imageInFolders = imageInFolders;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_row, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));


        if(dataList.get(position).getType().equals("dir") && imageInFolders.containsKey(dataList.get(position).getName())){
            holder.txtTitle.setText(dataList.get(position).getName());
            for(File file : imageInFolders.get(dataList.get(position).getName())) {
                if(file.getType().equals("file")) {
                    builder.build().load(file.getPreview())
                            .placeholder((R.drawable.ic_launcher_background))
                            .error(R.drawable.ic_launcher_background)
                            .into(holder.coverImage);
                    return;
                }
            }
            builder.build().load(imageInFolders.get(dataList.get(position).getName()).get(0).getPreview())
                    .placeholder((R.drawable.ic_launcher_background))
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.coverImage);
        }
        else if (dataList.get(position).getType().equals("file") && dataList.get(position).getMediaType().equals("image")) {
            holder.txtTitle.setText(dataList.get(position).getName());
            holder.icFolder.setVisibility(View.GONE);
            builder.build().load(dataList.get(position).getPreview())
                    .placeholder((R.drawable.ic_launcher_background))
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.coverImage);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface OnFileClickListener {
        void onFileClick(File file);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        private TextView txtTitle;
        private ImageView coverImage;
        private ImageView icFolder;

        CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            txtTitle = mView.findViewById(R.id.title);
            coverImage = mView.findViewById(R.id.coverImage);
            icFolder = mView.findViewById(R.id.iconFolder);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File file = dataList.get(getLayoutPosition());
                    onFileClickListener.onFileClick(file);
                }
            });
        }
    }
}
