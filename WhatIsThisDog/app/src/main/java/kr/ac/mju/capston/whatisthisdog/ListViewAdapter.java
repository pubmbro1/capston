package kr.ac.mju.capston.whatisthisdog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    // 강아지 정보 리스트
    private ArrayList<DogInfo> dogInfoList;

    public ListViewAdapter() {
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

        Context context = parent.getContext();

        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_doginfo, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.iconImageView = (ImageView) convertView.findViewById(R.id.dogImageView) ;
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.txt_name) ;
            viewHolder.descTextView = (TextView) convertView.findViewById(R.id.txt_desc) ;

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        DogInfo item = dogInfoList.get(position);

        if(item.getDogImage().contains("R.drawable")){ //강아지 사진이 drawable, 사전 등록인 경우
            Glide.with(context)
                    .load(R.drawable.test_puppy_icon)
                    .into(viewHolder.iconImageView);
        }
        else { //강아지 사진이 저장소(사용자 앨범)에 있는 경우
            String photoPath = FileManager.getPath() + "/" + item.getDogImage();

            Glide.with(context)
                    .load(photoPath)
                    .into(viewHolder.iconImageView);
        }
        viewHolder.titleTextView.setText(item.getName());
        viewHolder.descTextView.setText(item.getDesc());

        return convertView;
    }


    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return dogInfoList.get(position) ;
    }

    public void addItem(String icon, String name, String desc) {
        DogInfo item = new DogInfo();

        item.setDogImage(icon);
        item.setName(name);
        item.setDesc(desc);

        dogInfoList.add(item);
    }

    public class ViewHolder{
        public ImageView iconImageView;
        public TextView titleTextView;
        public TextView descTextView;
    }


}

