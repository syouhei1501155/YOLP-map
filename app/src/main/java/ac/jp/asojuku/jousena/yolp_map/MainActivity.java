package ac.jp.asojuku.jousena.yolp_map;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.jar.Manifest;

import jp.co.yahoo.android.maps.GeoPoint;
import jp.co.yahoo.android.maps.LabelTouchManager;
import jp.co.yahoo.android.maps.LabelTouchOverlay;
import jp.co.yahoo.android.maps.MapController;
import jp.co.yahoo.android.maps.MapView;

public class MainActivity extends AppCompatActivity implements LocationListener{
    LocationManager locationManager=null;
    MapView mapView=null;
    int lastLatitude=0;
    int lastLongitude=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView=new MapView(this,"dj0zaiZpPVp1VXdoVEpuS0YxWSZzPWNvbnN1bWVyc2VjcmV0Jng9NjE-");
        mapView.setBuiltInZoomControls(true);
        mapView.setScalebar(true);

        double lat=35.658517;
        double lon=139.701334;
        GeoPoint  gp=new GeoPoint((int)(lat*1000000),(int)(lon*1000000));
        MapController c=mapView.getMapController();

        c.setCenter(gp);
        c.setZoom(3);
        setContentView(mapView);
//注釈情報を取得する
        LabelTouchOverlay labelTouchOverlay = new LabelTouchOverlay(){
            @Override
            public void onLabelTouch(LabelTouchManager.LabelInfo labelInfo){
                //注記をタッチした際の処理
                AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this); //名称を表示
                ad.setMessage(labelInfo.name);
                ad.show();
            }
        };
        mapView.getOverlays().add(labelTouchOverlay);

        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria=new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        String provider=locationManager.getBestProvider(criteria,true);
        if(locationManager!=null){
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
                    ||ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    ==PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(provider,0,0,this);
            }
        }
    }

    @Override
    public  void  onLocationChanged(Location location){
        double lat=location.getLatitude();
        int latitude=(int)(lat*1000000);

        double lon=location.getLongitude();
        int longitude=(int)(lon*1000000);

        if(latitude/1000!=this.lastLatitude/1000||
                longitude/1000!=this.lastLongitude/1000){
            GeoPoint gp=new GeoPoint(latitude,longitude);
            MapController c=mapView.getMapController();
            c.setCenter(gp);
            this.lastLatitude=latitude;
            this.lastLongitude=longitude;
        }
    }

    @Override
    public  void  onProviderEnabled(String provider){

    }

    @Override
    public  void  onProviderDisabled(String provider){

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}
