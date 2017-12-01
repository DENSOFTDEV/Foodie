package com.iblinfotech.foodierecipe.utils;

import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    public static boolean checkEmpty(EditText edt) {
        if (edt.getText().toString().isEmpty()) {
            edt.setFocusable(true);
            edt.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean checkLimit(EditText edt) {
        if (edt.getText().toString().length() < 6 || edt.getText().toString().length() > 15) {
            edt.setFocusable(true);
            edt.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean checkDescLimit(EditText edt) {
        if (edt.getText().toString().length() > 300) {
//            Log.e("" + edt.getId(), "empty");
//            edt.setError("field required");
            edt.setFocusable(true);
            edt.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean checkPhnno(EditText edt) {

        String PHONE_PATTERN = "\\d{4}([- ]*)\\d{6}";
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(edt.getText().toString());
        boolean flg = matcher.matches();
        if (!flg) {
            edt.setFocusable(true);
            edt.requestFocus();
            edt.setError("Phone Number is not valid");
            return false;
        }
        return true;

    }

    public static boolean checkEmail(EditText edt) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(edt.getText().toString());

        boolean flg = matcher.matches();
        if (!flg) {
            edt.setFocusable(true);
            edt.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean checkAlphaNumeric(EditText edt) {
        String PATTERN = "[-\\p{Alnum}]+";
//        String PATTERN = "[/^[a-zA-Z0-9_.-s]{6,15}$/]";
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(edt.getText().toString());
        boolean flg = matcher.matches();
        if (!flg) {
            edt.setFocusable(true);
            edt.requestFocus();
            return false;
        }

        return true;
    }

    public static boolean checkOnlyAlpha(EditText edt) {
        String PATTERN = "[a-zA-Z]+";
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(edt.getText().toString());
        boolean flg = matcher.matches();
        if (!flg) {
            edt.setFocusable(true);
            edt.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean checkPhn(EditText edt) {
        String PATTERN = "^\\(?([0-9]{3})\\)?[-.\\\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$";
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(edt.getText().toString());
        boolean flg = matcher.matches();
        Log.e("---", "-" + matcher.matches());
        if (!flg) {
            edt.setFocusable(true);
            edt.requestFocus();
            return false;
        }
        return true;
    }


    public static boolean checkPasswordLength(EditText reg_password) {
        String value = reg_password.getText().toString();
        int password_length = value.length();
        if (password_length < 6) {
            reg_password.setFocusable(true);
            reg_password.requestFocus();
            return false;
        }
        if (password_length > 25) {
            reg_password.setFocusable(true);
            reg_password.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean checkPassword(EditText edt) {
//        String PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,25}";
        String PATTERN = "[/^[0-9A-Za-z!@#$%*]{6,50}$/]";
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(edt.getText().toString());
        boolean flg = matcher.matches();
        if (!flg) {
            edt.setFocusable(true);
            edt.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean checkBitmapNullOrNot(ImageView imageView) {
        if (imageView.getDrawable() == null) {
            return false;
        }

//        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] bytes = stream.toByteArray();
//        Log.e("bitmap to byte", Base64.encodeToString(bytes, 0));
//        String encode_value = Base64.encodeToString(bytes, 0);
//        if (encode_value.isEmpty()) {
//            return false;
//        }
        return true;
    }

    public static boolean checkAdult(int year, int month, int day) {
        Calendar userAge = new GregorianCalendar(year, month, day);
        Calendar minAdultAge = new GregorianCalendar();
        minAdultAge.add(Calendar.YEAR, -18);
        if (minAdultAge.before(userAge)) {
            return false;
        }
        return true;
    }
}
