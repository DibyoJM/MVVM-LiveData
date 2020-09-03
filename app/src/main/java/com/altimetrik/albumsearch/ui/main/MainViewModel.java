package com.altimetrik.albumsearch.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<AlbumData>> albumLiveData;
    private ArrayList<AlbumData> albumDataArrayList;

    public MainViewModel(@NonNull Application application) throws JSONException {
        super(application);
        albumLiveData = new MutableLiveData<>();
        init();
    }

    MutableLiveData<ArrayList<AlbumData>> getAlbumMutableLiveData() {
        return albumLiveData;
    }

    private void init() throws JSONException {
        populateList();
        albumLiveData.setValue(albumDataArrayList);
    }

    private void populateList() throws JSONException {
        albumDataArrayList = new ArrayList<>();
        String json = null;
        try {
            InputStream is = getApplication().getAssets().open("dummy.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        AlbumData albumData;
        assert json != null;
        JSONObject jsnobject = new JSONObject(json);
        JSONArray jsonArray = jsnobject.getJSONArray("results");
        for (int i = 0; i < jsonArray.length(); i++) {
            albumData = new AlbumData();
            JSONObject albumObject = jsonArray.getJSONObject(i);
            albumData.setArtistName(albumObject.getString("artistName"));
            //albumData.setCollectionName(albumObject.getString("collectionName"));
            albumData.setTrackName(albumObject.getString("trackName"));
            albumData.setArtworkUrl100(albumObject.getString("artworkUrl100"));
            albumData.setCollectionPrice(albumObject.getString("collectionPrice"));
            albumData.setReleaseDate(albumObject.getString("releaseDate"));

            albumDataArrayList.add(albumData);
        }

    }
}
