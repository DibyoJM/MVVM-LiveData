package com.altimetrik.albumsearch.ui.main;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.altimetrik.albumsearch.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private RecyclerView recyclerView;
    private EditText searchBox;
    private AlbumListAdapter adapter;
    private AlbumData albumData;
    List<AlbumData> albumDataList = new ArrayList<>();

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        searchBox = view.findViewById(R.id.search_edit_box);
        searchBox.addTextChangedListener(
                new TextWatcher() {
                    @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                    @Override
                    public void afterTextChanged(final Editable s) {
                        adapter.getFilter().filter(searchBox.getText().toString());
                    }
                }
        );

        try {
            loadJSONFromAsset(Objects.requireNonNull(getActivity()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }

    private void loadJSONFromAsset(FragmentActivity activity) throws JSONException {
        String json = null;
        try {
            InputStream is = activity.getAssets().open("dummy.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        assert json != null;
        JSONObject jsnobject = new JSONObject(json);
        JSONArray jsonArray = jsnobject.getJSONArray("results");
        for (int i = 0; i < jsonArray.length(); i++) {
            albumData = new AlbumData();
            JSONObject albumObject = jsonArray.getJSONObject(i);
            albumData.artistName = albumObject.getString("artistName");
            //albumData.collectionName = albumObject.getString("collectionName");
            albumData.trackName = albumObject.getString("trackName");
            albumData.artworkUrl100 = albumObject.getString("artworkUrl100");
            albumData.collectionPrice = albumObject.getString("collectionPrice");
            albumData.releaseDate = albumObject.getString("releaseDate");

            albumDataList.add(albumData);
        }

        adapter = new AlbumListAdapter(albumDataList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
    }


}
