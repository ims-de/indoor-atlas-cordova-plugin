package com.ialocation.plugin;

import java.util.Arrays;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;

import android.util.Log;

class Utils
{
    public static JSONArray reactToJSON(ReadableArray readableArray) throws JSONException
    {
        JSONArray jsonArray = new JSONArray();

        for (int i=0, l=readableArray.size(); i<l; i++) {
            ReadableType valueType = readableArray.getType(i);

            switch (valueType){
                case Null:
                    jsonArray.put(JSONObject.NULL);
                    break;

                case Boolean:
                    jsonArray.put(readableArray.getBoolean(i));
                    break;

                case Number:
                    jsonArray.put(readableArray.getDouble(i));
                    break;

                case String:
                    jsonArray.put(readableArray.getString(i));
                    break;

                case Map:
                    jsonArray.put(reactToJSON(readableArray.getMap(i)));
                    break;

                case Array:
                    jsonArray.put(reactToJSON(readableArray.getArray(i)));
                    break;
            }
        }

        return jsonArray;
    }

    public static JSONObject reactToJSON(ReadableMap readableMap) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        ReadableMapKeySetIterator iterator = readableMap.keySetIterator();
        while(iterator.hasNextKey()){
            String key = iterator.nextKey();
            ReadableType valueType = readableMap.getType(key);
            switch (valueType){
                case Null:
                    jsonObject.put(key,JSONObject.NULL);
                    break;
                case Boolean:
                    jsonObject.put(key, readableMap.getBoolean(key));
                    break;
                case Number:
                    jsonObject.put(key, readableMap.getDouble(key));
                    break;
                case String:
                    jsonObject.put(key, readableMap.getString(key));
                    break;
                case Map:
                    jsonObject.put(key, reactToJSON(readableMap.getMap(key)));
                    break;
                case Array:
                    jsonObject.put(key, reactToJSON(readableMap.getArray(key)));
                    break;
            }
        }

        return jsonObject;
    }

    public static WritableMap jsonToReact(JSONObject jsonObject) throws JSONException
    {
        WritableMap writableMap = Arguments.createMap();
        Iterator iterator = jsonObject.keys();

        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            Object value = jsonObject.get(key);

            if (value instanceof Float || value instanceof Double) {
                writableMap.putDouble(key, jsonObject.getDouble(key));
            } else if (value instanceof Number) {
                writableMap.putInt(key, jsonObject.getInt(key));
            } else if (value instanceof String) {
                writableMap.putString(key, jsonObject.getString(key));
            } else if (value instanceof JSONObject) {
                writableMap.putMap(key, jsonToReact(jsonObject.getJSONObject(key)));
            } else if (value instanceof JSONArray){
                writableMap.putArray(key, jsonToReact(jsonObject.getJSONArray(key)));
            } else if (value == JSONObject.NULL){
                writableMap.putNull(key);
            }
        }

        return writableMap;
    }

    public static WritableArray jsonToReact(JSONArray jsonArray) throws JSONException {
        WritableArray writableArray = Arguments.createArray();
        for(int i=0; i < jsonArray.length(); i++) {
            Object value = jsonArray.get(i);
            if (value instanceof Float || value instanceof Double) {
                writableArray.pushDouble(jsonArray.getDouble(i));
            } else if (value instanceof Number) {
                writableArray.pushInt(jsonArray.getInt(i));
            } else if (value instanceof String) {
                writableArray.pushString(jsonArray.getString(i));
            } else if (value instanceof JSONObject) {
                writableArray.pushMap(jsonToReact(jsonArray.getJSONObject(i)));
            } else if (value instanceof JSONArray){
                writableArray.pushArray(jsonToReact(jsonArray.getJSONArray(i)));
            } else if (value == JSONObject.NULL){
                writableArray.pushNull();
            }
        }
        return writableArray;
    }

    public static float[] getFloatValues(JSONArray floats, CallbackContext callbackContext) throws JSONException
    {
        if (floats.length() != 16) {
            callbackContext.error("Argument must be an array with exactly 16 floats");
        }

        float[] values = new float[16];

        for (int i = 0; i<16; i++) {
            values[i] = (float) floats.getDouble(i);
        }

        Log.d("IndoorAtlasUtils", "Input: " + floats);
        Log.d("IndoorAtlasUtils", "Output: " + Arrays.toString(values));

        return values;
    }

    public static float[] getFloatValues(ReadableArray floats, Callback error)
    {
        if (floats.size() != 16) {
            error.invoke("Argument must be an array with exactly 16 floats");
        }

        float[] values = new float[16];

        for (int i = 0; i<16; i++) {
            values[i] = (float) floats.getDouble(i);
        }

        Log.d("IndoorAtlasUtils", "Input: " + floats);
        Log.d("IndoorAtlasUtils", "Output: " + Arrays.toString(values));

        return values;
    }
}
