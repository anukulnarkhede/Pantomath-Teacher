package com.cproz.pantomath_teacher.Solve;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cproz.pantomath_teacher.Home.Home;
import com.cproz.pantomath_teacher.Home.HomeFragment;
import com.cproz.pantomath_teacher.Home.ViewPagerAdapter;
import com.cproz.pantomath_teacher.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jackandphantom.circularimageview.RoundedImage;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

public class Solve extends AppCompatActivity {


    Toolbar toolbar;
    Button  StudentTag, Answer, cancelImg1, cancelImg2, cancelAudio, PlayPause, Pause, RecordButton;
    TextView QuestionText, StudentNameText, addPhoto1, addPhoto2, record_text;
    ImageView recordIcon;
    EditText Link, AnswerText;
    ViewPager viewPager;
    RoundedImage answerImage1, answerImage2;
    ConstraintLayout constraintLayout;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    String email = user != null ? user.getEmail() : null;

    public static Boolean CANCELAUDIO = Solve.CANCELAUDIO = false;

    LinearLayoutCompat linearLayoutCompat, recordTimer, TapToRecord;
    String decision;
    Uri mCropImageUri, uriImg1, uriImg2, uri;
    Chronometer chronometer;

    int fl1 = 0, fl2 = 0;
    public FirebaseStorage storage;
    public StorageReference storageReference;
    public static String NAME = Solve.NAME;
    public static String PROFILEIMGURL = Solve.PROFILEIMGURL;
    String AnswerPhoto1url, AnswerPhoto2url, AudioUrl = "", FileUrl = "", TeacherName , TeacherImageUrl , GetAnswer, GetLink, Board, Class;
    Date DateTime;
    String recordPermission = Manifest.permission.RECORD_AUDIO;
    MediaRecorder recorder;
    long pauseOffset;

    SeekBar seekBar;
    String fileName = "";
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DocumentReference refx = firebaseFirestore.collection("Users/Teachers/Teacherinfo/" ).document(String.valueOf(email));
    private FirebaseFirestore fireDB;
    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;


    ProgressBar progressBar;


    public DocumentReference ref;
    FirebaseStorage firebaseStorage;
    private int PERMISSION_CODE = 120000;


    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve);
        Initialise();
        progressBar = findViewById(R.id.progressBarSolve);






        final Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        AudioUrl = bundle.getString("AudioUrl");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //recordTimer.setVisibility(View.VISIBLE);
        //chronometer.start();
        PlayPause.setBackgroundResource(R.drawable.play_disabled);

        PlayPause.setEnabled(false);

        progressBar.setVisibility(View.GONE);
        seekBar.setEnabled(false);

        QuestionText.setText(bundle.getString("QuestionText"));
        StudentNameText.setText(toTitleCase(Objects.requireNonNull("- " + bundle.getString("Name"))));
        StudentTag.setText(bundle.getString("STD") + " " + bundle.getString("Board"));

        if (Objects.equals(bundle.getString("STD"), "9th")){

            constraintLayout.setBackgroundResource(R.drawable.doubt_card_bg_physics);
            TapToRecord.setBackgroundResource(R.drawable.text_view_bg);
            record_text.setTextColor(Color.parseColor("#007AFF"));
            recordIcon.setImageResource(R.drawable.record_blue);
            //@SuppressLint("UseCompatLoadingForDrawables") Drawable img = this.getResources().getDrawable(R.drawable.record_blue);
            //img.setBounds(0, 0, 65, 65);
            //TapToRecord.setCompoundDrawables(img, null, null, null);
            StudentTag.setTextColor(Color.parseColor("#007AFF"));
            StudentTag.setBackgroundResource(R.drawable.subject_button_bg_phy);
            Answer.setBackgroundResource(R.drawable.text_view_bg);
            Answer.setTextColor(Color.parseColor("#007AFF"));
            recordTimer.setBackgroundResource(R.drawable.text_view_bg);
            chronometer.setTextColor(Color.parseColor("#007AFF"));
            Pause.setBackgroundResource(R.drawable.pause_blue);

            if (PlayPause.isEnabled()){
                PlayPause.setBackgroundResource(R.drawable.play_blue);
            }
            else
            {
                PlayPause.setBackgroundResource(R.drawable.play_disabled);
            }



        }

        refx.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                TeacherName = documentSnapshot.getString("Name");
                TeacherImageUrl = documentSnapshot.getString("profileURl");

                Answer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DateTime = new Date();
                        GetAnswer = AnswerText.getText().toString();
                        GetLink = Link.getText().toString();
                        try {
                            Validation(bundle.getString("Uid"), bundle);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });



            }
        });








        //TapToRecord.setHeight(140);
        //TapToRecord.setWidth(320);
        cancelImg2.setVisibility(View.GONE);
        cancelImg1.setVisibility(View.GONE);



        Board = bundle.getString("Board");
        Class = bundle.getString("STD");

        String[] imageUrls = new String[0];



        cancelAudio.setVisibility(View.GONE);



        if (Objects.equals(bundle.getString("QuestionImage1Url"), "")
                && Objects.equals(bundle.getString("QuestionImage2Url"), "")){
            viewPager.setVisibility(View.GONE);
            linearLayoutCompat.setVisibility(View.GONE);

        }
        else if (!Objects.equals(bundle.getString("QuestionImage1Url"), "")
                && Objects.equals(bundle.getString("QuestionImage2Url"), "")){
            viewPager.setVisibility(View.VISIBLE);

            imageUrls = new String[] {
                    bundle.getString("QuestionImage1Url")
            };

            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, imageUrls);
            viewPager.setAdapter(viewPagerAdapter);
            linearLayoutCompat.setVisibility(View.GONE);

        }
        else if (!Objects.equals(bundle.getString("QuestionImage1Url"), "")
                && !Objects.equals(bundle.getString("QuestionImage2Url"), "")){


            viewPager.setVisibility(View.VISIBLE);

            imageUrls = new String[] {
                    bundle.getString("QuestionImage1Url"),  bundle.getString("QuestionImage2Url")
            };

            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, imageUrls);
            viewPager.setAdapter(viewPagerAdapter);
            linearLayoutCompat.setVisibility(View.VISIBLE);


        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    linearLayoutCompat.setBackgroundResource(R.drawable.ic_dot_first_photo);
                }
                else if (position == 1){
                    linearLayoutCompat.setBackgroundResource(R.drawable.ic_dots_second_photo);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        if (Objects.equals(bundle.getString("Status"), "Solved")){


            AnswerText.setText(bundle.getString("AnsText"));
            if (!Objects.equals(bundle.getString("AnsImage1Url"), "")){
                Picasso.get().load(bundle.getString("AnsImage1Url")).into(answerImage1);
                cancelImg1.setVisibility(View.VISIBLE);
                addPhoto1.setVisibility(View.GONE);
            }

            if (!Objects.equals(bundle.getString("AnsImage2Url"), "")){
                Picasso.get().load(bundle.getString("AnsImage2Url")).into(answerImage2);
                cancelImg2.setVisibility(View.VISIBLE);
                addPhoto1.setVisibility(View.GONE);
            }



            Link.setText(bundle.getString("Link"));

        }





        answerImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fl1 = 1;
                fl2 = 0;
                onSelectImageClick(v);
                uriImg1 = mCropImageUri;

            }
        });

        answerImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fl2 = 1;
                fl1 = 0;
                onSelectImageClick(v);
                uriImg2 = mCropImageUri;
            }
        });



        //@SuppressLint("UseCompatLoadingForDrawables") final Drawable img = this.getResources().getDrawable(R.drawable.record_blue);

        RecordButton.setOnTouchListener(new View.OnTouchListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint({"ClickableViewAccessibility", "InlinedApi"})
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (checkPermissions()){


                    Answer.setEnabled(false);


                if (event.getAction() == MotionEvent.ACTION_DOWN){

                    fileName = Objects.requireNonNull(getExternalCacheDir()).getAbsolutePath();
                    fileName += "/recordedAnsAudio.3gp";
                    startRecording(fileName);

                    final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    assert vibrator != null;
                    vibrator.vibrate(VibrationEffect.createOneShot(25, 25));

                    RecordButton.setHapticFeedbackEnabled(true);



                    if (Class.equals("9th")){
                        TapToRecord.setBackgroundResource(R.drawable.tap_to_solve_change_blue);
                        record_text.setTextColor(Color.parseColor("#007AFF"));
                        recordIcon.setImageResource(R.drawable.record_blue);
                        //img.setBounds(0, 0, 0, 0);
                        //TapToRecord.setCompoundDrawables(img, null, null, null);




                    }else if (Class.equals("10th")){
                        TapToRecord.setBackgroundResource(R.drawable.tap_to_solve_change_red);
                    }

                    recordTimer.setVisibility(View.VISIBLE);



                    //StartRecording();
                    StartChronometer();



                }else
                    if (event.getAction() == MotionEvent.ACTION_UP){
                        Answer.setEnabled(true);
                        TapToRecord.setBackgroundResource(R.drawable.tap_to_record_disabled);
                        RecordButton.setEnabled(false);
                        final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                        assert vibrator != null;
                        vibrator.vibrate(VibrationEffect.createOneShot(25, 25));
                        TapToRecord.setEnabled(false);

                        TapToRecord.setHapticFeedbackEnabled(true);
                        if (Class.equals("9th")){
                            TapToRecord.setBackgroundResource(R.drawable.text_view_bg);
                            record_text.setTextColor(Color.parseColor("#007AFF"));

                            recordIcon.setImageResource(R.drawable.record_blue);
                            //img.setBounds(0, 0, 65, 65);
                            //TapToRecord.setCompoundDrawables(img, null, null, null);
                            PlayPause.setEnabled(true);

                            PlayPause.setBackgroundResource(R.drawable.play_blue);



                        }
                        else if (Class.equals("10th")){
                            TapToRecord.setBackgroundResource(R.drawable.text_view_bg_red);
                            PlayPause.setBackgroundResource(R.drawable.play_red);

                        }

                        stopRecording();
                        //StopRecording();








                        chronometer.stop();


                    }

                }

                return false;
            }
        });





        cancelImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (addPhoto1.getVisibility() == View.GONE && addPhoto2.getVisibility() == View.GONE){




                    uriImg1 = uriImg2;
                    uriImg2 = null;
                    answerImage1.setImageURI(uriImg1);
                    addPhoto2.setVisibility(View.VISIBLE);


                    answerImage2.setImageResource(R.drawable.image_view_bg);
                    addPhoto2.setVisibility(View.VISIBLE);

                    cancelImg2.setVisibility(View.GONE);

                }

                else if (addPhoto1.getVisibility() == View.GONE && addPhoto2.getVisibility() == View.VISIBLE){
                    answerImage1.setImageResource(R.drawable.image_view_bg);

                    addPhoto2.setVisibility(View.VISIBLE);

                    cancelImg1.setVisibility(View.GONE);

                    addPhoto1.setVisibility(View.VISIBLE);
                    uriImg1 = null;
                    uriImg2 = null;
                }




            }
        });

        cancelImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                answerImage2.setImageResource(R.drawable.image_view_bg);
                //doubtImage2.setVisibility(View.GONE);
                addPhoto2.setVisibility(View.VISIBLE);

                //imageCancel1.setVisibility(View.GONE);
                cancelImg2.setVisibility(View.GONE);


                uriImg2 = null;



            }
        });


        cancelAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CancelAudioPopUp log = new CancelAudioPopUp();
                //log.show(getSupportFragmentManager(), "CancelAudio");
                //DeleteFile();
                //deleteFile(fileName);

                RecordButton.setEnabled(true);

                PlayPause.setEnabled(false);
                PlayPause.setBackgroundResource(R.drawable.play_disabled);
                try {
                    mediaPlayer.reset();
                }catch (Exception e){
                    e.printStackTrace();
                }



                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.stop();
                cancelAudio.setVisibility(View.GONE);
                fileName = "";

                seekBar.setEnabled(false);
                seekBar.setProgress(0);
                Toast.makeText(Solve.this, fileName, Toast.LENGTH_SHORT).show();

            }
        });








        //StartRecording();
        //StopRecording();





    }


    private void startRecording(String fileName) {

        recorder = new MediaRecorder();
        cancelAudio.setVisibility(View.VISIBLE);

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC_ELD);

        try {
            recorder.prepare();
            recorder.start();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void stopRecording() {



        try {
            recorder.stop();
        }
        catch(RuntimeException e) {
        }
        finally {
            recorder.reset();


            recorder.release();
            recorder = null;
        }


        Toast.makeText(this, "recorded", Toast.LENGTH_SHORT).show();


        try {
            Uri uri = Uri.fromFile(new File(fileName));
            mediaPlayer(uri);
        }
        catch(RuntimeException e) {
            System.out.println("Error Occured" + e);
        }



    }




    private void Initialise() {
        toolbar = findViewById(R.id.SolveToolBar);
        StudentTag = findViewById(R.id.StudentTagSolve);
        Answer = findViewById(R.id.UploadSolve);
        cancelImg1 = findViewById(R.id.Image1cancel_button);
        cancelImg2 = findViewById(R.id.Image2cancel_button);
        QuestionText = findViewById(R.id.questionTextSolve);
        StudentNameText = findViewById(R.id.StudentnameSolve);
        addPhoto1 = findViewById(R.id.addPhoto1);
        addPhoto2 = findViewById(R.id.addPhoto2);
        Link = findViewById(R.id.LinkText);
        AnswerText = findViewById(R.id.answerSolve);
        viewPager = findViewById(R.id.imageSliderAnswerQuestion);
        answerImage1 = findViewById(R.id.DoubtUploadImage1);
        answerImage2 = findViewById(R.id.DoubtUploadImage2);
        constraintLayout = findViewById(R.id.CardBGsolve);
        linearLayoutCompat = findViewById(R.id.linearLayout_dotsIndicator_Solve);
        chronometer = findViewById(R.id.timerDD);
        recordTimer = findViewById(R.id.TimerRecordDD);
        cancelAudio = findViewById(R.id.CancelAudio);
        PlayPause = findViewById(R.id.PlayPauseDD);
        seekBar = findViewById(R.id.audioSeekBarDD);
        Pause = findViewById(R.id.PauseDD);
        TapToRecord = findViewById(R.id.recordButt);
        record_text = findViewById(R.id.RecordButtText);
        recordIcon = findViewById(R.id.AudioIcon);
        RecordButton = findViewById(R.id.TapToRecord);


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



    @SuppressLint("SetTextI18n")
    public void GalleryOrCamera(String actionx) {
        String action;
        action = actionx;

        if (action == null) {
            System.out.println("Wait");
        } else if (action.equals("gallery")) {
            decision = "gallery";


        } else if (action.equals("camera")) {
            decision = "camera";
        }
    }



    public void onSelectImageClick(View view) {
        //CropImage.startPickImageActivity(this);
        CropImage.activity().start(this);


    }




    @Override
    @SuppressLint({"NewApi", "SetTextI18n"})
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of pick image chooser
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(Objects.requireNonNull(this), data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mCropImageUri = result.getUri();

                //profilePicture.setImageURI(mCropImageUri);
                //SelectPhotoText.setText("Change Photo");



                if (fl1 == 1 && fl2 == 0) {

                    uriImg1 = mCropImageUri;
                    answerImage1.setImageURI(mCropImageUri);
                    addPhoto1.setVisibility(View.GONE);
                    System.out.println(mCropImageUri);

                    //Image1.setBackgroundColor(Color.parseColor("#00FFFFFF"));


                    cancelImg1.setVisibility(View.VISIBLE);





                } else if (fl2 == 1 && fl1 == 0) {


                    uriImg2 = mCropImageUri;
                    answerImage2.setImageURI(uriImg2);
                    addPhoto2.setVisibility(View.GONE);

                    //Image2.setBackgroundColor(Color.parseColor("#00FFFFFF"));

                    System.out.println(mCropImageUri);
                    cancelImg2.setVisibility(View.VISIBLE);






                }







            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri);
        }
        else {
            Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }


    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public void UploadToFirebase(final String Uid, Bundle bundle) throws IOException {


        fireDB = FirebaseFirestore.getInstance();





        if (fileName.equals("")){

            AudioUrl = bundle.getString("AudioUrl");


        }
        else
        {
            AudioUpload(Uid);
        }



        if (uriImg1 == null && uriImg2 == null){

            AnswerPhoto1url = "";
            AnswerPhoto2url = "";
            //UploadToFirestore(Uid);
            UpdateAnswer(Uid);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(100, true);
            startActivity(new Intent(Solve.this, Home.class));



        }

        else if (uriImg1 != null && uriImg2 != null){

            final StorageReference reference1 = storageReference.child("Doubts/" + Uid + "/AnsDoubtImg1.jpg");
            final StorageReference reference2 = storageReference.child("Doubts/" + Uid + "/AnsDoubtImg2.jpg");



            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriImg1);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
            byte[] data = baos.toByteArray();


            Bitmap bitmapx = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriImg2);
            ByteArrayOutputStream baosx = new ByteArrayOutputStream();
            bitmapx.compress(Bitmap.CompressFormat.JPEG, 75, baosx);
            final byte[] data2 = baosx.toByteArray();




            reference1.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            AnswerPhoto1url = uri.toString();


                            reference2.putBytes(data2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    reference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @RequiresApi(api = Build.VERSION_CODES.N)
                                        @Override
                                        public void onSuccess(Uri urix) {
                                            AnswerPhoto2url = urix.toString();

                                            UpdateAnswer(Uid);
                                            progressBar.setVisibility(View.VISIBLE);
                                            progressBar.setProgress(100, true);
                                            startActivity(new Intent(Solve.this, Home.class));



                                        }
                                    });

                                    Toast.makeText(Solve.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(Solve.this, "Failed to Upload Image 1", Toast.LENGTH_SHORT).show();
                                    //ConfirmDoubt.setEnabled(true);
                                    //progressBar.setVisibility(View.GONE);
                                }
                            });


                        }
                    });

                    Toast.makeText(Solve.this, "Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Solve.this, "Failed to Upload Image 1", Toast.LENGTH_SHORT).show();
                    //ConfirmDoubt.setEnabled(true);
                    //progressBar.setVisibility(View.GONE);
                }
            });






        }

        else if (uriImg1 != null && uriImg2 == null){

            final StorageReference reference1 = storageReference.child("Doubts/" + Uid + "/AnsDoubtImg1.jpg");



            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriImg1);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
            byte[] data = baos.toByteArray();





            reference1.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            AnswerPhoto2url = "";
                            AnswerPhoto1url = uri.toString();
                            UpdateAnswer(Uid);
                            progressBar.setVisibility(View.VISIBLE);
                            progressBar.setProgress(100, true);
                            startActivity(new Intent(Solve.this, Home.class));

                        }
                    });

                    Toast.makeText(Solve.this, "Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //ConfirmDoubt.setEnabled(true);
                    //progressBar.setVisibility(View.GONE);
                    Toast.makeText(Solve.this, "Failed to Upload Image 1", Toast.LENGTH_SHORT).show();
                }
            });



        }



    }

    public void UpdateAnswer(String Uid){

            ref = firebaseFirestore.collection("Doubts" ).document(Uid);
            String Status = "Solved";

        ref.update("AnsPhotoUrl1", AnswerPhoto1url, "AnsPhotoUrl2", AnswerPhoto2url, "AnsText", GetAnswer, "AudioUrl", AudioUrl, "DateTime", DateTime , "FileUrl", FileUrl, "Link", GetLink
        ,"Status", Status ,"Teacher", TeacherName, "TeacherImageUrl", TeacherImageUrl
        ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                //Toast.makeText(Solve.this, "Profile Picture Updated", Toast.LENGTH_SHORT).show();
                //progressBar.setVisibility(View.GONE);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressBar.setVisibility(View.GONE);
                        System.out.println("Profile Picture was unable to update");
                        //progressBar.setVisibility(View.GONE);

                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void Validation(String Uid, Bundle bundle) throws IOException {
        if (GetAnswer.isEmpty() && Link.getText().toString().isEmpty()&&AudioUrl.isEmpty()&&answerImage1.getResources().equals(R.drawable.image_view_bg)){
            AnswerText.setError("Atleast one field must be filled");
            //AnswerText.requestFocus();
            //Toast.makeText(this, fileName, Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, "Answer is required", Toast.LENGTH_SHORT).show();
        }
        else
        {
            UploadToFirebase(Uid, bundle);
        }
    }


    private boolean checkPermissions(){
        if (ActivityCompat.checkSelfPermission(Solve.this, recordPermission) == PackageManager.PERMISSION_GRANTED){
            return true;
        }else {
            ActivityCompat.requestPermissions(Solve.this, new String[]{recordPermission}, PERMISSION_CODE);


            return false;
        }
        }


    private void  StartChronometer(){
        chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
        chronometer.start();
    }



    public void AudioUpload(final String Uid){
        final StorageReference reference1 = storageReference.child("Doubts/" + Uid + "/AnsAudio.3gp");


        uri = Uri.fromFile(new File(fileName));
        reference1.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        AudioUrl = uri.toString();
                        UpdateAnswer(Uid);
                    }
                });
                Toast.makeText(Solve.this, "Audio Uploaded", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void mediaPlayer(Uri uri){
        seekBar.setEnabled(true);

        mediaPlayer = MediaPlayer.create(Solve.this, uri);



        try {
            runnable = new Runnable() {
                @Override
                public void run() {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    handler.postDelayed(this, 0);


                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }



        PlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration()-90);
                handler.postDelayed(runnable, 0);
                PlayPause.setVisibility(View.GONE);
                Pause.setVisibility(View.VISIBLE);


            }
        });

        Pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                handler.removeCallbacks(runnable);
                PlayPause.setVisibility(View.VISIBLE);
                Pause.setVisibility(View.GONE);
            }
        });

        mediaPlayer.setOnCompletionListener(cListener);







    }

    MediaPlayer.OnCompletionListener cListener = new MediaPlayer.OnCompletionListener(){

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void onCompletion(MediaPlayer mp){

            Pause.setVisibility(View.GONE);
            PlayPause.setVisibility(View.VISIBLE);

        }
    };

    public void DeleteFile(){
        File filex = new File(getFilesDir(), fileName);
        if (filex.exists()){
            deleteFile(fileName);
            Toast.makeText(this, "Audio deleted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "File Does Not Exist ", Toast.LENGTH_SHORT).show();
        }
    }

    public void StreamAudio(String uid, Bundle bundle){
        AudioUrl = bundle.getString("AudioUrl");
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(AudioUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}