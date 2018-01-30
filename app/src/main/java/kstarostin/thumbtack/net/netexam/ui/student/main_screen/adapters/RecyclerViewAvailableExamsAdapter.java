package kstarostin.thumbtack.net.netexam.ui.student.main_screen.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kstarostin.thumbtack.net.netexam.R;
import kstarostin.thumbtack.net.netexam.network.models.Exam;
import kstarostin.thumbtack.net.netexam.network.models.ExamsList;
import kstarostin.thumbtack.net.netexam.ui.base.RecyclerClickListener;

/**
 * Created by Cyril on 12.03.2017.
 */

public class RecyclerViewAvailableExamsAdapter extends RecyclerView.Adapter<RecyclerViewAvailableExamsAdapter.ViewHolder> {

    private RecyclerClickListener recyclerClickListener;
    private ExamsList examsList;

    public RecyclerViewAvailableExamsAdapter(ExamsList examsList){
        this.examsList=examsList;
    }

    @Override
    public RecyclerViewAvailableExamsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_exam,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAvailableExamsAdapter.ViewHolder holder, int position) {
        Exam exam = examsList.getExams().get(position);
        holder.examName.setText(exam.getName());
    }

    @Override
    public int getItemCount() {
        return examsList.getExams().size();
    }

    public Exam getItem(int position){
        return examsList.getExams().get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.recycler_view_exam_name)
        TextView examName;

        ViewHolder (View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @OnClick(R.id.recycler_view_exam_name)
        public void onClick (View v){
            recyclerClickListener.onItemClick(getAdapterPosition(), v);
        }

    }

    public void setOnItemClickListener(RecyclerClickListener recyclerClickListener) {
        this.recyclerClickListener = recyclerClickListener;
    }
}

