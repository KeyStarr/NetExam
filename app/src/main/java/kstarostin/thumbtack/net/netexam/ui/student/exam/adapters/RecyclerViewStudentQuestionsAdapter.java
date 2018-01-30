package kstarostin.thumbtack.net.netexam.ui.student.exam.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kstarostin.thumbtack.net.netexam.R;
import kstarostin.thumbtack.net.netexam.network.models.ExamAnswers;
import kstarostin.thumbtack.net.netexam.network.models.Question;
import kstarostin.thumbtack.net.netexam.network.models.QuestionList;
import kstarostin.thumbtack.net.netexam.ui.base.RecyclerClickListener;

/**
 * Created by Cyril on 01.04.2017.
 */

public class RecyclerViewStudentQuestionsAdapter extends RecyclerView.Adapter<RecyclerViewStudentQuestionsAdapter.QuestionViewHolder> {
    private RecyclerClickListener recyclerClickListener;
    private int questionsCount;
    private View previousItemView;
    private final String questionWord;
    private RecyclerView recyclerView;

    public RecyclerViewStudentQuestionsAdapter(int questionsCount, Context context){
        this.questionsCount = questionsCount;
        questionWord = context.getResources().getString(R.string.question);
    }

    @Override
    public void onAttachedToRecyclerView (RecyclerView recyclerView){
        this.recyclerView = recyclerView;
    }

    @Override
    public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_student_exam_question,parent,false);
        return new QuestionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(QuestionViewHolder holder, int position) {
        if (previousItemView == null && position == 0){
            holder.itemContainer.setSelected(true);
            previousItemView = holder.itemContainer;
        }
        holder.questionNumber.setText(new StringBuilder()
                .append(questionWord)
                .append(position+1));
    }

    @Override
    public int getItemCount() {
        return questionsCount;
    }

    public void setOnItemClickListener(RecyclerClickListener recyclerClickListener) {
        this.recyclerClickListener = recyclerClickListener;
    }

    public boolean performOnClick(int position){
        View currentItemView = recyclerView.getChildAt(position);
        if (previousItemView != null) {
            previousItemView.setSelected(false);
        }
        currentItemView.setSelected(true);
        previousItemView = currentItemView;
        return true;
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.question_number)
        TextView questionNumber;
        @BindView(R.id.item_container)
        RelativeLayout itemContainer;

        QuestionViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.item_container)
        public void click(View v){
            if (previousItemView != null){
                previousItemView.setSelected(false);
            }
            previousItemView = v;
            itemContainer.setSelected(true);
            recyclerClickListener.onItemClick(getAdapterPosition(), v);
        }
    }
}
