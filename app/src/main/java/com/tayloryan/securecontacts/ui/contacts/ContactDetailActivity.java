package com.tayloryan.securecontacts.ui.contacts;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.event.ReloadContactsEvent;
import com.tayloryan.securecontacts.model.Address;
import com.tayloryan.securecontacts.model.Email;
import com.tayloryan.securecontacts.model.PhoneNumber;
import com.tayloryan.securecontacts.model.ScContact;
import com.tayloryan.securecontacts.service.ContactService;
import com.tayloryan.securecontacts.ui.BaseActivity;
import com.tayloryan.securecontacts.util.DialogUtil;
import com.tayloryan.securecontacts.util.MD5Util;
import com.tayloryan.securecontacts.widget.EncryptDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;

@EActivity(R.layout.activity_contact_detail)
public class ContactDetailActivity extends BaseActivity {

    @ViewById(R.id.close_image)
    protected ImageView mCloseImage;

    @ViewById(R.id.contact_name)
    protected TextView mContactNameText;

    @ViewById(R.id.contact_job)
    protected TextView mContactJobText;

    @ViewById(R.id.phone_item_layout)
    protected LinearLayout mPhoneContainer;

    @ViewById(R.id.email_item_layout)
    protected LinearLayout mEmailItemContainer;

    @ViewById(R.id.addr_item_layout)
    protected LinearLayout mAddrItemContainer;

    @ViewById(R.id.contact_header_layout)
    protected RelativeLayout mHeaderLayout;

    @ViewById(R.id.text_edit)
    protected TextView mEditButton;

    @ViewById(R.id.text_delete)
    protected TextView mDeleteButton;

    @Bean
    protected ContactService mContactService;

    private ScContact currentContact;
    private AlertDialog mEditDialog, mDeleteDialog;
    private EncryptDialog mEncryptDialog;
    private EncryptDialog mDecryptDialog;
    private boolean isEncrypted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentContact = (ScContact) getIntent().getSerializableExtra("EXTRA_CONTACT");
    }

    @AfterViews
    protected void afterViews() {
        if (null != currentContact) {
            initialHeaderLayout();
            initialPhoneItems();
            initialEmailItems();
            initialAddressItems();
        }
    }

    @Background
    protected void encryptContactField(int dataId, String password) {
        mContactService.encryptContactField(dataId, password);
        onPostEncrypt();
    }

    @UiThread
    protected void onPostEncrypt() {
        EventBus.getDefault().postSticky(new ReloadContactsEvent());
    }

    private void initialHeaderLayout() {
        String name = currentContact.getName();
        mContactNameText.setText(TextUtils.isEmpty(name)? "未知" : name);
        mContactJobText.setText((null == currentContact.getJob()? "" : currentContact.getJob()) +
                (!TextUtils.isEmpty(currentContact.getCompanyName())? ("-" + currentContact.getCompanyName()) : ""));
    }

    @Click({R.id.close_image, R.id.text_edit, R.id.text_delete})
    protected void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_image:
                finish();
                break;
            case R.id.text_edit:
                Intent intent = new Intent(this, AddContactActivity_.class);
                intent.putExtra("EXTRA_CONTACT", currentContact);
                startActivity(intent);
                break;
            case R.id.text_delete:
                if (isEncrypted) {
                    Toast.makeText(ContactDetailActivity.this, "此联系人已加密，请解密后重试。", Toast.LENGTH_SHORT).show();
                    return;
                }
                mDeleteDialog = DialogUtil.createTipDialog(ContactDetailActivity.this,
                        "提示", "确认删除此联系人?", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteContact(currentContact);
                            }
                        });
                mDeleteDialog.show();
                break;
        }
    }

    @Background
    protected void deleteContact(ScContact contact) {
        mContactService.deleteContact(contact.getId());
        onPostDeleteContact();
    }

    private void onPostDeleteContact() {
        EventBus.getDefault().postSticky(new ReloadContactsEvent());
        finish();
    }

    private void initialAddressItems() {
        if (currentContact.getAddresses().size() < 1) {
            mAddrItemContainer.setVisibility(View.GONE);
        } else {
            for (Address address : currentContact.getAddresses()) {
                addAddrItemView(address);
            }
        }

    }

    private void addAddrItemView(final Address address) {
        RelativeLayout addrItem = (RelativeLayout) View.inflate(this, R.layout.contact_mail_layout, null);
        mAddrItemContainer.addView(addrItem);
        TextView addrLabel = (TextView) addrItem.findViewById(R.id.mail_label);
        final TextView addrText = (TextView) addrItem.findViewById(R.id.mail_text);
        addrLabel.setText(Address.addrTypeToString(address.getType()));

        if (null == address.getEncryption()) {
            addrText.setText(address.getAddress());
        } else {
            addrText.setText(address.getAddress().replaceAll(".", "*"));
            isEncrypted = true;
        }

        addrItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null == address.getEncryption()) {
                    if (null != currentContact.getEncryption()) {
                        Toast.makeText(ContactDetailActivity.this, "此联系人已整体加密", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    mEditDialog = DialogUtil.createEditDialog(ContactDetailActivity.this, currentContact.getName(), new String[]{"加密"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mEncryptDialog = DialogUtil.createEncryptDialog(ContactDetailActivity.this, "加密", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String p = mEncryptDialog.getPasswordText();
                                    if (TextUtils.isEmpty(p)) {
                                        Toast.makeText(ContactDetailActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    encryptContactField(address.getId(), p);
                                    mEncryptDialog.dismiss();
                                    address.setEncryption(MD5Util.encrypt(p));
                                    addrText.setText(address.getAddress().replaceAll(".", "*"));
                                    Toast.makeText(ContactDetailActivity.this, "此信息已加密", Toast.LENGTH_SHORT).show();
                                }
                            });
                            mEncryptDialog.show();
                        }
                    });
                    mEditDialog.show();
                } else {
                    mEditDialog = DialogUtil.createEditDialog(ContactDetailActivity.this, "请选择", new String[]{"查看", "去除密码"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (0 == which) {
                                mDecryptDialog = DialogUtil.createEncryptDialog(ContactDetailActivity.this, "查看信息", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String p = mDecryptDialog.getPasswordText();
                                        if (TextUtils.isEmpty(p)) {
                                            Toast.makeText(ContactDetailActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (MD5Util.encrypt(p).equals(address.getEncryption())) {
                                            address.setEncryption(null);
                                            addrText.setText(address.getAddress());
                                            mDecryptDialog.dismiss();
                                        } else {
                                            Toast.makeText(ContactDetailActivity.this, "密码错误，请重试。", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                mDecryptDialog.show();
                            } else {
                                mDecryptDialog = DialogUtil.createEncryptDialog(ContactDetailActivity.this, "解密", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String p = mDecryptDialog.getPasswordText();
                                        if (TextUtils.isEmpty(p)) {
                                            Toast.makeText(ContactDetailActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (MD5Util.encrypt(p).equals(address.getEncryption())) {
                                            encryptContactField(address.getId(), null);
                                            address.setEncryption(null);
                                            addrText.setText(address.getAddress());
                                            mDecryptDialog.dismiss();
                                        } else {
                                            Toast.makeText(ContactDetailActivity.this, "密码错误，请重试。", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                mDecryptDialog.show();
                            }
                        }
                    });
                    mEditDialog.show();
                }
                return true;
            }
        });
    }

    private void initialEmailItems() {
        if (currentContact.getEmails().size() < 1) {
            mEmailItemContainer.setVisibility(View.GONE);
        } else {
            for (Email email : currentContact.getEmails()) {
                addEmailItemView(email);
            }
        }
    }

    private void addEmailItemView(final Email email) {
        RelativeLayout mailItem = (RelativeLayout) View.inflate(this, R.layout.contact_mail_layout, null);
        mEmailItemContainer.addView(mailItem);
        TextView mailLabel = (TextView) mailItem.findViewById(R.id.mail_label);
        final TextView mailText = (TextView) mailItem.findViewById(R.id.mail_text);
        mailLabel.setText(Email.mailTypeToString(email.getType()));
        if (null == email.getEncryption()) {
            mailText.setText(email.getAddress());
//            mailItem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(Intent.ACTION_SENDTO);
//                    intent.setData(Uri.parse("mailto" + email.getAddress()));
//                    startActivity(intent);
//                }
//            });
        } else {
            mailText.setText(email.getAddress().replaceAll(".", "*"));
            isEncrypted = true;
        }

        mailItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null == email.getEncryption()) {
                    if (null != currentContact.getEncryption()) {
                        Toast.makeText(ContactDetailActivity.this, "此联系人已整体加密", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    mEditDialog = DialogUtil.createEditDialog(ContactDetailActivity.this, currentContact.getName(), new String[]{"加密"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mEncryptDialog = DialogUtil.createEncryptDialog(ContactDetailActivity.this, "加密", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String p = mEncryptDialog.getPasswordText();
                                    if (TextUtils.isEmpty(p)) {
                                        Toast.makeText(ContactDetailActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    encryptContactField(email.getId(), p);
                                    mEncryptDialog.dismiss();
                                    email.setEncryption(MD5Util.encrypt(p));
                                    mailText.setText(email.getAddress().replaceAll(".", "*"));
                                    Toast.makeText(ContactDetailActivity.this, "此信息已加密", Toast.LENGTH_SHORT).show();
                                }
                            });
                            mEncryptDialog.show();
                        }
                    });
                    mEditDialog.show();
                } else {
                    mEditDialog = DialogUtil.createEditDialog(ContactDetailActivity.this, "请选择", new String[]{"查看", "去除密码"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (0 == which) {
                                mDecryptDialog = DialogUtil.createEncryptDialog(ContactDetailActivity.this, "查看信息", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String p = mDecryptDialog.getPasswordText();
                                        if (TextUtils.isEmpty(p)) {
                                            Toast.makeText(ContactDetailActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (MD5Util.encrypt(p).equals(email.getEncryption())) {
                                            email.setEncryption(null);
                                            mailText.setText(email.getAddress());
                                            mDecryptDialog.dismiss();
                                        } else {
                                            Toast.makeText(ContactDetailActivity.this, "密码错误，请重试。", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                mDecryptDialog.show();
                            } else {
                                mDecryptDialog = DialogUtil.createEncryptDialog(ContactDetailActivity.this, "解密", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String p = mDecryptDialog.getPasswordText();
                                        if (TextUtils.isEmpty(p)) {
                                            Toast.makeText(ContactDetailActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (MD5Util.encrypt(p).equals(email.getEncryption())) {
                                            encryptContactField(email.getId(), null);
                                            email.setEncryption(null);
                                            mailText.setText(email.getAddress());
                                            mDecryptDialog.dismiss();
                                        } else {
                                            Toast.makeText(ContactDetailActivity.this, "密码错误，请重试。", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                mDecryptDialog.show();
                            }
                        }
                    });
                    mEditDialog.show();
                }
                return true;
            }
        });
    }

    private void initialPhoneItems() {
        if (currentContact.getPhoneNumbers().size() < 1) {
            mPhoneContainer.setVisibility(View.GONE);
        } else {
            for (PhoneNumber phoneNumber : currentContact.getPhoneNumbers()) {
                addPhoneItemView(phoneNumber);
            }

        }
    }

    private void addPhoneItemView(final PhoneNumber phoneNumber) {
        RelativeLayout phoneItem = (RelativeLayout) View.inflate(this, R.layout.contact_phone_layout, null);
        TextView phoneLabel = (TextView) phoneItem.findViewById(R.id.phone_label);
        final TextView phoneNumberText = (TextView) phoneItem.findViewById(R.id.phone_text);
        ImageView sendMessageImage = (ImageView) phoneItem.findViewById(R.id.send_message_image);
        LinearLayout phoneRoot = (LinearLayout) phoneItem.findViewById(R.id.phone_root_layout);
        mPhoneContainer.addView(phoneItem);
        phoneLabel.setText(PhoneNumber.phoneTypeToString(phoneNumber.getType()));
        phoneNumberText.setText(null == phoneNumber.getEncryption() ?
                phoneNumber.getNumber() : phoneNumber.getNumber().replaceAll(".", "*"));

        if (null == phoneNumber.getEncryption()) {
            phoneRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("tel:" + phoneNumber.getNumber());
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(uri);
                    startActivity(intent);
                }
            });

            sendMessageImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else {
            sendMessageImage.setEnabled(false);
            isEncrypted = true;
        }

        phoneRoot.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null == phoneNumber.getEncryption()) {
                    if (null != currentContact.getEncryption()) {
                        Toast.makeText(ContactDetailActivity.this, "此联系人已整体加密", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    mEditDialog = DialogUtil.createEditDialog(ContactDetailActivity.this, currentContact.getName(), new String[]{"加密"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mEncryptDialog = DialogUtil.createEncryptDialog(ContactDetailActivity.this, "加密", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String p = mEncryptDialog.getPasswordText();
                                    if (TextUtils.isEmpty(p)) {
                                        Toast.makeText(ContactDetailActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    encryptContactField(phoneNumber.getId(), p);
                                    mEncryptDialog.dismiss();
                                    phoneNumber.setEncryption(MD5Util.encrypt(p));
                                    phoneNumberText.setText(phoneNumber.getNumber().replaceAll(".", "*"));
                                    Toast.makeText(ContactDetailActivity.this, "此信息已加密", Toast.LENGTH_SHORT).show();
                                }
                            });
                            mEncryptDialog.show();
                        }
                    });
                    mEditDialog.show();
                } else {
                    mEditDialog = DialogUtil.createEditDialog(ContactDetailActivity.this, "请选择", new String[]{"查看", "去除密码"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (0 == which) {
                                mDecryptDialog = DialogUtil.createEncryptDialog(ContactDetailActivity.this, "查看信息", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String p = mDecryptDialog.getPasswordText();
                                        if (TextUtils.isEmpty(p)) {
                                            Toast.makeText(ContactDetailActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (MD5Util.encrypt(p).equals(phoneNumber.getEncryption())) {
                                            phoneNumber.setEncryption(null);
                                            phoneNumberText.setText(phoneNumber.getNumber());
                                            mDecryptDialog.dismiss();
                                        } else {
                                            Toast.makeText(ContactDetailActivity.this, "密码错误，请重试。", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                mDecryptDialog.show();
                            } else {
                                mDecryptDialog = DialogUtil.createEncryptDialog(ContactDetailActivity.this, "解密", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String p = mDecryptDialog.getPasswordText();
                                        if (TextUtils.isEmpty(p)) {
                                            Toast.makeText(ContactDetailActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (MD5Util.encrypt(p).equals(phoneNumber.getEncryption())) {
                                            encryptContactField(phoneNumber.getId(), null);
                                            phoneNumber.setEncryption(null);
                                            phoneNumberText.setText(phoneNumber.getNumber());
                                            mDecryptDialog.dismiss();
                                        } else {
                                            Toast.makeText(ContactDetailActivity.this, "密码错误，请重试。", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                mDecryptDialog.show();
                            }
                        }
                    });
                    mEditDialog.show();
                }
                return true;
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DialogUtil.dismiss(mEditDialog, mEncryptDialog, mDecryptDialog, mDeleteDialog);
    }

}
