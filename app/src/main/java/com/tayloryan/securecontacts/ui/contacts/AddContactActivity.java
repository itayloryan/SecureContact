package com.tayloryan.securecontacts.ui.contacts;

import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.event.ReloadContactsEvent;
import com.tayloryan.securecontacts.model.Address;
import com.tayloryan.securecontacts.model.Email;
import com.tayloryan.securecontacts.model.PhoneNumber;
import com.tayloryan.securecontacts.model.ScContact;
import com.tayloryan.securecontacts.service.ContactService;
import com.tayloryan.securecontacts.ui.BaseActivity;
import com.tayloryan.securecontacts.util.ColorUtil;
import com.tayloryan.securecontacts.util.PinYinUtil;
import com.tayloryan.securecontacts.util.StringUtil;
import com.tayloryan.securecontacts.util.ToolBarConfig;
import com.tayloryan.securecontacts.widget.AvatarView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

@EActivity(R.layout.activity_add_contact)
public class AddContactActivity extends BaseActivity {

    @ViewById(R.id.contact_image)
    protected AvatarView mContactImage;
    @ViewById(R.id.contact_name_text)
    protected EditText mContactName;
    @ViewById(R.id.contact_company_text)
    protected EditText mContactCompany;
    @ViewById(R.id.contact_job_text)
    protected EditText mContactJob;

    @ViewById(R.id.phone_item_layout)
    protected LinearLayout mPhoneLayout;
    @ViewById(R.id.mail_item_layout)
    protected LinearLayout mMailLayout;
    @ViewById(R.id.addr_item_layout)
    protected LinearLayout mAddrLayout;

    @Bean
    protected ContactService mContactService;

    private String mFirstNameText;
    private ScContact mContact;
    private int mContactNameColor;

    private Map<Integer, RelativeLayout> phoneItemMap = new HashMap<>();
    private Map<Integer, RelativeLayout> mailItemMap = new HashMap<>();
    private Map<Integer, RelativeLayout> addrItemMap = new HashMap<>();

    private int phoneItemId, mailItemId, addrItemId;
    private int colorIndex = new Random().nextInt(10);

    private ScContact currentContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentContact = (ScContact) getIntent().getSerializableExtra("EXTRA_CONTACT");
    }

    @AfterViews
    protected void afterViews() {
        initToolBar();
        mContactNameColor = ColorUtil.getRandomColorDrawableRes(colorIndex);

        if (null == currentContact) {
            initialView();
        } else {
            initialData();
        }


    }

    private void initialData() {
        mContactImage.setFirstTextString(currentContact.getFirstTextOfName());
        mContactImage.setFirstTextColor(currentContact.getNameBackColor());
        mContactName.setText(StringUtil.getStringFromNull(currentContact.getName()));
        mContactCompany.setText(StringUtil.getStringFromNull(currentContact.getCompanyName()));
        mContactJob.setText(StringUtil.getStringFromNull(currentContact.getJob()));

        if (currentContact.getPhoneNumbers().size() < 1) {
            addPhoneView(null);
        }
        if (currentContact.getEmails().size() < 1) {
            addMailView(null);
        }
        if (currentContact.getAddresses().size() < 1) {
            addAddressView(null);
        }

        for (PhoneNumber phoneNumber : currentContact.getPhoneNumbers()) {
            addPhoneView(phoneNumber);
        }

        for(Email email : currentContact.getEmails()) {
            addMailView(email);
        }

        for (Address address : currentContact.getAddresses()) {
            addAddressView(address);
        }
    }

    private void initialView() {
        mContactImage.setFirstTextString("");
        mContactImage.setFirstTextColor(mContactNameColor);
        addPhoneView(null);
        addMailView(null);
        addAddressView(null);
    }

    @TextChange(R.id.contact_name_text)
    protected void onTextChanged() {
        if (!TextUtils.isEmpty(mContactName.getText()) && null == mFirstNameText) {
            mFirstNameText = mContactName.getText().toString().trim().substring(0,1);
            mContactImage.setFirstTextString(mFirstNameText);
        } else if(TextUtils.isEmpty(mContactName.getText())) {
            mContactImage.setFirstTextString("");
            mFirstNameText = null;
        }
    }

    @Click({R.id.add_phone_image, R.id.add_mail_image, R.id.add_addr_image})
    protected void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_phone_image:
                addPhoneView(null);
                break;

            case R.id.add_mail_image:
                addMailView(null);
                break;

            case R.id.add_addr_image:
                addAddressView(null);
                break;
        }
    }

    private void initToolBar() {
        ToolBarConfig.with(this)
                .showBackButton(false)
                .setLeftButtonRes(R.string.cancel_text)
                .setTitle(currentContact == null ? R.string.add_contact_text : R.string.edit_contact_text)
                .setRightButtonRes(R.string.save_text)
                .setOnRightButtonClickListener(mSaveButtonClickListener)
                .configuration();
    }

    private void addPhoneView(PhoneNumber phoneNumber) {
        RelativeLayout phoneItemView = (RelativeLayout) View.inflate(this, R.layout.contact_phone_item_layout, null);
        AppCompatSpinner spinner = (AppCompatSpinner) phoneItemView.findViewById(R.id.phone_type_spinner);
        EditText phoneText = (EditText) phoneItemView.findViewById(R.id.phone_text);
        ImageView removeImage = (ImageView) phoneItemView.findViewById(R.id.remove_phone_image);
        mPhoneLayout.addView(phoneItemView);
        phoneItemMap.put(phoneItemId, phoneItemView);
        removeImage.setTag(phoneItemId);
        removeImage.setVisibility(phoneItemId > 0 || phoneNumber != null ? View.VISIBLE : View.GONE);
        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhoneLayout.removeView(phoneItemMap.get(v.getTag()));
                phoneItemMap.remove(v.getTag());
            }
        });
        if (null != phoneNumber) {
            spinner.setSelection(PhoneNumber.Type.fromType(phoneNumber.getType()) - 1);
            phoneText.setText(phoneNumber.getEncryption() == null ?
                    phoneNumber.getNumber() : phoneNumber.getNumber().replaceAll(".", "*"));
            phoneText.setEnabled(phoneNumber.getEncryption() == null);
            removeImage.setEnabled(phoneNumber.getEncryption() == null);
            spinner.setEnabled(phoneNumber.getEncryption() == null);
        }
        phoneItemId ++;
    }

    private void addMailView(Email email) {
        RelativeLayout mailItemView = (RelativeLayout) View.inflate(this, R.layout.contact_mail_item_layout, null);
        AppCompatSpinner spinner = (AppCompatSpinner) mailItemView.findViewById(R.id.mail_type_spinner);
        EditText mailText = (EditText) mailItemView.findViewById(R.id.mail_text);
        ImageView removeImage = (ImageView) mailItemView.findViewById(R.id.remove_mail_image);
        mMailLayout.addView(mailItemView);
        mailItemMap.put(mailItemId, mailItemView);
        removeImage.setTag(mailItemId);
        removeImage.setVisibility(mailItemId > 0 || email != null ? View.VISIBLE : View.GONE);
        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMailLayout.removeView(mailItemMap.get(v.getTag()));
                mailItemMap.remove(v.getTag());
            }
        });
        if (email != null) {
            spinner.setSelection(Email.Type.fromType(email.getType()) - 1);
            mailText.setText(email.getEncryption() == null ?
                    email.getAddress() :email.getAddress().replaceAll(".", "*") );
            mailText.setEnabled(email.getEncryption() == null);
            removeImage.setEnabled(email.getEncryption() == null);
            spinner.setEnabled(email.getEncryption() == null);
        }
        mailItemId ++;
    }

    private void addAddressView(Address address) {
        RelativeLayout addrItemView = (RelativeLayout) View.inflate(this, R.layout.contact_address_item_layout, null);
        AppCompatSpinner spinner = (AppCompatSpinner) addrItemView.findViewById(R.id.addr_type_spinner);
        EditText addrText = (EditText) addrItemView.findViewById(R.id.addr_text);
        ImageView removeImage = (ImageView) addrItemView.findViewById(R.id.remove_addr_image);
        mAddrLayout.addView(addrItemView);
        addrItemMap.put(addrItemId, addrItemView);
        removeImage.setTag(addrItemId);
        removeImage.setVisibility(addrItemId > 0 || address != null ? View.VISIBLE : View.GONE);
        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddrLayout.removeView(addrItemMap.get(v.getTag()));
                addrItemMap.remove(v.getTag());
            }
        });
        if (null != address) {
            spinner.setSelection(Address.Type.fromType(address.getType()) - 1);
            addrText.setText(address.getEncryption() == null ?
                    address.getAddress() :address.getAddress().replaceAll(".", "*") );
            addrText.setEnabled(address.getEncryption() == null);
            removeImage.setEnabled(address.getEncryption() == null);
            spinner.setEnabled(address.getEncryption() == null);
        }
        addrItemId ++;
    }

    private View.OnClickListener mSaveButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (currentContact == null) {
                saveContact();
            } else {
                updateContact();
            }
        }
    };

    private void updateContact() {
        currentContact = new ScContact();
        currentContact.setName(mContactName.getText().toString().trim());
        currentContact.setPinOfName(PinYinUtil.getPingYin(mContact.getName()));
        currentContact.setPinHeaderOfName(mContact.getName());
        currentContact.setFirstLetterOfName(mContact.getPinOfName().length() < 1 ?
                "#" : PinYinUtil.getPinYinHeadChar(mContact.getPinOfName().substring(0, 1)).toUpperCase());
        currentContact.setCompanyName(mContactCompany.getText().toString().trim());
        currentContact.setJob(mContactJob.getText().toString().trim());
        currentContact.setPhotoUri(null);
        currentContact.setPhoneNumbers(getPhoneNumbers());
        currentContact.setEmails(getEmails());
        currentContact.setAddresses(getAddresses());
    }

    @Background
    protected void saveContact() {
        extractContact();
        mContactService.saveContact(mContact);
        onPostSaveContact();
    }

    private void onPostSaveContact() {
        EventBus.getDefault().postSticky(new ReloadContactsEvent());
        finish();
    }

    private void extractContact() {
        mContact = new ScContact();
        mContact.setName(mContactName.getText().toString().trim());
        mContact.setNameBackColor(mContactNameColor);
        mContact.setLookUpKey(String.valueOf(System.currentTimeMillis()));
        mContact.setPinOfName(PinYinUtil.getPingYin(mContact.getName()));
        mContact.setPinHeaderOfName(PinYinUtil.getPinYinHeadChar(mContact.getName()));
        mContact.setFirstLetterOfName(mContact.getPinOfName().length() < 1 ?
                "#" : PinYinUtil.getPinYinHeadChar(mContact.getPinOfName().substring(0, 1)).toUpperCase());
        mContact.setCompanyName(mContactCompany.getText().toString().trim());
        mContact.setJob(mContactJob.getText().toString().trim());
        mContact.setPhotoUri(null);
        mContact.setPhoneNumbers(getPhoneNumbers());
        mContact.setEmails(getEmails());
        mContact.setAddresses(getAddresses());
    }

    private List<Address> getAddresses() {
        List<Address> addresses = new ArrayList<>();
        Iterator<Map.Entry<Integer, RelativeLayout>> items = addrItemMap.entrySet().iterator();

        while (items.hasNext()) {
            RelativeLayout addrView = items.next().getValue();
            AppCompatSpinner spinner = (AppCompatSpinner) addrView.findViewById(R.id.addr_type_spinner);
            EditText addrText = (EditText) addrView.findViewById(R.id.addr_text);
            int addressType = spinner.getSelectedItemPosition() + 1;
            String address = addrText.getText().toString().trim();
            if (!TextUtils.isEmpty(address)) {
                addresses.add(new Address(address, Address.Type.fromValue(addressType), null));
            }
        }
        return addresses;
    }

    private List<PhoneNumber> getPhoneNumbers() {
        List<PhoneNumber> phoneNumbers = new ArrayList<>();
        Iterator<Map.Entry<Integer, RelativeLayout>> items = phoneItemMap.entrySet().iterator();
        while (items.hasNext()) {
            RelativeLayout phoneView = items.next().getValue();
            AppCompatSpinner spinner = (AppCompatSpinner) phoneView.findViewById(R.id.phone_type_spinner);
            EditText phoneText = (EditText) phoneView.findViewById(R.id.phone_text);
            int addressType = spinner.getSelectedItemPosition() + 1;
            String phoneNumber = phoneText.getText().toString().trim();
            if (!TextUtils.isEmpty(phoneNumber)) {
                phoneNumbers.add(new PhoneNumber(phoneNumber, PhoneNumber.Type.fromValue(addressType), null));
            }
        }
        return phoneNumbers;
    }

    private List<Email> getEmails() {
        List<Email> emails = new ArrayList<>();
        Iterator<Map.Entry<Integer, RelativeLayout>> items = mailItemMap.entrySet().iterator();
        while (items.hasNext()) {
            RelativeLayout mailView = items.next().getValue();
            AppCompatSpinner spinner = (AppCompatSpinner) mailView.findViewById(R.id.mail_type_spinner);
            EditText mailText = (EditText) mailView.findViewById(R.id.mail_text);
            int mailType = spinner.getSelectedItemPosition() + 1;
            String email = mailText.getText().toString().trim();
            if (!TextUtils.isEmpty(email)) {
                emails.add(new Email(email, Email.Type.fromValue(mailType), null));
            }
        }
        return emails;
    }

}
