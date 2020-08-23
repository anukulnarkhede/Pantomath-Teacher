package com.cproz.pantomath_teacher.Notifications;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.cproz.pantomath_teacher.Home.DoubtDetails;
import com.cproz.pantomath_teacher.Home.HomeDoubtData;
import com.cproz.pantomath_teacher.R;
import com.jackandphantom.circularimageview.CircleImage;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder>{

    private Context mContext;
    private List<HomeDoubtData> DoubtList;

    public NotificationAdapter(Context mContext, List<HomeDoubtData> doubtList) {
        DoubtList = doubtList;
        this.mContext = mContext;
    }





    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_card, parent, false);
        return new NotificationViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final NotificationViewHolder holder, int position) {

        Date dateTime = DoubtList.get(position).getDateTime();

        assert dateTime != null;
        long mili = dateTime.getTime();

        final String datex = DateUtils.getRelativeTimeSpanString(mili).toString();

        holder.TimeTextNotification.setText(datex);

        holder.StudentTagNotifications.setText(DoubtList.get(position).getSTD().toString() + " " + DoubtList.get(position).getBoard());

        holder.NewQuestionTextNotifications.setText(DoubtList.get(position).getNameHome()+"\nhas asked a doubt.");

        if (DoubtList.get(position).getProfileImageURL().equals("")){
            holder.profileImage.setImageResource(R.drawable.personal_info);
        }else
            if (!DoubtList.get(position).getProfileImageURL().equals("")){
                Picasso.get().load(DoubtList.get(position).getProfileImageURL()).into(holder.profileImage);
            }


        //holder.StudentTagNotifications.setText(DoubtList.get(position).getSubject());

        if (DoubtList.get(position).getBoard().equals("SSC")&&DoubtList.get(position).getSTD().equals("9th")){
            holder.constraintLayout.setBackgroundResource(R.drawable.doubt_card_bg_physics);
            holder.StudentTagNotifications.setBackgroundResource(R.drawable.subject_button_bg_phy);
            holder.StudentTagNotifications.setTextColor(Color.parseColor("#0078FF"));
        }else
            if (DoubtList.get(position).getBoard().equals("SSC")&&DoubtList.get(position).getSTD().equals("10th")){
            holder.constraintLayout.setBackgroundResource(R.drawable.doubt_card_bg);
            holder.StudentTagNotifications.setBackgroundResource(R.drawable.subject_button_bg);
            holder.StudentTagNotifications.setTextColor(Color.parseColor("#FF2829"));
        }else
            if (DoubtList.get(position).getBoard().equals("CBSE")&&DoubtList.get(position).getSTD().equals("9th")){
                holder.constraintLayout.setBackgroundResource(R.drawable.doubt_card_bg_geom);
                holder.StudentTagNotifications.setBackgroundResource(R.drawable.subject_button_bg_geom);
                holder.StudentTagNotifications.setTextColor(Color.parseColor("#9A0D91"));
            }else
                if (DoubtList.get(position).getBoard().equals("CBSE")&&DoubtList.get(position).getSTD().equals("10th")){
                    holder.constraintLayout.setBackgroundResource(R.drawable.doubt_card_bg_geog);
                    holder.StudentTagNotifications.setBackgroundResource(R.drawable.subject_button_bg_geog);
                    holder.StudentTagNotifications.setTextColor(Color.parseColor("#009F37"));
                }

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DoubtDetails.class);
                intent.putExtra("QuestionImage1Url", DoubtList.get(holder.getAdapterPosition()).getPhoto1url());
                intent.putExtra("QuestionImage2Url", DoubtList.get(holder.getAdapterPosition()).getPhoto2url());
                intent.putExtra("AnsImage1Url", DoubtList.get(holder.getAdapterPosition()).getAnsPhotoUrl1());
                intent.putExtra("AnsImage2Url", DoubtList.get(holder.getAdapterPosition()).getAnsPhotoUrl2());
                intent.putExtra("AnsText", DoubtList.get(holder.getAdapterPosition()).getAnsText());
                intent.putExtra("AudioUrl", DoubtList.get(holder.getAdapterPosition()).getAudioUrl());
                intent.putExtra("QuestionText", DoubtList.get(holder.getAdapterPosition()).getQText());
                intent.putExtra("Board", DoubtList.get(holder.getAdapterPosition()).getBoard());
                intent.putExtra("Chapter", DoubtList.get(holder.getAdapterPosition()).getChapter());
                intent.putExtra("DateTime", datex);
                intent.putExtra("Email", DoubtList.get(holder.getAdapterPosition()).getEmailHome());
                intent.putExtra("FileUrl", DoubtList.get(holder.getAdapterPosition()).getFileUrl());
                intent.putExtra("Link", DoubtList.get(holder.getAdapterPosition()).getLink());
                intent.putExtra("Name", DoubtList.get(holder.getAdapterPosition()).getNameHome());
                intent.putExtra("ProfileImage", DoubtList.get(holder.getAdapterPosition()).getProfileImageURL());
                intent.putExtra("STD", DoubtList.get(holder.getAdapterPosition()).getSTD());
                intent.putExtra("Status", DoubtList.get(holder.getAdapterPosition()).getStatus());
                intent.putExtra("Subject", DoubtList.get(holder.getAdapterPosition()).getSubject());
                intent.putExtra("Teacher", DoubtList.get(holder.getAdapterPosition()).getTeacher());
                intent.putExtra("TeacherImage", DoubtList.get(holder.getAdapterPosition()).getTeacherImageUrl());
                intent.putExtra("Uid", DoubtList.get(holder.getAdapterPosition()).getUid());

                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return DoubtList.size();
    }
}




class NotificationViewHolder extends RecyclerView.ViewHolder{

    TextView NewQuestionTextNotifications, TimeTextNotification;
    Button StudentTagNotifications;
    ConstraintLayout constraintLayout;
    CircleImage profileImage;

    public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);
        Initialization(itemView);



    }

    public void Initialization(View view){
        NewQuestionTextNotifications = view.findViewById(R.id.NewDoubtTextNotification);
        TimeTextNotification = view.findViewById(R.id.timeTextNotification);
        StudentTagNotifications = view.findViewById(R.id.StudentTag_Notification);
        constraintLayout = view.findViewById(R.id.notification_card_bg);
        profileImage = view.findViewById(R.id.profilePicStu);
    }
}