package edu.dartmouthcs65.museumtour.RoomKit;

import com.google.gson.JsonPrimitive;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class Classifier {
    public interface Listener {
        public void onClassify(Integer roomIndex, String room);
    }
    private Listener listener;

    public Classifier(Listener listener) {
        this.listener = listener;
    }

    public void classifiy(Map<String, Double> beacons, String map_id) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("authorization", "29ece557ad5f54dc1b813d56d53e7ac60dc7da1417734079b0");
        RequestParams params = new RequestParams(beacons);
        client.post("https://roomkit.herokuapp.com/maps/" + map_id, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    int roomIndex = (Integer) response.get("roomIndex");
                    String room = (String) response.get("room");
                    listener.onClassify(roomIndex, room);
                } catch (JSONException exp) {

                }
            }
        });
    }
}
