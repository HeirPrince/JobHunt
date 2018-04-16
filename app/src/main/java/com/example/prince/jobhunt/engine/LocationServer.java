package com.example.prince.jobhunt.engine;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;

import com.example.prince.jobhunt.model.Loc;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * Created by Prince on 3/19/2018.
 */

public class LocationServer {

	private Context ctx;
	private FirebaseAgent agent;
	private AuthManager authManager;
	private FusedLocationProviderClient fusedLocationProviderClient;

	public LocationServer(Context ctx) {
		this.ctx = ctx;
		this.agent = new FirebaseAgent(ctx);
		this.authManager = new AuthManager();
		this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ctx);
	}

	public void updateUserLocation(){
		if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}

		fusedLocationProviderClient.getLastLocation()
				.addOnSuccessListener(new OnSuccessListener<Location>() {
					@Override
					public void onSuccess(Location location) {
						if (location != null){
							Loc loc = new Loc();
							loc.setLatitude(location.getLatitude());
							loc.setLongitude(location.getLongitude());

							agent.updateUserLocation(loc);
						}
					}
				});
	}

	public void getJobLocation(LatLng latLng){

	}


}
