package com.cproz.pantomath_teacher.Feedback;

import java.util.Date;

public class FeedbackCarier {

    public String Name, Email, Feedback, ProfilePicture, FeedbackImage, User;
    public Date FeedbackTime;

    public FeedbackCarier(String name, String email, String feedback, String profilePicture, String feedbackImage, String user, Date feedbackTime) {
        Name = name;
        Email = email;
        Feedback = feedback;
        ProfilePicture = profilePicture;
        FeedbackImage = feedbackImage;
        User = user;
        FeedbackTime = feedbackTime;
    }
}
