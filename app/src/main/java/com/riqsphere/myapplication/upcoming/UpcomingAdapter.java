package com.riqsphere.myapplication.upcoming;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.riqsphere.myapplication.R;

import java.util.ArrayList;

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<UpcomingModel> mList;
    public UpcomingAdapter(Context context, ArrayList<UpcomingModel> list){
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        (holder).BindView(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RoundedImageView card_AnimeImage;
        TextView card_AnimeTittle;
        TextView card_EpTittle;
        TextView card_EpNum;
        TextView card_Time;


        public ViewHolder(View itemView){
            super(itemView);
            card_AnimeImage = itemView.findViewById(R.id.uc_image);
            card_AnimeTittle = itemView.findViewById(R.id.uc_anime_tittle);
            card_EpTittle = itemView.findViewById(R.id.uc_ep_tittle);
            card_EpNum = itemView.findViewById(R.id.uc_ep_num);
            card_Time = itemView.findViewById(R.id.uc_time);
        }

        public void BindView (int position){
            UpcomingModel ucCard = mList.get(position);

            card_AnimeImage.setImageResource(ucCard.getImage());
            card_AnimeTittle.setText(ucCard.getAnimeTittle());
            card_EpTittle.setText(ucCard.getEpTittle());
            card_EpNum.setText(ucCard.getEpNum());
            card_Time.setText(ucCard.getTime());
        }
    }
}
