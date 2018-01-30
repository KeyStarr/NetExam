package kstarostin.thumbtack.net.netexam.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import kstarostin.thumbtack.net.netexam.network.models.Credentials;
import kstarostin.thumbtack.net.netexam.network.models.Exam;
import kstarostin.thumbtack.net.netexam.network.models.ExamAnswers;
import kstarostin.thumbtack.net.netexam.network.models.ExamsList;
import kstarostin.thumbtack.net.netexam.network.models.PassedExamsList;
import kstarostin.thumbtack.net.netexam.network.models.QuestionList;
import kstarostin.thumbtack.net.netexam.network.models.User;

/**
 * Created by Cyril on 29.03.2017.
 */

public class Preferences {
    private static final String APP_PREFERENCES = "net_exam_settings";
    private static final String APP_PREFERENCES_USER = "prefs_user";
    private static final String APP_PREFERENCES_CREDENTIALS = "prefs_credentials";
    private static final String APP_PREFERENCES_TOKEN = "prefs_token";
    private static final String APP_PREFERENCES_STUDENT_AVAILABLE_EXAMS = "prefs_available_exams";
    private static final String APP_PREFERENCES_STUDENT_PASSED_EXAMS = "prefs_passed_exams";
    private static final String APP_PREFERENCES_STUDENT_IS_UPDATE_AVAILABLE_EXAMS = "prefs_update_available";
    private static final String APP_PREFERENCES_STUDENT_IS_UPDATE_PASSED_EXAMS = "prefs_update_passed";
    private static final String EXAM_PREFERENCES = "net_exam_exam_settings";
    private static final String EXAM_PREFERENCES_STUDENT_EXAM_END_TIME = "prefs_exam_time";
    private static final String EXAM_PREFERENCES_STUDENT_EXAM  = "prefs_exam";
    private static final String EXAM_PREFERENCES_STUDENT_EXAM_QUESTION_LIST = "prefs_exam_question_list";
    private static final String EXAM_PREFERENCES_STUDENT_EXAM_ANSWERS = "prefs_exam_answers";
    private static final String EXAM_PREFERENCES_STUDENT_EXAM_FINISHING = "prefs_exam_finishing";

    private SharedPreferences appSettings;
    private SharedPreferences examSettings;
    private Gson gson;

    public Preferences(Context context){
        appSettings = context.getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE);
        examSettings = context.getSharedPreferences(EXAM_PREFERENCES,Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public User getUser(){
        String json = appSettings.getString(APP_PREFERENCES_USER, null);
        return gson.fromJson(json,User.class);
    }

    public Credentials getCredentials() {
        //Saving password only for testing purposes
        String json = appSettings.getString(APP_PREFERENCES_CREDENTIALS, null);
        return gson.fromJson(json, Credentials.class);
    }

    public String getToken(){
        //Token is also stored in user json but
        //Since i need to rewrite token few times
        //I don't want to explicitly rewrite whole user
        //Just for updating one field
        return appSettings.getString(APP_PREFERENCES_TOKEN, null);
    }

    public ExamsList getAvailableExams() {
        String json = appSettings.getString(APP_PREFERENCES_STUDENT_AVAILABLE_EXAMS, null);
        return gson.fromJson(json, ExamsList.class);
    }

    public PassedExamsList getPassedExams() {
        String json = appSettings.getString(APP_PREFERENCES_STUDENT_PASSED_EXAMS, null);
        return gson.fromJson(json, PassedExamsList.class);
    }
    
    public boolean isUpdatePassedExams(){
        return appSettings.getBoolean(APP_PREFERENCES_STUDENT_IS_UPDATE_PASSED_EXAMS, true);
    }
    
    public boolean isUpdateAvailableExams(){
        return appSettings.getBoolean(APP_PREFERENCES_STUDENT_IS_UPDATE_AVAILABLE_EXAMS, true);
    }

    public boolean isExamFinishing(){
        return examSettings.getBoolean(EXAM_PREFERENCES_STUDENT_EXAM_FINISHING, false);
    }

    public long getExamEndTime(){
        return examSettings.getLong(EXAM_PREFERENCES_STUDENT_EXAM_END_TIME, 0);
    }

    public Exam getExam(){
        String json = appSettings.getString(EXAM_PREFERENCES_STUDENT_EXAM, null);
        return gson.fromJson(json, Exam.class);
    }

    public QuestionList getExamQuestionList() {
        String json = examSettings.getString(EXAM_PREFERENCES_STUDENT_EXAM_QUESTION_LIST, null);
        return gson.fromJson(json, QuestionList.class);
    }

    public ExamAnswers getExamAnswers() {
        String json = examSettings.getString(EXAM_PREFERENCES_STUDENT_EXAM_ANSWERS, null);
        return gson.fromJson(json, ExamAnswers.class);
    }

    public void setUser(User user){
        String json = gson.toJson(user, User.class);
        appSettings.edit().putString(APP_PREFERENCES_USER, json).apply();
    }

    public void setCredentials(Credentials creds){
        String json = gson.toJson(creds, Credentials.class);
        appSettings.edit().putString(APP_PREFERENCES_CREDENTIALS, json).apply();
    }

    public void setToken(String token){
        appSettings.edit().putString(APP_PREFERENCES_TOKEN, token).apply();
    }

    public void setAvailableExams(ExamsList exams) {
        String json = gson.toJson(exams, ExamsList.class);
        appSettings.edit().putString(APP_PREFERENCES_STUDENT_AVAILABLE_EXAMS, json).apply();
    }

    public void setPassedExams(PassedExamsList passedExams) {
        String json = gson.toJson(passedExams, PassedExamsList.class);
        appSettings.edit().putString(APP_PREFERENCES_STUDENT_PASSED_EXAMS, json).apply();
    }

    public void setUpdateAvailableExams(boolean value) {
        appSettings.edit().putBoolean(APP_PREFERENCES_STUDENT_IS_UPDATE_AVAILABLE_EXAMS, value).apply();
    }

    public void setUpdatePassedExams(boolean value) {
        appSettings.edit().putBoolean(APP_PREFERENCES_STUDENT_IS_UPDATE_PASSED_EXAMS, value).apply();
    }

    public void setExamEndTime(long value){
        examSettings.edit().putLong(EXAM_PREFERENCES_STUDENT_EXAM_END_TIME, value).apply();
    }

    public void setExam(Exam exam){
        String json = gson.toJson(exam, Exam.class);
        appSettings.edit().putString(EXAM_PREFERENCES_STUDENT_EXAM, json).apply();
    }

    public void setExamQuestionList(QuestionList questionList) {
        String json = gson.toJson(questionList, QuestionList.class);
        examSettings.edit().putString(EXAM_PREFERENCES_STUDENT_EXAM_QUESTION_LIST, json).apply();
    }

    public void setExamAnswers(ExamAnswers answers) {
        String json = gson.toJson(answers, ExamAnswers.class);
        examSettings.edit().putString(EXAM_PREFERENCES_STUDENT_EXAM_ANSWERS, json).apply();
    }

    public void setExamFinishing(boolean finishing){
        examSettings.edit().putBoolean(EXAM_PREFERENCES_STUDENT_EXAM_FINISHING, finishing).apply();
    }

    public void clearExam(){
        examSettings.edit().clear().apply();
    }

    public void clearAll (){
        clearExam();
        appSettings.edit().clear().apply();
    }

    public void clearCredentials(){
        setCredentials(null);
    }
}
