package com.example.yangchenghuan.android_item_test4;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.example.yangchenghuan.myListener.RecyclerItemClickListener;
import com.example.yangchenghuan.myUtils.StudentInfo;
import com.example.yangchenghuan.myUtils.UserUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PersonalActivity extends Activity {
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    JSONObject info;
    List<List<String>> listdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        mRecyclerView = (RecyclerView) this.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, onItemClickListener));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //   mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        mAdapter = new MyAdapter(this);
        setData();
        mRecyclerView.setAdapter(mAdapter);
    }
    private void setData(){
         info = UserUtil.readData(PersonalActivity.this,
                UserUtil.STUDENT_INFO_FILE_NAME);
         listdata=new ArrayList<List<String>>();
      String datas[]=new String[]{StudentInfo.id,StudentInfo.name,StudentInfo.grade,StudentInfo.specialty,StudentInfo.cardID,StudentInfo.mail,
              StudentInfo.teacher,StudentInfo.instructor,StudentInfo.hasCredit,
              StudentInfo.crediting,StudentInfo.willCredit,StudentInfo.point,
              StudentInfo.requiredCourseSum,StudentInfo.requiredCourseHas,StudentInfo.selectiveCourseHas
           };
      String texts[]=getResources().getStringArray(R.array.personaldata);

        try {
            for(int i=0;i<datas.length;i++) {
                List<String> list1 = new ArrayList<>();
                list1.add(texts[i]);
                list1.add(info.getString(datas[i]));

                listdata.add(list1);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_personal, menu);
        return true;
    }


    private RecyclerItemClickListener.OnItemClickListener onItemClickListener = new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
      //      Wheel wheel = mAdapter.getWheel(position);
//            Intent intent = new Intent(PersonalActivity.this, wheelDetailActivity.class);
//            intent.putExtra("wheel", wheel);
//
//            ActivityOptionsCompat options =
//                    ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
//                            view.findViewById(R.id.ivBook), getString(R.string.transition_book_img));
//
//            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());

        }
    };

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private final int mBackground;
  //      private List<Wheel> mWheels = new ArrayList<Wheel>();
       private final TypedValue mTypedValue = new TypedValue();

        private static final int ANIMATED_ITEMS_COUNT = 4;

        private boolean animateItems = false;
        private int lastAnimatedPosition = -1;

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(Context context) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView tvTitle;
            public TextView tvDesc;

            public int position;

            public ViewHolder(View v) {
                super(v);
                tvTitle = (TextView) v.findViewById(R.id.tvTitle);
                tvDesc = (TextView) v.findViewById(R.id.tvDesc);
            }
        }


        private void runEnterAnimation(View view, int position) {
            if (!animateItems || position >= ANIMATED_ITEMS_COUNT - 1) {
                return;
            }

            if (position > lastAnimatedPosition) {
                lastAnimatedPosition = position;
              //  view.setTranslationY(Utils.getScreenHeight(getActivity()));
                view.animate()
                        .translationY(0)
                        .setStartDelay(100 * position)
                        .setInterpolator(new DecelerateInterpolator(3.f))
                        .setDuration(700)
                        .start();
            }
        }


//        public void updateItems(List<Wheel> wheels, boolean animated) {
//            animateItems = animated;
//            lastAnimatedPosition = -1;
//            mWheels.addAll(wheels);
//            notifyDataSetChanged();
//        }
//
//        public void clearItems() {
//            mWheels.clear();
//            notifyDataSetChanged();
//        }


        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.personal_item, parent, false);
           //   v.setBackgroundResource(mBackground);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            runEnterAnimation(holder.itemView, position);
            List<String> data = listdata.get(position);
            holder.tvTitle.setText(""+data.get(0)+"\t");

            holder.tvDesc.setText(""+data.get(1));

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return listdata.size();
        }


        public List<String> getList(int pos) {
            return listdata.get(pos);
        }
    }

}
