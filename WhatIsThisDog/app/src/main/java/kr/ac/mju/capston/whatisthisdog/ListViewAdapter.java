package kr.ac.mju.capston.whatisthisdog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    // 강아지 정보 리스트
    private ArrayList<DogInfo> dogInfoList = new ArrayList<DogInfo>() ;

    public ListViewAdapter() {
    }

    @Override
    public int getCount() {
        return dogInfoList.size() ;
    }

    //리스트 뷰 반복 작성
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_doginfo, parent, false);
        }

        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.dogImageView) ;
        TextView titleTextView = (TextView) convertView.findViewById(R.id.txt_name) ;
        TextView descTextView = (TextView) convertView.findViewById(R.id.txt_desc) ;

        DogInfo item = dogInfoList.get(position);

        iconImageView.setImageDrawable(item.getDogImage());
        titleTextView.setText(item.getName());
        descTextView.setText(item.getDesc());

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

    public void addItem(Drawable icon, String name, String desc) {
        DogInfo item = new DogInfo();

        item.setDogImage(icon);
        item.setName(name);
        item.setDesc(desc);

        dogInfoList.add(item);
    }
}
