package com.cproz.pantomath_teacher.TeacherProfile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.cproz.pantomath_teacher.Home.DoubtDetails;
import com.cproz.pantomath_teacher.Home.HomeDoubtData;
import com.cproz.pantomath_teacher.R;
import com.jackandphantom.circularimageview.RoundedImage;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

public class ProfileDoubtsAdapter extends RecyclerView.Adapter<ProfileDoubtViewHolder> {

    public ProfileDoubtsAdapter(Context mContext, List<HomeDoubtData> doubtList) {
        this.mContext = mContext;
        DoubtList = doubtList;
    }

    private Context mContext;
    public List<HomeDoubtData> DoubtList ;


    @NonNull
    @Override
    public ProfileDoubtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.teacher_profile_doubt_card, parent, false);
        return new ProfileDoubtViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ProfileDoubtViewHolder holder, int position) {






        if (DoubtList.get(position).getPhoto1url().equals("")){
            holder.roundedImage.setVisibility(View.GONE);

        }else{
            Picasso.get().load(DoubtList.get(position).getPhoto1url()).into(holder.roundedImage);
        }

        holder.QuestionText.setText(DoubtList.get(position).getQText());


        holder.subTag.setText(DoubtList.get(position).getSTD() + " " + DoubtList.get(position).getBoard() );

        if (DoubtList.get(position).getBoard().equals("SSC")&&DoubtList.get(position).getSTD().equals("9th")){

            holder.subTag.setBackgroundResource(R.drawable.subject_button_bg_phy);
            holder.subTag.setTextColor(Color.parseColor("#0078FF"));
            holder.constraintLayout.setBackgroundResource(R.drawable.smart_suggestion_card_bg);
            holder.solved.setBackgroundResource(R.drawable.square_small_bg);
            if (DoubtList.get(position).getStatus().equals("Unsolved")){
                holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_grey);
                holder.solved.setBackgroundResource(R.drawable.square_small_bg_grey);
            }
            else if (DoubtList.get(position).getStatus().equals("Solved")){
                holder.solved.setImageResource(R.drawable.ic_round_check_circle_24);
            }


        }else if (DoubtList.get(position).getBoard().equals("SSC")&&DoubtList.get(position).getSTD().equals("10th")){

            holder.subTag.setBackgroundResource(R.drawable.subject_button_bg);
            holder.subTag.setTextColor(Color.parseColor("#FF2829"));
            holder.constraintLayout.setBackgroundResource(R.drawable.smart_suggestion_card_bg_alg);
            holder.solved.setBackgroundResource(R.drawable.square_small_bg_alg);
            if (DoubtList.get(position).getStatus().equals("Unsolved")){
                holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_grey);
                holder.solved.setBackgroundResource(R.drawable.square_small_bg_grey);
            }
            else if (DoubtList.get(position).getStatus().equals("Solved")){
                holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_alg);
            }


        }else if (DoubtList.get(position).getBoard().equals("CBSE")&&DoubtList.get(position).getSTD().equals("9th")){

            holder.subTag.setBackgroundResource(R.drawable.subject_button_bg_geom);
            holder.subTag.setTextColor(Color.parseColor("#9A0D91"));
            holder.constraintLayout.setBackgroundResource(R.drawable.smart_suggestion_card_bg_geom);
            holder.solved.setBackgroundResource(R.drawable.square_small_bg_geom);
            if (DoubtList.get(position).getStatus().equals("Unsolved")){
                holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_grey);
                holder.solved.setBackgroundResource(R.drawable.square_small_bg_grey);
            }
            else if (DoubtList.get(position).getStatus().equals("Solved")){
                holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_geom);
            }


        }else if (DoubtList.get(position).getBoard().equals("CBSE")&&DoubtList.get(position).getSTD().equals("10th")){

            holder.subTag.setBackgroundResource(R.drawable.subject_button_bg_geog);
            holder.subTag.setTextColor(Color.parseColor("#009F37"));
            holder.constraintLayout.setBackgroundResource(R.drawable.smart_suggestion_card_bg_geog);
            holder.solved.setBackgroundResource(R.drawable.small_squar_bg_geog);
            if (DoubtList.get(position).getStatus().equals("Unsolved")){
                holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_grey);
                holder.solved.setBackgroundResource(R.drawable.square_small_bg_grey);
            }
            else if (DoubtList.get(position).getStatus().equals("Solved")){
                holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_geog);
            }

        }


        Date dateTime = DoubtList.get(position).getDateTime();
        assert dateTime != null;
        long mili = dateTime.getTime();

        final String datex = DateUtils.getRelativeTimeSpanString(mili).toString();



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


class ProfileDoubtViewHolder extends RecyclerView.ViewHolder{


    RoundedImage roundedImage;
    TextView QuestionText;
    Button subTag;
    ImageView solved;
    ConstraintLayout constraintLayout;


    public ProfileDoubtViewHolder(@NonNull View itemView) {
        super(itemView);



        roundedImage = itemView.findViewById(R.id.roundedImageSScard);
        QuestionText = itemView.findViewById(R.id.QuestionTextSScard);
        subTag = itemView.findViewById(R.id.subjectTagSScard);
        solved = itemView.findViewById(R.id.solvedSScard);
        constraintLayout  = itemView.findViewById(R.id.SmartSuggestionCard);


    }
}