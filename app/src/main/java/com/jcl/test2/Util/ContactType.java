package com.jcl.test2.Util;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class ContactType {
    private final Context context;
    private JSONObject contactData;
    private JSONObject jsonObject;

    public ContactType(Context context) {
        this.context = context;
    }

    //获取联系人所有信息（这里返回String，你也可以直接返回其他类型改改就可以了）
    public String getInformation() throws JSONException {
        contactData = new JSONObject();
        int num = 0;
        // 获得所有的联系人
        Cursor cur = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME
                        + " COLLATE LOCALIZED ASC");
        // 循环遍历
        if (cur.moveToFirst()) {
            int idColumn = cur.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
            int displayNameColumn = cur
                    .getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
            do {
                jsonObject = new JSONObject();
                contactData.put("information" + num, jsonObject);
                num++;
                // 获得联系人的ID号
                String contactId = cur.getString(idColumn);
                // 获得联系人姓名
                String disPlayName = cur.getString(displayNameColumn);

                // 查看该联系人有多少个电话号码。如果没有这返回值为0
                int phoneCount = cur.getInt(cur.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                jsonObject.put("name", disPlayName);
                if (phoneCount > 0) {
                    // 获得联系人的电话号码
                    Cursor phones = context.getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + " = " + contactId, null, null);
                    if (phones.moveToFirst()) {
                        do {
                            // 遍历所有的电话号码
                            int phoneType = phones.getInt(phones.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.TYPE)); // 手机
                            // 住宅电话
                            if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_HOME) {
                                String homeNum = phones.getString(phones.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                jsonObject.put("homeNum", homeNum);
                            }
                            // 单位电话
                            if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_WORK) {
                                String jobNum = phones.getString(phones
                                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                jsonObject.put("jobNum", jobNum);
                            }
                            // 单位传真
                            if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK) {
                                String workFax = phones.getString(phones
                                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                jsonObject.put("workFax", workFax);
                            }
                            // 住宅传真
                            if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME) {
                                String homeFax = phones.getString(phones
                                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                jsonObject.put("homeFax", homeFax);
                            } // 寻呼机
                            if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_PAGER) {
                                String pager = phones.getString(phones
                                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                jsonObject.put("pager", pager);
                            }
                            // 回拨号码
                            if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_CALLBACK) {
                                String quickNum = phones.getString(phones
                                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                jsonObject.put("quickNum", quickNum);
                            }
                            // 公司总机
                            if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_COMPANY_MAIN) {
                                String jobTel = phones.getString(phones
                                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                jsonObject.put("jobTel", jobTel);
                            }
                            // 车载电话
                            if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_CAR) {
                                String carNum = phones.getString(phones
                                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                jsonObject.put("carNum", carNum);
                            } // ISDN
                            if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_ISDN) {
                                String isdn = phones.getString(phones
                                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                jsonObject.put("isdn", isdn);
                            } // 总机
                            if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_MAIN) {
                                String tel = phones.getString(phones
                                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                jsonObject.put("tel", tel);
                            }
                            // 无线装置
                            if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_RADIO) {
                                String wirelessDev = phones.getString(phones
                                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                jsonObject.put("wirelessDev", wirelessDev);
                            } // 电报
                            if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_TELEX) {
                                String telegram = phones.getString(phones
                                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                jsonObject.put("telegram", telegram);
                            }
                            // TTY_TDD
                            if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_TTY_TDD) {
                                String tty_tdd = phones.getString(phones
                                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                jsonObject.put("tty_tdd", tty_tdd);
                            }
                            // 单位手机
                            if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE) {
                                String jobMobile = phones.getString(phones
                                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                jsonObject.put("jobMobile", jobMobile);
                            }
                            // 单位寻呼机
                            if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_WORK_PAGER) {
                                String jobPager = phones.getString(phones
                                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                jsonObject.put("jobPager", jobPager);
                            } // 助理
                            if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_ASSISTANT) {
                                String assistantNum = phones.getString(phones
                                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                jsonObject.put("assistantNum", assistantNum);
                            } // 彩信
                            if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_MMS) {
                                String mms = phones.getString(phones
                                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                jsonObject.put("mms", mms);
                            }

                            String mobileEmail = phones.getString(phones
                                    .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.DATA));
                            jsonObject.put("mobileEmail", mobileEmail);
                            String phoneNumber = phones.getString(phones.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            jsonObject.put("phoneNumber", phoneNumber);

                        } while (phones.moveToNext());
                    }
                }else if (phoneCount < 0){

                }
                // 获取该联系人邮箱
                Cursor emails = context.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                + " = " + contactId, null, null);
                if (emails.moveToFirst()) {
                    do {
                        // 遍历所有的电话号码
//                        String emailType = emails
//                                .getString(emails
//                                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.TYPE));
                        String emailValue = emails
                                .getString(emails
                                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.DATA));
//                        jsonObject.put("emailType", emailType);
                        jsonObject.put("emailValue", emailValue);

                    } while (emails.moveToNext());
                }

                // 获取该联系人IM
                Cursor IMs = context.getContentResolver().query(
                        Data.CONTENT_URI,
                        new String[]{Data._ID, Im.PROTOCOL, Im.DATA},
                        Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='"
                                + Im.CONTENT_ITEM_TYPE + "'",
                        new String[]{contactId}, null);
                if (IMs.moveToFirst()) {
                    do {
                        String protocol = IMs.getString(IMs
                                .getColumnIndexOrThrow(Im.PROTOCOL));
                        String date = IMs
                                .getString(IMs.getColumnIndexOrThrow(Im.DATA));
                        jsonObject.put("protocol", protocol);
                        jsonObject.put("date", date);

                    } while (IMs.moveToNext());
                }

                // 获取该联系人地址
                Cursor address = context.getContentResolver()
                        .query(
                                ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                        + " = " + contactId, null, null);
                if (address.moveToFirst()) {
                    do {
                        // 遍历所有的地址
                        String street = address
                                .getString(address
                                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                        String city = address
                                .getString(address
                                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                        String region = address
                                .getString(address
                                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
                        String postCode = address
                                .getString(address
                                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
                        String formatAddress = address
                                .getString(address
                                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
                        jsonObject.put("street", street);
                        jsonObject.put("city", city);
                        jsonObject.put("region", region);
                        jsonObject.put("postCode", postCode);
                        jsonObject.put("formatAddress", formatAddress);

                    } while (address.moveToNext());
                }
                // 获取该联系人组织
                Cursor organizations = context.getContentResolver().query(
                        Data.CONTENT_URI,
                        new String[]{Data._ID, Organization.COMPANY,
                                Organization.TITLE},
                        Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='"
                                + Organization.CONTENT_ITEM_TYPE + "'",
                        new String[]{contactId}, null);
                if (organizations.moveToFirst()) {
                    do {
                        String company = organizations.getString(organizations
                                .getColumnIndexOrThrow(Organization.COMPANY));
                        String title = organizations.getString(organizations
                                .getColumnIndexOrThrow(Organization.TITLE));
                        jsonObject.put("company", company);
                        jsonObject.put("title", title);

                    } while (organizations.moveToNext());
                }

                // 获取备注信息
                Cursor notes = context.getContentResolver().query(
                        Data.CONTENT_URI,
                        new String[]{Data._ID, Note.NOTE},
                        Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='"
                                + Note.CONTENT_ITEM_TYPE + "'",
                        new String[]{contactId}, null);
                if (notes.moveToFirst()) {
                    do {
                        String noteinfo = notes.getString(notes
                                .getColumnIndexOrThrow(Note.NOTE));
                        jsonObject.put("noteinfo", noteinfo);

                    } while (notes.moveToNext());
                }

                // 获取nickname信息
                Cursor nicknames = context.getContentResolver().query(
                        Data.CONTENT_URI,
                        new String[]{Data._ID, Nickname.NAME},
                        Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='"
                                + Nickname.CONTENT_ITEM_TYPE + "'",
                        new String[]{contactId}, null);
                if (nicknames.moveToFirst()) {
                    do {
                        String nickname_ = nicknames.getString(nicknames
                                .getColumnIndexOrThrow(Nickname.NAME));
                        jsonObject.put("nickname", nickname_);

                    } while (nicknames.moveToNext());
                }
            } while (cur.moveToNext());
        }
        Log.e("联系人信息=====", contactData.toString());
        return contactData.toString();
    }

}