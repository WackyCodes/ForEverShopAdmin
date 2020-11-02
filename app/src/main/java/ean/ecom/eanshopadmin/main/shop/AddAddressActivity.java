package ean.ecom.eanshopadmin.main.shop;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.other.CheckInternetConnection;
import ean.ecom.eanshopadmin.other.DialogsClass;

import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_DATA_MODEL;

public class AddAddressActivity extends AppCompatActivity implements OnMapReadyCallback, UpdateShop.OnImageRequest.OnImageRequestComplete {

    public static Button addSaveBtn;
    private EditText addHNo;
    private EditText addColonyVillage;
    private EditText addPinCode;
    private EditText addLandmark;
    private EditText addCity;

    private CheckBox checkBoxAutoPointer;
    private ImageView backImageBtn;

    private int index;
    private Dialog dialog;

    /// Map ---
    private FrameLayout mapFrameLayout;

    public static AppCompatActivity addAddressActivity;

    private UpdateShop.OnImageRequest onImageRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_address );
        addAddressActivity = this;
        onImageRequest = new UpdateShopQuery();

        dialog = DialogsClass.getDialog( this );

        // Assign Variables..
        addSaveBtn = findViewById( R.id.add_save_btn );
        addHNo = findViewById( R.id.add_h_no );
        addColonyVillage = findViewById( R.id.add_colony_village );
        addPinCode = findViewById( R.id.add_pin_code );
        addLandmark = findViewById( R.id.add_landmark );
        addCity = findViewById( R.id.add_city_edit_text );

        checkBoxAutoPointer = findViewById( R.id.checkBox_auto_pick_location );
        backImageBtn = findViewById( R.id.back_image_view );

        // Map...
        mapFrameLayout = findViewById( R.id.map_frame_layout );

        // ---------------------------------------
        addSaveBtn.setOnClickListener( v -> {
            setAddSaveBtnClick(  );
        } );
        // On Back Pressed..!
        backImageBtn.setOnClickListener( v -> onBackPressed() );

        checkBoxAutoPointer.setChecked( true );

        // Call Map...
        mapInstance( savedInstanceState );

        // Text Watcher...
        onTextInputChecker();
    }

    private String shopAddress;
    private void setAddSaveBtnClick( ) {
        String HNo = addHNo.getText().toString().trim();
        String colony = addColonyVillage.getText().toString().trim();
        String areaCode = addPinCode.getText().toString().trim();
        String city = addCity.getText().toString().trim();
        String landmark = addLandmark.getText().toString().trim();

        // Step 1... : Check All the info filled or not...
        if (!isValidData(  HNo, colony, areaCode, city )) {
            showToast( "Fill missing field!" );
            return;
        }
        // Check Whether it is null or not...!
        if (tempAddress == null) {
            showToast( "Turn on GPS " );
            return;
        }
        // Check Whether pinCode matched or not..
        if (!areaCode.trim().equals( tempAddress.getPostalCode() )) {
//            showToast( "Please set your location pin. Current pin doesn't belong to your address");
            addPinCode.setError( "Pin not matched with marker PinCode!" );
            return;
        }
        // Check Net Connection...
        if (!isInternetConnect()) {
            showToast( "Please check your internet Connection!" );
//            showToast( "All Is Good" );
            return;
        }

        // Step 2.. : Process...
        if (dialog!=null){
            dialog.show();
        }
        addSaveBtn.setEnabled( false );
        shopAddress = HNo + ", " + colony + ", " + city + " (" + landmark + ") " + areaCode;

        Map <String, Object> updateMap = new HashMap <>();
        updateMap.put( "shop_address", shopAddress );
//        updateMap.put( "shop_area_code", areaCode );
        updateMap.put( "shop_geo_point",   new GeoPoint( tempAddress.getLatitude(), tempAddress.getLongitude() ) );

        onImageRequest.onUpdateShopOnDatabase( this,  updateMap );

    }

    private void onTextInputChecker() {
        //  ,admin=Madhya Pradesh,sub-admin=Bhopal,locality=Bhopal
        // On Area Change ...
        addColonyVillage.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (checkBoxAutoPointer.isChecked()) {
                    // Set Marker....
                    if (!TextUtils.isEmpty( s ) && !TextUtils.isEmpty( addCity.getText().toString() )) {
                        getAddressFromText( s.toString() + " " + addCity.getText().toString() );
                    }
                }
            }
        } );

        // On City Change ...
        addCity.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (checkBoxAutoPointer.isChecked()) {
                    // Set Marker....
                    if (!TextUtils.isEmpty( s ) && !TextUtils.isEmpty( addColonyVillage.getText().toString() )) {
                        getAddressFromText( addColonyVillage.getText().toString() + " " + s.toString() );
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );

        // checkBoxAutoPointer listener...
        checkBoxAutoPointer.setOnCheckedChangeListener( (buttonView, isChecked) -> {
            if (isChecked) {
                // Set Marker....
                if (!TextUtils.isEmpty( addCity.getText().toString() ) && !TextUtils.isEmpty( addColonyVillage.getText().toString() )) {
                    getAddressFromText( addColonyVillage.getText().toString() + " " + addCity.getText().toString() );
                }
            }
        } );

    }

    // Check Validity ...
    private boolean isValidData( String HNo, String colony, String areaCode, String city) {

        if (TextUtils.isEmpty( HNo ) && TextUtils.isEmpty( colony ) && TextUtils.isEmpty( areaCode )) {
            addHNo.setError( "Required!" );
            addColonyVillage.setError( "Required!" );
            addPinCode.setError( "Required!" );
            return false;
        }  else if (TextUtils.isEmpty( HNo )) {
            addHNo.setError( "Enter Shop No / Flat No. or Building Name " );
            return false;
        } else if (TextUtils.isEmpty( colony )) {
            addColonyVillage.setError( "Required!" );
            return false;
        } else if (TextUtils.isEmpty( areaCode )) {
            addPinCode.setError( "Enter Your Area Code" );
            return false;
        } else if (!TextUtils.isEmpty( areaCode )) {
            if (areaCode.length() < 6) {
                addPinCode.setError( "Please enter Correct Area Code..!" );
                return false;
            }
        } else if (TextUtils.isEmpty( city )) {
            addCity.setError( "Enter Your City" );
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private boolean isInternetConnect() {
        return CheckInternetConnection.isInternetConnected( this );
    }

    @Override
    public void onUploadImageFinished(String imageLink, boolean isSuccess) {
        // Not usable...

    }

    @Override
    public void onUpdateFinish(boolean isSuccess) {
        dismissDialog();
        if (isSuccess){
            showToast( "Update address Successfully..!" );
            if (shopAddress != null)
                SHOP_DATA_MODEL.setShop_address( shopAddress );
            finish();
        }else{
            showToast( "Update address Failed..!" );
            addSaveBtn.setEnabled( true );
        }
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText( AddAddressActivity.this, msg, Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void dismissDialog() {
        dialog.dismiss();
    }

    //-----------  Map -----------------------------------------------------------------------------

    private SupportMapFragment mapFragment;
    private GoogleMap mGoogleMap;
    private MarkerOptions mLocationMarker;
    private Marker currentMarker;
    private FusedLocationProviderClient mFusedLocationClient;
    private Address tempAddress;

    private void mapInstance(Bundle savedInstanceState) {
        mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add( mapFrameLayout.getId(), mapFragment ).commit();

        // Async... Call onMap Ready...
        mapFragment.getMapAsync( this );

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient( AddAddressActivity.this );
        if (mGoogleMap != null) {
            mGoogleMap.setOnMyLocationButtonClickListener( () -> {
                setMyLocation();
                return false;
            } );
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Assign google map...
        mGoogleMap = googleMap;
        onMoveMarkerListener();
        setMyLocation();
    }

    // Get Last Location...
    private void setMyLocation() {
        if (mGoogleMap == null) {
            return;
        }

        if (mFusedLocationClient == null) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient( AddAddressActivity.this );
        }

        // Set My Location....
        if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            checkForLocationPermission();
            return;
        }
        mGoogleMap.setMyLocationEnabled( true );
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

        try {
            Task <Location> locationResult = mFusedLocationClient.getLastLocation();
            locationResult.addOnCompleteListener( task -> {
                if (task.isSuccessful()) {
                    try{
                        // Set the map's camera position to the current location of the device.
                        Location location = task.getResult();
                        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
//                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentLatLng, 18);
//                    myGoogleMap.moveCamera(update);
//                    mGoogleMap.animateCamera( update );

                        mLocationMarker = new MarkerOptions().position( currentLatLng ).draggable( true )
                                .title( "My Location" );
//                                .icon( BitmapDescriptorFactory.fromResource( R.drawable.ic_my_location_pin_circle_24 ) );
                        setmLocationMarker( mLocationMarker );
                    }catch(Exception e){
                        e.printStackTrace();
//                        Log.e( "GET_M_LOCATION", e.getMessage() );
                    }
                }
            } );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setmLocationMarker( MarkerOptions marker){
        removeOldMarker();
        currentMarker = mGoogleMap.addMarker( marker );
        // Set the map's camera position to the current location of the device.
        setMoveCamera(marker.getPosition().latitude, marker.getPosition().longitude);
    }

    private void setMoveCamera( double latitude, double longitude ){
        LatLng currentLatLng = new LatLng( latitude, longitude);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentLatLng, 18);
//                    myGoogleMap.moveCamera(update);
        mGoogleMap.animateCamera( update );
    }

    // Camera Drag listener//
    private void onMoveMarkerListener( ){
        if(mGoogleMap != null)
            mGoogleMap.setOnMarkerDragListener( new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) { }
                @Override
                public void onMarkerDrag(Marker marker) { }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    try{
                        setMoveCamera( marker.getPosition().latitude, marker.getPosition().longitude );
                        getAddressFromLatLng( marker.getPosition().latitude, marker.getPosition().longitude );
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            } );
    }

    // Search and assign Address..--------------
    private void getAddressFromText(String searchText){
        Geocoder geocoder = new Geocoder( AddAddressActivity.this );
        try {
            List <Address> addressList  = geocoder.getFromLocationName( searchText, 1 );
            tempAddress = addressList.get( 0 );
//            Log.e("GEOCODER", "List Data :" + title + " lat Long "+ lat + lng);
//            setMarker( tempAddress.getLatitude(), tempAddress.getLongitude(),  tempAddress.getAddressLine( 0 ));
            LatLng latLng = new LatLng( tempAddress.getLatitude(), tempAddress.getLongitude() );
            if (!latLng.equals( null )){
                mLocationMarker = new MarkerOptions().position( latLng ).draggable( true )
                        .title( tempAddress.getSubAdminArea() + ", " + tempAddress.getAdminArea() + ", " + tempAddress.getPostalCode() );
//                .icon( BitmapDescriptorFactory.fromResource( R.drawable.ic_my_location_pin_circle_24 ) );
                if (mLocationMarker!=null){
                    setmLocationMarker( mLocationMarker );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getAddressFromLatLng( double lat, double lng ){
        Geocoder geocoder = new Geocoder( AddAddressActivity.this );
        try {
            List <Address> addressList  = geocoder.getFromLocation( lat, lng, 1 );
            tempAddress = addressList.get( 0 ); // Assign address...
            currentMarker.setTitle( tempAddress.getSubAdminArea() + ", " + tempAddress.getAdminArea() + ", " + tempAddress.getPostalCode() );
//            setMarker( tempAddress.getLatitude(), tempAddress.getLongitude(),  tempAddress.getAddressLine( 0 ));
//            Log.e( "All_DATA", tempAddress.toString() );
            // Fill Info if Auto Set Off...
            if (!checkBoxAutoPointer.isChecked()){
                //  ,admin=Madhya Pradesh,sub-admin=Bhopal,locality=Bhopal
                addCity.setText( tempAddress.getSubAdminArea() );
            }
            addPinCode.setText( tempAddress.getPostalCode() );
            currentMarker.notify();
        } catch (Exception e) {
//            Log.e( "UPDATE_MARKER", e.getMessage() );
            e.printStackTrace();
        }
    }
    //////////////////////////////////////
    private void removeOldMarker(){
        if (currentMarker != null){
            currentMarker.remove();
            currentMarker = null;
        }
    }

    // Permission............
    private void checkForLocationPermission(){
        if (ActivityCompat.checkSelfPermission( AddAddressActivity.this, Manifest.permission.ACCESS_FINE_LOCATION )
                != PackageManager.PERMISSION_GRANTED ) {
            askGPSPermission();
        }
    }

    private final int MAP_PERMISSION = 1;
    private void askGPSPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale( this,
                android.Manifest.permission.ACCESS_FINE_LOCATION )){
            new AlertDialog.Builder( this )
                    .setTitle( "Location Permission" )
                    .setMessage( "Location permission is needed!" )
                    .setPositiveButton( "OK",
                            (dialogInterface, i) ->{
                                ActivityCompat.requestPermissions( AddAddressActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MAP_PERMISSION ) ;
                            })
                    .setNegativeButton( "Cancel",
                            (dialogInterface, i) -> {
                                dialogInterface.dismiss();
                                askGPSPermission();
                            } ).create().show();
        }
        else{
            ActivityCompat.requestPermissions( AddAddressActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION
                    }, MAP_PERMISSION );
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode== MAP_PERMISSION){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                showToast( "Turn on GPS to locate your location!" );
                setMyLocation();
            }
            else{
                showToast( "Permission DENIED!" );
                askGPSPermission();
            }
        }
    }





}
