package sumagoscope.madipt.b3volleydemo;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        productList = new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2, LinearLayoutManager.VERTICAL, false));
        getDataFromServer();
    }

    private void getDataFromServer() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://dummyjson.com/products?select=title,price,thumbnail,category";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("mytag", response);
                try {
                    JSONObject mainObject = new JSONObject(response);
                    JSONArray jsonArray = mainObject.getJSONArray("products");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Product product = new Product();
                        product.setTitle(jsonObject.getString("title"));
                        product.setThumbnail(jsonObject.getString("thumbnail"));
                        product.setCategory(jsonObject.getString("category"));
                        product.setPrice(jsonObject.getDouble("price"));
                        productList.add(product);
                    }
                    recyclerView.setAdapter(new ProductAdapter(productList));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("mytag", "onErrorResponse => " + error.getMessage());
            }
        });
        requestQueue.add(stringRequest);
    }
}