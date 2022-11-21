package edu.usc.eventme;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

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

    private double longitude=0.0, latitude=0.0;
    private Location currentlocation;
    HashMap<String, String> markermap = new HashMap<String, String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize view
        View view=inflater.inflate(R.layout.fragment_maps, container, false);

        // Initialize map fragment
        SupportMapFragment supportMapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);



        //String adr2= results.getEventList().get(1).getLocation();


                        // Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap=googleMap;
                // Assign variable


                // Initialize location client
                client = LocationServices
                        .getFusedLocationProviderClient(
                                getActivity());
                boolean permission=false;
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
//                        System.out.println("no permission");
//                        requestPermissions(
//                                new String[]{
//                                        Manifest.permission
//                                                .ACCESS_FINE_LOCATION,
//                                        Manifest.permission
//                                                .ACCESS_COARSE_LOCATION},
//                                100);
//                        ActivityResultLauncher<String[]> locationPermissionRequest =
//                                registerForActivityResult(new ActivityResultContracts
//                                                .RequestMultiplePermissions(), result -> {
//                                            Boolean fineLocationGranted = result.getOrDefault(
//                                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
//                                            Boolean coarseLocationGranted = result.getOrDefault(
//                                                    Manifest.permission.ACCESS_COARSE_LOCATION,false);
//                                            if (fineLocationGranted != null && fineLocationGranted) {
//                                                // Precise location access granted.
//                                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
//                                                // Only approximate location access granted.
//                                            } else {
//                                                // No location access granted.
//                                            }
//                                        }
//                                );
//                    getCurrentLocation();
                    }
                String address1="3551 Trousdale Pkwy, Los Angeles, CA 90089";
                if(permission) {
                    //LatLng latLng1 = getLocationFromAddress(getContext(), address1, false, null);
//                    LatLng latLng1 =new LatLng(34.0200135, -118.2898305);
//                    CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(latLng1, 15);
//                    mMap.moveCamera(cu);
//                    mMap.setMyLocationEnabled(true);
                    //add marker for all locations
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    EventList results = new EventList();
                    db.collection("events").get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        System.out.println("succeed!!!!!\n");
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Event event = document.toObject(Event.class);
                                            results.addEvent(event);
                                            //System.out.println(event.getLocation());
                                            //LatLng temploc = getLocationFromAddress(getContext(), event.getLocation(),true, event);
                                            LatLng temploc = new LatLng(event.getLatitude(), event.getLongitude());
                                            Marker marker = mMap.addMarker(new MarkerOptions().position(temploc));
<<<<<<< Updated upstream
                                            markermap.put(marker.getId(), event.getID());
=======
                                            marker.setTitle(event.getEventTitle());
                                            markermap.put(marker.getId(), event.getId());
>>>>>>> Stashed changes
                                        }
                                        //results.sort("price");
                                    }
                                    else {
                                        System.out.println("No Event"+ task.getException().getMessage());
                                    }

                                }
                            });
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
                        Bundle args = new Bundle();
                        args.putString("currentid", markermap.get(marker.getId()));
                        args.putDouble("lat", latitude);
                        args.putDouble("lon", longitude);
                        //System.out.println(markermap);
                        //System.out.println("Id:"+marker.getId()+", eventid:"+markermap.get(marker.getId()));
                        bottomSheet.setArguments(args);
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
                        //googleMap.clear();
                        // Animating to zoom the marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                        // Add marker on map
                        //googleMap.addMarker(markerOptions);
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
                                        latitude=location.getLatitude();
                                        // set longitude
                                        longitude= location.getLongitude();
                                        currentlocation=location;
                                        System.out.println(currentlocation.getLatitude());
                                        LatLng currentlatlng=new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude());

//                                        mMap.addMarker(new MarkerOptions().position(currentlatlng).title("Your position"));
                                        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(currentlatlng, 15);;
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
                                                latitude=location1.getLatitude();
                                                // Set longitude
                                                longitude= location1.getLongitude();
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

            public LatLng getLocationFromAddress(Context context, String strAddress, boolean hasmarker, Event event) {
                Geocoder coder = new Geocoder(context);
                List<Address> address;
                LatLng p1 = null;
                int trytime = 0;
                while (trytime < 2) {
                    try {
                        // May throw an IOException
                        address = coder.getFromLocationName(strAddress, 5);
                        if (address == null) {
                            return null;
                        }

                        Address location = address.get(0);
                        p1 = new LatLng(location.getLatitude(), location.getLongitude());
                        trytime=3;
                        if(hasmarker) {
                            Marker marker = mMap.addMarker(new MarkerOptions().position(p1).title("1"));
                            System.out.println("event:"+event.getEventTitle());
                            System.out.println(location.getLatitude());
                            System.out.println(location.getLongitude());
                            markermap.put(marker.getId(), event.getID());
                            marker.showInfoWindow();
                        }
                    } catch (IOException ex) {

                        trytime++;
                    }
                    catch (Exception ex) {
                        trytime++;
                    }


                }
                return p1;
            }
        });
        // Return view
        return view;
    }
}


