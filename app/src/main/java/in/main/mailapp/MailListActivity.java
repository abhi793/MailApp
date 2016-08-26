package in.main.mailapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MailListActivity extends AppCompatActivity {
    private List<MailModel> mailList;
    private ListView listView;
    private CustomAdapter adapter;
    private String getListUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_list);

        getListUrl = getResources().getString(R.string.list_url);
        getEmailList("http://10.0.0.8:8088/api/message");

        mailList = new ArrayList<MailModel>();
        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomAdapter(this, mailList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MailModel mail = mailList.get(i);
                Intent intent = new Intent(MailListActivity.this, EmailActivity.class);
                intent.putExtra("Id", mail.getId());
                startActivity(intent);
                mail.setRead(true);
                View rowView = view;
                TextView sender = (TextView) rowView.findViewById(R.id.senderName);
                sender.setTypeface(null, Typeface.NORMAL);
                TextView subject = (TextView) rowView.findViewById(R.id.subject);
                subject.setTypeface(null, Typeface.NORMAL);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                MailModel mail = mailList.get(i);
                buildAlertMessageDelete(mail.getId());
                return true;
            }
        });


    }

    public  void getEmailList(String url)
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Downloading...");
        progressDialog.show();

        JsonArrayRequest emailReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        progressDialog.dismiss();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                MailModel mailModel = new MailModel();
                                mailModel.setSubject(obj.getString("subject"));
                                mailModel.setBody(obj.getString("preview"));
                                mailModel.setId(obj.getString("id"));
                                mailModel.setStarred(obj.getBoolean("isStarred"));
                                mailModel.setRead(obj.getBoolean("isRead"));

                                JSONArray participantsArry = obj.getJSONArray("participants");
                                ArrayList<String> participants = new ArrayList<String>();
                                for (int j = 0; j < participantsArry.length(); j++) {
                                    participants.add((String) participantsArry.get(j));
                                }
                                mailModel.setParticipants(participants);

                                // adding movie to movies array
                                mailList.add(mailModel);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

            }
        });

        RequestQueue requestQueue1 = Volley.newRequestQueue(MailListActivity.this);
        requestQueue1.add(emailReq);
    }
    private void buildAlertMessageDelete(String id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this msg?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick( final DialogInterface dialog, final int id) {
                        dialog.cancel();
                      deleteMessage("http://10.0.0.8:8088/api/message"+id);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    public void deleteMessage(String url)
    {
        StringRequest delete = new StringRequest(Request.Method.DELETE, url,
                   new Response.Listener<String>()
            {
                @Override
               public void onResponse(String response) {
                        // response
                       Toast.makeText(MailListActivity.this, response, Toast.LENGTH_LONG).show();
                    adapter.notifyDataSetChanged();
                }
            },
            new Response.ErrorListener()
            {
                 @Override
                 public void onErrorResponse(VolleyError error) {
                         // error.

                    }
            });
        RequestQueue requestQueue1 = Volley.newRequestQueue(MailListActivity.this);
        requestQueue1.add(delete);
    }
}
