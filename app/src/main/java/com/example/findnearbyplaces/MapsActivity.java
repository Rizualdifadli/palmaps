package com.example.findnearbyplaces;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private  GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private  static final int Request_User_Location_Code = 99;
    private  double latitude, longitude;
    private int ProximityRadius = 50000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkUserLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void  onClick (View v)
    {
        String hospital = "hospital", school="school", restaurant ="restaurant", mosque ="mosque", atm = "atm",train_station="train_station"
                ,airport = "airport",bank="bank",bus="bus",buying="buying",car_service="car_service",car_wash="car_wash",church="church"
                ,convenience_store="convenience_store",doctor="doctor",gas="gas",hindu="hindu",library="library",museum="museum",park="park"
                ,pharmacy="pharmacy",police="police",post="post",stadium="stadium",shopping_mall="shopping_mall",theme_park="theme_park",university="university"
                ,zoo="zoo";
        Object transferData[] = new  Object[2];
        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();

        switch (v.getId())
        {
            case R.id.search_address:
                EditText addressField = (EditText) findViewById(R.id.location_search);
                String address =  addressField.getText().toString();

                List<Address> addressList = null;
                MarkerOptions userMarkerOptions = new MarkerOptions();

                if (!TextUtils.isEmpty(address))
                {
                    Geocoder geocoder = new Geocoder(this);
                    try {
                        addressList = geocoder.getFromLocationName(address, 6);

                        if (addressList != null)
                        {
                            for (int i=0 ; i<addressList.size(); i++)
                            {
                                Address userAddress = addressList.get(i);
                                LatLng latLng = new LatLng(userAddress.getLatitude(),userAddress.getLongitude());

                                userMarkerOptions.position(latLng);
                                userMarkerOptions.title(address);
                                userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                mMap.addMarker(userMarkerOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
                            }
                        }

                        else
                        {
                            Toast.makeText(this, "Your Search Location Not Found", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                    {
                        Toast.makeText(this, "Please Write Any Location Name", Toast.LENGTH_SHORT).show();
                    }
                break;


            case R.id.hospitals_nearby:
                mMap.clear();
                String url = getUrl(latitude,longitude,hospital);
                transferData[0] = mMap;
                transferData[1]= url;

                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Sedang Mencari Rumah Sakit Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Menampilkan Rumah Sakit Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                break;

            case R.id.schools_nearby:
                mMap.clear();
                url = getUrl(latitude,longitude,school);
                transferData[0] = mMap;
                transferData[1]= url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Sedang Mencari Sekolah Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Menampilkan Sekolah Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                break;

            case R.id.restaurants_nearby:
                mMap.clear();
                url = getUrl(latitude,longitude,restaurant);
                transferData[0] = mMap;
                transferData[1]= url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Sedang Mencari Restaurant Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Menampilkan Restaurant Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                break;

            case R.id.mosque_nearby:
                mMap.clear();
                url = getUrl(latitude,longitude,mosque);
                transferData[0] = mMap;
                transferData[1]= url;

                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Sedang Mencari Masjid Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Menampilkan Masjid Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                break;

            case R.id.atm_nearby:
                mMap.clear();
                url = getUrl(latitude,longitude,atm);
                transferData[0] = mMap;
                transferData[1]= url;

                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Sedang Mencari ATM Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Menampilkan ATM Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                break;

            case R.id.train_station:
                mMap.clear();
                url = getUrl(latitude,longitude,train_station);
                transferData[0] = mMap;
                transferData[1]= url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Sedang Mencari Stasiun Kerata Api Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Menampilkan Stasiun Kerata Api Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                break;

            case R.id.airport:
                mMap.clear();
                url = getUrl(latitude,longitude,airport);
                transferData[0] = mMap;
                transferData[1]= url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Sedang Mencari Bandara Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Menampilkan Bandara Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                break;
            //                bermasalah

            case R.id.bank:
                mMap.clear();
                url = getUrl(latitude,longitude,bank);
                transferData[0] = mMap;
                transferData[1]= url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Sedang Mencari Bank Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Menampilkan Bank Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                break;

            case R.id.bus_station:
                mMap.clear();
                url = getUrl(latitude,longitude,bus);
                transferData[0] = mMap;
                transferData[1]= url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Sedang Mencari Halte Bus Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Menampilkan Halte Bus Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                break;
//                bermasalah

            case R.id.buying:
                mMap.clear();
                url = getUrl(latitude,longitude,buying);
                transferData[0] = mMap;
                transferData[1]= url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Sedang Mencari SuperMarket Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Menampilkan SuperMarket Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                break;
            //                bermasalah

            case R.id.car_service:
                mMap.clear();
                url = getUrl(latitude,longitude,car_service);
                transferData[0] = mMap;
                transferData[1]= url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Sedang Mencari Car Service Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Menampilkan Car Service Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                break;
            //                bermasalah

            case R.id.car_wash:
                mMap.clear();
                url = getUrl(latitude,longitude,car_wash);
                transferData[0] = mMap;
                transferData[1]= url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Sedang Mencari Steam Mobil Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Menampilkan Steam Mobil Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                break;

            case R.id.church:
                mMap.clear();
                url = getUrl(latitude,longitude,church);
                transferData[0] = mMap;
                transferData[1]= url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Sedang Mencari Gereja Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Menampilkan Gereja Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                break;

            case R.id.convenience_store:
                mMap.clear();
                url = getUrl(latitude,longitude,convenience_store);
                transferData[0] = mMap;
                transferData[1]= url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Sedang Mencari Toko Serba Ada Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Menampilkan Toko Serba Ada Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                break;

            case R.id.doctor:
                mMap.clear();
                url = getUrl(latitude,longitude,doctor);
                transferData[0] = mMap;
                transferData[1]= url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Sedang Mencari Doktor Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Menampilkan Doktor Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                break;

            case R.id.gas:
                mMap.clear();
                url = getUrl(latitude,longitude,gas);
                transferData[0] = mMap;
                transferData[1]= url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Sedang Mencari SPBU Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Menampilkan SPBU Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                break;
            //                bermasalah

            case R.id.hindu:
                mMap.clear();
                url = getUrl(latitude,longitude,hindu);
                transferData[0] = mMap;
                transferData[1]= url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Sedang Mencari Vihara Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Menampilkan Vihara Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                break;
            //                bermasalah

            case R.id.library:
                mMap.clear();
                url = getUrl(latitude,longitude,library);
                transferData[0] = mMap;
                transferData[1]= url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Sedang Mencari Perpustakaan Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Menampilkan Perpustakaan Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                break;
            //                bermasalah

            case R.id.museum:
                mMap.clear();
                url = getUrl(latitude,longitude,museum);
                transferData[0] = mMap;
                transferData[1]= url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Sedang Mencari Museum Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Menampilkan Museum Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                break;

            case R.id.park:
                mMap.clear();
                url = getUrl(latitude,longitude,park);
                transferData[0] = mMap;
                transferData[1]= url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Sedang Mencari Taman Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Menampilkan Taman Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                break;

            case R.id.pharmacy:
                mMap.clear();
                url = getUrl(latitude,longitude,pharmacy);
                transferData[0] = mMap;
                transferData[1]= url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Sedang Mencari Farmasi Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Menampilkan Farmasi Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                break;

            case R.id.police:
                mMap.clear();
                url = getUrl(latitude,longitude,police);
                transferData[0] = mMap;
                transferData[1]= url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Sedang Mencari Kantor Polisi Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Menampilkan Kantor Polisi Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                break;

            case R.id.post:
                mMap.clear();
                url = getUrl(latitude,longitude,post);
                transferData[0] = mMap;
                transferData[1]= url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Sedang Mencari Kantor Pos Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Menampilkan Kantor Pos Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                break;
            //                bermasalah

            case R.id.shopping_mall:
                mMap.clear();
                url = getUrl(latitude,longitude,shopping_mall);
                transferData[0] = mMap;
                transferData[1]= url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Sedang Mencari Pusat Perbelanjaan Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Menampilkan Pusat Perbelanjaan Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                break;
            //                bermasalah Toko bukan Mall

            case R.id.stadium:
                mMap.clear();
                url = getUrl(latitude,longitude,stadium);
                transferData[0] = mMap;
                transferData[1]= url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Sedang Mencari Stadion Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Menampilkan Stadion Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                break;

            case R.id.theme_park:
                mMap.clear();
                url = getUrl(latitude,longitude,theme_park);
                transferData[0] = mMap;
                transferData[1]= url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Sedang Mencari Taman Bermain Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Menampilkan Taman Bermain Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                break;
            //                bermasalah

            case R.id.university:
                mMap.clear();
                url = getUrl(latitude,longitude,university);
                transferData[0] = mMap;
                transferData[1]= url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Sedang Mencari Universitas Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Menampilkan Universitas Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                break;
            //                bermasalah

            case R.id.zoo:
                mMap.clear();
                url = getUrl(latitude,longitude,zoo);
                transferData[0] = mMap;
                transferData[1]= url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Sedang Mencari Kebun Binatang Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Menampilkan Kebun Binatang Di Sekitar Anda", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private String getUrl(double latitude,double longitude, String nearbyPlace)
    {
        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleURL.append("location="+ latitude+"," + longitude);
        googleURL.append("&radius=" + ProximityRadius);
        googleURL.append("&type=" + nearbyPlace);
        googleURL.append("&sensor=true");
        googleURL.append("&key=" + "AIzaSyBBZ3eygmersaY_b0RJ7PnO4EqJMyr_huE");

        Log.d("GoogleMapsActivity", "url = "+ googleURL.toString());

        return googleURL.toString();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            buildGooleApiClient();

            mMap.setMyLocationEnabled(true);
        }


    }

    public boolean checkUserLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            }
            else
                {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
                }
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case Request_User_Location_Code:
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    if (googleApiClient == null)
                    {
                        buildGooleApiClient();
                    }
                    mMap.setMyLocationEnabled(true);
                }
            }
            else
            {
                Toast.makeText(this, "Permission Denied...", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }

    protected synchronized void  buildGooleApiClient()
    {
    googleApiClient = new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build();
    googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(@NonNull Location location)
    {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        lastLocation = location;

        if (currentUserLocationMarker != null)
        {
            currentUserLocationMarker.remove();
        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(13));

        if (googleApiClient != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    locationRequest= new LocationRequest();
    locationRequest.setInterval(1100);
    locationRequest.setFastestInterval(1100);
    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    {
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}