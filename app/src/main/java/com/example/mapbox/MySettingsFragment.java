
package com.example.mapbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.Locale;

public class MySettingsFragment extends PreferenceFragmentCompat {
    Activity activity;                      //생명주기 설정을 위한 Activty 변수
    static public SharedPreferences prefs;  //모든 환경변수 공유 변수

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference, null);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences sp, String key) {   //환경설적 클릭 리스너
                if(prefs.getString("language", "").equals("ko")){
                    changeLang("ko");
                }else if(prefs.getString("language", "").equals("en")){
                    changeLang("en");
                }else if(prefs.getString("language", "").equals("zh")){
                    changeLang("zh");
                }
            }});
    }

    //환경설정 한국어, 영어, 일본어 클릭시 언어 바꿔주는 메서드
    public void changeLang(String str){
        Locale locale = new Locale(str);   //입력받은 언어 value저장
        try {
            MainActivity.config = activity.getResources().getConfiguration();  //Mainactivity의 configuration에 현재 configuration 저장
            MainActivity.config.setLocale(locale);                             //Mainactivity의 configuration에 저장되어 있는 언어를 변경된 언어로 변경

            //변경된 setLocale을 업데이트 하는 코드
            activity.getResources().updateConfiguration(MainActivity.config, activity.getResources().getDisplayMetrics());

            //preferences창들에 있는 것들을 편집하는 코드
            //sharedpreferences를 생성
            SharedPreferences.Editor editor = prefs.edit();

            //sharedpreferences 안에 선택한 locale값 넣기
            editor.putString("locale", MainActivity.locale);

            // 저장
            editor.commit();

            // 어플 재시작
            Intent intent = new Intent(activity, MainActivity.class);
            //activity.getPackageManager().getLaunchIntentForPackage(activity.getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().finish();
            startActivity(intent);

        }catch(NullPointerException e){
        }
    }

    //생명주기로인해 activity null반환 현상 해결하기위한 코드
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity)
            activity = (Activity) context;
    }
    //선택한 언어에 맞는 String값을 반환하는 코드 (휴대폰 버전에 따른 언어변경시 필요)
    /*@NonNull
    public static String getStringByLocal(Activity context, int resId, String locale) {
        //버전에 따라서 언어를 설정해주는 방식이 다르기 때문에 분류해서 사용합니다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            return getStringByLocalPlus17(context, resId, locale);
        else return getStringByLocalBefore17(context, resId, locale); }

    @NonNull
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static String getStringByLocalPlus17(Activity context, int resId, String locale) {
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(new Locale(locale));
        return context.createConfigurationContext(configuration).getResources().getString(resId);
    }
    //젤리빈 버전 이하일 경우
    private static String getStringByLocalBefore17(Context context, int resId, String language) {
        Resources currentResources = context.getResources();
        AssetManager assets = currentResources.getAssets();
        DisplayMetrics metrics = currentResources.getDisplayMetrics();
        Configuration config = new Configuration(currentResources.getConfiguration());
        Locale locale = new Locale(language); Locale.setDefault(locale);
        config.locale = locale; // Note: This (temporarily) changes the devices locale!
        //TODO find a  better way to get the string in the specific locale
        Resources defaultLocaleResources = new Resources(assets, metrics, config);
        String string = defaultLocaleResources.getString(resId);
        // Restore device-specific locale
        new Resources(assets, metrics, currentResources.getConfiguration()); return string; }*/

}
