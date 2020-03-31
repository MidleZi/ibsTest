package ru.azaychikov.ibstestmvvm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
import ru.azaychikov.ibstestmvvm.R;
import ru.azaychikov.ibstestmvvm.model.File;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private List<File> dataList = new ArrayList<>();
    private Context context;
    private OnFileClickListener onFileClickListener;
    private Map<String, List<File>> imageInFolders;

    public CustomAdapter(Context context, Map<String, List<File>> imageInFolders, OnFileClickListener onFileClickListener) {
        this.context = context;
        this.imageInFolders = imageInFolders;
        this.onFileClickListener = onFileClickListener;
        dataList = imageInFolders.get("SolarSystem");
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_row, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        if (dataList.get(position).getType().equals("dir") && imageInFolders.containsKey(dataList.get(position).getName())) {
            holder.txtTitle.setText(dataList.get(position).getName());
            holder.icFolder.setVisibility(View.VISIBLE);
            for (File file : imageInFolders.get(dataList.get(position).getName())) {
                if (dataList.get(position).getType().equals("file")) {
                    Glide.with(context)
                            .load(dataList.get(position).getPreview())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_foreground)
                            .into(holder.coverImage);
                    return;
                }
            }
        } else if (dataList.get(position).getType().equals("file") && dataList.get(position).getMediaType().equals("image")) {
            holder.txtTitle.setText(dataList.get(position).getName());
            holder.icFolder.setVisibility(View.GONE);
            Glide.with(context)
                    .load(dataList.get(position).getPreview())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(holder.coverImage);
        }
    }

    @Override
    public int getItemCount() {
        if(dataList != null) {
            return dataList.size();
        } else {
            return 0;
        }
    }

    public interface OnFileClickListener {
        void onFileClick(File file);
    }

    public void setImageInFolders(Map<String, List<File>> imageInFolders) {
        this.imageInFolders = imageInFolders;
        notifyDataSetChanged();
    }

    public Map<String, List<File>> getImageInFolders() {
        return imageInFolders;

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
