package com.mehmetakiftutuncu.muezzin.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mehmetakiftutuncu.muezzin.R;
import com.mehmetakiftutuncu.muezzin.adapters.CitiesAdapter;
import com.mehmetakiftutuncu.muezzin.models.City;
import com.mehmetakiftutuncu.muezzin.utilities.Log;
import com.mehmetakiftutuncu.muezzin.utilities.MuezzinAPIClient;
import com.mehmetakiftutuncu.muezzin.utilities.optional.Optional;

import java.util.ArrayList;

/**
 * Created by akif on 08/05/16.
 */
public class CitySelectionFragment extends Fragment implements MuezzinAPIClient.OnCitiesDownloadedListener {
    private RecyclerView recyclerViewCitySelection;

    private LinearLayoutManager linearLayoutManager;
    private CitiesAdapter citiesAdapter;

    private int countryId = 2;

    public CitySelectionFragment() {}

    @Override public void onStart() {
        super.onStart();

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewCitySelection.setLayoutManager(linearLayoutManager);

        loadCities();
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_cityselection, container, false);

        recyclerViewCitySelection = (RecyclerView) layout.findViewById(R.id.recyclerView_citySelection);

        return layout;
    }

    @Override public void onCitiesDownloaded(@NonNull ArrayList<City> cities) {
        Log.debug(getClass(), "Saving cities for country '%d' to database...", countryId);

        if (!City.saveCities(getContext(), countryId, cities)) {
            Snackbar.make(recyclerViewCitySelection, "Failed to save cities to database!", Snackbar.LENGTH_INDEFINITE).setAction("OK", null).show();
        }

        setCities(cities);
    }

    @Override public void onCitiesDownloadFailed() {
        Snackbar.make(recyclerViewCitySelection, "Failed to download cities!", Snackbar.LENGTH_INDEFINITE).setAction("OK", null).show();
    }

    private void loadCities() {
        Log.debug(getClass(), "Loading cities for country '%d' from database...", countryId);

        Optional<ArrayList<City>> maybeCitiesFromDatabase = City.getCities(getContext(), countryId);

        if (maybeCitiesFromDatabase.isEmpty) {
            Snackbar.make(recyclerViewCitySelection, "Failed to load cities from database!", Snackbar.LENGTH_INDEFINITE).setAction("OK", null).show();
        } else {
            ArrayList<City> citiesFromDatabase = maybeCitiesFromDatabase.get();

            if (citiesFromDatabase.isEmpty()) {
                Log.debug(getClass(), "No cities for country '%d' were found on database! Downloading...", countryId);

                MuezzinAPIClient.getCities(countryId, this);
            } else {
                Log.debug(getClass(), "Loaded cities for country '%d' from database!", countryId);

                setCities(citiesFromDatabase);
            }
        }
    }

    private void setCities(@NonNull ArrayList<City> cities) {
        citiesAdapter = new CitiesAdapter(cities);
        recyclerViewCitySelection.setAdapter(citiesAdapter);
    }
}
