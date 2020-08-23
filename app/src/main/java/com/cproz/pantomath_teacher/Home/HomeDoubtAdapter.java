package com.cproz.pantomath_teacher.Home;



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
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.cproz.pantomath_teacher.R;

import com.cproz.pantomath_teacher.Solve.Solve;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeDoubtAdapter extends RecyclerView.Adapter<HomeDoubtViewHolder> {




    private Context mContext;
    private List<HomeDoubtData> DoubtList;

    public HomeDoubtAdapter(Context mContext, List<HomeDoubtData> doubtList){

        this.mContext = mContext;
        DoubtList = doubtList;
        notifyDataSetChanged();

    }


    @NonNull
    @Override
    public HomeDoubtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.doubt_card_home, parent, false);
        return new HomeDoubtViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final HomeDoubtViewHolder holder, int position) {

        String [] imageUrls = new String[0];





        holder.UserName.setText(toTitleCase(DoubtList.get(position).getNameHome()));

        holder.questionText.setText(DoubtList.get(position).getQText());

        Date dateTime = DoubtList.get(position).getDateTime();

        assert dateTime != null;
        long mili = dateTime.getTime();

        final String datex = DateUtils.getRelativeTimeSpanString(mili).toString();

        holder.DateText.setText(datex);


        String AnsText = DoubtList.get(position).getAnsText();
        String AnsPhotoUrl1 = DoubtList.get(position).getAnsPhotoUrl1(), AnsPhotoUrl2 = DoubtList.get(position).getAnsPhotoUrl2(), FileUrl = DoubtList.get(position).getFileUrl(),
                Link = DoubtList.get(position).getLink(), Board = DoubtList.get(position).getBoard(), AudioUrl = DoubtList.get(position).getAudioUrl(),
                Chapter = DoubtList.get(position).getChapter(), Email = DoubtList.get(position).getEmailHome(), STD = DoubtList.get(position).getSTD(), Teacher = DoubtList.get(position).getTeacher(),
                Uid = DoubtList.get(position).getUid();



        if (!DoubtList.get(position).getProfileImageURL().equals("")){
            Picasso.get().load(DoubtList.get(position).getProfileImageURL()).into(holder.profilePictureDC);
        }
        else if (DoubtList.get(position).getProfileImageURL().equals(""))
        {
            holder.profilePictureDC.setImageResource(R.drawable.personal_info);

        }

        if (DoubtList.get(position).getPhoto1url().equals("") && DoubtList.get(position).getPhoto2url().equals("")){
            holder.imageHolder.setVisibility(View.GONE);
            holder.linearLayout.setVisibility(View.GONE);

        }
        else if (!DoubtList.get(position).getPhoto1url().equals("") && DoubtList.get(position).getPhoto2url().equals("")){
            holder.imageHolder.setVisibility(View.VISIBLE);
            imageUrls = new String[]{
                    DoubtList.get(position).getPhoto1url()

            };


            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(mContext, imageUrls);
            holder.imageHolder.setAdapter(viewPagerAdapter);
            holder.linearLayout.setVisibility(View.GONE);
        }

        else if (!DoubtList.get(position).getPhoto1url().equals("") && !DoubtList.get(position).getPhoto2url().equals("")){
            holder.imageHolder.setVisibility(View.VISIBLE);
            imageUrls = new String[]{
                    DoubtList.get(position).getPhoto1url(), DoubtList.get(position).getPhoto2url()

            };

            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(mContext, imageUrls);
            holder.imageHolder.setAdapter(viewPagerAdapter);
            holder.linearLayout.setVisibility(View.VISIBLE);

        }

        holder.imageHolder.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    holder.linearLayout.setBackgroundResource(R.drawable.ic_dot_first_photo);
                }
                else if (position == 1){
                    holder.linearLayout.setBackgroundResource(R.drawable.ic_dots_second_photo);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        holder.subjectTag.setText(DoubtList.get(position).getSubject());



        if (DoubtList.get(position).getSTD().equals("9th")&&DoubtList.get(position).getBoard().equals("SSC")){
            holder.CardBg.setBackgroundResource(R.drawable.doubt_card_bg_physics);
            holder.subjectTag.setText(DoubtList.get(position).getSTD() + " " + DoubtList.get(position).getBoard());
            holder.UserName.setTextColor(Color.parseColor("#0078FF"));
            holder.subjectTag.setBackgroundResource(R.drawable.subject_button_bg_phy);
            holder.TapToSolve.setBackgroundResource(R.drawable.tap_to_solve);
            holder.TapToSolve.setTextColor(Color.parseColor("#0078FF"));
            holder.subjectTag.setTextColor(Color.parseColor("#0078FF"));
            holder.record.setBackgroundResource(R.drawable.square_small_bg);
            holder.record.setImageResource(R.drawable.record_blue);
            if (DoubtList.get(position).getStatus().equals("Unsolved")){
                holder.solved.setVisibility(View.GONE);
                holder.solvedText.setVisibility(View.GONE);
                holder.TapToSolve.setVisibility(View.VISIBLE);

            }
            else if (DoubtList.get(position).getStatus().equals("Solved")){
                holder.solved.setVisibility(View.VISIBLE);
                holder.solvedText.setVisibility(View.VISIBLE);
                holder.TapToSolve.setVisibility(View.GONE);
                holder.solvedText.setText("Solved");
                holder.solvedText.setTextColor(Color.parseColor("#0078FF"));
                holder.solved.setImageResource(R.drawable.ic_round_check_circle_24);
                holder.solved.setBackgroundResource(R.drawable.square_small_bg);
            }


        }else if (DoubtList.get(position).getSTD().equals("10th")&&DoubtList.get(position).getBoard().equals("SSC")){

            holder.CardBg.setBackgroundResource(R.drawable.doubt_card_bg);
            holder.subjectTag.setText(DoubtList.get(position).getSTD() + " " + DoubtList.get(position).getBoard());
            holder.UserName.setTextColor(Color.parseColor("#FF2829"));
            holder.subjectTag.setTextColor(Color.parseColor("#FF2829"));
            holder.TapToSolve.setBackgroundResource(R.drawable.tap_to_solve_red);
            holder.TapToSolve.setTextColor(Color.parseColor("#FF2829"));
            holder.subjectTag.setBackgroundResource(R.drawable.subject_button_bg);
            holder.record.setBackgroundResource(R.drawable.square_small_bg_alg);
            holder.record.setImageResource(R.drawable.record_red);
            if (DoubtList.get(position).getStatus().equals("Unsolved")){
                holder.solved.setVisibility(View.GONE);
                holder.solvedText.setVisibility(View.GONE);
                holder.TapToSolve.setVisibility(View.VISIBLE);

            }
            else if (DoubtList.get(position).getStatus().equals("Solved")){
                holder.solved.setVisibility(View.VISIBLE);
                holder.solvedText.setVisibility(View.VISIBLE);
                holder.TapToSolve.setVisibility(View.GONE);
                holder.solvedText.setText("Solved");
                holder.solvedText.setTextColor(Color.parseColor("#FF2829"));
                holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_alg);
                holder.solved.setBackgroundResource(R.drawable.square_small_bg_alg);
            }

        }else if (DoubtList.get(position).getSTD().equals("9th")&&DoubtList.get(position).getBoard().equals("CBSE")){
            holder.CardBg.setBackgroundResource(R.drawable.doubt_card_bg_geom);
            holder.subjectTag.setText(DoubtList.get(position).getSTD() + " " + DoubtList.get(position).getBoard());
            holder.UserName.setTextColor(Color.parseColor("#9A0D91"));
            holder.subjectTag.setBackgroundResource(R.drawable.subject_button_bg_geom);
            holder.TapToSolve.setBackgroundResource(R.drawable.tap_to_solve_geom);
            holder.TapToSolve.setTextColor(Color.parseColor("#9A0D91"));
            holder.subjectTag.setTextColor(Color.parseColor("#9A0D91"));
            holder.record.setBackgroundResource(R.drawable.square_small_bg_geom);
            holder.record.setImageResource(R.drawable.record_geom);
            if (DoubtList.get(position).getStatus().equals("Unsolved")){
                holder.solved.setVisibility(View.GONE);
                holder.solvedText.setVisibility(View.GONE);
                holder.TapToSolve.setVisibility(View.VISIBLE);

            }
            else if (DoubtList.get(position).getStatus().equals("Solved")){
                holder.solved.setVisibility(View.VISIBLE);
                holder.solvedText.setVisibility(View.VISIBLE);
                holder.TapToSolve.setVisibility(View.GONE);
                holder.solvedText.setText("Solved");
                holder.solvedText.setTextColor(Color.parseColor("#9A0D91"));
                holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_geom);
                holder.solved.setBackgroundResource(R.drawable.square_small_bg_geom);
            }


        }else if (DoubtList.get(position).getSTD().equals("10th")&&DoubtList.get(position).getBoard().equals("CBSE")){

            holder.CardBg.setBackgroundResource(R.drawable.doubt_card_bg_geog);
            holder.subjectTag.setText(DoubtList.get(position).getSTD() + " " + DoubtList.get(position).getBoard());
            holder.UserName.setTextColor(Color.parseColor("#009F37"));
            holder.subjectTag.setTextColor(Color.parseColor("#009F37"));
            holder.TapToSolve.setBackgroundResource(R.drawable.tap_to_solve_geog);
            holder.TapToSolve.setTextColor(Color.parseColor("#009F37"));
            holder.subjectTag.setBackgroundResource(R.drawable.subject_button_bg_geog);
            holder.record.setBackgroundResource(R.drawable.small_squar_bg_geog);
            holder.record.setImageResource(R.drawable.record_geog);
            if (DoubtList.get(position).getStatus().equals("Unsolved")){
                holder.solved.setVisibility(View.GONE);
                holder.solvedText.setVisibility(View.GONE);
                holder.TapToSolve.setVisibility(View.VISIBLE);

            }
            else if (DoubtList.get(position).getStatus().equals("Solved")){
                holder.solved.setVisibility(View.VISIBLE);
                holder.solvedText.setVisibility(View.VISIBLE);
                holder.TapToSolve.setVisibility(View.GONE);
                holder.solvedText.setText("Solved");
                holder.solvedText.setTextColor(Color.parseColor("#009F37"));
                holder.solved.setImageResource(R.drawable.ic_round_check_circle_24_geog);
                holder.solved.setBackgroundResource(R.drawable.small_squar_bg_geog);
            }

        }


        holder.TapToSolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Solve.class);
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



        holder.CardBg.setOnClickListener(new View.OnClickListener() {
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



    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder(input.length());
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }


}


class HomeDoubtViewHolder extends RecyclerView.ViewHolder {


    CircleImageView profilePictureDC;
    ImageView record, solved;
    Button subjectTag , TapToSolve;
    ViewPager imageHolder;
    TextView questionText, UserName, DateText, solvedText;
    LinearLayoutCompat linearLayout;
    ConstraintLayout CardBg;



    public HomeDoubtViewHolder(@NonNull View itemView) {
        super(itemView);


        profilePictureDC = itemView.findViewById(R.id.ProfilePictureDCHome);
        record = itemView.findViewById(R.id.record);
        solved = itemView.findViewById(R.id.solvedDCHome);
        subjectTag = itemView.findViewById(R.id.subjectTagDChome);
        imageHolder = itemView.findViewById(R.id.imageSliderCardHome);
        questionText = itemView.findViewById(R.id.QuestionTextDCHome);
        UserName = itemView.findViewById(R.id.NameHolderDCHome);
        DateText = itemView.findViewById(R.id.timeText);
        solvedText = itemView.findViewById(R.id.solvedTextDCHome);
        linearLayout = itemView.findViewById(R.id.linearLayout_dotsIndicator);
        CardBg = itemView.findViewById(R.id.cardHome);
        TapToSolve = itemView.findViewById(R.id.tapToSolve);

    }
}