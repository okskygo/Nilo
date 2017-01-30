package com.silver.cat.nilo.view.main;

import android.Manifest;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.OnRebindCallback;
import android.databinding.ViewDataBinding;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.silver.cat.nilo.NiloApplication;
import com.silver.cat.nilo.R;
import com.silver.cat.nilo.config.dagger.activity.ActivityModule;
import com.silver.cat.nilo.databinding.ActivityMainBinding;
import com.silver.cat.nilo.util.permission.PermissionResult;
import com.silver.cat.nilo.view.main.model.MainViewModel;
import com.silver.cat.nilo.view.main.model.SearchViewModel;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class MainActivity extends RxAppCompatActivity implements GoogleApiClient
        .ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, SearchViewModel
        .OnSearchStatusChangeListener {

    private static final LatLngBounds DEFAULT_TAIPEI = new LatLngBounds(
            new LatLng(24.949232, 121.521469), new LatLng(25.2573275, 121.5290217));
    @Inject
    PermissionResult permissionResult;
    private LatLngBounds latLngBounds;
    private GoogleApiClient googleApiClient;
    private ActivityMainBinding dataBinding;
    private MainViewModel mainViewModel;
    private Subject<CharSequence> loadSuggestion = PublishSubject.create();
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s == null || s.toString().trim().length() <= 0) {
                mainViewModel.updateSuggestion(Collections.emptyList());
            }
            loadSuggestion.onNext(s);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((NiloApplication) getApplication()).getComponent()
                .newActivitySubComponent(new ActivityModule(this))
                .inject(this);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        dataBinding.addOnRebindCallback(new OnRebindCallback() {
            @Override
            public boolean onPreBind(ViewDataBinding binding) {
                TransitionManager.beginDelayedTransition(
                        (ViewGroup) binding.getRoot());
                return super.onPreBind(binding);
            }
        });

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        loadSuggestion
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .debounce(500, TimeUnit.MILLISECONDS)
                .map(charSequence -> {
                    if (!googleApiClient.isConnected()) {
                        return Collections.EMPTY_LIST;
                    }

                    PendingResult<AutocompletePredictionBuffer> results =
                            Places.GeoDataApi.getAutocompletePredictions(googleApiClient,
                                    charSequence.toString(),
                                    latLngBounds == null ? DEFAULT_TAIPEI : latLngBounds,
                                    new AutocompleteFilter.Builder().setTypeFilter
                                            (AutocompleteFilter.TYPE_FILTER_NONE).build()
                            );

                    AutocompletePredictionBuffer autocompletePredictions = results
                            .await(60, TimeUnit.SECONDS);

                    // Confirm that the query completed successfully, otherwise return null
                    final Status status = autocompletePredictions.getStatus();
                    if (!status.isSuccess()) {
                        autocompletePredictions.release();
                        return Collections.EMPTY_LIST;
                    }
                    // Freeze the results immutable representation that can be stored safely.
                    return DataBufferUtils.freezeAndClose(autocompletePredictions);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(autocompletePredictions -> mainViewModel.updateSuggestion
                        (autocompletePredictions))
                .subscribe();
        mainViewModel = new MainViewModel(this, this, textWatcher);
        dataBinding.setModel(mainViewModel);


//        dataBinding.button.setOnClickListener(v -> {
//            int PLACE_PICKER_REQUEST = 1;
//            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//
//            try {
//                startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
//            } catch (GooglePlayServicesRepairableException e) {
//                e.printStackTrace();
//            } catch (GooglePlayServicesNotAvailableException e) {
//                e.printStackTrace();
//            }
//        });


        permissionResult.request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
                .ACCESS_COARSE_LOCATION).compose(bindUntilEvent
                (ActivityEvent.DESTROY)).subscribe(granted -> {
            if (granted) {
//                getCurrentPlace();
                getLastBestLocation();
            }
        });

    }

    @SuppressWarnings("MissingPermission")
    private void getCurrentPlace() {
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace
                (googleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    Log.i("onResult", String.format("Place '%s' has likelihood: %g",
                            placeLikelihood.getPlace().getName(), placeLikelihood.getLikelihood()));
                }
                likelyPlaces.release();
            }
        });
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        System.out.println("bundle = [" + bundle + "]");
    }

    @Override
    protected void onDestroy() {
        mainViewModel.destroy();
        super.onDestroy();
    }

    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("i = [" + i + "]");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("connectionResult = [" + connectionResult + "]");
    }

    @Override
    public void change(boolean willSearchExpand) {
        mainViewModel.change(dataBinding.recycler, willSearchExpand);
    }

    private void getLastBestLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context
                .LOCATION_SERVICE);

        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNet = locationManager.getLastKnownLocation(LocationManager
                .NETWORK_PROVIDER);

        if (locationGPS == null && locationNet == null) {
            return;
        }

        long GPSLocationTime = 0;
        if (null != locationGPS) {
            GPSLocationTime = locationGPS.getTime();
        }

        long NetLocationTime = 0;

        if (null != locationNet) {
            NetLocationTime = locationNet.getTime();
        }

        if (0 < GPSLocationTime - NetLocationTime) {
            latLngBounds = new LatLngBounds(new LatLng(locationGPS.getLatitude() - 1, locationGPS
                    .getLongitude() - 1), new LatLng(locationGPS.getLatitude() + 1, locationGPS
                    .getLongitude() + 1));
        } else {
            latLngBounds = new LatLngBounds(new LatLng(locationNet.getLatitude() - 1, locationNet
                    .getLongitude() - 1), new LatLng(locationNet.getLatitude() + 1, locationNet
                    .getLongitude() + 1));
        }
    }
}
