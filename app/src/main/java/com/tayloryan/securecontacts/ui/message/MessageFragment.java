package com.tayloryan.securecontacts.ui.message;

import android.support.v4.app.Fragment;

import com.tayloryan.securecontacts.R;

import org.androidannotations.annotations.EFragment;

/**
 * Created by taylor.yan on 3/9/17.
 */

@EFragment(R.layout.fragment_contacts)
public class MessageFragment extends Fragment {

    public static MessageFragment create() {
        return new MessageFragment_();
    }
}
