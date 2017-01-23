package com.silver.cat.nilo.view.main;

import android.Manifest;
import android.databinding.DataBindingUtil;
import android.databinding.OnRebindCallback;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.silver.cat.nilo.NiloApplication;
import com.silver.cat.nilo.R;
import com.silver.cat.nilo.config.dagger.activity.ActivityModule;
import com.silver.cat.nilo.databinding.ActivityMainBinding;
import com.silver.cat.nilo.util.permission.PermissionResult;
import com.silver.cat.nilo.view.main.model.LogoViewModel;
import com.silver.cat.nilo.view.main.model.MainViewModel;
import com.silver.cat.nilo.view.main.model.RecyclerViewModel;
import com.silver.cat.nilo.view.main.model.SearchViewModel;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import javax.inject.Inject;

public class MainActivity extends RxAppCompatActivity implements GoogleApiClient
        .ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @Inject
    PermissionResult permissionResult;

    private GoogleApiClient mGoogleApiClient;
    private ActivityMainBinding dataBinding;
    private RecyclerViewModel recyclerViewModel;

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

        recyclerViewModel = new RecyclerViewModel(this, dataBinding.recycler);

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



        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Places.GEO_DATA_API).addApi
                (Places.PLACE_DETECTION_API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        permissionResult.request(Manifest.permission.ACCESS_FINE_LOCATION).compose(bindUntilEvent
                (ActivityEvent.DESTROY)).subscribe(granted -> {
            if (granted) {
                getCurrentPlace();
            }
        });

    }

    @SuppressWarnings("MissingPermission")
    private void getCurrentPlace() {
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace
                (mGoogleApiClient, null);
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
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        System.out.println("bundle = [" + bundle + "]");
    }

    @Override
    protected void onDestroy() {
//        mainViewModel.destroy();
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
}
