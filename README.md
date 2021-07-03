# 목차
* 어플소개
* 안드로이드 스튜디오
  * splash 화면설정(loading 화면)
  * MAPBOX
  * MAPBOX SDK 및 MAPBOX 네비게이션 SDK 설치 방법
  * getRoute 메소드
  * 목적지 설정 방법
  * 사용자 위치 표시 방법
* 유니티
  * 안드로이드 연동
  * AR
 * 개선방안
---

# 어플소개
DK GUIDE 는 학교지리에 익숙치 않은 사용자를 위한 네비게이션 기반 앱으로써
증강현실(AR) 기술을 활용하여 사용자가 평면 지도를 활용한 길찾기 방식보다
보다 쉽게 길을 찾을 수 있도록 도와주는 가이드앱이다.

또한 사용자는 해당 목적지에 도착시 해당 위치나 건물에 대한 짧은 정보를 
AR 애니메이션 캐릭터를 통해 음성으로 얻을 수 있다.

맵박스에서 제공하는 튜토리얼을 중심으로 제작되었다.
참고 : https://docs.mapbox.com/help/tutorials/android-navigation-sdk/

---


# 안드로이드 스튜디오
AR 을 활용하기전 기본적인 네비게이션 기능을 구현하기 위해 안드로이드 스튜디오와 맵박스를 활용하였다.
##  0.SPLASH-ACTIVITY
어플이 시작되고 2-3초 정도의 로딩화면을 띄움으로써 어플이 시작되고있음을 알린다.



##  1. MAPBOX
맵박스는 3차원 동적 지도 웹서비스 개발을 지원하고 맞춤형 디자인 맵을 위한 오픈 소스 매핑 플랫폼이다.
맵박스를 이용해 동적지도, 3차원 지도, 네비게이션, 증강현실 다양한 것을 만들 수 있다.


## 2. MAPBOX SDK 및 MAPBOX 네비게이션 SDK 설치방법
맵박스를 활용하여 네비게이션 기능을 구현하기 위해서는 안드로이드 스튜디오에
맵박스 SDK 및 맵박스 네비게이션 SDK 두가지를 설치해야한다.

참고  
https://docs.mapbox.com/help/tutorials/android-navigation-sdk/  
https://docs.mapbox.com/android/maps/guides/install/  
(맵박스 SDK 설치 및 맵박스 네비게이션 튜토리얼)

1. 다음과 같이 [MAPBOX Account](#https://account.mapbox.com/) 페이지에 접속하여 로그인 후 다음과 같이 ACCESS 토큰을 복사한다.
![캡처](https://user-images.githubusercontent.com/85132068/123761993-4fad1200-d8fd-11eb-9c7f-4f4d256cebf9.PNG)


2. 모듈수준의 build.gradle 의 dependencies 에 다음과 같이 최신버전의 SDK 두개를 추가해준다.
```
implementation 'com.mapbox.mapboxsdk:mapbox-android-sdk:9.6.1'
implementation 'com.mapbox.mapboxsdk:mapbox-android-navigation-ui:0.42.6'
```
3. 프로젝트 수준의 build.gradle 의 repositories 에 다음을 추가해준다.
```
maven { url 'https://mapbox.bintray.com/mapbox' }
```

5. string.xml 에 복사한 access 토큰을 붙여넣는다.
```
<string name="mapbox_access_token" translatable="false">MAMBOX_ACCESS_TOKEN</string>
```

6. Manifest.xml 에 맵박스 관련 권한을 추가해주기 위해서 다음을 추가해준다.
```
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

7. 애플리케이션이 초기화 될 때 사용될 지도 스타일을 포함하여 MapView 의 속성과 네비게이션 스타트 버튼을 추가해주기 위해 activity_main.xml 에 다음을 추가해준다.
```
<com.mapbox.mapboxsdk.maps.MapView  
  android:id="@+id/mapView"  
  android:layout_width="match_parent"  
  android:layout_height="match_parent"  
  mapbox:mapbox_cameraTargetLat="38.9098"  
  mapbox:mapbox_cameraTargetLng="-77.0295"  
  mapbox:mapbox_cameraZoom="15.5" />  
  
  
//2d네비게이션 스타트 버튼  
<Button  
  android:id="@+id/startButton"  
  android:layout_width="50dp"  
  android:layout_height="wrap_content"  
  android:layout_marginRight="16dp"  
  android:layout_marginBottom="20dp"  
  android:background="@color/mapboxGrayLight"  
  android:enabled="false"  
  android:text="2D"  
  android:textColor="@color/mapboxWhite"  
  mapbox:layout_constraintRight_toRightOf="parent"  
  mapbox:layout_constraintBottom_toBottomOf="parent" />
  ```

8.  맵박스 지도를 사용하기 위해 MainActivity 에 다음을 추가해준다.
```
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener, PermissionsListener {

  private MapView mapView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Mapbox.getInstance(this, getString(R.string.access_token));
    setContentView(R.layout.activity_main);
    mapView = findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(@NonNull final MapboxMap mapboxMap) {

        mapboxMap.setStyle(Style.LIGHT, new Style.OnStyleLoaded() {
          @Override
          public void onStyleLoaded(@NonNull Style style) {
              // Map is set up and the style has loaded. Now you can add data or make other map adjustments
          }
        });
      }
    });
  }

  // Add the mapView's own lifecycle methods to the activity's lifecycle methods
  @Override
  public void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  public void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }
  ```

## 3. getRoute 메소드
getRoute 는 사용자의 위치와 목적지 사이의 경로를 구해주는 메소드이다.
아래는 도보 전용 길찾기 메소드이다. 차량용 길찾기로 설정을 바꾸려면
.profile(DirectionsCriteria.PROFILE_WALKING) 이 부분을 지워주거나 주석처리한다.

```
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
 return;  } else if (response.body().routes().size() < 1) {  
                        Log.e(TAG, "No routes found");  
 return;  }  
  
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
```

## 4. 목적지 설정 방법
MAPBOX 튜토리얼은 맵에 클릭한 곳의 좌표를 목적지로 받아오고 사용자와의 루트를 구해주는 방식이었다.
하지만 DK GIUDE 는 단국대 전용 가이드 앱이므로 팝업 메뉴를 활용하여 위치를 한정하였다.

1.  [GeoPlaner](https://www.geoplaner.com/) 를 활용하여 내가 원하는 곳의 좌표를 맵클릭을 통해 보다 세세하게 얻어 올 수 있다.
![캡처1](https://user-images.githubusercontent.com/85132068/123775117-082c8300-d909-11eb-8258-207980cbe0a9.PNG)

2. 각 건물에 얻어온 좌표를 각각 직접 입력으로 넣어준다. 각 if 문에는 건물에 해당하는 좌표값을 destinationPoint 로 전달을 하였고 getRoute 메소드를 활용하여 사용자의 위치를 나타내는 originPoint 와 destinationPoint 사이의 도보경로를 구해준다.
아래 코드는 단국대학교 죽전캠퍼스의 건물 중심으로 코딩을 한것이다.
```
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
  button.setBackgroundResource(R.color.mapboxBlue);  
  }  
                //제 2공학관  
  else if (item.getItemId() == R.id.action_menu2){  
                    destinationPoint = Point.fromLngLat(127.12629,37.32080);  
  originPoint = Point.fromLngLat(Lo,La);  
  getRoute(originPoint, destinationPoint);  
  button.setEnabled(true);  
  button.setBackgroundResource(R.color.mapboxBlue);  
  }  
                //제 3공학관  
  else if (item.getItemId() == R.id.action_menu3){  
                    destinationPoint = Point.fromLngLat(127.12676,37.32043);  
  originPoint = Point.fromLngLat(Lo,La);  
  getRoute(originPoint, destinationPoint);  
  button.setEnabled(true);  
  button.setBackgroundResource(R.color.mapboxBlue);  
  }  
                //사회과학관  
  else if (item.getItemId() == R.id.action_menu4){  
                    destinationPoint = Point.fromLngLat(127.12564,37.32137);  
  originPoint = Point.fromLngLat(Lo,La);  
  getRoute(originPoint, destinationPoint);  
  button.setEnabled(true);  
  button.setBackgroundResource(R.color.mapboxBlue);  
  }  
                //소프트웨어 ICT관  
  else if (item.getItemId() == R.id.action_menu5){  
                    destinationPoint = Point.fromLngLat(127.12753,37.32274);  
  originPoint = Point.fromLngLat(Lo,La);  
  getRoute(originPoint, destinationPoint);  
  button.setEnabled(true);  
  button.setBackgroundResource(R.color.mapboxBlue);  
  }  
                //미디어센터  
  else if (item.getItemId() == R.id.action_menu6){  
                    destinationPoint = Point.fromLngLat(127.12753,37.32242);  
  originPoint = Point.fromLngLat(Lo,La);  
  getRoute(originPoint, destinationPoint);  
  button.setEnabled(true);  
  button.setBackgroundResource(R.color.mapboxBlue);  
  }  
                //범정관  
  else if (item.getItemId() == R.id.action_menu7){  
                    destinationPoint = Point.fromLngLat(127.12641,37.32196);  
  originPoint = Point.fromLngLat(Lo,La);  
  getRoute(originPoint, destinationPoint);  
  button.setEnabled(true);  
  button.setBackgroundResource(R.color.mapboxBlue);  
  }  
                //인문관  
  else if (item.getItemId() == R.id.action_menu8){  
                    destinationPoint = Point.fromLngLat(127.12896,37.32178);  
  originPoint = Point.fromLngLat(Lo,La);  
  getRoute(originPoint, destinationPoint);  
  button.setEnabled(true);  
  button.setBackgroundResource(R.color.mapboxBlue);  
  }  
                //상경관  
  else if (item.getItemId() == R.id.action_menu9){  
                    destinationPoint = Point.fromLngLat(127.12894,37.32227);  
  originPoint = Point.fromLngLat(Lo,La);  
  getRoute(originPoint, destinationPoint);  
  button.setEnabled(true);  
  button.setBackgroundResource(R.color.mapboxBlue);  
  }  
                //사범관  
  else if (item.getItemId() == R.id.action_menu10){  
                    destinationPoint = Point.fromLngLat(127.12897,37.32272);  
  originPoint = Point.fromLngLat(Lo,La);  
  getRoute(originPoint, destinationPoint);  
  button.setEnabled(true);  
  button.setBackgroundResource(R.color.mapboxBlue);  
  }  
                //혜당관  
  else if (item.getItemId() == R.id.action_menu11){  
                    destinationPoint = Point.fromLngLat(127.12827,37.32047);  
  originPoint = Point.fromLngLat(Lo,La);  
  getRoute(originPoint, destinationPoint);  
  button.setEnabled(true);  
  button.setBackgroundResource(R.color.mapboxBlue);  
  }  
                //퇴계기념중앙도서관  
  else if (item.getItemId() == R.id.action_menu12){  
                    destinationPoint = Point.fromLngLat(127.12746,37.32116);  
  originPoint = Point.fromLngLat(Lo,La);  
  getRoute(originPoint, destinationPoint);  
  button.setEnabled(true);  
  button.setBackgroundResource(R.color.mapboxBlue);  
  }  
                //법학관, 대학원동  
  else if (item.getItemId() == R.id.action_menu13){  
                    destinationPoint = Point.fromLngLat(127.12921,37.32107);  
  originPoint = Point.fromLngLat(Lo,La);  
  getRoute(originPoint, destinationPoint);  
  button.setEnabled(true);  
  button.setBackgroundResource(R.color.mapboxBlue);  
  }  
                //국제관  
  else if (item.getItemId() == R.id.action_menu14){  
                    destinationPoint = Point.fromLngLat(127.12717,37.31919);  
  originPoint = Point.fromLngLat(Lo,La);  
  getRoute(originPoint, destinationPoint);  
  button.setEnabled(true);  
  button.setBackgroundResource(R.color.mapboxBlue);  
  }  
                //종합실험동  
  else if (item.getItemId() == R.id.action_menu15){  
                    destinationPoint = Point.fromLngLat(127.12577,37.32014);  
  originPoint = Point.fromLngLat(Lo,La);  
  getRoute(originPoint, destinationPoint);  
  button.setEnabled(true);  
  button.setBackgroundResource(R.color.mapboxBlue);  
  }  
                //곰상  
  else if (item.getItemId() == R.id.action_menu16){  
                    destinationPoint = Point.fromLngLat(127.12892,37.31996);  
  originPoint = Point.fromLngLat(Lo,La);  
  getRoute(originPoint, destinationPoint);  
  button.setEnabled(true);  
  button.setBackgroundResource(R.color.mapboxBlue);  
  }  
                //정문  
  else if (item.getItemId() == R.id.action_menu17){  
                    destinationPoint = Point.fromLngLat(127.12547,37.32346);  
  originPoint = Point.fromLngLat(Lo,La);  
  getRoute(originPoint, destinationPoint);  
  button.setEnabled(true);  
  button.setBackgroundResource(R.color.mapboxBlue);  
  }  
  
                //체육관  
  else if (item.getItemId() == R.id.action_menu18){  
                    destinationPoint = Point.fromLngLat(127.13213,37.31936);  
  originPoint = Point.fromLngLat(Lo,La);  
  getRoute(originPoint, destinationPoint);  
  button.setEnabled(true);  
  button.setBackgroundResource(R.color.mapboxBlue);  
  }  
                //대운동장  
  else if (item.getItemId() == R.id.action_menu19){  
                    destinationPoint = Point.fromLngLat(127.13300,37.32080);  
  originPoint = Point.fromLngLat(Lo,La);  
  getRoute(originPoint, destinationPoint);  
  button.setEnabled(true);  
  button.setBackgroundResource(R.color.mapboxBlue);  
  }  
  
                //무용관  
  else if (item.getItemId() == R.id.action_menu20){  
                    destinationPoint = Point.fromLngLat(127.12724,37.31585);  
  originPoint = Point.fromLngLat(Lo,La);  
  getRoute(originPoint, destinationPoint);  
  button.setEnabled(true);  
  button.setBackgroundResource(R.color.mapboxBlue);  
  }  
  
                //진리관  
  else if (item.getItemId() == R.id.action_menu21){  
                    destinationPoint = Point.fromLngLat(127.12682,37.31479);  
  originPoint = Point.fromLngLat(Lo,La);  
  getRoute(originPoint, destinationPoint);  
  button.setEnabled(true);  
  button.setBackgroundResource(R.color.mapboxBlue);  
  }  
  
                //웅비홀  
  else if (item.getItemId() == R.id.action_menu22){  
                    destinationPoint = Point.fromLngLat(127.12698,37.31567);  
  originPoint = Point.fromLngLat(Lo,La);  
  getRoute(originPoint, destinationPoint);  
  button.setEnabled(true);  
  button.setBackgroundResource(R.color.mapboxBlue);  
  }  
  
                //집현재  
  else if (item.getItemId() == R.id.action_menu23){  
                    destinationPoint = Point.fromLngLat(127.12695,37.31667);  
  originPoint = Point.fromLngLat(Lo,La);  
  getRoute(originPoint, destinationPoint);  
  button.setEnabled(true);  
  button.setBackgroundResource(R.color.mapboxBlue);  
  }  
  
                //죽전치과병원  
  else if (item.getItemId() == R.id.action_menu24){  
                    destinationPoint = Point.fromLngLat(127.12513,37.32187);  
  originPoint = Point.fromLngLat(Lo,La);  
  getRoute(originPoint, destinationPoint);  
  button.setEnabled(true);  
  button.setBackgroundResource(R.color.mapboxBlue);  
  }  
  
                //노천마당, 풋살장  
  else if (item.getItemId() == R.id.action_menu25){  
                    destinationPoint = Point.fromLngLat(127.12724,37.31988);  
  originPoint = Point.fromLngLat(Lo,La);  
  getRoute(originPoint, destinationPoint);  
  button.setEnabled(true);  
  button.setBackgroundResource(R.color.mapboxBlue);  
  }  
  
                //음악관  
  else if (item.getItemId() == R.id.action_menu26){  
                    destinationPoint = Point.fromLngLat(127.12935,37.31918);  
  originPoint = Point.fromLngLat(Lo,La);  
  getRoute(originPoint, destinationPoint);  
  button.setEnabled(true);  
  button.setBackgroundResource(R.color.mapboxBlue);  
  }  
  
                //미술관  
  else if (item.getItemId() == R.id.action_menu27){  
                    destinationPoint = Point.fromLngLat(127.13088,37.31983);  
  originPoint = Point.fromLngLat(Lo,La);  
  getRoute(originPoint, destinationPoint);  
  button.setEnabled(true);  
  button.setBackgroundResource(R.color.mapboxBlue);  
  }  
  
                //폭포공원  
  else if (item.getItemId() == R.id.action_menu28){  
                    destinationPoint = Point.fromLngLat(127.13065,37.32082);  
  originPoint = Point.fromLngLat(Lo,La);  
  getRoute(originPoint, destinationPoint);  
  button.setEnabled(true);  
  button.setBackgroundResource(R.color.mapboxBlue);  
  }  
               
  return false;  
  }  
        });  
  
  //팝업메뉴를 보이게 하는거  
  popupMenu.show();  
  }  
}
```

## 5. 사용자 위치
GPS 를 활용하여 사용자의 현재 위도, 경도 값을 double 변수인 Lo, La 에 전달해주고 Lo, La 변수를 이용하여 사용자의 위치를 나타내는 originPoint 에 전달을 해주는 방식이다.

이 방식을 이용하여 맵이 로드되면 그 위에 사용자의 위치를 표시하였다.

1.  사용자의 위치를 지도에 표시 하기 위한 메소드
```
//User 의 위치를 나타내주는 메소드  
@SuppressWarnings( {"MissingPermission"})  
private void enableLocationComponent(@NonNull Style loadedMapStyle) {  
    // Check if permissions are enabled and if not request  
  if (PermissionsManager.areLocationPermissionsGranted(this)) {  
        // Activate the MapboxMap LocationComponent to show user location  
 // Adding in LocationComponentOptions is also an optional parameter // Set the LocationComponent activation options  
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
  
@Override  
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
```

2. 사용자의 현재위치 위도, 경도값을 originPoint 에 전달해주는 double 변수 Lo, La 에 넣어주기 위한 클래스
Lo, La 에는 사용자의 위도, 경도 값이 각각 들어가 있다.
```
class MainActivityLocationCallback implements LocationEngineCallback<LocationEngineResult> {  
    private final WeakReference<MainActivity> activityWeakReference;  
  MainActivityLocationCallback(MainActivity activity) {  
        this.activityWeakReference = new WeakReference<>(activity);  
  }  
    /**  
 * The LocationEngineCallback interface's method which fires when the device's location has changed. * @param result the LocationEngineResult object which has the last known location within it.  
 */  @Override  
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
 * The LocationEngineCallback interface's method which fires when the device's location can not be captured * * @param exception the exception message  
 */  @Override  
  public void onFailure(@NonNull Exception exception) {  
        Log.e("LocationChangeActivity", exception.getLocalizedMessage());  
  MainActivity activity = activityWeakReference.get();  
 if (activity != null) {  
            Toast.makeText(activity, exception.getLocalizedMessage(),  
  Toast.LENGTH_SHORT).show();  
  }  
    }  
}
```

3. onstyleLoaded 메소드에 다음을 추가하여 맵이 로드되면 사용자의 위치가 맵에 표시되도록 한다.
```
enableLocationComponent(style);
```



---
# 유니티
## 안드로이드와 연동
## AR을 활용한 방향 표시
