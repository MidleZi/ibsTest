package com.example.ibstestmvp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ibstestmvp.R;
import com.example.ibstestmvp.entities.Item;
import com.example.ibstestmvp.view.FolderActivity;
import com.example.ibstestmvp.view.ImageActivity;
import java.util.List;
import java.util.Map;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    private Map<String, List<Item>> items;
    private Context context;
    private LayoutInflater inflater;
    private List<Item> dataList;

    public void loadedNewItems(Map<String, List<Item>> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public Adapter(Context context, List<Item> dataList){
        this.context = context;
        this.dataList = Item.getImageFromFolder(dataList);
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(dataList.get(position).getType().equals("dir")){
            holder.txtTitle.setText(dataList.get(position).getName());
            holder.icFolder.setVisibility(View.VISIBLE);
            if(items.containsKey(dataList.get(position).getName())) {
                for (Item item : items.get(dataList.get(position).getName())) {
                    if (item.getType().equals("file")) {
                        loadImageWithGlide(holder, item);
                        return;
                    }
                }
            }
        }
        else if (dataList.get(position).getType().equals("file") && dataList.get(position).getMediaType().equals("image")) {
            holder.txtTitle.setText(dataList.get(position).getName());
            holder.icFolder.setVisibility(View.GONE);
            loadImageWithGlide(holder,dataList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private void loadImageWithGlide(ViewHolder holder, Item item) {
        Glide.with(context)
                .load(item.getPreview())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.coverImage);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public final View itemView;
        private ImageView coverImage;
        private TextView txtTitle;
        private ImageView icFolder;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.coverImage = itemView.findViewById(R.id.coverImage);
            this.txtTitle = itemView.findViewById(R.id.title);
            this.icFolder = itemView.findViewById(R.id.iconFolder);
            this.icFolder.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Item item = null;
                    for(Item itemInData : dataList){
                        if(itemInData.getName().equals(txtTitle.getText().toString())) {
                            item = itemInData;
                            break;
                        }
                    }
                    if(item.getType().equals("file")) {
                        Intent intent = new Intent(context, ImageActivity.class);
                        intent.putExtra(Item.class.getSimpleName(), item);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, FolderActivity.class);
                        intent.putExtra(Item.class.getSimpleName(), item);
                        context.startActivity(intent);
                    }
                }
            });
        }


    }
}
