package com.tayloryan.securecontacts.ui;

import android.support.v4.app.Fragment;

import com.tayloryan.securecontacts.R;

import org.androidannotations.annotations.EFragment;

/**
 * Created by taylor.yan on 1/20/17.
 */

@EFragment(R.layout.fragment_contacts)
public class ContactFragment extends Fragment {


    public static Fragment create() {
        return new ContactFragment_();
    }
}
