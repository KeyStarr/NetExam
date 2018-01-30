package kstarostin.thumbtack.net.netexam.ui;

import android.content.Intent;
import android.os.Bundle;

import kstarostin.thumbtack.net.netexam.ui.base.BaseActivity;
import kstarostin.thumbtack.net.netexam.ui.entry.EntryActivity;
import kstarostin.thumbtack.net.netexam.ui.student.exam.StudentExamActivity;
import kstarostin.thumbtack.net.netexam.ui.student.main_screen.StudentMainScreenActivity;

import static kstarostin.thumbtack.net.netexam.ui.entry.EntryActivity.RESULT_CODE_AUTHORIZED;
import static kstarostin.thumbtack.net.netexam.ui.entry.EntryActivity.RESULT_CODE_REFRESH_TOKEN_NET_ERROR;
import static kstarostin.thumbtack.net.netexam.ui.student.exam.StudentExamActivity.RESULT_CODE_EXAM_FINISH;
import static kstarostin.thumbtack.net.netexam.ui.student.main_screen.StudentMainScreenActivity.RESULT_CODE_EXAM_FINISH_CANCEL;
import static kstarostin.thumbtack.net.netexam.ui.student.main_screen.StudentMainScreenActivity.RESULT_CODE_STUDENT_EXAM_START;
import static kstarostin.thumbtack.net.netexam.ui.student.main_screen.StudentMainScreenActivity.RESULT_CODE_STUDENT_LOGOUT;

public class MainActivity extends BaseActivity {

    public static final int REQUEST_CODE_ENTRY = 112;
    public static final int REQUEST_CODE_STUDENT_MAIN_SCREEN = 144;
    public static final int REQUEST_CODE_STUDENT_EXAM = 248;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getPreferences().getExamQuestionList()!=null){
            //then exam is in progress
            startStudentExam();
        } else {
            startEntry();
        }
    }

    private void startEntry(){
        Intent intent = new Intent(this, EntryActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ENTRY);
    }

    private void startStudentMainScreen(){
        Intent intent = new Intent (this, StudentMainScreenActivity.class);
        startActivityForResult(intent, REQUEST_CODE_STUDENT_MAIN_SCREEN);
    }

    private void startStudentExam(){
        Intent intent = new Intent (this, StudentExamActivity.class);
        startActivityForResult(intent, REQUEST_CODE_STUDENT_EXAM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case REQUEST_CODE_ENTRY:
                switch (resultCode) {
                    case RESULT_CODE_AUTHORIZED:
                        startStudentMainScreen();
                        break;
                    case RESULT_CODE_REFRESH_TOKEN_NET_ERROR:
                        startStudentMainScreen();
                        break;
                    case RESULT_CANCELED:
                        finish();
                        break;
                }
            case REQUEST_CODE_STUDENT_MAIN_SCREEN:
                switch (resultCode){
                    case RESULT_CODE_STUDENT_LOGOUT:
                        startEntry();
                        break;
                    case RESULT_CODE_STUDENT_EXAM_START:
                        startStudentExam();
                        break;
                    case RESULT_CODE_EXAM_FINISH_CANCEL:
                        startStudentExam();
                        break;
                    case RESULT_CANCELED:
                        finish();
                        break;
            }
            case REQUEST_CODE_STUDENT_EXAM:
                switch (resultCode){
                    case RESULT_CODE_EXAM_FINISH:
                        startStudentMainScreen();
                        break;
                }
        }
    }
}
