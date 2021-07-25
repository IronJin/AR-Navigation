package com.example.mapbox;

import com.mapbox.api.directions.v5.models.LegStep;
import com.mapbox.api.directions.v5.models.RouteLeg;
import com.mapbox.core.constants.Constants;
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
import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconKeepUpright;

public class MapPage extends MainActivity implements RouteListener, ProgressChangeListener, OffRouteListener {


    private MapboxNavigation mapboxNavigation;
    private RouteFetcher routeFetcher;
    private RouteProgress lastRouteProgress;
    private static final String PERMISSION_FOREGROUND_SERVICE = "android.permission.FOREGOUND_SERVICE";

    MainActivityLocationCallback callback = new MainActivityLocationCallback(this);

    private static final String TAG = MapPage.class.getSimpleName();

    private DirectionsRoute directionsRoute;

    private boolean visionManagerWasInit = false;
    private boolean navigationWasStarted = false;

    private Point Origin_Point = Point.fromLngLat(Lo,La);
    private Point DestinationPoint = Point.fromLngLat(127.20616, 37.19234);

    private LocationEngineCallback<LocationEngineResult> locationEngineCallback;

    private long LOCATION_INTERVAL_DEFAULT = 0L;
    private long LOCATION_INTERVAL_FAST = 1000L;

    VisionArView ar_view;

    private LocationEngine locationEngine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar_navigation);
        ar_view = findViewById(R.id.mapbox_ar_view);
    }


    protected void setArRenderOptions(@NotNull final VisionArView visionArView){
        visionArView.setFenceVisible(true);
        visionArView.setLaneVisible(true);
        VisionArManager.setLaneLength(40f);

        final LaneVisualParams laneVisualParams = new LaneVisualParams(
                new Color(1.0f, 0.0f, 0.0f, 1.0f),
                1f,
                2.5f
        );
        visionArView.setLaneVisualParams(laneVisualParams);

        visionArView.setFenceVisible(true);

        final FenceVisualParams fenceVisualParams = new FenceVisualParams(
                new Color(1.0f, 1.0f, 0.0f, 1.0f),
                2f,
                1f,
                3f,
                3
        );
        visionArView.setFenceVisualParams(fenceVisualParams);
        visionArView.setArQuality(0.8f);
    }


    @Override
    protected void onStart() {
        super.onStart();
        startVisionManager();
        startNavigation();
    }
    @Override
    protected void onResume(){
        super.onResume();
        startVisionManager();
        startNavigation();
    }

    @Override
    protected void onStop(){
        super.onStop();
        stopVisionManager();
        stopNavigation();
    }

    private void startVisionManager() {
        if(allPermissionsGranted() && !visionManagerWasInit) {
            VisionManager.create();
            VisionManager.setModelPerformance(new On(ModelPerformanceMode.DYNAMIC, ModelPerformanceRate.LOW.INSTANCE));
            VisionManager.start();
            VisionManager.setVisionEventsListener(new VisionEventsListener() {
                @Override
                public void onAuthorizationStatusUpdated(@NotNull AuthorizationStatus authorizationStatus) {

                }

                @Override
                public void onFrameSegmentationUpdated(@NotNull FrameSegmentation frameSegmentation) {

                }

                @Override
                public void onFrameDetectionsUpdated(@NotNull FrameDetections frameDetections) {

                }

                @Override
                public void onFrameSignClassificationsUpdated(@NotNull FrameSignClassifications frameSignClassifications) {

                }

                @Override
                public void onRoadDescriptionUpdated(@NotNull RoadDescription roadDescription) {

                }

                @Override
                public void onWorldDescriptionUpdated(@NotNull WorldDescription worldDescription) {

                }

                @Override
                public void onVehicleStateUpdated(@NotNull VehicleState vehicleState) {

                }

                @Override
                public void onCameraUpdated(@NotNull Camera camera) {

                }

                @Override
                public void onCountryUpdated(@NotNull Country country) {

                }

                @Override
                public void onUpdateCompleted() {

                }
            });

            VisionArView visionArView = ar_view;

            VisionArManager.create(VisionManager.INSTANCE);
            visionArView.setArManager(VisionArManager.INSTANCE);
            setArRenderOptions(visionArView);

            visionManagerWasInit = true;
        }
    }

    private void stopVisionManager(){
        if(visionManagerWasInit) {
            VisionArManager.destroy();
            VisionManager.stop();
            VisionManager.destroy();

            visionManagerWasInit = false;
        }
    }

    private void startNavigation() {
        if(allPermissionsGranted() && !navigationWasStarted) {
            mapboxNavigation = new MapboxNavigation(
                    this,
                    getString(R.string.mapbox_access_token),
                    MapboxNavigationOptions.builder().build()
            );

            routeFetcher = new RouteFetcher(this, getString(R.string.mapbox_access_token));
            routeFetcher.addRouteListener(this);

            //빈 부분 작성 안한부분임
            locationEngine = LocationEngineProvider.getBestLocationEngine(this);

            LocationEngineRequest request = new LocationEngineRequest.Builder(0L)
                    .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                    .setFastestInterval(1000L)
                    .build();

            locationEngineCallback = new LocationEngineCallback<LocationEngineResult>() {
                @Override
                public void onSuccess(LocationEngineResult result) {

                }

                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            };

            try {
                locationEngine.requestLocationUpdates(request, callback, Looper.getMainLooper());
            }catch (SecurityException se){
                VisionLogger.Companion.e(TAG, se.toString());
            }


            initDirectionsRoute();


            mapboxNavigation.addOffRouteListener(this);
            mapboxNavigation.addProgressChangeListener(this);

            navigationWasStarted =true;
        }
    }

    private void stopNavigation(){
        if(navigationWasStarted){
            locationEngine.removeLocationUpdates(callback);

            mapboxNavigation.removeProgressChangeListener(this);
            mapboxNavigation.removeOffRouteListener(this);
            mapboxNavigation.stopNavigation();

            navigationWasStarted = false;
        }
    }

    private void initDirectionsRoute(){
        NavigationRoute.builder(this)
                .accessToken(getString(R.string.mapbox_access_token))
                .profile(DirectionsCriteria.PROFILE_WALKING)
                .origin(Origin_Point)
                .destination(DestinationPoint)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if(response.body() == null || response.body().routes().isEmpty()){
                            return;
                        }


                        DirectionsRoute route = response.body().routes().get(0);
                        mapboxNavigation.startNavigation(route);

                        VisionArManager.setRoute(new Route(
                                getRoutePoints(route),
                                route.duration().floatValue(),
                                "Source Location",
                                "Target Location"
                        ));
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        Log.e(TAG, "Error: " + t.getMessage());
                    }
                });
    }



    @Override
    public void userOffRoute(Location location) {
        routeFetcher.findRouteFromRouteProgress(location, lastRouteProgress);
    }

    @Override
    public void onResponseReceived(@NotNull DirectionsResponse response,  RouteProgress routeProgress) {
        mapboxNavigation.stopNavigation();
        if(response.routes().isEmpty()){
            Toast.makeText(this, "Can not calculate", Toast.LENGTH_SHORT).show();
        } else {
            DirectionsRoute route = response.routes().get(0);

            mapboxNavigation.startNavigation(route);

            VisionArManager.setRoute(new Route(
                    getRoutePoints(route),
                    (float) routeProgress.durationRemaining(),
                    "Source Location",
                    "Destination Location"
            ));
        }
    }

    @Override
    public void onErrorReceived(Throwable throwable) {
        if(throwable != null){
            throwable.printStackTrace();
        }
        mapboxNavigation.stopNavigation();
        Toast.makeText(this,"Can not calculate", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressChange(Location location, RouteProgress routeProgress) {
        lastRouteProgress = routeProgress;
    }

    private RoutePoint[] getRoutePoints(@NotNull DirectionsRoute route){
        ArrayList<RoutePoint> routePoints = new ArrayList<>();

        List<RouteLeg> legs = route.legs();
        if(legs != null){
            for (RouteLeg leg : legs){
                List<LegStep> steps = leg.steps();
                if(steps != null){
                    for(LegStep step : steps){
                        RoutePoint point = new RoutePoint((new GeoCoordinate(
                                step.maneuver().location().latitude(),
                                step.maneuver().location().longitude()
                        )), mapToManeuverType(step.maneuver().type()));

                        routePoints.add(point);


                        List<Point> geometryPoints = buildStepPointsFromGeometry(step.geometry());
                        for(Point geometryPoint : geometryPoints){
                            point = new RoutePoint((new GeoCoordinate(
                                    geometryPoint.latitude(),
                                    geometryPoint.longitude()
                            )), ManeuverType.None);

                            routePoints.add(point);
                        }
                    }
                }
            }
        }
        return routePoints.toArray(new RoutePoint[0]);
    }

    private List<Point> buildStepPointsFromGeometry(String geometry){
        return PolylineUtils.decode(geometry, Constants.PRECISION_6);
    }

    private ManeuverType mapToManeuverType(@Nullable String maneuver){
        if (maneuver == null) {
            return ManeuverType.None;
        }
        switch (maneuver) {
            case "turn":
                return ManeuverType.Turn;
            case "depart":
                return ManeuverType.Depart;
            case "arrive":
                return ManeuverType.Arrive;
            case "merge":
                return ManeuverType.Merge;
            case "on ramp":
                return ManeuverType.OnRamp;
            case "off ramp":
                return ManeuverType.OffRamp;
            case "fork":
                return ManeuverType.Fork;
            case "roundabout":
                return ManeuverType.Roundabout;
            case "exit roundabout":
                return ManeuverType.RoundaboutExit;
            case "end of road":
                return ManeuverType.EndOfRoad;
            case "new name":
                return ManeuverType.NewName;
            case "continue":
                return ManeuverType.Continue;
            case "rotary":
                return ManeuverType.Rotary;
            case "roundabout turn":
                return ManeuverType.RoundaboutTurn;
            case "notification":
                return ManeuverType.Notification;
            case "exit rotary":
                return ManeuverType.RotaryExit;
            default:
                return ManeuverType.None;
        }
    }
    protected boolean allPermissionsGranted() {
        for(String permission : getRequirePermissions()){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                if(permission.equals(PERMISSION_FOREGROUND_SERVICE)){
                    continue;
                }
            }
            return true;
        }
        return true;
    }
    private String[] getRequirePermissions() {
        String[] permissions;
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] requestedPermissions = info.requestedPermissions;
            if(requestedPermissions != null && requestedPermissions.length > 0) {
                permissions = requestedPermissions;
            } else {
                permissions = new String[]{};
            }
        } catch (PackageManager.NameNotFoundException e) {
            permissions = new String[]{};
        }
        return permissions;
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

}