package com.cproz.pantomath_teacher.Home;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.rtp.AudioStream;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cproz.pantomath_teacher.R;
import com.cproz.pantomath_teacher.Solve.Solve;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoubtDetails extends AppCompatActivity {

    Toolbar toolbar;
    CircleImageView StudentProfilePic, TeacherProfilePic;
    TextView StudentUserName, TeacherUserName, TimeText1, TimeText2, QuestionText, AnswerText, Solved;
    ViewPager viewPager, viewPagerAns;
    ImageView SolvedIcon, upArrow;
    Button SubjectTag, SolveNow, Play, Pause;
    SeekBar seekBar;
    Chronometer chronometer;
    LinearLayoutCompat linearLayout, AnslinearLayoutCompat, AudioPlayer;
    ConstraintLayout constraintLayout;
    String Answer, StudentName,Question, QuestionImage1, QuestionImage2, AnsImage1,AnsImage2, Link, AudioUrl,Board, STD, Uid, Status;
    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;
    ImageView report;

    FirebaseFirestore firebaseFirestore;

    TextView reportText;





    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doubt_details);


        firebaseFirestore = FirebaseFirestore.getInstance();

        Initialisation();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);





        final Bundle bundle = getIntent().getExtras();
        assert bundle != null;




        constraintLayout.setVisibility(View.GONE);
        upArrow.setVisibility(View.GONE);
        SolveNow.setVisibility(View.VISIBLE);
        Solved.setTextColor(Color.parseColor("#999999"));
        SolvedIcon.setBackgroundResource(R.drawable.square_small_bg_grey);
        SolvedIcon.setImageResource(R.drawable.ic_round_check_circle_24_grey);


        StudentUserName.setText(toTitleCase(Objects.requireNonNull(bundle.getString("Name"))));

        if (Objects.equals(bundle.getString("Link"), "")){
            AnswerText.setText(bundle.getString("AnsText"));
        }
        else
        {
            String AnswerTextx = bundle.getString("AnsText") + "\n\nHere's a link for you: " + bundle.getString("Link");

            AnswerText.setText(AnswerTextx);
        }



        if (bundle.getString("Status").equals("Solved")){
            report.setVisibility(View.GONE);
            reportText.setVisibility(View.GONE);
        }

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ReportPopup reportPopup = ReportPopup();
//                reportPopup.show()

                ReportPopup reportPopup = new ReportPopup();
                Bundle args = new Bundle();

                args.putString("Email", bundle.getString("Email"));
                args.putString("uid", bundle.getString("Uid"));
                args.putString("Teacher", bundle.getString("TeacherName"));


                reportPopup.setArguments(args);
                reportPopup.show(getSupportFragmentManager(), "cdfer");

            }
        });


        Solved.setText(bundle.getString("Status"));


        if (Objects.equals(bundle.getString("ProfileImage"), "")){
            StudentProfilePic.setImageResource(R.drawable.personal_info);
        }
        else if (!Objects.equals(bundle.getString("ProfileImage"), "")){
            Picasso.get().load(bundle.getString("ProfileImage")).into(StudentProfilePic);
        }

        if (Objects.equals(bundle.getString("TeacherImage"), "")){
            TeacherProfilePic.setImageResource(R.drawable.personal_info);
        }
        else
        if (!Objects.equals(bundle.getString("TeacherImage"), "")){
            Picasso.get().load(bundle.getString("TeacherImage")).into(TeacherProfilePic);
        }

        QuestionText.setText(bundle.getString("QuestionText"));


        if (Objects.equals(bundle.getString("STD"), "9th")&&Objects.equals(bundle.getString("Board"), "SSC")){

            SubjectTag.setText(bundle.getString("STD") + " " + bundle.getString("Board"));
            SubjectTag.setBackgroundResource(R.drawable.subject_button_bg_phy);
            SubjectTag.setTextColor(Color.parseColor("#0078FF"));
            StudentUserName.setTextColor(Color.parseColor("#0078FF"));
            SolvedIcon.setImageResource(R.drawable.ic_round_check_circle_24);
            SolvedIcon.setBackgroundResource(R.drawable.square_small_bg);
            Solved.setTextColor(Color.parseColor("#0078FF"));
            TeacherUserName.setTextColor(Color.parseColor("#0078FF"));
            constraintLayout.setBackgroundResource(R.drawable.doubt_card_bg_physics);
            SolveNow.setTextColor(Color.parseColor("#0078FF"));
            SolveNow.setBackgroundResource(R.drawable.tap_to_solve_selector_blue);
            Play.setBackgroundResource(R.drawable.play_blue);
            Pause.setBackgroundResource(R.drawable.pause_blue);
            AudioPlayer.setBackgroundResource(R.drawable.text_view_bg);
            chronometer.setTextColor(Color.parseColor("#0476D9"));


        }else
            if (Objects.equals(bundle.getString("STD"), "10th")&&Objects.equals(bundle.getString("Board"), "SSC")){

                SubjectTag.setText(bundle.getString("STD") + " " + bundle.getString("Board"));
                SubjectTag.setBackgroundResource(R.drawable.subject_button_bg);
                SubjectTag.setTextColor(Color.parseColor("#FF2829"));
                StudentUserName.setTextColor(Color.parseColor("#FF2829"));
                SolvedIcon.setImageResource(R.drawable.ic_round_check_circle_24_alg);
                SolvedIcon.setBackgroundResource(R.drawable.square_small_bg_alg);
                Solved.setTextColor(Color.parseColor("#FF2829"));
                TeacherUserName.setTextColor(Color.parseColor("#FF2829"));
                constraintLayout.setBackgroundResource(R.drawable.doubt_card_bg);
                SolveNow.setTextColor(Color.parseColor("#FF2829"));
                SolveNow.setBackgroundResource(R.drawable.text_view_bg_red);
                Play.setBackgroundResource(R.drawable.play_red);
                Pause.setBackgroundResource(R.drawable.pause_red);
                AudioPlayer.setBackgroundResource(R.drawable.text_view_bg_red);
                chronometer.setTextColor(Color.parseColor("#FF2829"));

            }
            else
            if (Objects.equals(bundle.getString("STD"), "9th")&&Objects.equals(bundle.getString("Board"), "CBSE")){

                SubjectTag.setText(bundle.getString("STD") + " " + bundle.getString("Board"));
                SubjectTag.setBackgroundResource(R.drawable.subject_button_bg_geom);
                SubjectTag.setTextColor(Color.parseColor("#9A0D91"));
                StudentUserName.setTextColor(Color.parseColor("#9A0D91"));
                SolvedIcon.setImageResource(R.drawable.ic_round_check_circle_24_geom);
                SolvedIcon.setBackgroundResource(R.drawable.square_small_bg_geom);
                Solved.setTextColor(Color.parseColor("#9A0D91"));
                TeacherUserName.setTextColor(Color.parseColor("#9A0D91"));
                constraintLayout.setBackgroundResource(R.drawable.doubt_card_bg_geom);
                SolveNow.setTextColor(Color.parseColor("#9A0D91"));
                SolveNow.setBackgroundResource(R.drawable.text_view_bg_geom);
                Play.setBackgroundResource(R.drawable.play_geometry);
                Pause.setBackgroundResource(R.drawable.pause_geom);
                AudioPlayer.setBackgroundResource(R.drawable.text_view_bg_geom);
                chronometer.setTextColor(Color.parseColor("#9A0D91"));

            }else
            if (Objects.equals(bundle.getString("STD"), "10th")&&Objects.equals(bundle.getString("Board"), "CBSE")){

                SubjectTag.setText(bundle.getString("STD") + " " + bundle.getString("Board"));
                SubjectTag.setBackgroundResource(R.drawable.subject_button_bg_geog);
                SubjectTag.setTextColor(Color.parseColor("#009F37"));
                StudentUserName.setTextColor(Color.parseColor("#009F37"));
                SolvedIcon.setImageResource(R.drawable.ic_round_check_circle_24_geog);
                SolvedIcon.setBackgroundResource(R.drawable.small_squar_bg_geog);
                Solved.setTextColor(Color.parseColor("#009F37"));
                TeacherUserName.setTextColor(Color.parseColor("#009F37"));
                constraintLayout.setBackgroundResource(R.drawable.doubt_card_bg_geog);
                SolveNow.setTextColor(Color.parseColor("#009F37"));
                SolveNow.setBackgroundResource(R.drawable.text_view_bg_geog);
                Play.setBackgroundResource(R.drawable.play_geog);
                Pause.setBackgroundResource(R.drawable.pause_geog);
                AudioPlayer.setBackgroundResource(R.drawable.text_view_bg_geom);
                chronometer.setTextColor(Color.parseColor("#009F37"));

            }else
            if (Objects.equals(bundle.getString("STD"), "9th")&&Objects.equals(bundle.getString("Board"), "ICSE")){

                SubjectTag.setText(bundle.getString("STD") + " " + bundle.getString("Board"));
                SubjectTag.setBackgroundResource(R.drawable.subject_button_bg_chem);
                SubjectTag.setTextColor(Color.parseColor("#FF9B00"));
                StudentUserName.setTextColor(Color.parseColor("#FF9B00"));
                SolvedIcon.setImageResource(R.drawable.ic_round_check_circle_24_chem);
                SolvedIcon.setBackgroundResource(R.drawable.small_square_bg_chem);
                Solved.setTextColor(Color.parseColor("#FF9B00"));
                TeacherUserName.setTextColor(Color.parseColor("#FF9B00"));
                constraintLayout.setBackgroundResource(R.drawable.doubt_card_bg_chem);
                SolveNow.setTextColor(Color.parseColor("#FF9B00"));
                SolveNow.setBackgroundResource(R.drawable.text_view_bg_chem);
                Play.setBackgroundResource(R.drawable.play_chem);
                Pause.setBackgroundResource(R.drawable.pause_chem);
                AudioPlayer.setBackgroundResource(R.drawable.text_view_bg_chem);
                chronometer.setTextColor(Color.parseColor("#FF9B00"));

            }else
            if (Objects.equals(bundle.getString("STD"), "10th")&&Objects.equals(bundle.getString("Board"), "ICSE")){

                SubjectTag.setText(bundle.getString("STD") + " " + bundle.getString("Board"));
                SubjectTag.setBackgroundResource(R.drawable.subject_button_bg_his);
                SubjectTag.setTextColor(Color.parseColor("#813912"));
                StudentUserName.setTextColor(Color.parseColor("#813912"));
                SolvedIcon.setImageResource(R.drawable.ic_round_check_circle_24_his);
                SolvedIcon.setBackgroundResource(R.drawable.small_squar_bg_his);
                Solved.setTextColor(Color.parseColor("#813912"));
                TeacherUserName.setTextColor(Color.parseColor("#813912"));
                constraintLayout.setBackgroundResource(R.drawable.doubt_card_bg_his);
                SolveNow.setTextColor(Color.parseColor("#813912"));
                SolveNow.setBackgroundResource(R.drawable.text_view_bg_red);
                Play.setBackgroundResource(R.drawable.play_his);
                Pause.setBackgroundResource(R.drawable.pause_his);
                AudioPlayer.setBackgroundResource(R.drawable.text_view_bg_his);
                chronometer.setTextColor(Color.parseColor("#813912"));

            }




        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();




            if (Objects.equals(bundle.getString("Status"), "Unsolved")){

                constraintLayout.setVisibility(View.GONE);
                upArrow.setVisibility(View.GONE);
                SolveNow.setVisibility(View.VISIBLE);
                Solved.setTextColor(Color.parseColor("#999999"));
                SolvedIcon.setBackgroundResource(R.drawable.square_small_bg_grey);
                SolvedIcon.setImageResource(R.drawable.ic_round_check_circle_24_grey);


            }
            else
                if (Objects.equals(bundle.getString("Status"), "Solved")){

                    assert user != null;
                    if (Objects.equals(bundle.getString("TeacherEmail"), user.getEmail())){
                        constraintLayout.setVisibility(View.VISIBLE);
                        upArrow.setVisibility(View.VISIBLE);
                        SolveNow.setText("Edit Answer");
                    }
                    else{
                        SolveNow.setVisibility(View.GONE);
                    }
                }



                SolveNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = getIntent().getExtras();
                        assert bundle != null;
                        Answer = bundle.getString("AnsText");
                        StudentName = bundle.getString("Name");
                        Question = bundle.getString("QuestionText");
                        QuestionImage1 = bundle.getString("QuestionImage1Url");
                        QuestionImage2 = bundle.getString("QuestionImage2Url");
                        AnsImage1 =  bundle.getString("AnsImage1Url");
                        AnsImage2 =  bundle.getString("AnsImage2Url");
                        Link = bundle.getString("Link");
                        AudioUrl = bundle.getString("AudioUrl");
                        Board = bundle.getString("Board");
                        STD = bundle.getString("STD");
                        Uid = bundle.getString("Uid");
                        Status = bundle.getString("Status");
                        Intent intent = new Intent(DoubtDetails.this, Solve.class);
                        intent.putExtra("AnsText",Answer );
                        intent.putExtra("Name",StudentName );
                        intent.putExtra("QuestionText", Question);
                        intent.putExtra("QuestionImage1Url", QuestionImage1);
                        intent.putExtra("QuestionImage2Url", QuestionImage2);
                        intent.putExtra("AnsImage1Url",AnsImage1);
                        intent.putExtra("AnsImage2Url", AnsImage2);
                        intent.putExtra("Link", Link);
                        intent.putExtra("AudioUrl",AudioUrl);
                        intent.putExtra("Board",Board);
                        intent.putExtra("STD",STD);
                        intent.putExtra("Uid", Uid);
                        intent.putExtra("Status", Status);

                        startActivity(intent);
                    }
                });



                if (Objects.equals(bundle.getString("AudioUrl"), "")){
                    AudioPlayer.setVisibility(View.GONE);
                }else if (!Objects.equals(bundle.getString("AudioUrl"), "")){
                    AudioPlayer.setVisibility(View.VISIBLE);
                    Play.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            StreamAudio(bundle);
                            Play.setVisibility(View.GONE);
                            Pause.setVisibility(View.VISIBLE);
                        }
                    });


                }

                Pause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mediaPlayer.pause();
                        handler.removeCallbacks(runnable);
                        Play.setVisibility(View.VISIBLE);
                        Pause.setVisibility(View.GONE);
                    }
                });


        TimeText1.setText(bundle.getString("DateTime"));
        TimeText2.setText(bundle.getString("DateTime"));
        TeacherUserName.setText(bundle.getString("Teacher"));


        String[] imageUrls = new String[0];


        if (Objects.equals(bundle.getString("QuestionImage1Url"), "") && Objects.equals(bundle.getString("QuestionImage2Url"), "")){
            viewPager.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);

        }
        else if (!Objects.equals(bundle.getString("QuestionImage1Url"), "") && Objects.equals(bundle.getString("QuestionImage2Url"), "")){
            viewPager.setVisibility(View.VISIBLE);

            imageUrls = new String[] {
                    bundle.getString("QuestionImage1Url")
            };

            ViewPagerAdapterx viewPagerAdapter = new ViewPagerAdapterx(this, imageUrls);
            viewPager.setAdapter(viewPagerAdapter);
            linearLayout.setVisibility(View.GONE);

        }
        else if (!Objects.equals(bundle.getString("QuestionImage1Url"), "") && !Objects.equals(bundle.getString("QuestionImage2Url"), "")){


            viewPager.setVisibility(View.VISIBLE);

            imageUrls = new String[] {
                    bundle.getString("QuestionImage1Url"),  bundle.getString("QuestionImage2Url")
            };

            ViewPagerAdapterx viewPagerAdapter = new ViewPagerAdapterx(this, imageUrls);
            viewPager.setAdapter(viewPagerAdapter);
            linearLayout.setVisibility(View.VISIBLE);


        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    linearLayout.setBackgroundResource(R.drawable.ic_dot_first_photo);
                }
                else if (position == 1){
                    linearLayout.setBackgroundResource(R.drawable.ic_dots_second_photo);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        String[] imageUrls1 = new String[0];


        if (Objects.equals(bundle.getString("AnsImage1Url"), "") && Objects.equals(bundle.getString("AnsImage2Url"), "")){
            viewPagerAns.setVisibility(View.GONE);
            AnslinearLayoutCompat.setVisibility(View.GONE);

        }
        else if (!Objects.equals(bundle.getString("AnsImage1Url"), "") && Objects.equals(bundle.getString("AnsImage2Url"), "")){
            viewPagerAns.setVisibility(View.VISIBLE);

            imageUrls1 = new String[] {
                    bundle.getString("AnsImage1Url")
            };

            ViewPagerAdapterx viewPagerAdapter = new ViewPagerAdapterx(this, imageUrls1);
            viewPagerAns.setAdapter(viewPagerAdapter);
            AnslinearLayoutCompat.setVisibility(View.GONE);

        }
        else if (!Objects.equals(bundle.getString("AnsImage1Url"), "") && !Objects.equals(bundle.getString("AnsImage2Url"), "")){


            viewPagerAns.setVisibility(View.VISIBLE);

            imageUrls1 = new String[] {
                    bundle.getString("AnsImage1Url"),  bundle.getString("AnsImage2Url")
            };

            ViewPagerAdapterx viewPagerAdapter = new ViewPagerAdapterx(this, imageUrls1);
            viewPagerAns.setAdapter(viewPagerAdapter);
            AnslinearLayoutCompat.setVisibility(View.VISIBLE);


        }

        viewPagerAns.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    AnslinearLayoutCompat.setBackgroundResource(R.drawable.ic_dot_first_photo);
                }
                else if (position == 1){
                    AnslinearLayoutCompat.setBackgroundResource(R.drawable.ic_dots_second_photo);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });










    }

    public void Initialisation(){
        toolbar = findViewById(R.id.DoubtDetailsToolBar);
        StudentProfilePic = findViewById(R.id.ProfilePictureDoubtsDetails);
        TeacherProfilePic = findViewById(R.id.ProfilePictureTeacherDoubtsDetails);
        StudentUserName = findViewById(R.id.NameHolderDoubtDetails);
        TeacherUserName = findViewById(R.id.NameHolderDoubtDetailsTeacher);
        TimeText1 = findViewById(R.id.timeTextDD);
        TimeText2 = findViewById(R.id.timeTextDDTeacher);
        QuestionText = findViewById(R.id.QuestionTextDD);
        AnswerText = findViewById(R.id.AnswerTextDD);
        Solved = findViewById(R.id.solvedTextDD);
        viewPager = findViewById(R.id.imageSliderDD);
        SolvedIcon = findViewById(R.id.solvedDD);
        linearLayout = findViewById(R.id.linearLayout_dotsIndicator_Solve);
        SubjectTag = findViewById(R.id.subjectTagDD);
        constraintLayout = findViewById(R.id.cardHomeDD);
        upArrow = findViewById(R.id.UpArrow);
        viewPagerAns = findViewById(R.id.AnsimageSliderDD);
        AnslinearLayoutCompat = findViewById(R.id.AnslinearLayout_dotsIndicator_DD);
        SolveNow = findViewById(R.id.SolveNow);
        AudioPlayer = findViewById(R.id.TimerRecordDD);
        seekBar = findViewById(R.id.audioSeekBarDD);
        Play = findViewById(R.id.PlayPauseDD);
        Pause = findViewById(R.id.PauseDD);
        chronometer = findViewById(R.id.timerDD);
        report = findViewById(R.id.report);
        reportText = findViewById(R.id.reportText);


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

    public void StreamAudio(Bundle bundle){



        runnable = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 00);


            }
        };

        AudioUrl = bundle.getString("AudioUrl");
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


        try {
            mediaPlayer.setDataSource(AudioUrl);
            //loding
            mediaPlayer.prepare();
            int duration = mediaPlayer.getDuration();
            @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(duration),
                    TimeUnit.MILLISECONDS.toSeconds(duration) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
            );
            chronometer.setText(time);

        } catch (IOException e) {
            e.printStackTrace();
        }


        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration()-90);
        handler.postDelayed(runnable, 0);
        mediaPlayer.setOnCompletionListener(cListener);





    }

    MediaPlayer.OnCompletionListener cListener = new MediaPlayer.OnCompletionListener(){

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void onCompletion(MediaPlayer mp){

            Pause.setVisibility(View.GONE);
            Play.setVisibility(View.VISIBLE);
            //seekBar.setProgress(1);

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}