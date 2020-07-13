package com.example.firstproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SimpleTextAdapter extends RecyclerView.Adapter<SimpleTextAdapter.Holder> {

    public interface OnListItemLongSelectedInterface {
        void onItemLongSelected(View v, int position);
    }
    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }
    private OnListItemSelectedInterface mListener;
    private OnListItemLongSelectedInterface mLongListener;


    private Context context;
    private List<Item> list = new ArrayList<>();

    public SimpleTextAdapter(Context context, List<Item> list, OnListItemSelectedInterface listener, OnListItemLongSelectedInterface longListener) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
        this.mLongListener = longListener;
    }


    // ViewHolder 생성
    // row layout을 화면에 뿌려주고 holder에 연결
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    /*
     * Todo 만들어진 ViewHolder에 data 삽입 ListView의 getView와 동일
     *
     * */
    @Override
    public void onBindViewHolder(Holder holder, int position) {
        // 각 위치에 문자열 세팅
        int itemposition = position;
        holder.nameText.setText(list.get(itemposition).getName());
        holder.numberText.setText(list.get(itemposition).getNumber());
        //Log.e("StudyApp", "onBindViewHolder" + itemposition);

//        View view;
//        holder.itemView.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                Context context=view.getContext();
//                Intent intent = new Intent(context,MoreInfo.class);
        //putextra 만들어주기
        //               context.startActivity(intent);
        //           }
        //       });
    }

    @Override
    public int getItemCount() {
        return list.size(); // RecyclerView의 size return
    }

    public class Holder extends RecyclerView.ViewHolder{
        public TextView nameText;
        public TextView numberText;

        public Holder(View view){
            super(view);
            nameText = (TextView) view.findViewById(R.id.name_tv);
            numberText = (TextView) view.findViewById(R.id.number_tv);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Tab1", "onClick"+ getAdapterPosition());
                    int position = getAdapterPosition();
                    mListener.onItemSelected(v, position);
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.d("Tab1", "onLongClick"+ getAdapterPosition());
                    mLongListener.onItemLongSelected(v, getAdapterPosition());
                    return false;
                }
            });
        }
    }
}