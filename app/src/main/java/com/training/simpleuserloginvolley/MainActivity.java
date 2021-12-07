package com.training.simpleuserloginvolley;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;

    private String baseUrl = "https://40968f05-5a2b-492f-8ef2-a8d667146b83.mock.pstmn.io";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = VolleySingleton.getmInstance(this).getRequestQueue();

        final EditText emailField = findViewById(R.id.txtEmail);
        EditText passwordField = findViewById(R.id.txtPwd);
        Button btnLogin = findViewById(R.id.btnLogin);

        TextView register = (TextView) findViewById(R.id.lnkRegister);
        register.setMovementMethod(LinkMovementMethod.getInstance());

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

//                Toast.makeText(MainActivity.this, "Email: " + email + " Password:" + password, Toast.LENGTH_LONG).show();
                postData(email, password);

//                getData();
            }
        });
    }

    // Post Request For JSONObject
    public void postData(String email, String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("title", email);
            object.put("body", "password");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
//        String url = baseUrl + "/api/login";
        String url = "https://jsonplaceholder.typicode.com/posts";
        CustomJsonObjectRequest jsonObjectRequest = new CustomJsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "String Response: " + response.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("JsonObjectRequest", "String Response: " + response.toString());
//                        resultTextView.setText("String Response : "+ response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error getting response", Toast.LENGTH_LONG).show();
                Log.d("JsonObjectRequest", "String Response: " + error.getLocalizedMessage());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    // Get Request For JSONObject
    public void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        try {
            String url = "https://jsonplaceholder.typicode.com/users";
            JSONObject object = new JSONObject();
            CustomJsonObjectRequest jsonObjectRequest = new CustomJsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
//                    resultTextView.setText("Response : " + response.toString());
                    Toast.makeText(getApplicationContext(), "I am OK !" + response.toString(), Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error: " + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class CustomJsonObjectRequest extends JsonObjectRequest {

        public CustomJsonObjectRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
            super(method, url, jsonRequest, listener, errorListener);
        }

        public CustomJsonObjectRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
            super(url, jsonRequest, listener, errorListener);
        }

        @Override
        protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
            try {
                String jsonString = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers));

                JSONObject result = null;

                if (jsonString != null && jsonString.length() > 0)
                    result = new JSONObject(jsonString);

                return Response.success(result,
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JSONException je) {
                return Response.error(new ParseError(je));

            }
        }
    }
}