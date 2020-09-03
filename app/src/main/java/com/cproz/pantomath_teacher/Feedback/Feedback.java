package com.cproz.pantomath_teacher.Feedback;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cproz.pantomath_teacher.Home.Home;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class Feedback extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    String email = user != null ? user.getEmail() : null;
    private DocumentReference ref = firebaseFirestore.collection("Users/Teachers/Teacherinfo/" ).document(String.valueOf(email));
    String StudentClass = null, StudentBoard = null;

    Toolbar toolbar;
    Uri mCropImageUri, uriImg1 = null, uriImg2;
    String decision;
    private FirebaseFirestore db;
    EditText feedBackText;
    Button Addfeedback, FeedbackImageCancle;
    RoundedImage feedbackImage;
    ProgressBar progressBar;
    TextView addPhoto;

    String feedback;
    StorageReference storageReference;
    public static String PROFILEIMGURL = Feedback.PROFILEIMGURL;


    FirebaseStorage firebaseStorage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        toolbar = findViewById(R.id.FeedbackToolBar);
        feedBackText = findViewById(R.id.FeedbackText);
        Addfeedback = findViewById(R.id.AddFeedback);
        feedbackImage = findViewById(R.id.FeedbackImage);
        FeedbackImageCancle = findViewById(R.id.FeedbackImageCancle);
        progressBar = findViewById(R.id.progressBarFeedback);
        addPhoto = findViewById(R.id.addPhoto);

        progressBar.setVisibility(View.GONE);
        FeedbackImageCancle.setVisibility(View.GONE);









        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);




        feedbackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectImageClick(v);
            }
        });


        FeedbackImageCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                feedbackImage.setImageResource(R.drawable.image_view_bg);
                //doubtImage2.setVisibility(View.GONE);
                addPhoto.setVisibility(View.VISIBLE);

                //imageCancel1.setVisibility(View.GONE);
                FeedbackImageCancle.setVisibility(View.GONE);


                uriImg1 = null;



            }
        });






        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                PROFILEIMGURL = documentSnapshot.getString("ProfileImage");
                final String name;
                final String email;

                final String profilePicture;
                final String user;

                name = documentSnapshot.getString("Name");
                email = documentSnapshot.getString("Email");

                profilePicture = PROFILEIMGURL;
                user = documentSnapshot.getString("User");

                Addfeedback.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
                        feedback  = feedBackText.getText().toString();

                        if (feedBackText.getText().toString().isEmpty()&& uriImg1 == null){
                            feedBackText.requestFocus();
                            feedBackText.setError("At least screenshot or text is required to add your feedback");
                        }else if (uriImg1 != null){
                            progressBar.setVisibility(View.VISIBLE);
                            progressBar.setProgress(100, true);
                            Addfeedback.setEnabled(false);
                            AddFeedback(name, email, feedback, profilePicture,  user);
                        }else {
                            Date date = new Date();
                            Feefbackfirestore(name,email, feedback, profilePicture, "",user,date);
                        }



                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Addfeedback.setEnabled(true);
            }
        });


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
        CropImage.activity().start(Feedback.this);


    }




    @Override
    @SuppressLint({"NewApi", "SetTextI18n"})
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of pick image chooser
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(Objects.requireNonNull(Feedback.this), data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(Feedback.this, imageUri)) {
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

                feedbackImage.setImageURI(mCropImageUri);
                addPhoto.setVisibility(View.GONE);
                FeedbackImageCancle.setVisibility(View.VISIBLE);
                uriImg1 = mCropImageUri;
                //profilePicture.setImageURI(mCropImageUri);
                //SelectPhotoText.setText("Change Photo");











            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(Feedback.this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
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
            Toast.makeText(Feedback.this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }


    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(Feedback.this);
    }


    public void AddFeedback(final String name, final String email, final String feedback, final String profilePicture, final String user){
        //final String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();

        //profile picture upload........\
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        Random random = new Random();
        String uid = random.toString();
        final StorageReference reference = storageReference.child("Feedback/" + uid + "/feedBackScrsht.jpg");



        Bitmap bitmap = null;
        try {


            bitmap = MediaStore.Images.Media.getBitmap(Feedback.this.getContentResolver(), mCropImageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        assert bitmap != null;
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] data = baos.toByteArray();

        reference.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String ProfileURL = uri.toString();
                        Date date = new Date();

                        Feefbackfirestore(name,email,feedback,profilePicture,ProfileURL,user,date);



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Feedback.this, "Failed to upload your feedback", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(Feedback.this, Home.class));
        super.onBackPressed();
    }

    public void Feefbackfirestore(String name, String email, String feedback, String profilePicture, String ProfileURL, String user, Date date){


        FeedbackCarier feedbackCarier = new FeedbackCarier(name, email,feedback, profilePicture, ProfileURL,user, date);
        firebaseFirestore.collection("Feedback/").document().set(feedbackCarier).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                Toast.makeText(Feedback.this, "Thank you for your feedback", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(Feedback.this, Home.class));
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Feedback.this, "Signup Failed", Toast.LENGTH_SHORT).show();
                        System.out.println("Document upload failed");
                        Toast.makeText(Feedback.this, "Failed to upload your feedback", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        Addfeedback.setEnabled(true);
                    }
                });
    }
}