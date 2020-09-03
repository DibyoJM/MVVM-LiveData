package com.altimetrik.albumsearch.ui.main;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.altimetrik.albumsearch.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.ViewHolder> implements Filterable {

    private List<AlbumData> mDataset;
    private List<AlbumData> mDatasetFilter;


    AlbumListAdapter(List<AlbumData> mDataset) {
        this.mDataset = mDataset;
        this.mDatasetFilter = mDataset;
    }


    @NonNull
    @Override
    public AlbumListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_list_item, parent, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final AlbumListAdapter.ViewHolder holder, int position) {
        try {
            holder.artistName.setText(mDatasetFilter.get(position).getArtistName());
            holder.trackName.setText(mDatasetFilter.get(position).getTrackName());
            //holder.collectionName.setText(mDatasetFilter.get(position).collectionName);
            holder.collectionPrice.setText(String.format("$ %s", mDatasetFilter.get(position).getCollectionPrice()));

            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault());
            Date startDate = inputDateFormat.parse(mDatasetFilter.get(position).getReleaseDate());
            assert startDate != null;
            holder.releaseDate.setText(outputDateFormat.format(startDate));

            Picasso.get()
                    .load(mDatasetFilter.get(position).getArtworkUrl100())
                    .into(holder.imgView);

        } catch (Exception e) {
            Log.d("Exception:", " " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mDatasetFilter.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                Log.i(charString, "===tt==" + charString);

                if (charString.isEmpty()) {
                    mDatasetFilter = mDataset;
                } else {
                    List<AlbumData> filteredList = new ArrayList<>();
                    for (AlbumData row : mDataset) {
                        if (row.getArtistName().toLowerCase().contains(charString.toLowerCase()) || row.getTrackName().toLowerCase().contains(charString.toLowerCase())) {
                            Log.i(charString, "=====" + row.getArtistName());
                            filteredList.add(row);
                        }
                    }

                    mDatasetFilter = filteredList;
                }
                Log.i(charString, "=====" + mDatasetFilter.size());

                FilterResults filterResults = new FilterResults();
                filterResults.values = mDatasetFilter;
                Log.i(charString, "=====" + filterResults.count);
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                //  mDatasetFilter = (ArrayList<DocumentListQuery.Document>) filterResults.values;
                Log.i("publishResults", "=====" + mDatasetFilter.size());

                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView artistName, trackName, collectionName, collectionPrice, releaseDate;
        ImageView imgView;

        ViewHolder(View itemView) {
            super(itemView);

            artistName = itemView.findViewById(R.id.artist_name);
            trackName = itemView.findViewById(R.id.track_name);
            //collectionName = itemView.findViewById(R.id.collection_name);
            collectionPrice = itemView.findViewById(R.id.collection_price);
            releaseDate = itemView.findViewById(R.id.release_date);

            imgView = itemView.findViewById(R.id.img_view);

        }
    }
}
