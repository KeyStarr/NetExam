package kstarostin.thumbtack.net.netexam.ui.student.main_screen.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kstarostin.thumbtack.net.netexam.R;
import kstarostin.thumbtack.net.netexam.network.models.Exam;
import kstarostin.thumbtack.net.netexam.network.models.ExamResult;

/**
 * Created by Cyril on 01.05.2017.
 */

public class RecyclerViewExamResultAdapter extends RecyclerView.Adapter<RecyclerViewExamResultAdapter.ViewHolder>{
    private List<String> results;

    public RecyclerViewExamResultAdapter(List<String> results){
        this.results = results;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_exam_result_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewExamResultAdapter.ViewHolder holder, int position) {
        holder.questionNum.setText(String.valueOf(position));
        holder.questionResult.setText(results.get(position));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.question_number)
        TextView questionNum;
        @BindView(R.id.question_result)
        TextView questionResult;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

