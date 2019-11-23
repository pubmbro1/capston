package kr.ac.mju.capston.whatisthisdog.Util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import kr.ac.mju.capston.whatisthisdog.Data.DogInfo;
import kr.ac.mju.capston.whatisthisdog.R;

public class ListViewAdapter extends BaseAdapter {
    // 강아지 정보 리스트
    private ArrayList<DogInfo> dogInfoList;
    private int itemResId;

    public ListViewAdapter(int itemResId) {
        this.itemResId = itemResId;
        dogInfoList = new ArrayList<>();
    }

    public void setList(ArrayList<DogInfo> list){
        dogInfoList = list;
    }

    public ArrayList<DogInfo> getDogInfoList() {
        return dogInfoList;
    }

    @Override
    public int getCount() {
        return dogInfoList.size() ;
    }

    //리스트 뷰 반복 작성
    //뷰 홀더 패턴 적용 시 성능 향상
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(itemResId == R.layout.listview_album_item)
            return getAlbumView(position, convertView, parent);
        else if(itemResId == R.layout.listview_rank_item)
            return getRankView(position, convertView, parent);
        else{
            Log.d("ListviewAdapter", String.valueOf(position) + "//resId : " + String.valueOf(itemResId));
            return null; //ERROR
        }
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return dogInfoList.get(position) ;
    }

    private View getAlbumView(int position, View convertView, ViewGroup parent){
        Context context = parent.getContext();

        albumViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(itemResId, parent, false);

            viewHolder = new albumViewHolder();

            viewHolder.iconImageView = (ImageView) convertView.findViewById(R.id.albumDogImageView) ;
            viewHolder.name = (TextView) convertView.findViewById(R.id.album_name) ;
            viewHolder.matchRate = (TextView) convertView.findViewById(R.id.album_rate) ;
            viewHolder.date = (TextView) convertView.findViewById(R.id.album_date) ;
            viewHolder.select = (ImageView) convertView.findViewById(R.id.select);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (albumViewHolder)convertView.getTag();
        }


        DogInfo item = dogInfoList.get(position);
        String photoPath = FileManager.getPath() + "/" + item.getDogImage();

        viewHolder.iconImageView.setClipToOutline(true);
        Glide.with(context)
                .load(photoPath)
                .placeholder(R.drawable.test_puppy_icon)
                .error(R.drawable.icon_sadpuppy)
                .centerCrop()
                .into(viewHolder.iconImageView);

        viewHolder.name.setText(item.getName());
        viewHolder.matchRate.setText(item.getMatchRate() + "%");
        viewHolder.date.setText(item.getDogImage());

        return convertView;
    }

    private View getRankView(int position, View convertView, ViewGroup parent){
        Context context = parent.getContext();

        rankViewHolder viewHolder;

        DogInfo item = dogInfoList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(itemResId, parent, false);

            viewHolder = new rankViewHolder();

            viewHolder.iconImageView = (ImageView) convertView.findViewById(R.id.rankDogImageView) ;
            viewHolder.rankImage = (ImageView) convertView.findViewById(R.id.rankImage);
            viewHolder.name = (TextView) convertView.findViewById(R.id.rank_name) ;
            viewHolder.matchRate = (TextView) convertView.findViewById(R.id.rank_rate) ;

            int resId = context.getResources().getIdentifier(item.getDogImage() , "drawable", context.getPackageName());
            Glide.with(context)
                    .load(resId)
                    .placeholder(R.drawable.test_puppy_icon)
                    .error(R.drawable.icon_sadpuppy)
                    .fitCenter()
                    .into(viewHolder.iconImageView);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (rankViewHolder)convertView.getTag();
        }

        int res;
        if(position == 0)
            res = R.drawable.icon_first;
        else if(position == 1)
            res = R.drawable.icon_second;
        else
            res = R.drawable.icon_third;

        Glide.with(context)
                .load(res)
                .into(viewHolder.rankImage);

        viewHolder.name.setText(item.getName());
        viewHolder.matchRate.setText(item.getMatchRate() + "%");

        return convertView;
    }

    public class albumViewHolder{
        public ImageView iconImageView;
        public TextView name;
        public TextView matchRate;
        public TextView date;
        public ImageView select;
    }

    public class rankViewHolder{
        public ImageView iconImageView;
        public ImageView rankImage;
        public TextView name;
        public TextView matchRate;
    }

}