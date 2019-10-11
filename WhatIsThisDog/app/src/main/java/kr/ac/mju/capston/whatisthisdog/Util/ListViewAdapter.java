package kr.ac.mju.capston.whatisthisdog.Util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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

    public void removeItem(DogInfo item) {
        /*

        삭제 코드 추가
         */
        dogInfoList.remove(item);
    }

    private View getAlbumView(int position, View convertView, ViewGroup parent){
        Context context = parent.getContext();

        albumViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(itemResId, parent, false);

            viewHolder = new albumViewHolder();

            viewHolder.iconImageView = (ImageView) convertView.findViewById(R.id.albumDogImageView) ;
            viewHolder.iconImageView.setClipToOutline(true);
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.album_txt_name) ;
            //viewHolder.descTextView = (TextView) convertView.findViewById(R.id.txt_desc) ;

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (albumViewHolder)convertView.getTag();
        }

        DogInfo item = dogInfoList.get(position);

        String photoPath = FileManager.getPath() + "/" + item.getDogImage();

        Glide.with(context)
                .load(photoPath)
                .placeholder(R.drawable.test_puppy_icon)
                .error(R.drawable.test_puppy_icon)
                .override(viewHolder.iconImageView.getWidth())
                .centerCrop()
                .into(viewHolder.iconImageView);

        viewHolder.titleTextView.setText(item.getName());
        //viewHolder.descTextView.setText(item.getDesc());

        return convertView;
    }

    private View getRankView(int position, View convertView, ViewGroup parent){
        Context context = parent.getContext();

        rankViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(itemResId, parent, false);

            viewHolder = new rankViewHolder();

            viewHolder.iconImageView = (ImageView) convertView.findViewById(R.id.rankDogImageView) ;
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.rank_txt_name) ;
            //viewHolder.descTextView = (TextView) convertView.findViewById(R.id.txt_desc) ;

            viewHolder.rankImage = (ImageView) convertView.findViewById(R.id.rankImage);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (rankViewHolder)convertView.getTag();
        }

        DogInfo item = dogInfoList.get(position);

        String photoPath = FileManager.getPath() + "/" + item.getDogImage();

        Glide.with(context)
                .load(photoPath)
                .placeholder(R.drawable.test_puppy_icon)
                .error(R.drawable.test_puppy_icon)
                .override(viewHolder.iconImageView.getWidth())
                .centerCrop()
                .into(viewHolder.iconImageView);

        int res;
        if(position == 0)
            res = R.drawable.first;
        else if(position == 1)
            res = R.drawable.second;
        else
            res = R.drawable.third;

        Glide.with(context)
                .load(res)
                .placeholder(R.drawable.test_puppy_icon)
                .error(R.drawable.test_puppy_icon)
                .into(viewHolder.rankImage);

        viewHolder.titleTextView.setText(item.getName());
        //viewHolder.descTextView.setText(item.getDesc());

        return convertView;
    }

    public class albumViewHolder{
        public ImageView iconImageView;
        public TextView titleTextView;
        //public TextView descTextView;
    }

    public class rankViewHolder{
        public ImageView iconImageView;
        public TextView titleTextView;
        public ImageView rankImage;
        //public TextView descTextView;
    }

}

