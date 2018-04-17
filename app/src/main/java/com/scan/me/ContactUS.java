package com.scan.me;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactUS extends AppCompatActivity {

    @BindView(R.id.add)
    FloatingActionButton mAddQuestion;
    @BindView(R.id.recycle)
    RecyclerView mBlogList;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Questions");
        mDatabase.keepSynced(true);
        mBlogList.setHasFixedSize(true);
        user = FirebaseAuth.getInstance().getCurrentUser();
        LinearLayoutManager layoutManager = new LinearLayoutManager(ContactUS.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mBlogList.setLayoutManager(layoutManager);

        mAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddQuestion alert = new AddQuestion();
                alert.showDialog(ContactUS.this);
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Questions, ContactUS.BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Questions, ContactUS.BlogViewHolder>(
                Questions.class,
                R.layout.question_row,
                ContactUS.BlogViewHolder.class,
                mDatabase
        ){
            @Override
            protected void populateViewHolder(final ContactUS.BlogViewHolder viewHolder, Questions model, int position) {
                final String post_key = getRef(position).getKey();
                viewHolder.setQuestion(model.getQuestion());
            }
        };
        mBlogList.setAdapter(firebaseRecyclerAdapter);

    }
    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;
        FirebaseUser user;
        DatabaseReference mDatabase;
        FirebaseAuth mAuth;
        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Questions");
            mAuth = FirebaseAuth.getInstance();
            user = FirebaseAuth.getInstance().getCurrentUser();
        }
        public void setQuestion(String question){
            TextView question_txt = (TextView) mView.findViewById(R.id.question);
            question_txt.setText(question);
        }
    }
}
