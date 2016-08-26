package in.main.mailapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Abhishek Pc on 20-08-2016.
 */
public class CustomAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<MailModel> mailModel;

    public CustomAdapter(Activity activity, List<MailModel> mailModel) {
        this.activity = activity;
        this.mailModel = mailModel;
    }

    @Override
    public int getCount() {
        return mailModel.size();
    }

    @Override
    public Object getItem(int location) {
        return mailModel.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_item, null);


        TextView participants = (TextView) convertView.findViewById(R.id.senderName);
        TextView subject = (TextView) convertView.findViewById(R.id.subject);
        TextView body = (TextView) convertView.findViewById(R.id.mailBody);
        final ImageView isStarred = (ImageView) convertView.findViewById(R.id.starred);



      final   MailModel m = mailModel.get(position);

        isStarred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(m.getStarred()==false) {
                    isStarred.setImageResource(R.drawable.gold_star_icon);
                    m.setStarred(true);
                }
                else
                {
                    isStarred.setImageResource(R.drawable.star);
                    m.setStarred(false);
                }
            }
        });
        String participantsStr = "";
        for (String str : m.getParticipants()) {
            if(m.getParticipants().size()>1)
            participantsStr += str + ", ";
            else
                participantsStr = str;
        }
        participants.setText(participantsStr);
        subject.setText(m.getSubject());
        body.setText(m.getBody());


        return convertView;
    }

}
