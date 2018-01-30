package kstarostin.thumbtack.net.netexam.di;

import javax.inject.Singleton;

import dagger.Component;
import kstarostin.thumbtack.net.netexam.ui.MainActivity;
import kstarostin.thumbtack.net.netexam.ui.base.BaseActivity;
import kstarostin.thumbtack.net.netexam.ui.entry.fragments.AuthorizationFragment;
import kstarostin.thumbtack.net.netexam.ui.base.BaseFragment;
import kstarostin.thumbtack.net.netexam.ui.entry.EntryActivity;
import kstarostin.thumbtack.net.netexam.ui.entry.fragments.RegistrationFragment;
import kstarostin.thumbtack.net.netexam.ui.entry.fragments.RegistrationProfessorFragment;
import kstarostin.thumbtack.net.netexam.ui.entry.fragments.RegistrationStudentFragment;
import kstarostin.thumbtack.net.netexam.ui.student.exam.StudentExamActivity;
import kstarostin.thumbtack.net.netexam.ui.student.main_screen.StudentMainScreenActivity;
import kstarostin.thumbtack.net.netexam.ui.student.main_screen.fragments.AvailableExamsFragment;
import kstarostin.thumbtack.net.netexam.ui.student.main_screen.fragments.ExamResultFragment;
import kstarostin.thumbtack.net.netexam.ui.student.main_screen.fragments.PassedExamsFragment;

/**
 * Created by Cyril on 17.02.2017.
 */

@Singleton
@Component(modules=AppModule.class)
public interface AppComponent {
    void inject (BaseActivity baseActivity);

    void inject (BaseFragment baseFragment);
}
