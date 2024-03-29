package com.cproz.pantomath_teacher.TeacherProfile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cproz.pantomath_teacher.Home.HomeDoubtAdapter;
import com.cproz.pantomath_teacher.Home.HomeDoubtData;
import com.cproz.pantomath_teacher.Home.HomeFragment;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.cproz.pantomath_teacher.Home.HomeFragment.BOARD;
import static com.cproz.pantomath_teacher.Home.HomeFragment.CLASS;

public class TeacherProfileFragment extends Fragment {


    Toolbar toolbar;
    CircleImageView profilePicture, editPhotoButt;
    TextView UserName, Subjectx;
    RecyclerView recyclerView;
    ImageView setting, noResults;

    ProfileDoubtsAdapter profileDoubtsAdapter;
    private List<HomeDoubtData> DoubtList2;
    private HomeDoubtData homeDoubtData;
    private FirebaseFirestore db;
    String ProfileUrl;
    Button solved, unsolved;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    String email = user != null ? user.getEmail() : null;
    private DocumentReference ref = firebaseFirestore.collection("Users/Teachers/Teacherinfo/" ).document(String.valueOf(email));

    String decision;
    Uri mCropImageUri;
    FirebaseStorage firebaseStorage;

    String Status = "Solved";

    String Subject;
    String SSC, CBSE;
    String class9, class10;
    RadioGroup radioGroup;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile_fragment, container, false);


        Initialisation(root);






        firebaseStorage = FirebaseStorage.getInstance();


        db = FirebaseFirestore.getInstance();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        DoubtList2 = new ArrayList<>();

        noResults.setVisibility(View.GONE);

        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                BOARD = documentSnapshot.getString("Board");
                CLASS = documentSnapshot.getString("STD");
                Subject = documentSnapshot.getString("Subject");
                UserName.setText(toTitleCase(Objects.requireNonNull(documentSnapshot.getString("Name"))));
                ProfileUrl = documentSnapshot.getString("profileURl");


                assert ProfileUrl != null;
                if (ProfileUrl.equals("")){
                    profilePicture.setImageResource(R.drawable.personal_info);
                }else{
                    Picasso.get().load(ProfileUrl).into(profilePicture);
                }

                Subjectx.setText(toTitleCase(Subject));

                Doubts("Solved");




                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId){
                            case R.id.Solvedbutt:


                                DoubtList2.clear();
                                Doubts("Solved");
                                solved.setEnabled(false);
                                unsolved.setEnabled(false);


                                break;

                            case R.id.Unsolvedbutt:

                                Status = "Unsolved";
                                DoubtList2.clear();
                                Doubts("Unsolved");
                                unsolved.setEnabled(false);
                                solved.setEnabled(false);

                                break;
                        }
                    }
                });





            }
        });



        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Settings.class));
            }
        });








        return root;

    }

    public void Initialisation(View root){
        toolbar = root.findViewById(R.id.StudentProfileToolBar);
        profilePicture = root.findViewById(R.id.ProfilePicture_profile);

        recyclerView = root.findViewById(R.id.recyclerViewProfile);
        UserName = root.findViewById(R.id.UserNameProfile);
        Subjectx = root.findViewById(R.id.ClassAndBoard_Profile);
        setting = root.findViewById(R.id.settingsProfile);
        solved = root.findViewById(R.id.Solvedbutt);
        unsolved = root.findViewById(R.id.Unsolvedbutt);
        noResults = root.findViewById(R.id.noResults);
        radioGroup = root.findViewById(R.id.scrollHorLayout);



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




//    public void Decision(String Status){
//
//        if (BOARD.equals("Both")){
//
//            SSC = "SSC";
//            CBSE = "CBSE";
//
//            if (CLASS.equals("Both")){
//
//                //Both board Both class
//                DoubtList2.clear();
//                //BothBoardBothClass(Status);
//                class9 = "9th";
//                class10 = "10th";
//
//            }
//            else{
//                //Both board 1 class
//                DoubtList2.clear();
//                BothBoardOneClass(Status);
//            }
//
//        }
//        else{
//
//            if (CLASS.equals("Both")){
//
//
//                //1 board Both class
//                DoubtList2.clear();
//                OneBoardBothClass(Status);
//
//
//                class9 = "9th";
//                class10 = "10th";
//
//            }
//            else{
//                //1 board 1 class
//                DoubtList2.clear();
//                OneBoardOneClass(Status);
//
//            }
//
//        }
//
//
//    }

























    public void Doubts(String Status) {


        db.collection("Doubts").whereEqualTo("Subject", Subject).whereEqualTo("Status", Status).orderBy("DateTime", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot querySnapshot : Objects.requireNonNull(task.getResult())) {


                    //Date date = new Date();

                    String email = user.getEmail();

                    if (Objects.equals(querySnapshot.getString("TeacherEmail"), email) && !Objects.equals(querySnapshot.getString("Status"), "Reported")){





                        if (BOARD.equals("SSC, CBSE & ICSE") && CLASS.equals("9th & 10th")){
                            if ((Objects.equals(querySnapshot.getString("STD"), "9th") || Objects.equals(querySnapshot.getString("STD"), "10th")) &&
                                    (Objects.equals(querySnapshot.getString("Board"), "SSC")||Objects.equals(querySnapshot.getString("Board"), "CBSE")||
                                            Objects.equals(querySnapshot.getString("Board"), "ICSE"))){
                                homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
                                        querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
                                        querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
                                        querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
                                        querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
                                        querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
                                        , querySnapshot.getDate("DateTime"), querySnapshot.getString("TeacherImageUrl"),querySnapshot.getString("TeacherEmail"));

                                DoubtList2.add(homeDoubtData);
                            }
                        }else if(BOARD.equals("SSC & CBSE") && CLASS.equals("9th & 10th")){
                            if ((Objects.equals(querySnapshot.getString("STD"), "9th") || Objects.equals(querySnapshot.getString("STD"), "10th") )&&
                                    (Objects.equals(querySnapshot.getString("Board"), "SSC")||Objects.equals(querySnapshot.getString("Board"), "CBSE"))){
                                homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
                                        querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
                                        querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
                                        querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
                                        querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
                                        querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
                                        , querySnapshot.getDate("DateTime"), querySnapshot.getString("TeacherImageUrl"),querySnapshot.getString("TeacherEmail"));

                                DoubtList2.add(homeDoubtData);
                            }
                        }else if (BOARD.equals("CBSE & ICSE") && CLASS.equals("9th & 10th")){
                            if ((Objects.equals(querySnapshot.getString("STD"), "9th") || Objects.equals(querySnapshot.getString("STD"), "10th")) &&
                                    (Objects.equals(querySnapshot.getString("Board"), "CBSE")||Objects.equals(querySnapshot.getString("Board"), "ICSE"))){
                                homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
                                        querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
                                        querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
                                        querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
                                        querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
                                        querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
                                        , querySnapshot.getDate("DateTime"), querySnapshot.getString("TeacherImageUrl"),querySnapshot.getString("TeacherEmail"));

                                DoubtList2.add(homeDoubtData);
                            }
                        }else if (BOARD.equals("SSC & ICSE") && CLASS.equals("9th & 10th")){
                            if ((Objects.equals(querySnapshot.getString("STD"), "9th") || Objects.equals(querySnapshot.getString("STD"), "10th")) &&
                                    (Objects.equals(querySnapshot.getString("Board"), "SSC")||Objects.equals(querySnapshot.getString("Board"), "ICSE"))){
                                homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
                                        querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
                                        querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
                                        querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
                                        querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
                                        querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
                                        , querySnapshot.getDate("DateTime"), querySnapshot.getString("TeacherImageUrl"),querySnapshot.getString("TeacherEmail"));

                                DoubtList2.add(homeDoubtData);
                            }
                        }else if (BOARD.equals("SSC") && CLASS.equals("9th & 10th")){
                            if ((Objects.equals(querySnapshot.getString("STD"), "9th") || Objects.equals(querySnapshot.getString("STD"), "10th")) &&
                                    (Objects.equals(querySnapshot.getString("Board"), "SSC"))){
                                homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
                                        querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
                                        querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
                                        querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
                                        querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
                                        querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
                                        , querySnapshot.getDate("DateTime"), querySnapshot.getString("TeacherImageUrl"),querySnapshot.getString("TeacherEmail"));

                                DoubtList2.add(homeDoubtData);
                            }
                        }else if (BOARD.equals("CBSE") && CLASS.equals("9th & 10th")){
                            if ((Objects.equals(querySnapshot.getString("STD"), "9th") || Objects.equals(querySnapshot.getString("STD"), "10th")) &&
                                    (Objects.equals(querySnapshot.getString("Board"), "CBSE"))){
                                homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
                                        querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
                                        querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
                                        querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
                                        querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
                                        querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
                                        , querySnapshot.getDate("DateTime"), querySnapshot.getString("TeacherImageUrl"),querySnapshot.getString("TeacherEmail"));

                                DoubtList2.add(homeDoubtData);
                            }
                        }else if (BOARD.equals("ICSE") && CLASS.equals("9th & 10th")){
                            if ((Objects.equals(querySnapshot.getString("STD"), "9th") || Objects.equals(querySnapshot.getString("STD"), "10th")) &&
                                    (Objects.equals(querySnapshot.getString("Board"), "ICSE"))){
                                homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
                                        querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
                                        querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
                                        querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
                                        querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
                                        querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
                                        , querySnapshot.getDate("DateTime"), querySnapshot.getString("TeacherImageUrl"),querySnapshot.getString("TeacherEmail"));

                                DoubtList2.add(homeDoubtData);
                            }
                        }else
                        if (BOARD.equals("SSC, CBSE & ICSE") && CLASS.equals("9th")){
                            if ((Objects.equals(querySnapshot.getString("STD"), "9th"))  &&
                                    (Objects.equals(querySnapshot.getString("Board"), "SSC")||Objects.equals(querySnapshot.getString("Board"), "CBSE")||
                                            Objects.equals(querySnapshot.getString("Board"), "ICSE"))){
                                homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
                                        querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
                                        querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
                                        querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
                                        querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
                                        querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
                                        , querySnapshot.getDate("DateTime"), querySnapshot.getString("TeacherImageUrl"),querySnapshot.getString("TeacherEmail"));

                                DoubtList2.add(homeDoubtData);
                            }
                        }else if(BOARD.equals("SSC & CBSE") && CLASS.equals("9th")){
                            if ((Objects.equals(querySnapshot.getString("STD"), "9th")) &&
                                    (Objects.equals(querySnapshot.getString("Board"), "SSC")||Objects.equals(querySnapshot.getString("Board"), "CBSE"))){
                                homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
                                        querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
                                        querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
                                        querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
                                        querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
                                        querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
                                        , querySnapshot.getDate("DateTime"), querySnapshot.getString("TeacherImageUrl"),querySnapshot.getString("TeacherEmail"));

                                DoubtList2.add(homeDoubtData);
                            }
                        }else if (BOARD.equals("CBSE & ICSE") && CLASS.equals("9th")){
                            if ((Objects.equals(querySnapshot.getString("STD"), "9th") ) &&
                                    (Objects.equals(querySnapshot.getString("Board"), "CBSE")||Objects.equals(querySnapshot.getString("Board"), "ICSE"))){
                                homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
                                        querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
                                        querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
                                        querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
                                        querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
                                        querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
                                        , querySnapshot.getDate("DateTime"), querySnapshot.getString("TeacherImageUrl"),querySnapshot.getString("TeacherEmail"));

                                DoubtList2.add(homeDoubtData);
                            }
                        }else if (BOARD.equals("SSC & ICSE") && CLASS.equals("9th")){
                            if ((Objects.equals(querySnapshot.getString("STD"), "9th")) &&
                                    (Objects.equals(querySnapshot.getString("Board"), "SSC")||Objects.equals(querySnapshot.getString("Board"), "ICSE"))){
                                homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
                                        querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
                                        querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
                                        querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
                                        querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
                                        querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
                                        , querySnapshot.getDate("DateTime"), querySnapshot.getString("TeacherImageUrl"),querySnapshot.getString("TeacherEmail"));

                                DoubtList2.add(homeDoubtData);
                            }
                        }else if (BOARD.equals("SSC") && CLASS.equals("9th")){
                            if ((Objects.equals(querySnapshot.getString("STD"), "9th"))&&
                                    (Objects.equals(querySnapshot.getString("Board"), "SSC"))){
                                homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
                                        querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
                                        querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
                                        querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
                                        querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
                                        querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
                                        , querySnapshot.getDate("DateTime"), querySnapshot.getString("TeacherImageUrl"),querySnapshot.getString("TeacherEmail"));

                                DoubtList2.add(homeDoubtData);
                            }
                        }else if (BOARD.equals("CBSE") && CLASS.equals("9th")){
                            if ((Objects.equals(querySnapshot.getString("STD"), "9th"))&&
                                    (Objects.equals(querySnapshot.getString("Board"), "CBSE"))){
                                homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
                                        querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
                                        querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
                                        querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
                                        querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
                                        querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
                                        , querySnapshot.getDate("DateTime"), querySnapshot.getString("TeacherImageUrl"),querySnapshot.getString("TeacherEmail"));

                                DoubtList2.add(homeDoubtData);
                            }
                        }else if (BOARD.equals("ICSE") && CLASS.equals("9th")){
                            if ((Objects.equals(querySnapshot.getString("STD"), "9th"))&&
                                    (Objects.equals(querySnapshot.getString("Board"), "ICSE"))){
                                homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
                                        querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
                                        querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
                                        querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
                                        querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
                                        querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
                                        , querySnapshot.getDate("DateTime"), querySnapshot.getString("TeacherImageUrl"),querySnapshot.getString("TeacherEmail"));

                                DoubtList2.add(homeDoubtData);
                            }
                        }else
                        if (BOARD.equals("SSC, CBSE & ICSE") && CLASS.equals("10th")){
                            if ((Objects.equals(querySnapshot.getString("STD"), "10th"))  &&
                                    (Objects.equals(querySnapshot.getString("Board"), "SSC")||Objects.equals(querySnapshot.getString("Board"), "CBSE")||
                                            Objects.equals(querySnapshot.getString("Board"), "ICSE"))){
                                homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
                                        querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
                                        querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
                                        querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
                                        querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
                                        querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
                                        , querySnapshot.getDate("DateTime"), querySnapshot.getString("TeacherImageUrl"),querySnapshot.getString("TeacherEmail"));

                                DoubtList2.add(homeDoubtData);
                            }
                        }else if(BOARD.equals("SSC & CBSE") && CLASS.equals("10th")){
                            if ((Objects.equals(querySnapshot.getString("STD"), "10th")) &&
                                    (Objects.equals(querySnapshot.getString("Board"), "SSC")||Objects.equals(querySnapshot.getString("Board"), "CBSE"))){
                                homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
                                        querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
                                        querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
                                        querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
                                        querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
                                        querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
                                        , querySnapshot.getDate("DateTime"), querySnapshot.getString("TeacherImageUrl"),querySnapshot.getString("TeacherEmail"));

                                DoubtList2.add(homeDoubtData);
                            }
                        }else if (BOARD.equals("CBSE & ICSE") && CLASS.equals("10th")){
                            if ((Objects.equals(querySnapshot.getString("STD"), "10th"))  &&
                                    (Objects.equals(querySnapshot.getString("Board"), "CBSE")||Objects.equals(querySnapshot.getString("Board"), "ICSE"))){
                                homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
                                        querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
                                        querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
                                        querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
                                        querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
                                        querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
                                        , querySnapshot.getDate("DateTime"), querySnapshot.getString("TeacherImageUrl"),querySnapshot.getString("TeacherEmail"));

                                DoubtList2.add(homeDoubtData);
                            }
                        }else if (BOARD.equals("SSC & ICSE") && CLASS.equals("10th")){
                            if ((Objects.equals(querySnapshot.getString("STD"), "10th") )&&
                                    (Objects.equals(querySnapshot.getString("Board"), "SSC")||Objects.equals(querySnapshot.getString("Board"), "ICSE"))){
                                homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
                                        querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
                                        querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
                                        querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
                                        querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
                                        querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
                                        , querySnapshot.getDate("DateTime"), querySnapshot.getString("TeacherImageUrl"),querySnapshot.getString("TeacherEmail"));

                                DoubtList2.add(homeDoubtData);
                            }
                        }else if (BOARD.equals("SSC") && CLASS.equals("10th")){
                            if ((Objects.equals(querySnapshot.getString("STD"), "10th"))&&
                                    (Objects.equals(querySnapshot.getString("Board"), "SSC"))){
                                homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
                                        querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
                                        querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
                                        querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
                                        querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
                                        querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
                                        , querySnapshot.getDate("DateTime"), querySnapshot.getString("TeacherImageUrl"),querySnapshot.getString("TeacherEmail"));

                                DoubtList2.add(homeDoubtData);
                            }
                        }else if (BOARD.equals("CBSE") && CLASS.equals("10th")){
                            if ((Objects.equals(querySnapshot.getString("STD"), "10th"))&&
                                    (Objects.equals(querySnapshot.getString("Board"), "CBSE"))){
                                homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
                                        querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
                                        querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
                                        querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
                                        querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
                                        querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
                                        , querySnapshot.getDate("DateTime"), querySnapshot.getString("TeacherImageUrl"),querySnapshot.getString("TeacherEmail"));

                                DoubtList2.add(homeDoubtData);
                            }
                        }else if (BOARD.equals("ICSE") && CLASS.equals("10th")){
                            if ((Objects.equals(querySnapshot.getString("STD"), "10th"))&&
                                    (Objects.equals(querySnapshot.getString("Board"), "ICSE"))){
                                homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
                                        querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
                                        querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
                                        querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
                                        querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
                                        querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
                                        , querySnapshot.getDate("DateTime"), querySnapshot.getString("TeacherImageUrl"),querySnapshot.getString("TeacherEmail"));

                                DoubtList2.add(homeDoubtData);
                            }
                        }
                    }



                    profileDoubtsAdapter = new ProfileDoubtsAdapter(getContext(), DoubtList2);


                    recyclerView.setItemViewCacheSize(40);

                    recyclerView.setAdapter(profileDoubtsAdapter);


                }
                if (DoubtList2.isEmpty()){
                    recyclerView.setAlpha(0);
                    noResults.setVisibility(View.VISIBLE);

                }else{
                    recyclerView.setAlpha(1);
                    noResults.setVisibility(View.GONE);
                }

                solved.setEnabled(true);
                unsolved.setEnabled(true);
            }
        });
    }




//    public void BothBoardOneClass(String Status) {
//
//
//        db.collection("Doubts").whereEqualTo("Subject", Subject).whereEqualTo("STD", CLASS).whereEqualTo("Status", Status).orderBy("DateTime", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                for (QueryDocumentSnapshot querySnapshot : Objects.requireNonNull(task.getResult())) {
//
//
//                    //Date date = new Date();
//
//                    String email = user.getEmail();
//                    if (Objects.equals(querySnapshot.getString("TeacherEmail"), email)){
//                        homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
//                                querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
//                                querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
//                                querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
//                                querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
//                                querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
//                                , querySnapshot.getDate("DateTime"), querySnapshot.getString("TeacherImageUrl"),querySnapshot.getString("TeacherEmail"));
//
//                        DoubtList2.add(homeDoubtData);
//                    }
//
//
//                    profileDoubtsAdapter = new ProfileDoubtsAdapter(getContext(), DoubtList2);
//
//
//                    recyclerView.setItemViewCacheSize(40);
//
//                    recyclerView.setAdapter(profileDoubtsAdapter);
//
//
//                }
//                if (DoubtList2.isEmpty()){
//                    recyclerView.setAlpha(0);
//                    noResults.setVisibility(View.VISIBLE);
//                }else{
//                    recyclerView.setAlpha(1);
//                    noResults.setVisibility(View.GONE);
//                }
//                solved.setEnabled(true);
//                unsolved.setEnabled(true);
//            }
//        });
//    }
//
//
//
//    public void OneBoardBothClass(String Status) {
//
//
//        db.collection("Doubts").whereEqualTo("Subject", Subject).whereEqualTo("Board", BOARD).whereEqualTo("Status", Status).orderBy("DateTime", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                for (QueryDocumentSnapshot querySnapshot : Objects.requireNonNull(task.getResult())) {
//
//
//                    //Date date = new Date();
//
//                    String email = user.getEmail();
//                    if (Objects.equals(querySnapshot.getString("TeacherEmail"), email)){
//                        homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
//                                querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
//                                querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
//                                querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
//                                querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
//                                querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
//                                , querySnapshot.getDate("DateTime"), querySnapshot.getString("TeacherImageUrl"),querySnapshot.getString("TeacherEmail"));
//
//                        DoubtList2.add(homeDoubtData);
//                    }
//
//                    profileDoubtsAdapter = new ProfileDoubtsAdapter(getContext(), DoubtList2);
//
//
//                    recyclerView.setItemViewCacheSize(40);
//
//                    recyclerView.setAdapter(profileDoubtsAdapter);
//
//
//                }
//                if (DoubtList2.isEmpty()){
//                    recyclerView.setAlpha(0);
//                    noResults.setVisibility(View.VISIBLE);
//                }else{
//                    recyclerView.setAlpha(1);
//                    noResults.setVisibility(View.GONE);
//                }
//                solved.setEnabled(true);
//                unsolved.setEnabled(true);
//            }
//        });
//    }
//
//
//    public void OneBoardOneClass(String Status) {
//
//
//        db.collection("Doubts").whereEqualTo("Subject", Subject).whereEqualTo("Board", BOARD).whereEqualTo("STD", CLASS).whereEqualTo("Status", Status).orderBy("DateTime", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                for (QueryDocumentSnapshot querySnapshot : Objects.requireNonNull(task.getResult())) {
//
//
//                    //Date date = new Date();
//
//                    String email = user.getEmail();
//                    if (Objects.equals(querySnapshot.getString("TeacherEmail"), email)){
//                        homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
//                                querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
//                                querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
//                                querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
//                                querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
//                                querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
//                                , querySnapshot.getDate("DateTime"), querySnapshot.getString("TeacherImageUrl"),querySnapshot.getString("TeacherEmail"));
//
//                        DoubtList2.add(homeDoubtData);
//                    }
//
//
//                    profileDoubtsAdapter = new ProfileDoubtsAdapter(getContext(), DoubtList2);
//
//
//                    recyclerView.setItemViewCacheSize(40);
//
//                    recyclerView.setAdapter(profileDoubtsAdapter);
//
//
//                }
//                if (DoubtList2.isEmpty()){
//                    recyclerView.setAlpha(0);
//                    noResults.setVisibility(View.VISIBLE);
//                }else{
//                    recyclerView.setAlpha(1);
//                    noResults.setVisibility(View.GONE);
//                }
//                solved.setEnabled(true);
//                unsolved.setEnabled(true);
//            }
//        });
//    }



}
