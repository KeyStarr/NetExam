package kstarostin.thumbtack.net.netexam.ui.student.main_screen.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kstarostin.thumbtack.net.netexam.R;
import kstarostin.thumbtack.net.netexam.network.models.PassedExamsList;
import kstarostin.thumbtack.net.netexam.network.models.PassedExam;
import kstarostin.thumbtack.net.netexam.ui.base.RecyclerClickListener;

/**
 * Created by Cyril on 18.03.2017.
 */

public class RecyclerViewPassedExamsAdapter extends RecyclerView.Adapter<RecyclerViewPassedExamsAdapter.ViewHolder> {

    private PassedExamsList passedExamsList;
    private RecyclerClickListener recyclerClickListener;
    private Context ctx;

    public RecyclerViewPassedExamsAdapter(Context ctx, PassedExamsList passedExamsList){
        this.passedExamsList=passedExamsList;
        this.ctx = ctx;
    }

    @Override
    public RecyclerViewPassedExamsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_passed_exam,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewPassedExamsAdapter.ViewHolder holder, int position) {
        PassedExam passedExam = passedExamsList.getExams().get(position);
        holder.examName.setText(passedExam.getName());
        holder.examScore.setText(passedExam.getScore(ctx));
    }

    @Override
    public int getItemCount() {
        return passedExamsList.getExams().size();
    }

    public void setRecyclerClickListener(RecyclerClickListener listener){
            recyclerClickListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.recycler_view_passed_exam_name)
        TextView examName;
        @BindView(R.id.recycler_view_passed_exam_score)
        TextView examScore;

        ViewHolder (View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.recycler_view_passed_exam_name)
        public void onClick(View v){
            recyclerClickListener.onItemClick(getAdapterPosition(), v);
        }
    }
}
