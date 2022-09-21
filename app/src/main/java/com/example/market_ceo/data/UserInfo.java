package com.example.market_ceo.data;

public class UserInfo {

    private String mb_idx; // 회원 idx
    private String mb_id; // 회원 아이디
    private String mb_name; // 회원 이름
    private String mb_email; // 회원 이메일
    private String mb_hp; // 회원 핸드폰번호
    private String mb_saupja; // 사업자번호
    private String mb_com_name; // 법인명
    private String mb_uptae; // 업태
    private String mb_jongmok; // 종목
    private String mb_bank; // 은행
    private String mb_bank_num; // 계좌번호
    private String mb_account_holder; // 예금주
    private String mb_level; // 회원 등급
    private String mb_status; // 회원 상태 1:사용중 |  0:탈퇴처리
    private String access_token;
    private String access_id;
    private String sp_bcode; //가게 동 코드

    public UserInfo(UserInfo uInfo) {

    }

    public String getMb_idx() {
        return mb_idx;
    }

    public void setMb_idx(String mb_idx) {
        this.mb_idx = mb_idx;
    }

    public String getMb_id() {
        return mb_id;
    }

    public void setMb_id(String mb_id) {
        this.mb_id = mb_id;
    }

    public String getMb_name() {
        return mb_name;
    }

    public void setMb_name(String mb_name) {
        this.mb_name = mb_name;
    }

    public String getMb_email() {
        return mb_email;
    }

    public void setMb_email(String mb_email) {
        this.mb_email = mb_email;
    }

    public String getMb_hp() {
        return mb_hp;
    }

    public void setMb_hp(String mb_hp) {
        this.mb_hp = mb_hp;
    }

    public String getMb_saupja() {
        return mb_saupja;
    }

    public void setMb_saupja(String mb_saupja) {
        this.mb_saupja = mb_saupja;
    }

    public String getMb_uptae() {
        return mb_uptae;
    }

    public void setMb_uptae(String mb_uptae) {
        this.mb_uptae = mb_uptae;
    }

    public String getMb_jongmok() {
        return mb_jongmok;
    }

    public void setMb_jongmok(String mb_jongmok) {
        this.mb_jongmok = mb_jongmok;
    }

    public String getMb_bank() {
        return mb_bank;
    }

    public void setMb_bank(String mb_bank) {
        this.mb_bank = mb_bank;
    }

    public String getMb_bank_num() {
        return mb_bank_num;
    }

    public void setMb_bank_num(String mb_bank_num) {
        this.mb_bank_num = mb_bank_num;
    }

    public String getMb_account_holder() {
        return mb_account_holder;
    }

    public void setMb_account_holder(String mb_account_holder) {
        this.mb_account_holder = mb_account_holder;
    }

    public String getMb_level() {
        return mb_level;
    }

    public void setMb_level(String mb_level) {
        this.mb_level = mb_level;
    }

    public String getMb_status() {
        return mb_status;
    }

    public void setMb_status(String mb_status) {
        this.mb_status = mb_status;
    }

    public String getMb_com_name() {
        return mb_com_name;
    }

    public void setMb_com_name(String mb_com_name) {
        this.mb_com_name = mb_com_name;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getAccess_id() {
        return access_id;
    }

    public void setAccess_id(String access_id) {
        this.access_id = access_id;
    }

    public String getSp_bcode() {
        return sp_bcode;
    }

    public void setSp_bcode(String sp_bcode) {
        this.sp_bcode = sp_bcode;
    }
}
