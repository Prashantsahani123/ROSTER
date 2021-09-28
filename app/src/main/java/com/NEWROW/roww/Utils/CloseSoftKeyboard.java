package com.NEWROW.row.Utils;

/*Created By Gaurav on 5thMay 2020*/

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

public class CloseSoftKeyboard {
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
