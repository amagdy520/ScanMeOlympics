package com.scan.me;

import android.widget.AutoCompleteTextView;
import android.widget.EditText;

/**
 * Created by mido on 17/04/18.
 */

public class Validation {


    public static boolean checkValidation(AutoCompleteTextView autoCompleteTextView) {
        if (autoCompleteTextView.getText().toString().equals("")) {
            autoCompleteTextView.setError("Please fill this field");
            return false;
        }
        return true;
    }

    public static boolean checkValidation(EditText editText) {
        if (editText.getText().toString().equals("")) {
            editText.setError("Please fill this field");
            return false;
        }
        return true;
    }
}
