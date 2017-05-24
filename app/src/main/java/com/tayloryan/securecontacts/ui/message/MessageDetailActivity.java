package com.tayloryan.securecontacts.ui.message;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.adapter.MessageAdapter;
import com.tayloryan.securecontacts.model.Message;
import com.tayloryan.securecontacts.ui.BaseActivity;
import com.tayloryan.securecontacts.util.StringUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.provider.Telephony.Mms.DEFAULT_SORT_ORDER;

@EActivity(R.layout.activity_message_detail)
public class MessageDetailActivity extends BaseActivity {

    @ViewById(R.id.back_button)
    protected ImageView backBtn;

    @ViewById(R.id.receiver_text)
    protected TextView receiverText;

    @ViewById(R.id.call_btn)
    protected ImageView callBtn;

    @ViewById(R.id.add_message)
    protected ImageView addMessageBtn;

    @ViewById(R.id.message_list)
    protected ListView messageList;

    @ViewById(R.id.message_text)
    protected EditText messageText;

    @ViewById(R.id.send_btn)
    protected ImageView sendBtn;

    private String receiverName, receiverPhone;
    private int threadId;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiverName = getIntent().getStringExtra("EXTRA_NAME");
        receiverPhone = getIntent().getStringExtra("EXTRA_PHONE");
        threadId = getIntent().getIntExtra("EXTRA_THREAD", -1);
    }

    @AfterViews
    protected void afterViews() {
        initialView();
        initialData();
    }

    @Background
    protected void initialData() {
        List<Message> messages = new ArrayList<>();
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(Telephony.Sms.CONTENT_URI, null, "thread_id = ?", new String[]{String.valueOf(threadId)}, "date ASC");
        while (cursor.moveToNext()) {
            Message message = new Message();
            message.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            Date date = new Date(cursor.getLong(cursor.getColumnIndex("date")));
            message.setDate(StringUtil.getFormatTime(date));
            message.setBody(cursor.getString(cursor.getColumnIndex("body")));
            message.setType(cursor.getInt(cursor.getColumnIndex("type")));
            message.setRead(1 == cursor.getInt(cursor.getColumnIndex("read")));
            messages.add(message);
        }
        cursor.close();
        displayMessages(messages);
    }

    @UiThread
    protected void displayMessages(List<Message> messages) {
        messageAdapter = new MessageAdapter(this, messages);
        messageList.setAdapter(messageAdapter);
        messageAdapter.notifyDataSetChanged();
    }

    private void initialView() {
        receiverText.setText(receiverName == null ? receiverPhone : receiverName);
    }

    @Click({R.id.back_button, R.id.call_btn, R.id.send_btn, R.id.add_message})
    protected void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.call_btn:
                callTo(receiverPhone);
                break;
            case R.id.send_btn:
                String messageBody = messageText.getText().toString().trim();
                if (TextUtils.isEmpty(messageBody)) {
                    showToast("请输入短信内容", Toast.LENGTH_SHORT);
                    return;
                }
                Message message = new Message();
                message.setAddress(receiverPhone);
                message.setBody(messageBody);
                message.setType(2);
                message.setDate(StringUtil.getFormatTime(new Date()));
                sendMessage(message);
                messageAdapter.addMessage(message);
                messageAdapter.notifyDataSetChanged();
                break;
            case R.id.add_message:
                break;
        }
    }

    protected void sendMessage(final Message message) {
        String SENT_SMS_ACTION = "SENT_SMS_ACTION";
        SmsManager smsManager = SmsManager.getDefault();
        List<String> divideContents = smsManager.divideMessage(message.getBody());
        Intent sentIntent = new Intent(SENT_SMS_ACTION);
        PendingIntent sentPI = PendingIntent.getBroadcast(MessageDetailActivity.this, 0, sentIntent, 0);
        for (String text : divideContents) {
             smsManager.sendTextMessage(message.getAddress(), null, text, sentPI, null);
        }
        MessageDetailActivity.this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    default:
                        message.setType(5);
                        messageAdapter.notifyDataSetChanged();
                        break;
                }

            }
        }, new IntentFilter(SENT_SMS_ACTION));
    }

}
