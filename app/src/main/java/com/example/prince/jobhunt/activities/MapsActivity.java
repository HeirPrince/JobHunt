package com.example.prince.jobhunt.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.engine.AuthManager;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.model.Loc;
import com.example.prince.jobhunt.model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

	private GoogleMap mMap;
	private ProgressDialog dialog;
	private FirebaseAgent agent;
	private AuthManager authManager;

	private static final String MYTAG = "mapsActivity";
	private static final int LOCATION_REQUEST_CODE = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		agent = new FirebaseAgent(this);
		authManager = new AuthManager();

		dialog = new ProgressDialog(this);
		dialog.setTitle("Loading Map");
		dialog.setMessage("Please wait...");
		dialog.setCancelable(true);

		//display progress
		dialog.show();

	}

	public void MapReady(GoogleMap gmap) {
		mMap = gmap;

		mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
			@Override
			public void onMapLoaded() {
				dialog.dismiss();
				askPermissions();
			}
		});

		mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		mMap.getUiSettings().setZoomControlsEnabled(true);
		mMap.getUiSettings().setRotateGesturesEnabled(true);
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		mMap.setMyLocationEnabled(true);

	}

	private void askPermissions() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			int coarse = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
			int fine = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

			if (coarse != PackageManager.PERMISSION_GRANTED || fine != PackageManager.PERMISSION_GRANTED) {
				//ask permission
				String[] permissions = new String[]{
						Manifest.permission.ACCESS_COARSE_LOCATION,
						Manifest.permission.ACCESS_FINE_LOCATION
				};

				ActivityCompat.requestPermissions(this, permissions, LOCATION_REQUEST_CODE);
				return;
			}

		}

		this.showMyLocation();
	}

	private void showMyLocation() {
		agent.getCurrentLocation(authManager.getCurrentUID(), new FirebaseAgent.imLocatedAt() {
			@Override
			public void location(Loc loc) {
				if (loc != null){
					Toast.makeText(MapsActivity.this, String.valueOf(loc.getLatitude()), Toast.LENGTH_LONG).show();
					final LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
					mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

					CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(latLng)
							.zoom(17)
							.bearing(90)
							.tilt(40)
							.build();
					mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

					//update location
					Loc loc1 = new Loc();
					loc1.setLongitude(latLng.longitude);
					loc1.setLatitude(latLng.latitude);
					agent.updateUserLocation(loc1);

					agent.getUserByUID(authManager.getCurrentUID(), new FirebaseAgent.getUser() {
						@Override
						public void gottenUser(User user) {
							MarkerOptions markerOptions = new MarkerOptions();

							markerOptions.title(user.getUsername())
									.snippet(user.getCareer())
									.position(latLng);
							Marker marker = mMap.addMarker(markerOptions);
							marker.showInfoWindow();
						}
					});
				}else {
					Toast.makeText(MapsActivity.this, "no location", Toast.LENGTH_SHORT).show();
				}
			}
		});




	}

	private String getEnabledLocationProvider(){
		LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

		Criteria criteria = new Criteria();

		String bestProvider = locationManager.getBestProvider(criteria, true);

		boolean enabled = locationManager.isProviderEnabled(bestProvider);

		if (!enabled){
			Toast.makeText(this, "No location provider enabled", Toast.LENGTH_SHORT).show();
			return null;
		}

		return bestProvider;

	}


	/**
	 * Manipulates the map once available.
	 * This callback is triggered when the map is ready to be used.
	 * This is where we can add markers or lines, add listeners or move the camera. In this case,
	 * we just add a marker near Sydney, Australia.
	 * If Google Play services is not installed on the device, the user will be prompted to install
	 * it inside the SupportMapFragment. This method will only be triggered once the user has
	 * installed Google Play services and returned to the app.
	 */
	@Override
	public void onMapReady(GoogleMap googleMap) {
		MapReady(googleMap);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode){
			case LOCATION_REQUEST_CODE:
				if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
					Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show();

					this.showMyLocation();
				}else {
					Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}

	@Override
	public void onLocationChanged(Location location) {

	}

	@Override
	public void onStatusChanged(String s, int i, Bundle bundle) {

	}

	@Override
	public void onProviderEnabled(String s) {

	}

	@Override
	public void onProviderDisabled(String s) {

	}
}
