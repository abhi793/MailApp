package in.main.mailapp;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;



public class EmailActivity extends AppCompatActivity {
    private TextView subject;
    private TextView senders;
    private TextView emailId;
    private TextView emailBody;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            String j =(String) b.get("Id");
            getMessage("http://127.0.0.1:8088/api/message/"+j);
        }

        subject = (TextView)findViewById(R.id.mailSubject);
        senders = (TextView)findViewById(R.id.mailSenders);
        emailId = (TextView)findViewById(R.id.emailid);
        emailBody = (TextView)findViewById(R.id.emailBody);




    }
    public void getMessage(String url)
    {


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {
                    subject.setText(response.getString("subject"));
                    emailBody.setText(response.getString("body"));
                    JSONArray genreArry = response.getJSONArray("participants");
                    String names = "";
                    String Emails = "";
                    for (int i = 0; i < genreArry.length(); i++)
                    {
                        JSONObject obj = genreArry.getJSONObject(i);
                        names+= obj.getString("name");
                        if(i<genreArry.length()-1)
                            names+=",";
                        Emails+= obj.getString("email");
                        if(i<genreArry.length()-1)
                               Emails+=",";
                    }
                    senders.setText(names);
                    emailId.setText(Emails);


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        RequestQueue requestQueue1 = Volley.newRequestQueue(EmailActivity.this);
        requestQueue1.add(jsonObjReq);
    }
}
