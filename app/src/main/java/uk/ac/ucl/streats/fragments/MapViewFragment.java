package uk.ac.ucl.streats.fragments;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.ac.ucl.streats.R;

public class MapViewFragment extends Fragment {

    GoogleMap map;
    SearchView searchView;
    private ArrayList<Marker> markers;
    private static final String TAG = MapViewFragment.class.getSimpleName();

    private final LatLng defaultLocation = new LatLng(51.5074,-0.1278);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";


    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            setUpSearchBar();

            map = googleMap;

            FirebaseAuth fAuth = FirebaseAuth.getInstance();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            String userID = fAuth.getUid();
            Task<QuerySnapshot> restaurantCollection = db.collection("restaurants").get();

            markers = new ArrayList<>();
            restaurantCollection.addOnCompleteListener(task -> {
                        if(task.isSuccessful())
                        {
                            List<DocumentSnapshot> restaurants = task.getResult().getDocuments();
                            for (DocumentSnapshot restaurant : restaurants)
                            {
                                addPin(restaurant);
                            }
                        } else {
                            Toast.makeText(getContext(), "Could not load restaurants.", Toast.LENGTH_SHORT).show();
                        }
                    });

            map.setOnInfoWindowClickListener(marker -> {
                Bundle bundle = new Bundle();
                bundle.putString("restaurantId", (String) marker.getTag());

                Navigation.findNavController(getView()).navigate(R.id.action_mapView_to_restaurantPageFragment, bundle);
            });

            updateLocationUI();
            getDeviceLocation();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }

        // Construct a PlacesClient
        Places.initialize(getActivity().getApplicationContext(), "@string/google_maps_key");

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        return inflater.inflate(R.layout.fragment_mapview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    public void setUpSearchBar() {
        searchView = getActivity().findViewById(R.id.sv_location);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();

                if (location != null || !location.equals("")){

                    int minimalDistance = Integer.MAX_VALUE;
                    int distance;
                    Marker marker = null;
                    for (Marker m : markers) {
                        distance = minimalDistanceBetweenStrings(location, m.getTitle());
                        if (distance < minimalDistance) {
                            marker = m;
                            minimalDistance = distance;
                        }
                    }
                    if (marker != null) {
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
                        marker.showInfoWindow();
                    }
                    else {
                        Toast.makeText(getContext(), "Could not find marker.", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void addPin(DocumentSnapshot restaurant) {

        Geocoder geocoder = new Geocoder(getActivity());

        Address address;
        try {
            address = geocoder.getFromLocationName((String) restaurant.get("location"), 1).get(0);

            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            Marker marker = map.addMarker(new MarkerOptions().position(latLng)
                    .title(restaurant.getString("name"))
                    .snippet(restaurant.getString("cuisine"))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
            marker.setTag(restaurant.getId());
            markers.add(marker);

        } catch (IOException e) {
            Toast.makeText(getActivity(), "Could not load restaurant." , Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.getResult();
                        if (lastKnownLocation != null) {
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lastKnownLocation.getLatitude(),
                                            lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", task.getException());
                        map.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                        map.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    // Levenshtein Algorithm
    private int minimalDistanceBetweenStrings(String s1, String s2) {
        int[][] matrix = new int[s1.length()+1][s2.length()+1];

        matrix[0][0] = 0;

        for (int i = 1; i < s1.length()+1; i++) {
            matrix[i][0] = i;
        }

        for (int i = 1; i < s2.length()+1; i++) {
            matrix[0][i] = i;
        }

        int min;
        for (int i = 1; i < s1.length()+1; i++) {
            for (int j = 1; j < s2.length()+1; j++) {
                min = Math.min(Math.min(matrix[i][j-1], matrix[i-1][j]), matrix[i-1][j-1]);
                if (Character.toLowerCase(s1.charAt(i-1)) == Character.toLowerCase(s2.charAt(j-1))) {
                    matrix[i][j] = min;
                }
                else {
                    matrix[i][j] = min + 1;
                }
            }
        }
        if (s1.length() < s2.length()) {
            return matrix[s1.length()][s1.length()];
        }
        else {
            return matrix[s1.length()][s2.length()];
        }
    }
}