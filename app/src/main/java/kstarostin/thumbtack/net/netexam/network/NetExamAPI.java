package kstarostin.thumbtack.net.netexam.network;

import kstarostin.thumbtack.net.netexam.network.models.Credentials;
import kstarostin.thumbtack.net.netexam.network.models.ExamAnswers;
import kstarostin.thumbtack.net.netexam.network.models.ExamResult;
import kstarostin.thumbtack.net.netexam.network.models.ExamsList;
import kstarostin.thumbtack.net.netexam.network.models.PassedExamsList;
import kstarostin.thumbtack.net.netexam.network.models.QuestionList;
import kstarostin.thumbtack.net.netexam.network.models.User;
import kstarostin.thumbtack.net.netexam.network.models.UserInfo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Cyril on 17.02.2017.
 */

public interface NetExamAPI {
    @POST("api/teacher")
    Call <User> registerProfessor(@Body UserInfo userInfo);
    @POST("api/student")
    Call <User> registerStudent(@Body UserInfo userInfo);
    @POST("api/session")
    Call <User> authorize (@Body Credentials login);
    @GET("api/exams")
    Call <ExamsList> studentGetExams(@Header("token") String token);
    @GET("api/exams/solutions")
    Call <PassedExamsList> studentGetResults(@Header("token") String token);
    @GET("api/exams/{id}/questions")
    Call <QuestionList> studentGetExamQuestions(@Path("id")int id, @Header("token") String token);
    @POST("api/exams/{id}/solutions")
    Call <ExamResult> studentSendExamSolution(@Path("id")int id,
                          @Header("token") String token, @Body ExamAnswers answers);
}