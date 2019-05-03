package com.fidelspike2;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;

import com.fidel.sdk.Fidel;
import com.fidel.sdk.LinkResult;

import org.json.JSONException;
import org.json.JSONObject;


public class FidelModule extends ReactContextBaseJavaModule {

    private Promise jsReturnPromise;

    public WritableMap cardData = new WritableNativeMap();

    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {

        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
            super.onActivityResult(activity, requestCode, resultCode, data);

            if(requestCode == Fidel.FIDEL_LINK_CARD_REQUEST_CODE) {
                if (jsReturnPromise != null) {
                    if (data != null && data.hasExtra(Fidel.FIDEL_LINK_CARD_RESULT_CARD)) {
                        LinkResult card = (LinkResult) data.getParcelableExtra(Fidel.FIDEL_LINK_CARD_RESULT_CARD);
                        try {
                            cardData.putString("id", card.id);
                            cardData.putString("created", card.created);
                            cardData.putString("updated", card.updated);
                            cardData.putString("type", card.type);
                            cardData.putString("scheme", card.scheme);
                            cardData.putString("programId", card.programId);
                            cardData.putBoolean("mapped", card.mapped);
                            cardData.putBoolean("live", card.live);
                            cardData.putString("lastNumbers", card.lastNumbers);
                            cardData.putInt("expYear", card.expYear);
                            cardData.putInt("expMonth", card.expMonth);
                            cardData.putString("expDate", card.expDate);
                            cardData.putString("countryCode", card.countryCode);
                            cardData.putString("accountId", card.accountId);
                            if (card.metaData != null) {
                                String userID = card.metaData.getString("userID");
                                WritableMap metaData = new WritableNativeMap();
                                metaData.putString("userID", userID);
                                cardData.putMap("metaData", metaData);
                            }
                            jsReturnPromise.resolve(cardData);
                        } catch (Exception e) {
                            Log.e("Error with JSON", e.getLocalizedMessage());
                            jsReturnPromise.reject("Error with JSON", e.getLocalizedMessage());
                        }
                    }
                    jsReturnPromise = null;
                }
            }
        }
    };


    public FidelModule(ReactApplicationContext reactContext) {
        super(reactContext);
        final ReactApplicationContext ctx = reactContext;

        reactContext.addActivityEventListener(mActivityEventListener);

        Fidel.apiKey = BuildConfig.FidelApiKey;
        Fidel.programId = BuildConfig.FidelProgramId;

        return;
    }

    @Override
    public String getName() {
        return "Fidel";
    }

    @ReactMethod
    public void setMetaData(String userId) {

        JSONObject userID = new JSONObject();
        try {
            userID.put("userID", userId);
        }
        catch(JSONException e) {
            Log.e(Fidel.FIDEL_DEBUG_TAG, e.getLocalizedMessage());
        }

        try {
            Fidel.metaData = userID;
            return;
        }
        catch (IllegalArgumentException ex){
        }
    }

    @ReactMethod
    public void present(final Promise promise) {
        Activity currentActivity = getCurrentActivity();

        if (currentActivity == null) {
            promise.reject("Activity error", "Activity doesn't exist");
            return;
        }

        // Store the promise to resolve/reject when fidelSDK returns data
        jsReturnPromise = promise;

        try {
            Fidel.present(currentActivity);
        } catch (Exception e) {
            jsReturnPromise.reject("Error with Fidel", e);
            jsReturnPromise = null;
        }
    }
}
