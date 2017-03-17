package com.tayloryan.securecontacts.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.model.ScContact;
import com.tayloryan.securecontacts.widget.AvatarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by taylor.yan on 3/8/17.
 */

public class ContactListAdapter extends BaseAdapter {

    private List<ScContact> mContacts;
    private Context mContext;
    private Map<String,Integer> alphaIndexer;
    private List<String> sections;
    private OnGetAlphaIndexerAndSectionsListener listener;
    private boolean flag;//标志用于只执行一次代码

    public ContactListAdapter(Context context) {
        new ContactListAdapter(context, null);
    }

    public ContactListAdapter(Context context, List<ScContact> contacts) {
        mContext = context;
        mContacts = contacts;
        alphaIndexer = new HashMap<>();
        sections = new ArrayList<>();
        for (int i = 0; i < mContacts.size(); i++) {
            //当前汉语拼音的首字母
            String currentAlpha=mContacts.get(i).getFirstLetterOfName();
            //上一个拼音的首字母，如果不存在则为""
            String previewAlpha = (i-1) >= 0 ? mContacts.get(i-1).getFirstLetterOfName() : "";
            if (!previewAlpha.equals(currentAlpha)){
                String firstAlpha = mContacts.get(i).getFirstLetterOfName();
                alphaIndexer.put(firstAlpha,i);
                sections.add(firstAlpha);
            }
        }

    }

    public void setContacts(List<ScContact> contacts) {
        mContacts = contacts;
    }

    @Override
    public int getCount() {
        //注意：为什么没有直接把回调方法的调用写在构造器中呢？因为构造器只会调用一次，当第一次调用listener的时候是为空的
        //而要初始化listener对象,则需要先去创建对象再去通过对象调用set方法来初始化这个listener对象，再去new对象的时候又要去用到listener产生了矛盾
        //所以放在getCount中调用,只会调用一次,符合需求
        if (!flag){
            if (listener!=null){
                listener.getAlphaIndexerAndSectionsListner(alphaIndexer,sections);
            }
            flag=true;
        }

        return mContacts.size();
    }

    @Override
    public Object getItem(int position) {
        return mContacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        final ContactViewHolder viewHolder;
//        if (null == convertView) {
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.contact_list_item, null);
//            viewHolder = new ContactViewHolder((TextView) convertView.findViewById(R.id.contact_header),
//                    (AvatarView) convertView.findViewById(R.id.contact_avatar), (TextView) convertView.findViewById(R.id.contact_title));
//
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ContactViewHolder) convertView.getTag();
//        }
//
//        ScContact contact = (ScContact) getItem(position);
//
//        if (contact.getPhotoUri() != null) {
//            viewHolder.mAvatarView.setImageURI(contact.getPhotoUri());
//        } else {
//            viewHolder.mAvatarView.setFirstTextString(contact.getFirstTextOfName());
//        }
//        viewHolder.mContactNameText.setText(contact.getName());
//
//        if (position >= 1) {
//            String currentAlpha = contact.getFirstLetterOfName();
//            String previewAlpha = mContacts.get(position - 1).getFirstLetterOfName();
//            if (currentAlpha.equals(previewAlpha)) {
//                viewHolder.mHeaderText.setVisibility(View.GONE);
//            } else {
//                viewHolder.mHeaderText.setText(currentAlpha);
//            }
//        }
        return convertView;
    }

    public void setOnGetAlphaIndeserAndSectionListener(OnGetAlphaIndexerAndSectionsListener listener){
        this.listener=listener;
    }

    public interface OnGetAlphaIndexerAndSectionsListener{
        public void getAlphaIndexerAndSectionsListner(Map<String,Integer>alphaIndexer,List<String>sections);
    }

    static class ContactViewHolder {
        public TextView mHeaderText;
        public AvatarView mAvatarView;
        public TextView mContactNameText;

        public ContactViewHolder(TextView headerText, AvatarView avatarView, TextView contactNameText) {
            mHeaderText = headerText;
            mAvatarView = avatarView;
            mContactNameText = contactNameText;
        }
    }
}
