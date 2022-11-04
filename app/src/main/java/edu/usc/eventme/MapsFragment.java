package edu.usc.eventme;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;


import androidx.databinding.DataBindingUtil;

import edu.usc.eventme.databinding.*;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

public class MapsFragment extends Fragment {

    private static final String TAG = MapsFragment.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition cameraPosition;

    // The entry point to the Places API.
    private PlacesClient placesClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;

    // Keys for storing activity state.
    // [START maps_current_place_state_keys]
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    // [END maps_current_place_state_keys]

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] likelyPlaceNames;
    private String[] likelyPlaceAddresses;
    private List[] likelyPlaceAttributions;
    private LatLng[] likelyPlaceLatLngs;

    private FusedLocationProviderClient fusedLocationClient;
    private FusedLocationProviderClient client;

    private String longitude, latitude;
    private Location currentlocation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize view
        View view=inflater.inflate(R.layout.fragment_maps, container, false);

        // Initialize map fragment
        SupportMapFragment supportMapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);




        // Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap=googleMap;
                // Assign variable


                // Initialize location client
                client = LocationServices
                        .getFusedLocationProviderClient(
                                getActivity());
                boolean permission=false;
                while(!permission) {
                    if (ContextCompat.checkSelfPermission(
                            getActivity(),
                            Manifest.permission
                                    .ACCESS_FINE_LOCATION)
                            == PackageManager
                            .PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(
                            getActivity(),
                            Manifest.permission
                                    .ACCESS_COARSE_LOCATION)
                            == PackageManager
                            .PERMISSION_GRANTED) {
                        // When permission is granted
                        // Call method
                        System.out.println("have permission");
//                    System.out.println("latitude:");
//                    System.out.println(currentlocation.getLatitude());
                        permission=true;
                        getCurrentLocation();
                    } else {
                        // When permission is not granted
                        // Call method
                        System.out.println("no permission");
                        requestPermissions(
                                new String[]{
                                        Manifest.permission
                                                .ACCESS_FINE_LOCATION,
                                        Manifest.permission
                                                .ACCESS_COARSE_LOCATION},
                                100);
//                    getCurrentLocation();
                    }
                }

//                LatLng currentlatlng=new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude());
//                googleMap.addMarker(new MarkerOptions().position(currentlatlng).title("Your position"));
//                CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(currentlatlng, 6);;
//                googleMap.moveCamera(cu);
                // When map is loaded
//                LatLng sydney = new LatLng(-33.852, 151.211);
//                googleMap.addMarker(new MarkerOptions()
//                        .position(sydney)
//                        .title("Marker in Sydney"));

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        BottomsheetFragment bottomSheet = new BottomsheetFragment();
                        bottomSheet.show(getActivity().getSupportFragmentManager(),bottomSheet.getTag());
                        return true;
                    }
                });
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        // When clicked on map
                        // Initialize marker options
                        MarkerOptions markerOptions=new MarkerOptions();
                        // Set position of marker
                        markerOptions.position(latLng);
                        // Set title of marker
                        markerOptions.title(latLng.latitude+" : "+latLng.longitude);
                        // Remove all marker
                        googleMap.clear();
                        // Animating to zoom the marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                        // Add marker on map
                        googleMap.addMarker(markerOptions);
                    }
                });
            }

            @SuppressLint("MissingPermission")
            private void getCurrentLocation()
            {
                // Initialize Location manager
                LocationManager locationManager
                        = (LocationManager)getActivity()
                        .getSystemService(
                                Context.LOCATION_SERVICE);
                // Check condition
                if (locationManager.isProviderEnabled(
                        LocationManager.GPS_PROVIDER)
                        || locationManager.isProviderEnabled(
                        LocationManager.NETWORK_PROVIDER)) {
                    // When location service is enabled
                    // Get last location
                    client.getLastLocation().addOnCompleteListener(
                            new OnCompleteListener<Location>() {
                                @Override
                                public void onComplete(
                                        @NonNull Task<Location> task)
                                {

                                    // Initialize location
                                    Location location
                                            = task.getResult();
                                    System.out.println("got location");
                                    // Check condition
                                    if (location != null) {
                                        // When location result is not
                                        // null set latitude
                                        System.out.println("set location");
                                        System.out.println(location.getLatitude());
                                        latitude=String.valueOf(location.getLatitude());
                                        // set longitude
                                        longitude=
                                                String.valueOf(location.getLongitude());
                                        currentlocation=location;
                                        System.out.println(currentlocation.getLatitude());
                                        LatLng currentlatlng=new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude());
                                        mMap.addMarker(new MarkerOptions().position(currentlatlng).title("Your position"));
                                        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(currentlatlng, 12);;
                                        mMap.moveCamera(cu);
                                        mMap.setMyLocationEnabled(true);
//                                        mMap.setOnMyLocationButtonClickListener(this);
//                                        mMap.setOnMyLocationClickListener(this);
                                    }
                                    else {
                                        // When location result is null
                                        // initialize location request
                                        LocationRequest locationRequest
                                                = new LocationRequest()
                                                .setPriority(
                                                        LocationRequest
                                                                .PRIORITY_HIGH_ACCURACY)
                                                .setInterval(10000)
                                                .setFastestInterval(
                                                        1000)
                                                .setNumUpdates(1);

                                        // Initialize location call back
                                        LocationCallback
                                                locationCallback
                                                = new LocationCallback() {
                                            @Override
                                            public void
                                            onLocationResult(
                                                    LocationResult
                                                            locationResult)
                                            {
                                                // Initialize
                                                // location
                                                Location location1
                                                        = locationResult
                                                        .getLastLocation();
                                                // Set latitude
                                                latitude=String.valueOf(
                                                                location1
                                                                        .getLatitude());
                                                // Set longitude
                                                longitude=
                                                        String.valueOf(
                                                                location1
                                                                        .getLongitude());
                                                currentlocation=location1;
                                            }
                                        };

                                        // Request location updates
                                        client.requestLocationUpdates(
                                                locationRequest,
                                                locationCallback,
                                                Looper.myLooper());
                                    }
                                }
                            });
                }
                else {
                    // When location service is not enabled
                    // open location setting
                    startActivity(
                            new Intent(
                                    Settings
                                            .ACTION_LOCATION_SOURCE_SETTINGS)
                                    .setFlags(
                                            Intent.FLAG_ACTIVITY_NEW_TASK));
                }

            }
        });
        // Return view
        return view;
    }
}

//import androidx.activity.OnBackPressedCallback;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//public class MapsFragment extends Fragment {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public MapsFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment MapsFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static MapsFragment newInstance(String param1, String param2) {
//        MapsFragment fragment = new MapsFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
//            @Override
//            public void handleOnBackPressed() {
//                return;
//            }
//        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_maps, container, false);
//    }
//}
