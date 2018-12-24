package istanbul.codify.monju.utils;

/**
 * Created by egesert on 13.10.2018.
 */

        import android.content.Context;
        import android.content.SharedPreferences;
        import android.location.Location;
        import android.preference.PreferenceManager;
        import android.support.annotation.Nullable;
        import android.util.Log;

        import com.facebook.share.Share;

        import java.util.HashSet;
        import java.util.Set;

        import istanbul.codify.monju.MuudyApplication;


public class SharedPrefs {

    private static String APP_TAG = "MUUDY_SHARED";

    public static void setPlaceRecommendationNotificationPermission(Boolean permission,Context context){
        SharedPrefs.setValue("place_recommendation_permission",permission,context);
    }

    public static Boolean getPlaceRecommendationPermissionStatus(Context context){
        Boolean status = SharedPrefs.getValueBooleanDefaultTrue("place_recommendation_permission",context);
        return status;
    }


    public static void setLastTimeLocationSent(long time, Context context){
        SharedPrefs.setValue("last_time_location_sent",time+"",context);
    }

    public static long getLastTimeLocationSent(Context context){

        String lastTimeLocationSent = SharedPrefs.getValueString("last_time_location_sent",context);

        if(lastTimeLocationSent.equals("")){
            return 0;
        }else{
            return Long.parseLong(lastTimeLocationSent);

        }

    }

    public static void setLocation(Location loc, Context context){
        SharedPrefs.setValue("location_lat",loc.getLatitude()+"",context);
        SharedPrefs.setValue("location_long",loc.getLongitude()+"",context);
        SharedPrefs.setValue("location_prov",loc.getProvider(),context);

    }

    @Nullable
    public static Location getLocation(Context context){

        String lat = SharedPrefs.getValueString("location_lat",context);
        String lon = SharedPrefs.getValueString("location_long",context);
        String prov = SharedPrefs.getValueString("location_prov",context);


        Double mLat = 40.991955;
        Double mLon = 28.712913;
        if(lat.equals("")){

        }else{
            mLat = Double.parseDouble(lat);
            if(lon.equals("")){

            }else{
                mLon = Double.parseDouble(lon);
                Location loc = new Location(prov);
                loc.setLatitude(mLat);
                loc.setLongitude(mLon);
                return loc;
            }
        }

        return null;
    }

    public static void setLatitude(String lat,Context context){
        SharedPrefs.setValue("mLatitude",lat,context);
    }

    public static Double getLatitude(Context context){

        String lat = SharedPrefs.getValueString("mLatitude",context);


        Double mLat = 40.991955;

        if(lat.equals("")){

        }else{
            mLat = Double.parseDouble(lat);


        }

        return mLat;
    }

    public static void setLongitude(String lng,Context context){
        SharedPrefs.setValue("mLongitude",lng,context);
    }

    public static Double getLongitude(Context context){

        String lat = SharedPrefs.getValueString("mLongitude",context);


        Double mLat = 28.712913;
        if(lat.equals("")){

        }else{
            mLat = Double.parseDouble(lat);
        }

        return mLat;
    }



    public static void setValue(String key, String value, Context context) {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.apply();
            //Log.d(APP_TAG, "PreferencesDao - setValue(String keyParam, String valueParam) - Newly Setted Key/Value : " + key + "/" + value);
        } catch (Exception e) {
            Log.e(APP_TAG, "PreferencesDao - setValue(String keyParam, String valueParam) : " + e.toString());
        }
    }

    public static void setValue(String key, Boolean value,Context context) {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(key, value);
            editor.apply();
            //Log.d(APP_TAG, "PreferencesDao - setValue(String keyParam, Boolean valueParam) - Newly Setted Key/Value : " + key + "/" + value);
        } catch (Exception e) {
            Log.e(APP_TAG, "PreferencesDao - setValue(String keyParam, Boolean valueParam) : " + e.toString());
        }
    }

    public static void setValue(String key, Set<String> value) {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MuudyApplication.getAppContext());

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putStringSet(key, value);
            editor.apply();
            //Log.d(APP_TAG, "PreferencesDao - setValue(String keyParam, HashSet<String> valueParam) - Newly Setted Key/Value : " + key + "/" + value);
        } catch (Exception e) {
            Log.e(APP_TAG, "PreferencesDao - setValue(String keyParam, HashSet<String> valueParam) : " + e.toString());
        }
    }

    public static void setValue(String key, int value) {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MuudyApplication.getAppContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(key, value);
            editor.apply();
            //Log.d(APP_TAG, "PreferencesDao - setValue(String keyParam, int valueParam) - Newly Setted Key/Value : " + key + "/" + value);
        } catch (Exception e) {
            Log.e(APP_TAG, "PreferencesDao - setValue(String keyParam, int valueParam) : " + e.toString());
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String getValueString(String key, Context context) {
        SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor mPrefsEditor = mSharedPrefs.edit();
        String value = mSharedPrefs.getString(key, "");
        mPrefsEditor.apply();
        //Log.d(APP_TAG, "PreferencesDao - String getValueString(String keyParam) - Got Key/Value : " + key + "/" + value);
        return value;
    }

    public static Boolean getValueBoolean(String key,Context context) {
        SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor mPrefsEditor = mSharedPrefs.edit();
        Boolean value = mSharedPrefs.getBoolean(key, false);
        mPrefsEditor.apply();
        //Log.d(APP_TAG, "PreferencesDao - Boolean getValueBoolean(String keyParam) - Got Key/Value : " + key + "/" + value);
        return value;
    }

    public static Boolean getValueBooleanDefaultTrue(String key,Context context) {
        SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor mPrefsEditor = mSharedPrefs.edit();
        Boolean value = mSharedPrefs.getBoolean(key, true);
        mPrefsEditor.apply();
        //Log.d(APP_TAG, "PreferencesDao - Boolean getValueBoolean(String keyParam) - Got Key/Value : " + key + "/" + value);
        return value;
    }

    public static Set<String> getListValue(String key) {
        SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(MuudyApplication.getAppContext());
        SharedPreferences.Editor mPrefsEditor = mSharedPrefs.edit();
        Set<String> value = mSharedPrefs.getStringSet(key, new HashSet<String>());
        mPrefsEditor.apply();
        //Log.d(APP_TAG, "PreferencesDao - HashSet<String> getValueString(String keyParam) - Got Key/Value : " + key + "/" + value);
        return value;
    }

    public static int getValueInt(String key) {
        int value = 0;
        try {
            SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(MuudyApplication.getAppContext());
            SharedPreferences.Editor mPrefsEditor = mSharedPrefs.edit();
            value = mSharedPrefs.getInt(key, 0);
            mPrefsEditor.apply();
            //Log.d(APP_TAG, "PreferencesDao - int getValueString(String keyParam) - Got Key/Value : " + key + "/" + value);
        } catch (Exception e) {
            Log.e(APP_TAG, "PreferencesDao - int getValueString(String keyParam) : " + e.toString());
        }
        return value;
    }

    public static void removeValue(String key) {
        try {
            SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(MuudyApplication.getAppContext());
            mSharedPrefs.edit().remove(key).apply();
            //Log.d(APP_TAG, "PreferencesDao - removeValue(String keyParam) - Removed Key : " + key);
        } catch (Exception e) {
            Log.e(APP_TAG, "PreferencesDao - removeValue() : " + e.toString());
        }
    }

}
