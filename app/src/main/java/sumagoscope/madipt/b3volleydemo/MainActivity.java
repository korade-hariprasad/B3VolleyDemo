package sumagoscope.madipt.b3volleydemo;

import android.content.Intent;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Product> productList;
    FloatingActionButton fabLogout;
    FirebaseAuth mAuth;
    FirebaseUser user;
    TextView tvUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        tvUsername = findViewById(R.id.tvUsername);
        fabLogout = findViewById(R.id.fabLogout);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!=null) user = mAuth.getCurrentUser();
        tvUsername.setText(user.getEmail());
        /*
        Log.d("debug", user.getDisplayName());
        Log.d("debug", user.getPhoneNumber());
        */

        productList = new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2, LinearLayoutManager.VERTICAL, false));
        getDataFromServer();
        fabLogout.setOnClickListener(v->{
            mAuth.signOut();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        });
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