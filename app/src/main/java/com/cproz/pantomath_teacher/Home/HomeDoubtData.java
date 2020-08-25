package com.cproz.pantomath_teacher.Home;

import java.util.Date;

public class HomeDoubtData {


    String AnsPhotoUrl1, AnsPhotoUrl2, AnsText, AudioUrl, Board, Chapter,Email, FileUrl, Link, Name, Photo1url, Photo2url, ProfileImageURL, QText, STD, Status, Subject, Teacher, Uid, TeacherImageUrl,
            TeacherEmail;
    Date DateTime;

    public String getTeacherImageUrl() {
        return TeacherImageUrl;
    }

    public String getTeacherEmail() {
        return TeacherEmail;
    }

    public HomeDoubtData(String ansPhotoUrl1, String ansPhotoUrl2, String ansText, String audioUrl,
                         String board, String chapter, String email, String fileUrl, String link, String name,
                         String photo1url, String photo2url, String profileImageURL, String QText, String STD,
                         String status, String subject, String teacher, String uid, Date dateTime, String TeacherImageUrl, String TeacherEmail) {
        AnsPhotoUrl1 = ansPhotoUrl1;
        AnsPhotoUrl2 = ansPhotoUrl2;
        AnsText = ansText;
        AudioUrl = audioUrl;
        Board = board;
        Chapter = chapter;
        Email = email;
        FileUrl = fileUrl;
        Link = link;
        Name = name;
        Photo1url = photo1url;
        Photo2url = photo2url;
        ProfileImageURL = profileImageURL;
        this.QText = QText;
        this.STD = STD;
        Status = status;
        Subject = subject;
        Teacher = teacher;
        Uid = uid;
        DateTime = dateTime;
        this.TeacherImageUrl = TeacherImageUrl;
        this.TeacherEmail = TeacherEmail;
    }

    public String getAnsPhotoUrl1() {
        return AnsPhotoUrl1;
    }

    public String getAnsPhotoUrl2() {
        return AnsPhotoUrl2;
    }

    public String getAnsText() {
        return AnsText;
    }

    public String getAudioUrl() {
        return AudioUrl;
    }

    public String getBoard() {
        return Board;
    }

    public String getChapter() {
        return Chapter;
    }

    public String getEmailHome() {
        return Email;
    }

    public String getFileUrl() {
        return FileUrl;
    }

    public String getLink() {
        return Link;
    }

    public String getNameHome() {
        return Name;
    }

    public String getPhoto1url() {
        return Photo1url;
    }

    public String getPhoto2url() {
        return Photo2url;
    }

    public String getProfileImageURL() {
        return ProfileImageURL;
    }

    public String getQText() {
        return QText;
    }

    public String getSTD() {
        return STD;
    }

    public String getStatus() {
        return Status;
    }

    public String getSubject() {
        return Subject;
    }

    public String getTeacher() {
        return Teacher;
    }

    public String getUid() {
        return Uid;
    }

    public Date getDateTime() {
        return DateTime;
    }
}
