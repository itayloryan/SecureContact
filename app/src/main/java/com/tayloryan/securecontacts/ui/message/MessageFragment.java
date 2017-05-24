package com.tayloryan.securecontacts.ui.message;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.adapter.ConversationAdapter;
import com.tayloryan.securecontacts.model.Conversation;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@EFragment(R.layout.fragment_message)
public class MessageFragment extends Fragment {

    public static final Uri MMSSMS_FULL_CONVERSATION_URI = Uri.parse("content://mms-sms/conversations");
    public static final Uri MMSSMS_CONTENT_URI = Uri.parse("content://mms-sms");
    public static final Uri CONVERSATION_URI = MMSSMS_FULL_CONVERSATION_URI.buildUpon().appendQueryParameter("simple", "true").build();

    private ConversationAdapter conversationAdapter;

    public static MessageFragment create() {
        return new MessageFragment_();
    }

    @ViewById(R.id.message_list)
    protected ListView conversationListView;
    @ViewById(R.id.btn_new_message)
    protected ImageButton newMessageButton;

    @AfterViews
    protected void afterViews() {
        loadMessage();
        conversationListView.setOnItemClickListener(clickListener);
    }

    @Background
    protected void loadMessage() {
        List<Conversation> conversations = new ArrayList<>();
        ContentResolver resolver = getContext().getContentResolver();
        Cursor cursor = resolver.query(CONVERSATION_URI, null, null, null, Telephony.Mms.DEFAULT_SORT_ORDER);

        while (cursor.moveToNext()) {
            Conversation conversation = new Conversation();
            conversation.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            conversation.setSnippet(cursor.getString(cursor.getColumnIndex("snippet")));
            conversation.setMessageCount(cursor.getInt(cursor.getColumnIndex("message_count")));
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                conversation.setUnReadCount(cursor.getInt(cursor.getColumnIndex("unread_count")));
            } else {
                conversation.setUnReadCount(conversation.getMessageCount() - cursor.getInt(cursor.getColumnIndex("readcount")));
            }
            long date = cursor.getLong(cursor.getColumnIndex("date"));
            Calendar current = Calendar.getInstance();
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);

            current.setTimeInMillis(date);
            if (current.after(today)) {
                conversation.setDate(new SimpleDateFormat("HH:mm").format(new Date(date)));
            } else if (current.get(Calendar.YEAR) < today.get(Calendar.YEAR)) {
                conversation.setDate(new SimpleDateFormat("yyyy年").format(new Date(date)));
            } else {
                conversation.setDate(new SimpleDateFormat("MM月dd日").format(new Date(date)));
            }


            int recipientId = cursor.getInt(cursor.getColumnIndex("recipient_ids"));
            Uri uri = Uri.withAppendedPath(MMSSMS_CONTENT_URI, "canonical-addresses");
            String selection = "_id = ?";
            String[] selectionArgs = new String[] { String.valueOf(recipientId) };
            Cursor phoneCursor = resolver.query(uri, null, selection, selectionArgs, null);
            if (phoneCursor.moveToNext()) {
                String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex("address"));
                conversation.setPhoneNumber(phoneNumber);
                Cursor contactCursor = resolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.NUMBER + " = ?",
                new String[] { phoneNumber }, null);
                if (contactCursor.moveToNext()) {
                    String name = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    conversation.setReceiver(name);
                } else {
                    conversation.setReceiver(phoneNumber);
                }
                phoneCursor.close();
                contactCursor.close();
            }



            conversations.add(conversation);
        }

        displayConversations(conversations);
    }

    @UiThread
    protected void displayConversations(List<Conversation> conversations) {
        conversationAdapter = new ConversationAdapter(getContext(), conversations);
        conversationListView.setAdapter(conversationAdapter);
        conversationAdapter.notifyDataSetChanged();

    }

    private AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Conversation conversation = (Conversation) conversationAdapter.getItem(position);
            Intent intent = new Intent(getActivity(), MessageDetailActivity_.class);
            intent.putExtra("EXTRA_NAME",conversation.getReceiver());
            intent.putExtra("EXTRA_PHONE",conversation.getPhoneNumber());
            intent.putExtra("EXTRA_THREAD",conversation.getId());
            startActivity(intent);
        }
    };
}
