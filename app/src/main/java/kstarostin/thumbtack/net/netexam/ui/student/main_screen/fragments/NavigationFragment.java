package kstarostin.thumbtack.net.netexam.ui.student.main_screen.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import kstarostin.thumbtack.net.netexam.R;
import kstarostin.thumbtack.net.netexam.events.LogoutEvent;
import kstarostin.thumbtack.net.netexam.events.RefreshExamsClickedEvent;
import kstarostin.thumbtack.net.netexam.events.SnackbarDismissEvent;
import kstarostin.thumbtack.net.netexam.network.models.User;
import kstarostin.thumbtack.net.netexam.ui.base.BaseActivity;
import kstarostin.thumbtack.net.netexam.ui.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationFragment extends BaseFragment implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ic_refresh)
    ImageButton refreshButton;

    private int previousId;

    public NavigationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_screen_navigation, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle data) {
        super.onViewCreated(view, data);
        if (previousId == 0) {
            changeFragment(R.id.exam_container, new AvailableExamsFragment());
            previousId=R.id.available_exams;
        } else{
            changeFragment(R.id.exam_container, new PassedExamsFragment());
            previousId=R.id.passed_exams;
        }
        setToolbar();
        setNavigationDrawer();
        setNavigationViewHeaderInfo();
        setUpdateExams();
    }

    private void setToolbar(){
        refreshButton.setVisibility(View.VISIBLE);
        ((BaseActivity)getActivity()).setSupportActionBar(toolbar);
    }

    @OnClick(R.id.ic_refresh)
    public void refreshClicked(){
        getBus().post(new RefreshExamsClickedEvent());
    }

    private void setNavigationDrawer(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar , R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getBus().post(new SnackbarDismissEvent());
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setNavigationViewHeaderInfo(){
        View header=navigationView.getHeaderView(0);
        TextView nameText=(TextView)header.findViewById(R.id.name_text_view);
        TextView universityText=(TextView)header.findViewById(R.id.university_text_view);
        User user = getPreferences().getUser();
        nameText.setText(new StringBuilder()
                .append(user.getUserInfo().getLastName())
                .append(' ')
                .append(user.getUserInfo().getFirstName()));
        universityText.setText(new StringBuilder()
                .append(user.getUserInfo().getGroup())
                .append(", ")
                .append(user.getUserInfo().getSemester())
                .append(getString(R.string.student_semester)));
    }

    private void setUpdateExams(){
        getPreferences().setUpdatePassedExams(true);
        getPreferences().setUpdateAvailableExams(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.available_exams && previousId!=R.id.available_exams) {
            changeFragment(R.id.exam_container,new AvailableExamsFragment());
        } else if (id == R.id.passed_exams && previousId!=R.id.passed_exams) {
            changeFragment(R.id.exam_container, new PassedExamsFragment());
        } else if (id == R.id.logout) {
            getBus().post(new LogoutEvent());
        }
        previousId=id;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
