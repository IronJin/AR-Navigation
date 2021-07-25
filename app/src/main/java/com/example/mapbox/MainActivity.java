/*
작성자 : 양철진
수정일 : 2021.06.29
 */
//컴갤컴갤
//양철진

package com.example.mapbox;
//ar

import com.mapbox.api.directions.v5.models.LegStep;
import com.mapbox.api.directions.v5.models.RouteLeg;
import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.utils.PolylineUtils;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigationOptions;
import com.mapbox.vision.ar.FenceVisualParams;
import com.mapbox.vision.ar.LaneVisualParams;
import com.mapbox.vision.ar.VisionArManager;
import com.mapbox.vision.ar.core.models.Color;
import com.mapbox.vision.ar.core.models.ManeuverType;
import com.mapbox.vision.ar.core.models.Route;
import com.mapbox.vision.ar.core.models.RoutePoint;
import com.mapbox.vision.mobile.core.models.position.GeoCoordinate;
import com.mapbox.vision.performance.ModelPerformance.On;
import com.google.firebase.database.core.operation.Merge;

import com.mapbox.vision.mobile.core.interfaces.VisionEventsListener;
import com.mapbox.vision.mobile.core.models.AuthorizationStatus;
import com.mapbox.vision.mobile.core.models.Camera;
import com.mapbox.vision.mobile.core.models.Country;
import com.mapbox.vision.mobile.core.models.FrameSegmentation;
import com.mapbox.vision.mobile.core.models.classification.FrameSignClassifications;
import com.mapbox.vision.mobile.core.models.detection.FrameDetections;
import com.mapbox.vision.mobile.core.models.position.VehicleState;
import com.mapbox.vision.mobile.core.models.road.RoadDescription;
import com.mapbox.vision.mobile.core.models.world.WorldDescription;
import com.mapbox.vision.performance.*;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
<<<<<<< Updated upstream
=======
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
>>>>>>> Stashed changes
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
<<<<<<< Updated upstream

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
=======
import android.os.Looper;
import android.util.Log;
>>>>>>> Stashed changes
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.PopupMenu;
import android.widget.Toast;

<<<<<<< Updated upstream
// classes needed to initialize map
=======
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

>>>>>>> Stashed changes
import com.google.firebase.auth.FirebaseAuth;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.geojson.LineString;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

// classes needed to add the location component
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;

// classes needed to add a marker
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

// classes to calculate a route
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
<<<<<<< Updated upstream
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
=======
import com.mapbox.services.android.navigation.v5.offroute.OffRouteListener;
import com.mapbox.services.android.navigation.v5.route.RouteFetcher;
import com.mapbox.services.android.navigation.v5.route.RouteListener;
import com.mapbox.services.android.navigation.v5.routeprogress.ProgressChangeListener;
import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgress;
import com.mapbox.vision.VisionManager;
import com.mapbox.vision.ar.view.gl.VisionArView;
import com.mapbox.vision.utils.VisionLogger;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

>>>>>>> Stashed changes
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;

// classes needed to launch navigation UI
import android.view.View;
import android.widget.Button;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
<<<<<<< Updated upstream
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;
=======
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconKeepUpright;

// classes needed to initialize map
// classes needed to add the location component
// classes needed to add a marker
// classes to calculate a route
// classes needed to launch navigation UI
>>>>>>> Stashed changes


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener, PermissionsListener {

    private static final String ROUTE_LAYER_ID = "route-layer-id";
    private static final String ROUTE_SOURCE_ID = "route-source-id";
    private static final String ICON_LAYER_ID = "icon-layer-id";
    private static final String ICON_SOURCE_ID = "icon-source-id";
    private static final String RED_PIN_ICON_ID = "red-pin-icon-id";






    //firebase 관련
    private FirebaseAuth mFirebaseAuth;

    // variables for adding location layer
    private MapView mapView;
    private MapboxMap mapboxMap;
    // variables for adding location layer
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;
    // variables for calculating and drawing a route
    private DirectionsRoute currentRoute;
    private static final String TAG = MainActivity.class.getSimpleName();
    private NavigationMapRoute navigationMapRoute;
    // variables needed to initialize navigation

    //2d 네비게이션 버튼
    private Button button;
    private Button ArButton;

    //목적지 설정 버튼
    private Button button1;

    // 내위치에 대한 위도 경도 값을 받아오는 변수
    public static double Lo;
    public static double La;

    //내위치 Lo, La 를 이용해서 받아온 나의 위치
    private Point originPoint;
    //목적지 위치
    private Point destinationPoint;


    //목적지 위치 변수인 destinationPoint 의 좌표값을 위한 변수 설정
    //목적지 설정버튼에서는 아직 사용하지 않는중임 나중에 사용할 예정일수도있음
    private double destinationX;
    private double destinationY;

    private LocationEngine locationEngine;
    private long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    private long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;

    // Variables needed to listen to location updates
    MainActivityLocationCallback callback = new MainActivityLocationCallback(this);


<<<<<<< Updated upstream
=======
    //언어설정 변수
    static public String locale;
    Intent intent;
    static public Configuration config;

    //환경설정 필요 변수
    SharedPreferences sharedPreferences;
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
            });

    public MainActivity() {
    }

>>>>>>> Stashed changes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }


    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        MainActivity.this.mapboxMap = mapboxMap;

        //지도의 스타일을 담당
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);


                addDestinationIconSymbolLayer(style);
                mapboxMap.addOnMapClickListener(MainActivity.this);

                //start navigation 버튼 구현
                //button id 이용해서 메인액티비티에 띄우는 것
                button = findViewById(R.id.startButton);
                ArButton = findViewById(R.id.arbutton);
                //activity_main 에서 구현한 버튼을 MainActivity 와 버튼 id 를 이용해 연동시켜줌
                //목적지 설정 버튼임
                button1 = findViewById(R.id.destination);
                //button 클릭시
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*
                        boolean simulateRoute = true;
                        실제 운행이 아닌 시뮬레이션을 돌리려고 할때 simulateRoute 사용
                        구현방법 : 아래 .directionsRoute(currnetRoute).shouldSimulateRoute(simulateRoute).build();
                         */
                        //boolean simulateRoute = true;
                        NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                .directionsRoute(currentRoute)
                                .build();
                        // Call this method with Context from within an Activity
                        NavigationLauncher.startNavigation(MainActivity.this, options);
                    }
                });

                ArButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(MainActivity.this, MapPage.class);
                        startActivity(intent);
                    }
                });


                //button1 은 목적지설정 버튼
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        final PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
                        getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                //제 1공학관
                                if (item.getItemId() == R.id.action_menu1){
                                    destinationPoint = Point.fromLngLat(127.12584,37.32119);
                                    originPoint = Point.fromLngLat(Lo,La);
                                    getRoute(originPoint, destinationPoint);
                                    button.setEnabled(true);
                                    ArButton.setEnabled(true);
                                    button.setBackgroundResource(R.color.mapboxBlue);
                                }
                                //제 2공학관
                                else if (item.getItemId() == R.id.action_menu2){
                                    destinationPoint = Point.fromLngLat(127.12629,37.32080);
                                    originPoint = Point.fromLngLat(Lo,La);
                                    getRoute(originPoint, destinationPoint);
                                    button.setEnabled(true);
                                    ArButton.setEnabled(true);
                                    button.setBackgroundResource(R.color.mapboxBlue);
                                }
                                //제 3공학관
                                else if (item.getItemId() == R.id.action_menu3){
                                    destinationPoint = Point.fromLngLat(127.12676,37.32043);
                                    originPoint = Point.fromLngLat(Lo,La);
                                    getRoute(originPoint, destinationPoint);
                                    button.setEnabled(true);
                                    ArButton.setEnabled(true);
                                    button.setBackgroundResource(R.color.mapboxBlue);
                                }
                                //사회과학관
                                else if (item.getItemId() == R.id.action_menu4){
                                    destinationPoint = Point.fromLngLat(127.12564,37.32137);
                                    originPoint = Point.fromLngLat(Lo,La);
                                    getRoute(originPoint, destinationPoint);
                                    button.setEnabled(true);
                                    ArButton.setEnabled(true);
                                    button.setBackgroundResource(R.color.mapboxBlue);
                                }
                                //소프트웨어 ICT관
                                else if (item.getItemId() == R.id.action_menu5){
                                    destinationPoint = Point.fromLngLat(127.12753,37.32274);
                                    originPoint = Point.fromLngLat(Lo,La);
                                    getRoute(originPoint, destinationPoint);
                                    button.setEnabled(true);
                                    ArButton.setEnabled(true);
                                    button.setBackgroundResource(R.color.mapboxBlue);
                                }
                                //미디어센터
                                else if (item.getItemId() == R.id.action_menu6){
                                    destinationPoint = Point.fromLngLat(127.12753,37.32242);
                                    originPoint = Point.fromLngLat(Lo,La);
                                    getRoute(originPoint, destinationPoint);
                                    button.setEnabled(true);
                                    ArButton.setEnabled(true);
                                    button.setBackgroundResource(R.color.mapboxBlue);
                                }
                                //범정관
                                else if (item.getItemId() == R.id.action_menu7){
                                    destinationPoint = Point.fromLngLat(127.12641,37.32196);
                                    originPoint = Point.fromLngLat(Lo,La);
                                    getRoute(originPoint, destinationPoint);
                                    button.setEnabled(true);
                                    ArButton.setEnabled(true);
                                    button.setBackgroundResource(R.color.mapboxBlue);
                                }
                                //인문관
                                else if (item.getItemId() == R.id.action_menu8){
                                    destinationPoint = Point.fromLngLat(127.12896,37.32178);
                                    originPoint = Point.fromLngLat(Lo,La);
                                    getRoute(originPoint, destinationPoint);
                                    button.setEnabled(true);
                                    ArButton.setEnabled(true);
                                    button.setBackgroundResource(R.color.mapboxBlue);
                                }
                                //상경관
                                else if (item.getItemId() == R.id.action_menu9){
                                    destinationPoint = Point.fromLngLat(127.12894,37.32227);
                                    originPoint = Point.fromLngLat(Lo,La);
                                    getRoute(originPoint, destinationPoint);
                                    button.setEnabled(true);
                                    ArButton.setEnabled(true);
                                    button.setBackgroundResource(R.color.mapboxBlue);
                                }
                                //사범관
                                else if (item.getItemId() == R.id.action_menu10){
                                    destinationPoint = Point.fromLngLat(127.12897,37.32272);
                                    originPoint = Point.fromLngLat(Lo,La);
                                    getRoute(originPoint, destinationPoint);
                                    button.setEnabled(true);
                                    ArButton.setEnabled(true);
                                    button.setBackgroundResource(R.color.mapboxBlue);
                                }
                                //혜당관
                                else if (item.getItemId() == R.id.action_menu11){
                                    destinationPoint = Point.fromLngLat(127.12827,37.32047);
                                    originPoint = Point.fromLngLat(Lo,La);
                                    getRoute(originPoint, destinationPoint);
                                    button.setEnabled(true);
                                    ArButton.setEnabled(true);
                                    button.setBackgroundResource(R.color.mapboxBlue);
                                }
                                //퇴계기념중앙도서관
                                else if (item.getItemId() == R.id.action_menu12){
                                    destinationPoint = Point.fromLngLat(127.12746,37.32116);
                                    originPoint = Point.fromLngLat(Lo,La);
                                    getRoute(originPoint, destinationPoint);
                                    button.setEnabled(true);
                                    ArButton.setEnabled(true);
                                    button.setBackgroundResource(R.color.mapboxBlue);
                                }
                                //법학관, 대학원동
                                else if (item.getItemId() == R.id.action_menu13){
                                    destinationPoint = Point.fromLngLat(127.12921,37.32107);
                                    originPoint = Point.fromLngLat(Lo,La);
                                    getRoute(originPoint, destinationPoint);
                                    button.setEnabled(true);
                                    ArButton.setEnabled(true);
                                    button.setBackgroundResource(R.color.mapboxBlue);
                                }
                                //국제관
                                else if (item.getItemId() == R.id.action_menu14){
                                    destinationPoint = Point.fromLngLat(127.12717,37.31919);
                                    originPoint = Point.fromLngLat(Lo,La);
                                    getRoute(originPoint, destinationPoint);
                                    button.setEnabled(true);
                                    ArButton.setEnabled(true);
                                    button.setBackgroundResource(R.color.mapboxBlue);
                                }
                                //종합실험동
                                else if (item.getItemId() == R.id.action_menu15){
                                    destinationPoint = Point.fromLngLat(127.12577,37.32014);
                                    originPoint = Point.fromLngLat(Lo,La);
                                    getRoute(originPoint, destinationPoint);
                                    button.setEnabled(true);
                                    ArButton.setEnabled(true);
                                    button.setBackgroundResource(R.color.mapboxBlue);
                                }
                                //곰상
                                else if (item.getItemId() == R.id.action_menu16){
                                    destinationPoint = Point.fromLngLat(127.12892,37.31996);
                                    originPoint = Point.fromLngLat(Lo,La);
                                    getRoute(originPoint, destinationPoint);
                                    button.setEnabled(true);
                                    ArButton.setEnabled(true);
                                    button.setBackgroundResource(R.color.mapboxBlue);
                                }
                                //정문
                                else if (item.getItemId() == R.id.action_menu17){
                                    destinationPoint = Point.fromLngLat(127.12547,37.32346);
                                    originPoint = Point.fromLngLat(Lo,La);
                                    getRoute(originPoint, destinationPoint);
                                    button.setEnabled(true);
                                    ArButton.setEnabled(true);
                                    button.setBackgroundResource(R.color.mapboxBlue);
                                }

                                //체육관
                                else if (item.getItemId() == R.id.action_menu18){
                                    destinationPoint = Point.fromLngLat(127.13213,37.31936);
                                    originPoint = Point.fromLngLat(Lo,La);
                                    getRoute(originPoint, destinationPoint);
                                    button.setEnabled(true);
                                    ArButton.setEnabled(true);
                                    button.setBackgroundResource(R.color.mapboxBlue);
                                }
                                //대운동장
                                else if (item.getItemId() == R.id.action_menu19){
                                    destinationPoint = Point.fromLngLat(127.13300,37.32080);
                                    originPoint = Point.fromLngLat(Lo,La);
                                    getRoute(originPoint, destinationPoint);
                                    button.setEnabled(true);
                                    ArButton.setEnabled(true);
                                    button.setBackgroundResource(R.color.mapboxBlue);
                                }

                                //무용관
                                else if (item.getItemId() == R.id.action_menu20){
                                    destinationPoint = Point.fromLngLat(127.12724,37.31585);
                                    originPoint = Point.fromLngLat(Lo,La);
                                    getRoute(originPoint, destinationPoint);
                                    button.setEnabled(true);
                                    ArButton.setEnabled(true);
                                    button.setBackgroundResource(R.color.mapboxBlue);
                                }

                                //진리관
                                else if (item.getItemId() == R.id.action_menu21){
                                    destinationPoint = Point.fromLngLat(127.12682,37.31479);
                                    originPoint = Point.fromLngLat(Lo,La);
                                    getRoute(originPoint, destinationPoint);
                                    button.setEnabled(true);
                                    ArButton.setEnabled(true);
                                    button.setBackgroundResource(R.color.mapboxBlue);
                                }

                                //웅비홀
                                else if (item.getItemId() == R.id.action_menu22){
                                    destinationPoint = Point.fromLngLat(127.12698,37.31567);
                                    originPoint = Point.fromLngLat(Lo,La);
                                    getRoute(originPoint, destinationPoint);
                                    button.setEnabled(true);
                                    ArButton.setEnabled(true);
                                    button.setBackgroundResource(R.color.mapboxBlue);
                                }

                                //집현재
                                else if (item.getItemId() == R.id.action_menu23){
                                    destinationPoint = Point.fromLngLat(127.12695,37.31667);
                                    originPoint = Point.fromLngLat(Lo,La);
                                    getRoute(originPoint, destinationPoint);
                                    button.setEnabled(true);
                                    ArButton.setEnabled(true);
                                    button.setBackgroundResource(R.color.mapboxBlue);
                                }

                                //죽전치과병원
                                else if (item.getItemId() == R.id.action_menu24){
                                    destinationPoint = Point.fromLngLat(127.12513,37.32187);
                                    originPoint = Point.fromLngLat(Lo,La);
                                    getRoute(originPoint, destinationPoint);
                                    button.setEnabled(true);
                                    ArButton.setEnabled(true);
                                    button.setBackgroundResource(R.color.mapboxBlue);
                                }

                                //노천마당, 풋살장
                                else if (item.getItemId() == R.id.action_menu25){
                                    destinationPoint = Point.fromLngLat(127.12724,37.31988);
                                    originPoint = Point.fromLngLat(Lo,La);
                                    getRoute(originPoint, destinationPoint);
                                    button.setEnabled(true);
                                    ArButton.setEnabled(true);
                                    button.setBackgroundResource(R.color.mapboxBlue);
                                }

                                //음악관
                                else if (item.getItemId() == R.id.action_menu26){
                                    destinationPoint = Point.fromLngLat(127.12935,37.31918);
                                    originPoint = Point.fromLngLat(Lo,La);
                                    getRoute(originPoint, destinationPoint);
                                    button.setEnabled(true);
                                    ArButton.setEnabled(true);
                                    button.setBackgroundResource(R.color.mapboxBlue);
                                }

                                //미술관
                                else if (item.getItemId() == R.id.action_menu27){
                                    destinationPoint = Point.fromLngLat(127.13088,37.31983);
                                    originPoint = Point.fromLngLat(Lo,La);
                                    getRoute(originPoint, destinationPoint);
                                    button.setEnabled(true);
                                    ArButton.setEnabled(true);
                                    button.setBackgroundResource(R.color.mapboxBlue);
                                }

                                //폭포공원
                                else if (item.getItemId() == R.id.action_menu28){
                                    destinationPoint = Point.fromLngLat(127.13065,37.32082);
                                    originPoint = Point.fromLngLat(Lo,La);
                                    getRoute(originPoint, destinationPoint);
                                    button.setEnabled(true);
                                    ArButton.setEnabled(true);
                                    button.setBackgroundResource(R.color.mapboxBlue);
                                }
                                //리턴 값은 true, false 든 크게 상관없는 것 같음, 개발자도 잘 모르겠음
                                return false;
                            }
                        });

                        //팝업메뉴를 보이게 하는거
                        popupMenu.show();
                    }
                });

            }
        });
    }



    //맵 클릭시 뜨는 좌표 디자인
    private void addDestinationIconSymbolLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage("destination-icon-id",
                BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default));
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



    //맵클릭 활성화 되는 메소드
    //맵 클릭을 하지 않을시 이 메소드 자체를 비활성화 시키면 됨
    @SuppressWarnings( {"MissingPermission"})
    @Override
    public boolean onMapClick(@NonNull LatLng point) {


        //클릭한 곳의 좌표
        destinationPoint = Point.fromLngLat(point.getLongitude(), point.getLatitude());

        //나의 좌표
        originPoint = Point.fromLngLat(Lo, La);

        GeoJsonSource source = mapboxMap.getStyle().getSourceAs("destination-source-id");
        if (source != null) {
            source.setGeoJson(Feature.fromGeometry(destinationPoint));
        }

        //맵을 클릭하면 내 위치와 목적지 위치의 루트를 구해줌
        getRoute(originPoint, destinationPoint);

        button.setEnabled(true);
        button.setBackgroundResource(R.color.mapboxBlue);
        ArButton.setEnabled(true);
        ArButton.setBackgroundResource(R.color.mapbox_blue);

        return false;
    }



    //내 위치와 목적지 사이의 루트를 구해주는 함수
    //현재 도보 길찾기로 설정해놓은거임
    private void getRoute(Point origin, Point destination) {
        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .profile(DirectionsCriteria.PROFILE_WALKING) //도보 길찾기 -> 차량 길찾기시 주석처리
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        // You can get the generic HTTP info about the response
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);

                        if (navigationMapRoute != null){
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap);
                        }

                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }




    //User 의 위치를 나타내주는 메소드
    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Activate the MapboxMap LocationComponent to show user location
            // Adding in LocationComponentOptions is also an optional parameter
            // Set the LocationComponent activation options

            locationComponent = mapboxMap.getLocationComponent();

            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

            locationComponent.setLocationComponentEnabled(true);
            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            locationComponent.setRenderMode(RenderMode.COMPASS);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //완료
    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    //완료
    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationComponent(mapboxMap.getStyle());
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

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
            Log.e(TAG,"onSuccess 실행");
            MainActivity activity = activityWeakReference.get();
            if (activity != null) {
                Location location = result.getLastLocation();
                if (location == null) {
                    return;
                }
                // Create a Toast which displays the new location's coordinates
                //나의 위치를 받아와서 La, Lo 에 넣음
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


    /*
    //메뉴 함수
    // 앱상태바에 메뉴창이 뜨게해줌 , mainactivity 에서 메뉴를 add하는 방법
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE,1,Menu.NONE,"언어설정");
        menu.add(Menu.NONE,2,Menu.NONE,"기타");
        SubMenu sub =menu.addSubMenu("목적지 설정");
        sub.add(Menu.NONE,3,Menu.NONE,"1공학관");
        sub.add(Menu.NONE,4,Menu.NONE,"2공학관");
        return super.onCreateOptionsMenu(menu);
    }
     */

    //앱 상태바 메뉴를 메인이벤트에 연동시켜주는 메소드
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menuoptions, menu);
        return true;
    }

    //클릭 이벤트 처리
    //메뉴 클릭시 선택한 메뉴를 호출하게 하는 메소드
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.logout:

                mFirebaseAuth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this,"로그아웃 되었습니다", Toast.LENGTH_SHORT).show();

        }
        return false;
    }

    //뒤로가기 버튼을 막아서 로그인 화면으로 넘어가는 오류를 해결
    @Override
    public void onBackPressed(){
        //super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
        mapView.getMapAsync(this);
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
// Prevent leaks
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(callback);
        }
        mapView.onDestroy();
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


}
