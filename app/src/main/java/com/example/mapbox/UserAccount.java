package com.example.mapbox;

// 회원가입 사용자 계정 정보 모델 클래스
public class UserAccount {

    private String idToken; //Firebase Uid(고유 토큰 정보)
    private String emailId; // 이메일아이디
    private String password; // 비밀번호
    private String PhoneNumber;
    private String magor;
    private String gender;

    public String getMagor() {
        return magor;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setMagor(String magor) {
        this.magor = magor;
    }

    //firebase 는 빈 생성자를 만들어줘야 오류가 나지 않는다.
    public UserAccount() {
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
