package com.jcl.test2.Util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

public class ContactsUtil {
    @SuppressLint("Range")
    public static List<String> getPhoneContacts(Context context) {
        List<String> contact = new ArrayList();
        //得到ContentResolver对象
        ContentResolver cr = context.getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            //取得联系人姓名
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact.add(cursor.getString(nameFieldColumnIndex));
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            if (phone != null) {
                phone.moveToFirst();
                contact.add(phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            }
            phone.close();
            cursor.close();
        } else {
            return null;
        }
        return contact;
    }
}
