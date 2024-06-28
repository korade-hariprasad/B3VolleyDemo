package sumagoscope.madipt.b3volleydemo;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView tvResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResponse=findViewById(R.id.tvResponse);
        getDataFromServer();
    }

    private void getDataFromServer() {

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        String url="https://dummyjson.com/products?select=title,price,thumbnail,category";

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("mytag",response);

                        tvResponse.setText(response);

                        try {
                            JSONObject mainObject=new JSONObject(response);
                            JSONArray jsonArray=mainObject.getJSONArray("products");

                            for(int i=0;i<jsonArray.length();i++)
                            {

                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                Log.d("mytag",jsonObject.getString("title"));
                                Log.d("mytag",""+jsonObject.getDouble("price"));

                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("mytag","onErrorResponse => "+error.getMessage());
            }
        }
        );
        requestQueue.add(stringRequest);
    }

    public  void getListFromServer(){

        RequestQueue requestQueue=Volley.newRequestQueue(MainActivity.this);
        String url="https://dummyjson.com/products?select=title,price,thumbnail,category";

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("mytag",response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("mytag",error.getMessage());

            }
        });

        requestQueue.add(stringRequest);
    }
}