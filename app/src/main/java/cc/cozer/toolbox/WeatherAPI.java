package cc.cozer.toolbox;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.aigestudio.wheelpicker.WheelPicker;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by soneh on 2016/10/11.
 */

public class WeatherAPI extends AppCompatActivity implements WheelPicker.OnItemSelectedListener{
    private EditText editText;
    private Button button;
    private TextView textView;
    public String cityname;
    List<String> citynamepicker = new ArrayList<>( );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final RequestQueue mQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.weather_data);
        citynamepicker.add("襄阳");
        citynamepicker.add("西安");
        citynamepicker.add("广州");
        citynamepicker.add("上海");
        citynamepicker.add("北京");
        citynamepicker.add("洛阳");
        citynamepicker.add("郑州");

       WheelPicker wheelPicker = (WheelPicker) findViewById(R.id.citypicker);
        wheelPicker.setData(citynamepicker);
        wheelPicker.setOnItemSelectedListener(this);


        editText = (EditText) findViewById(R.id.city_name);
        button = (Button) findViewById(R.id.getweather);
        textView = (TextView) findViewById((R.id.result));

        button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        String inputText = editText.getText().toString();
                        cityname = inputText;
                        mQueue.add(stringRequest);
                }
            });
    }

    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {
        editText.setText(String.valueOf(data));

    }


    public static final String APPKEY = "5957ec3654a719abd0e0362946aaefbf";

    StringRequest stringRequest = new StringRequest(Request.Method.POST,"http://op.juhe.cn/onebox/weather/query",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("TAG",response);
                    Gson gson = new Gson();
                    //List<WeatherData> weather =  gson.fromJson(response, new TypeToken<List<WeatherData>>(){}.getType());
                    //for (WeatherData weatherData : weather) {
                       // textView.setText("cityname is "+ weatherData.getCityname());

                    //}
                    WeatherData weatherData = gson.fromJson(response, WeatherData.class);
                    String realtimeweather = weatherData.getResult().getData().getRealtime().getCity_name()+" "+
                            weatherData.getResult().getData().getRealtime().getDate()+" "+
                            weatherData.getResult().getData().getRealtime().getWeather().getTemperature()+"℃ "+
                            weatherData.getResult().getData().getRealtime().getWeather().getInfo();
                    textView.setText(realtimeweather);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error){
                    Log.e("TAG", error.getMessage(), error);
                }

    }) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> map = new HashMap<String, String>();
            map.put("cityname", cityname);
            map.put("key", "5957ec3654a719abd0e0362946aaefbf");
            map.put("dype", "json");
            return map;
        }
        //将map型转为请求参数型
        public String urlencode(Map<String,String> map) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry i : map.entrySet()) {
                try {
                    sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }
    };






 }

