package com.cproz.pantomath_teacher.Notifications;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cproz.pantomath_teacher.Home.HomeDoubtAdapter;
import com.cproz.pantomath_teacher.Home.HomeDoubtData;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private List<HomeDoubtData> DoubtList2;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseFirestore db;
    String Subject;
    String SSC, CBSE;
    String class9, class10;
    SwipeRefreshLayout swipeRefreshLayout;

    HomeDoubtData homeDoubtData;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    String email = user != null ? user.getEmail() : null;
    private DocumentReference ref = firebaseFirestore.collection("Users/Teachers/Teacherinfo/" ).document(String.valueOf(email));
    String BOARD, CLASS;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.notification_fragment, container, false);

        Initialisation(root);
        DoubtList2 = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        db = FirebaseFirestore.getInstance();
        swipeRefreshLayout = root.findViewById(R.id.swipeNotification);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);

        recyclerView.setLayoutManager(gridLayoutManager);
        swipeRefreshLayout.setColorSchemeResources(R.color.blue);

        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {


                BOARD = documentSnapshot.getString("Board");
                CLASS = documentSnapshot.getString("STD");
                Subject = documentSnapshot.getString("Subject");


                Notifications();

                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        swipeRefreshLayout.setEnabled(false);
                        Notifications();


                        swipeRefreshLayout.setRefreshing(false);
                    }
                });





            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Not Found", Toast.LENGTH_SHORT).show();
            }
        });



        return root;
    }

    public void Initialisation(View root){

        recyclerView = root.findViewById(R.id.recyclerviewNotification);


    }

////    public void Decision(){
////
////        if (BOARD.equals("Both")){
////
////            SSC = "SSC";
////            CBSE = "CBSE";
////
////            if (CLASS.equals("Both")){
////
////                //Both board Both class
////                DoubtList2.clear();
////                //BothBoardBothClass();
////
////                class9 = "9th";
////                class10 = "10th";
////
////            }
////            else{
////                //Both board 1 class
////                DoubtList2.clear();
////                BothBoardOneClass();
////
////            }
////
////        }
////        else{
////
////            if (CLASS.equals("Both")){
////
////
////                //1 board Both class
////                DoubtList2.clear();
////                OneBoardBothClass();
////
////
////
////                class9 = "9th";
////                class10 = "10th";
////
////            }
////            else{
////                //1 board 1 class
////                DoubtList2.clear();
////                OneBoardOneClass();
////
////
////            }
////
////        }
//
//
//
//
//
//    }

    public void Notifications() {


        db.collection("Doubts").whereEqualTo("Subject", Subject).orderBy("DateTime", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot querySnapshot : Objects.requireNonNull(task.getResult())) {


                    //Date date = new Date();


                    if (!Objects.equals(querySnapshot.getString("Status"), "Reported")){
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


                    notificationAdapter = new NotificationAdapter(getContext(), DoubtList2);


                    recyclerView.setItemViewCacheSize(40);

                    recyclerView.setAdapter(notificationAdapter);


                }
                swipeRefreshLayout.setEnabled(true);
            }
        });
    }




//    public void BothBoardOneClass() {
//
//
//        db.collection("Doubts").whereEqualTo("Subject", Subject).whereEqualTo("STD", CLASS).orderBy("DateTime", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                for (QueryDocumentSnapshot querySnapshot : Objects.requireNonNull(task.getResult())) {
//
//
//                    //Date date = new Date();
//
//
//                    homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
//                            querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
//                            querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
//                            querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
//                            querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
//                            querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
//                            , querySnapshot.getDate("DateTime"), querySnapshot.getString("TeacherImageUrl"),querySnapshot.getString("TeacherEmail"));
//
//                    DoubtList2.add(homeDoubtData);
//
//
//                    notificationAdapter = new NotificationAdapter(getContext(), DoubtList2);
//
//
//                    recyclerView.setItemViewCacheSize(40);
//
//                    recyclerView.setAdapter(notificationAdapter);
//
//
//                }
//            }
//        });
//    }
//
//
//
//    public void OneBoardBothClass() {
//
//
//        db.collection("Doubts").whereEqualTo("Subject", Subject).whereEqualTo("Board", BOARD).orderBy("DateTime", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                for (QueryDocumentSnapshot querySnapshot : Objects.requireNonNull(task.getResult())) {
//
//
//                    //Date date = new Date();
//
//
//                    homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
//                            querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
//                            querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
//                            querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
//                            querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
//                            querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
//                            , querySnapshot.getDate("DateTime"), querySnapshot.getString("TeacherImageUrl"),querySnapshot.getString("TeacherEmail"));
//
//                    DoubtList2.add(homeDoubtData);
//
//
//
//
//                    notificationAdapter = new NotificationAdapter(getContext(), DoubtList2);
//
//
//                    recyclerView.setItemViewCacheSize(40);
//
//                    recyclerView.setAdapter(notificationAdapter);
//
//
//                }
//            }
//        });
//    }
//
//
//    public void OneBoardOneClass() {
//
//
//        db.collection("Doubts").whereEqualTo("Subject", Subject).whereEqualTo("Board", BOARD).whereEqualTo("STD", CLASS).orderBy("DateTime", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                for (QueryDocumentSnapshot querySnapshot : Objects.requireNonNull(task.getResult())) {
//
//
//                    //Date date = new Date();
//
//
//                    homeDoubtData = new HomeDoubtData(querySnapshot.getString("AnsPhotoUrl1"), querySnapshot.getString("AnsPhotoUrl2"), querySnapshot.getString("AnsText"),
//                            querySnapshot.getString("AudioUrl"), querySnapshot.getString("Board"), querySnapshot.getString("Chapter"),
//                            querySnapshot.getString("Email"), querySnapshot.getString("FileUrl"), querySnapshot.getString("Link"),
//                            querySnapshot.getString("Name"), querySnapshot.getString("Photo1url"), querySnapshot.getString("Photo2url"),
//                            querySnapshot.getString("ProfileImageURL"), querySnapshot.getString("QText"), querySnapshot.getString("STD"),
//                            querySnapshot.getString("Status"), querySnapshot.getString("Subject"), querySnapshot.getString("Teacher"), querySnapshot.getString("Uid")
//                            , querySnapshot.getDate("DateTime"), querySnapshot.getString("TeacherImageUrl"),querySnapshot.getString("TeacherEmail"));
//
//                    DoubtList2.add(homeDoubtData);
//
//
//                    notificationAdapter = new NotificationAdapter(getContext(), DoubtList2);
//
//
//                    recyclerView.setItemViewCacheSize(40);
//
//                    recyclerView.setAdapter(notificationAdapter);
//
//
//                }
//            }
//        });
//    }
}