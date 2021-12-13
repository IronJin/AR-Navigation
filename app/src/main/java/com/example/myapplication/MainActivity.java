package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Process;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

// classes needed to initialize map
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

// classes needed to add the location component
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;

// classes needed to add a marker
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

// classes to calculate a route
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import android.util.Log;

// classes needed to launch navigation UI
import android.view.View;
import android.widget.Button;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener, PermissionsListener {
    // variables for adding location layer
    private MapView mapView;
    private MapboxMap mapboxMap;

    // variables for adding location layer
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;

    // variables for calculating and drawing a route
    private DirectionsRoute currentRoute;

    private static final String TAG = "DirectionsActivity";
    private NavigationMapRoute navigationMapRoute;

    // variables needed to initialize navigation
    private Button button;
//    Button myLocButton= findViewById(R.id.myLocButton);

    // Variables needed to add the location engine
    private LocationEngine locationEngine;
    private long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L; // 여기 고쳐야 업데이트가 더 빠르려나?
    private long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;

    // Variables needed to listen to location updates
    private MainActivityLocationCallback callback = new MainActivityLocationCallback(this);

    //firebase 관련
    private FirebaseAuth mFirebaseAuth;

    // Navigation
    private Marker destinationMarker;
    private Point originPosition;
    private Point destinatonPosition;
    private Point searchedPosition;
    private MapboxDirections client;
    private Button startButton, mylocButton, dkuButton, arButton, dtbutton;


    //EditText editText;

    double destinationLa; // latitude 목적지 위도
    double destinationLo; // longitude 목적지 경도
    public static double La; //latitude
    public static double Lo; // longitude

    // 학교 중앙 좌표
    // 37.321229, 127.127432
    public static double DKULa = 37.321229;
    public static double DKULo = 127.127432;

    // 자동 완성
    //static TextView txtView;

    //언어설정 변수
    static public String locale;
    Intent intent;
    static public Configuration config;

    //환경설정 필요 변수
    SharedPreferences sharedPreferences;
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
            });

    protected void destination_zoom(double lat, double lon){
        searchedPosition = Point.fromLngLat(lon,lat);
        originPosition = Point.fromLngLat(Lo,La);
        getRoute_walking(originPosition, searchedPosition);
        getRoute_navi_walking(originPosition, searchedPosition);
        startButton.setEnabled(true);
        startButton.setBackgroundResource(R.color.mapboxBlue);
        arButton.setEnabled(true);
        arButton.setBackgroundResource(R.color.mapboxBlue);
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(lat, lon)) // Sets the new camera position
                .zoom(16) // Sets the zoom , 줌 정도 숫자가 클수록 더많이 줌함
                .bearing(0) // Rotate the camera , 카메라 방향(북쪽이 0) 북쪽부터 시계방향으로 측정
                .tilt(0) // Set the camera tilt , 각도
                .build(); // Creates a CameraPosition from the builder
        //카메라 움직이기
        mapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), 7000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token)); // mapbox 사용하기 위한 토큰
        setContentView(R.layout.activity_main);

        // Setup the MapView
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        //editText =(EditText)findViewById(R.id.txtDestination);

        startButton = findViewById(R.id.btnStartNavigation);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                boolean simulateRoute = true; // 시뮬레이션용
                NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                        .directionsRoute(currentRoute) // Detailed information about an individual route such as the duration, distance and geometry
//                                .shouldSimulateRoute(simulateRoute) // 이거 있으면 지 혼자 시뮬레이션 돌아감
                        .build();
                // Call this method with Context from within an Activity
                NavigationLauncher.startNavigation(MainActivity.this, options);
            }
        });

        // TODO : DT 버튼
        dtbutton = findViewById(R.id.destination);
        dtbutton.setEnabled(true);
        dtbutton.setBackgroundResource(R.color.mapboxBlue);
        dtbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
                getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //제 1공학관
                        if (item.getItemId() == R.id.action_menu1){
                            destination_zoom(37.32119,127.12584);
                        }
                        //제 2공학관
                        else if (item.getItemId() == R.id.action_menu2){
                            destination_zoom(37.32080,127.12629);
                        }
                        //제 3공학관
                        else if (item.getItemId() == R.id.action_menu3){
                            destination_zoom(37.32043,127.12676);
                        }
                        //사회과학관
                        else if (item.getItemId() == R.id.action_menu4){
                            destination_zoom(37.32137,127.12564);
                        }
                        //소프트웨어 ICT관
                        else if (item.getItemId() == R.id.action_menu5){
                            destination_zoom(37.32274,127.12753);
                        }
                        //미디어센터
                        else if (item.getItemId() == R.id.action_menu6){
                            destination_zoom(37.32242,127.12753);
                        }
                        //범정관
                        else if (item.getItemId() == R.id.action_menu7){
                            destination_zoom(37.32196,127.12641);
                        }
                        //인문관
                        else if (item.getItemId() == R.id.action_menu8){
                            destination_zoom(37.32178,127.12896);
                        }
                        //상경관
                        else if (item.getItemId() == R.id.action_menu9){
                            destination_zoom(37.32227,127.12894);
                        }
                        //사범관
                        else if (item.getItemId() == R.id.action_menu10){
                            destination_zoom(37.32272,127.12897);
                        }
                        //혜당관
                        else if (item.getItemId() == R.id.action_menu11){
                            destination_zoom(37.32047,127.12827);
                        }
                        //퇴계기념중앙도서관
                        else if (item.getItemId() == R.id.action_menu12){
                            destination_zoom(37.32116,127.12746);
                        }
                        //법학관, 대학원동
                        else if (item.getItemId() == R.id.action_menu13){
                            destination_zoom(37.32107,127.12921);
                        }
                        //국제관
                        else if (item.getItemId() == R.id.action_menu14){
                            destination_zoom(37.31919,127.12717);
                        }
                        //종합실험동
                        else if (item.getItemId() == R.id.action_menu15){
                            destination_zoom(37.32014,127.12577);
                        }
                        //곰상
                        else if (item.getItemId() == R.id.action_menu16){
                            destination_zoom(37.31996,127.12892);
                        }
                        //정문
                        else if (item.getItemId() == R.id.action_menu17){
                            destination_zoom(37.32346,127.12547);
                        }

                        //체육관
                        else if (item.getItemId() == R.id.action_menu18){
                            destination_zoom(37.31936,127.13213);
                        }
                        //대운동장
                        else if (item.getItemId() == R.id.action_menu19){
                            destination_zoom(37.32080,127.13300);
                        }

                        //무용관
                        else if (item.getItemId() == R.id.action_menu20){
                            destination_zoom(37.31585,127.12724);
                        }

                        //진리관
                        else if (item.getItemId() == R.id.action_menu21){
                            destination_zoom(37.31479,127.12682);
                        }

                        //웅비홀
                        else if (item.getItemId() == R.id.action_menu22){
                            destination_zoom(37.31567,127.12698);
                        }

                        //집현재
                        else if (item.getItemId() == R.id.action_menu23){
                            destination_zoom(37.31667,127.12695);
                        }

                        //죽전치과병원
                        else if (item.getItemId() == R.id.action_menu24){
                            destination_zoom(37.32187,127.12513);
                        }

                        //노천마당, 풋살장
                        else if (item.getItemId() == R.id.action_menu25){
                            destination_zoom(37.31988,127.12724);
                        }

                        //음악관
                        else if (item.getItemId() == R.id.action_menu26){
                            destination_zoom(37.31918,127.12935);
                        }

                        //미술관
                        else if (item.getItemId() == R.id.action_menu27){
                            destination_zoom(37.31983,127.13088);
                        }

                        //폭포공원
                        else if (item.getItemId() == R.id.action_menu28){
                            destination_zoom(37.32082,127.13065);
                        }
                        //리턴 값은 true, false 든 크게 상관없는 것 같음, 개발자도 잘 모르겠음
                        return false;
                    }
                });

                //팝업메뉴를 보이게 하는거
                popupMenu.show();
            }
        });

        // TODO : 0928 AR 버튼
        arButton = findViewById(R.id.btnStartAR);
        arButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UnityPlayerActivity.class);
                startActivity(intent);
            }
        });

        // 내 위치로 카메라 이동
        mylocButton = findViewById(R.id.btnMyLoc);
        mylocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(La, Lo)) // Sets the new camera position
                        .zoom(17) // Sets the zoom , 줌 정도 숫자가 클수록 더많이 줌함
                        .bearing(180) // Rotate the camera , 카메라 방향(북쪽이 0) 북쪽부터 시계방향으로 측정
                        .tilt(0) // Set the camera tilt , 각도
                        .build(); // Creates a CameraPosition from the builder
                //카메라 움직이기
                mapboxMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(position), 7000);

                // TODO : 내위치 버튼 클릭하면 위도, 경도 대신 실제 주소 띄워보기
                Toast.makeText(getApplicationContext(), String.format("내 위치로 이동합니다."), Toast.LENGTH_LONG).show();
            }
        });

        // 학교 위치로 카메라 이동
        dkuButton = findViewById(R.id.btnDKU);
        dkuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(DKULa, DKULo)) // Sets the new camera position
                        .zoom(16) // Sets the zoom , 줌 정도 숫자가 클수록 더많이 줌함
                        .bearing(180) // Rotate the camera , 카메라 방향(북쪽이 0) 북쪽부터 시계방향으로 측정
                        .tilt(0) // Set the camera tilt , 각도
                        .build(); // Creates a CameraPosition from the builder
                //카메라 움직이기
                mapboxMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(position), 7000);

                Toast.makeText(getApplicationContext(), String.format("학교 위치로 이동합니다."), Toast.LENGTH_LONG).show();
            }
        });

        // 장소 자동완성

        /*Button search = findViewById(R.id.btnSearch);
        search.bringToFront();
        // TODO : TEST : 이게 되나?
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPointFromGeoCoder(String.valueOf(editText.getText()));
                originPosition = Point.fromLngLat(Lo, La);//현재 좌표
                searchedPosition = Point.fromLngLat(destinationLo, destinationLa);
                getRoute_walking(originPosition, searchedPosition);
                getRoute_navi_walking(originPosition, searchedPosition);
                startButton.setEnabled(true);
                startButton.setBackgroundResource(R.color.mapboxBlue);
                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(destinationLa, destinationLo)) // Sets the new camera position
                        .zoom(16) // Sets the zoom , 줌 정도 숫자가 클수록 더많이 줌함
                        .bearing(0) // Rotate the camera , 카메라 방향(북쪽이 0) 북쪽부터 시계방향으로 측정
                        .tilt(0) // Set the camera tilt , 각도
                        .build(); // Creates a CameraPosition from the builder
                //카메라 움직이기
                mapboxMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(position), 7000);
                arButton.setEnabled(true);
                arButton.setBackgroundResource(R.color.mapboxBlue);
            }
        });*/

        /*txtView = findViewById(R.id.txtDestination);

        Places.initialize(getApplicationContext(), "AIzaSyCVXwfS2pdm-KGbqvXc30RB8jGGJZ58mtc");
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                txtView.setText(String.valueOf(place.getName())); // 0820 : edittext 부분에 목적지 설정됨
                Log.e(TAG, "타입 테스트 : " + String.valueOf(place.getName()).getClass()); // String 타입인데 왜 안 될까?
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });*/

    }

    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.addOnMapClickListener(this); //맵 클릭 리스너 등록
        //↓ 초기 지도 스타일 지정
        //mapbox://styles/gouz7514/cke8d56tw4y5v19jv8ecm5l7v
        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/streets-v11"), new Style.OnStyleLoaded() { // Mapbox Studio에서 편집한 내용은 여기서 다 저장됨
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);
            }
        });
    }

    // 클릭시 마커 추가
    private void addDestinationIconSymbolLayer(@NonNull Style loadedMapStyle) {
//        loadedMapStyle.addImage("destination-icon-id",
//                BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default));
        // TODO : 지도상 클릭시 "경로가 화면상에 표시되면 버튼 누르세요" 메시지 추가해보기

        GeoJsonSource geoJsonSource = new GeoJsonSource("destination-source-id");
        loadedMapStyle.addSource(geoJsonSource);
        SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id", "destination-source-id");
        destinationSymbolLayer.withProperties(
                iconImage("destination-icon-id"),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        );
        loadedMapStyle.addLayer(destinationSymbolLayer);
    }

    @Override
    //지도 클릭시 자동 길찾기
    public boolean onMapClick(@NonNull LatLng point) {

        if (destinationMarker != null) {
            mapboxMap.removeMarker(destinationMarker);
        }
        destinationMarker = mapboxMap.addMarker(new MarkerOptions().position(point));//마커 추가
        destinatonPosition = Point.fromLngLat(point.getLongitude(), point.getLatitude());//클릭한곳의 좌표

        originPosition = Point.fromLngLat(Lo, La);//현재 좌표
        getRoute_walking(originPosition, destinatonPosition);
        getRoute_navi_walking(originPosition, destinatonPosition);
        startButton.setEnabled(true);   //네비게이션 버튼 활성화
        startButton.setBackgroundResource(R.color.mapboxBlue);
        arButton.setEnabled(true);
        arButton.setBackgroundResource(R.color.mapboxBlue);
        return false;
    }

    //getroute
    // 길찾기 함수 - 여기를 바꾸면 되겠구나
//    private void getRoute(Point origin, Point destination) {
//        NavigationRoute.builder(this)
//                .accessToken(Mapbox.getAccessToken())
//                .origin(origin) // 출발지 위도, 경도
//                .destination(destination) // 목적지 위도, 경도
//                .build()
//                .getRoute(new Callback<DirectionsResponse>() {
//                    @Override
//                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
//                        // You can get the generic HTTP info about the response
//                        Log.d(TAG, "Response code: " + response.code());
//                        if (response.body() == null) {
//                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
//                            return;
//                        } else if (response.body().routes().size() < 1) {
//                            Log.e(TAG, "No routes found");
//                            return;
//                        }
//
//                        currentRoute = response.body().routes().get(0);
//
//                        // Draw the route on the map
//                        if (navigationMapRoute != null) {
//                            navigationMapRoute.removeRoute();
//                        } else {
//                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
//                        }
//                        navigationMapRoute.addRoute(currentRoute);
//                    }
//
//                    @Override
//                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
//                        Log.e(TAG, "Error: " + throwable.getMessage());
//                    }
//                });
//    }

    // TODO : 도보로 길찾기 함수
    private void getRoute_walking(Point origin, Point destination) {
        Log.e(TAG,"getRoute 실행");
        client = MapboxDirections.builder()
                .origin(origin)//출발지 위도 경도
                .destination(destination)//도착지 위도 경도
                .overview(DirectionsCriteria.OVERVIEW_FULL)//정보 받는정도 최대
                .profile(DirectionsCriteria.PROFILE_WALKING)//길찾기 방법(도보,자전거,자동차)
                .accessToken(getString(R.string.access_token))
                .build();

        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                Log.e(TAG,"onResponse 실행");
                System.out.println(call.request().url().toString());
                // You can get the generic HTTP info about the response
                Log.e(TAG, "Response code: " + response.code());
                if (response.body() == null) {
                    Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                    return;
                } else if (response.body().routes().size() < 1) {
                    Log.e(TAG, "No routes found");
                    return;
                }
                // Print some info about the route
                currentRoute = response.body().routes().get(0);
                Log.e(TAG, "Distance: " + currentRoute.distance());

                int time = (int) (currentRoute.duration()/60);
                //예상 시간을초단위로 받아옴
                double distances = (currentRoute.distance()/1000);
                // TODO : geometry : Returns:an encoded polyline string
                //목적지까지의 거리를 m로 받아옴

                distances = Math.round(distances*100)/100.0;
                //Math.round() 함수는 소수점 첫째자리에서 반올림하여 정수로 남긴다
                //원래 수에 100곱하고 round 실행 후 다시 100으로 나눈다 -> 둘째자리까지 남김

                Toast.makeText(getApplicationContext(), String.format("예상 시간 : " + String.valueOf(time)+" 분 \n" +
                        "목적지 거리 : " +distances+ " km"), Toast.LENGTH_LONG).show();
                // Draw the route on the map
                // drawRoute(currentRoute);
            }
            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                Log.e(TAG, "Error: " + throwable.getMessage());
                Toast.makeText(MainActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getRoute_navi_walking (Point origin, Point destinaton) {
        // TODO : https://docs.mapbox.com/android/navigation/overview/map-matching/
        NavigationRoute.builder(this).accessToken(Mapbox.getAccessToken())
                .profile(DirectionsCriteria.PROFILE_WALKING)//도보 길찾기
                .origin(origin)//출발지
                .destination(destinaton).//도착지
                build().
                getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if (response.body() == null) {
                            return;
                        } else if (response.body().routes().size() ==0) {
                            return;
                        }
                        currentRoute = response.body().routes().get(0);
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }
                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                    }
                });
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        Log.e(TAG,"enableLocationComponent 실행");
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Activate the MapboxMap LocationComponent to show user location
            // Adding in LocationComponentOptions is also an optional parameter
            locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(this, loadedMapStyle);
            locationComponent.setLocationComponentEnabled(true);
            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
            initLocationEngine();
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressLint("MissingPermission")
    private void initLocationEngine() {
        Log.e(TAG,"initLocationEngine 실행");
        locationEngine = LocationEngineProvider.getBestLocationEngine(this);
        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();
        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
        locationEngine.getLastLocation(callback);
    }

    //안드로이드 기기 위치 추적
    //현재 위치 얻어오는 콜백
    class MainActivityLocationCallback implements LocationEngineCallback<LocationEngineResult> {
        private final WeakReference<MainActivity> activityWeakReference;
        MainActivityLocationCallback(MainActivity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }
        /**
         * The LocationEngineCallback interface's method which fires when the device's location has changed.
         * @param result the LocationEngineResult object which has the last known location within it.
         */

        @Override
        public void onSuccess(LocationEngineResult result) {
            Log.e(TAG,"MainActivityLocationCallback onSuccess 실행");
            MainActivity activity = activityWeakReference.get();
            if (activity != null) {
                Location location = result.getLastLocation();
                if (location == null) {
                    return;
                }
                // Create a Toast which displays the new location's coordinates
                La = result.getLastLocation().getLatitude();
                Lo = result.getLastLocation().getLongitude();

                // Pass the new location to the Maps SDK's LocationComponent
                if (activity.mapboxMap != null && result.getLastLocation() != null) {
                    activity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
                }
            }
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location can not be captured
         *
         * @param exception the exception message
         */
        @Override
        public void onFailure(@NonNull Exception exception) {
            Log.e("LocationChangeActivity", exception.getLocalizedMessage());
            MainActivity activity = activityWeakReference.get();
            if (activity != null) {
                Toast.makeText(activity, exception.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 목적지 주소값을 통해 목적지 위도 경도를 얻어오는 구문
    public void getPointFromGeoCoder(String destinationxy) {
        Log.e(TAG,"지오코더 실행");
        Geocoder geocoder = new Geocoder(this);
        List<Address> listAddress = null;
        try {
            listAddress = geocoder.getFromLocationName(destinationxy, 1);
            destinationLo = listAddress.get(0).getLongitude();
            destinationLa = listAddress.get(0).getLatitude();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //앱 상태바 메뉴를 메인이벤트에 연동시켜주는 메소드
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menuoptions, menu);
        return true;
    }
    //클릭 이벤트 처리
    //메뉴 클릭시 선택한 메뉴를 호출하게 하는 메소드
    public boolean onOptionsItemSelected(MenuItem item){

        int itemId = item.getItemId();
        if (itemId == R.id.logout) {
            mFirebaseAuth = mFirebaseAuth.getInstance();
            mFirebaseAuth.signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            Toast.makeText(MainActivity.this, R.string.logged_out, Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.menu1) {
            Intent intent;
            intent = new Intent(this, PreferencesActivity.class);
            launcher.launch(intent);

        }else if (itemId == R.id.menu2){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dankook.ac.kr/web/kor/campusmap?p_p_id=Campus_WAR_campusportlet&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-1&p_p_col_count=1&_Campus_WAR_campusportlet_sCampusId=1&_Campus_WAR_campusportlet_pageView=detail&_Campus_WAR_campusportlet_action=view"));
            //intent.setPackage("com.android.chrome");
            startActivity(intent);
        } else if (itemId == R.id.menu3) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(R.string.real_quit);
            builder.setTitle(R.string.real_quit)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            moveTaskToBack(true);

                            finish();

                            Process.killProcess(Process.myPid());
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setTitle(R.string.real_quit);
            alert.show();
        }
        return false;
    }

    @Override //위치권한
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override //권한 필요할때 나오는 메세지
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationComponent(mapboxMap.getStyle());
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override public void onBackPressed() {
        //super.onBackPressed(); }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}