package com.example.mapa2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MainActivity extends AppCompatActivity {

    private MapView map = null;
    private MyLocationNewOverlay myLocationOverlay;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        Configuration.getInstance().setUserAgentValue("M/ a@gmail.com");

        setContentView(R.layout.activity_main);

        map = findViewById(R.id.map);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        GeoPoint startPoint = new GeoPoint(-33.498895, -70.616617);
        GeoPoint punto2 = new GeoPoint(-33.498720, -70.616130);
        GeoPoint punto3 = new GeoPoint(-33.498561, -70.615666);

        map.getController().setZoom(18.0);
        map.getController().setCenter(startPoint);

        Marker markerRepartidor = new Marker(map);
        markerRepartidor.setPosition(startPoint);
        markerRepartidor.setTitle("Repartidor cerca");
        markerRepartidor.setSnippet("Tengo el código de la discordia");
        Drawable iconoRepartidor = ContextCompat.getDrawable(this, R.drawable.repartidor);
        if (iconoRepartidor != null) markerRepartidor.setIcon(iconoRepartidor);
        map.getOverlays().add(markerRepartidor);

        Marker markerPoli = new Marker(map);
        markerPoli.setPosition(punto2);
        markerPoli.setTitle("Punto Policial");
        markerPoli.setSnippet("Zona de vigilancia activa");
        Drawable iconoPoli = ContextCompat.getDrawable(this, R.drawable.poli);
        if (iconoPoli != null) markerPoli.setIcon(iconoPoli);
        map.getOverlays().add(markerPoli);

        Marker markerNormal = new Marker(map);
        markerNormal.setPosition(punto3);
        markerNormal.setTitle("Punto 3");
        markerNormal.setSnippet("Información adicional aquí");
        map.getOverlays().add(markerNormal);

        map.invalidate();

        comprobarPermisosUbicacion();
    }

    private void comprobarPermisosUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            activarGeolocalizacion();
        }
    }

    private void activarGeolocalizacion() {
        if (map != null) {
            myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), map);
            myLocationOverlay.enableMyLocation();
            myLocationOverlay.enableFollowLocation();
            map.getOverlays().add(myLocationOverlay);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map != null) map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (map != null) map.onPause();
    }
}


