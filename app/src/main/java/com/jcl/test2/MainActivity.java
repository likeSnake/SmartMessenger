package com.jcl.test2;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Telephony;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jcl.test2.Adapter.MainAdapter;
import com.jcl.test2.Util.DBUtil;
import com.jcl.test2.Util.GetLinkManUtil;
import com.jcl.test2.Util.MsgDbUtil;
import com.jcl.test2.pojo.ContactsInfo;
import com.jcl.test2.pojo.MainInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author SmallLetters@sina.com
 */


public class MainActivity extends AppCompatActivity{

    Connection conn;
    PreparedStatement ps;
    ResultSet rs;
    private Boolean b = false;
    private static final int MESSAGE_BACK = 1;
    private boolean isFlag = true;
    private ListView mListView;
    private SimpleAdapter sa;
    private MainAdapter mainAdapter;
    private LinearLayout left;
    private RelativeLayout relativeLayout;
    private RecyclerView recyclerView;
    private List<MainInfo> mainList = new ArrayList<>();
    private FloatingActionButton contacts;
    private Map<String, Object> m = new HashMap<String, Object>();
    private LinearLayout right;
    private EditText search_content;
    int t = 0;
    private List<Map<String, Object>> data = new ArrayList<>();
    List<Map<String, Object>> NewData = new ArrayList<>();
    public static final int REQ_CODE_CONTACT = 1;
    Map<String, Object> map;
    ContentObserver contentObserver = new ContentObserver(new Handler()){
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            initMsg();
            start(true,mainList);
            System.out.println("首页监听到短信");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_activity);
       // System.out.println("默认短信包名："+Telephony.Sms.getDefaultSmsPackage(this));
        //注册短信监听
        Uri smsUri = Uri.parse("content://sms");
        getContentResolver().registerContentObserver(smsUri,true,contentObserver);
        //mListView =  findViewById(R.id.sms_list);
        recyclerView = findViewById(R.id.sms_list);
        relativeLayout = findViewById(R.id.toolbar);
       // registerForContextMenu(mListView);
        left = findViewById(R.id.chat_left);
        right = findViewById(R.id.chat_right);
        contacts = findViewById(R.id.new_sms);
        search_content = findViewById(R.id.search_content);
        search_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int a = mainList.size();
                List<MainInfo> list = new ArrayList<>();
                if (charSequence.toString().length() > 0) {
                    for (int i3 = 0; i3 < a; i3++) {
                        int id = mainList.get(i3).getId();
                        String phone = mainList.get(i3).getAddress();
                        String body = mainList.get(i3).getBody();
                        String time = mainList.get(i3).getDate();
                        String fName = mainList.get(i3).getfName();
                        String name = mainList.get(i3).getName();
                        if (phone.indexOf(charSequence.toString()) != -1 || body.indexOf(charSequence.toString()) != -1) {
                            list.add(new MainInfo(phone, body, time, fName, name,id));
                        }
                    }start(false,list);
                }
               else {
                    start(true,mainList);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
               // Log.i(TAG, "dy: " + dy);
                /*if (t != 0){
                if (dy<0){
                    relativeLayout.setVisibility(View.VISIBLE);
                }else if (dy>0){
                    relativeLayout.setVisibility(View.GONE);
                }
            }
                t = 1;*/
            }
        });
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 判断是否有通讯录权限，没有则获取
                ContactsPermission();
                contacts();
            }
        });
        //注册
        /*IntentFilter filter = new IntentFilter();
        filter.addAction(SMS_RECEIVE_ACTION);
        registerReceiver(smsReceiver, filter);*/

       /* getContentResolver().registerContentObserver(
                Uri.parse("content://sms"), true, contentObserver);*/

      //  initView(data);
        /*mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,"长按",Toast.LENGTH_SHORT).show();

                return true;
            }
        });*/
        /*mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String,String> m=(HashMap<String,String>)adapterView.getItemAtPosition(i);
                String s = m.get("address");
                String phone = m.get("phone");
                String FName = m.get("fName");
                Intent intent = new Intent();
               // left.setVisibility(View.GONE);
                intent.setClass(MainActivity.this,MainActivity2.class);
                intent.putExtra("phone",phone);
                intent.putExtra("FName",FName);
                startActivity(intent);
            }
        });*/
        ContactsPermission();
        start(true,mainList);
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_BACK:
                    isFlag = true; // 在2s时,恢复isFlag的变量值
                    break;
            }
        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && isFlag) {
            isFlag = false;
            Toast.makeText(MainActivity.this, "Tap the back button again to exit the app", Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessageDelayed(MESSAGE_BACK, 2000);
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 保证在activity退出前,移除所有未被执行的消息和回调方法,避免出现内存泄漏!
        handler.removeCallbacksAndMessages(null);
    }
    public void start(Boolean b,List<MainInfo> list){
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        mainAdapter = new MainAdapter(list,this);
        if(b){
            recyclerView.scrollToPosition(mainAdapter.getItemCount()-1);
        }
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mainAdapter);
    }
    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //menu.setHeaderTitle("提示操作");
        menu.add(0,1,Menu.NONE,"删除");

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case 1:
                Toast.makeText(MainActivity.this,"已删除",Toast.LENGTH_SHORT).show();

        }
        return super.onContextItemSelected(item);
    }*/

    /**
     * 动态权限
     */

    private void initView(List<Map<String, Object>> list) {
        //得到ListView
        /*View headView = inflater.inflate(R.layout.listview_header, null, false);
        View footView = inflater.inflate(R.layout.listview_footer, null, false);
        mListView.addFooterView(footView);
        mListView.addHeaderView(headView);*/
        //配置适配置器S

        sa = new SimpleAdapter(this, list, R.layout.sms_item,
                new String[]{"address","names","time","fName"}, new int[]{R.id.sent_name,
                R.id.sent_content,R.id.sent_date,R.id.MsgName_tag});
        TextView t = findViewById(R.id.MsgName_tag);
        mListView.setAdapter(sa);

    }

    /**
     * 检查申请短信权限
     */
    public void checkSMSPermission() {
        System.out.println("检查短信权限");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            //未获取到读取短信权限

            //向系统申请权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS}, 2);
        } else {
            System.out.println("获取到短信权限");
            initMsg();
        }
    }

    public void ContactsPermission(){
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }
        else {
            System.out.println("已获取联系人权限");
            checkSMSPermission();
        }
    }


    public void readSMS() {
        checkSMSPermission();

    }
    public void contacts(){
        upload();
        Intent intent = new Intent();
        // left.setVisibility(View.GONE);
        intent.setClass(MainActivity.this,Contacts.class);
        startActivity(intent);
        //new MsgDbUtil(MainActivity.this).insertSms(this,"10086","我是傻B",1);
        //new MsgDbUtil(MainActivity.this).insertSms(this,"10086","收到",2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //获取到读取短信权限
            System.out.println("获取到短信");
            initMsg();
            start(true,mainList);
        } else {
            checkSMSPermission();
        }
    }
    /*//读取所有短信
    public void query() {
        data.clear();
        Uri uri = Uri.parse("content://sms/");
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{"thread_id", "address", "body", "date", "type","person"}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            List<ContactsInfo> linkMan = GetLinkManUtil.getLinkMan(getApplicationContext());
            int thread_id;
            String address;
            String body;
            String date;
            String person;
            String fName = null;
            String name = null;
            // 时间戳格式转换
            int type;
            List<String> l = new ArrayList();
            while (cursor.moveToNext()) {
                map = new HashMap<String, Object>();
                thread_id = cursor.getInt(0);
                address = cursor.getString(1);
                body = cursor.getString(2);
                date = cursor.getString(3);
                double t = Double.parseDouble(date);
                String result2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(t);
                //String format = sdf.format(l);
                type = cursor.getInt(4);
                person = cursor.getString(5);

                for (int i3 = 0; i3 < linkMan.size(); i3++) {
                    String phone = linkMan.get(i3).getPhone();
                    name = linkMan.get(i3).getName();
                     fName = linkMan.get(i3).getfName();
                    if (phone.equals(address) || phone.equals("+86"+address) || ("+86"+phone).equals(address)){
                        //default_avatar.setVisibility(View.GONE);
                        //name_tag.setText(fName);
                        map.put("fName",fName);
                        System.out.println("赋值"+name);
                        map.put("address",name);
                        b = true;
                        break;
                    }else {
                        map.put("fName","P");
                        b = false;
                    }
                }
                if (!b){
                    map.put("address",address);
                }
                map.put("phone",address);
                map.put("names", body);
                map.put("time",result2);
                if(l.contains(address)){
                    continue;
                }else {
                    l.add(address);
                    l.add("+86"+address);
                }
                data.add(map);
                mainList.add(new MainInfo(address,body,result2,fName,name));
            }
            System.out.println(b);
            //通知适配器发生改变
            //sa.notifyDataSetChanged();
        }
    }*/
    public void upload(){
        String p = "12345678910";
        String name = "测试";
        String sql = "insert into contacts(name,phone) values('"+name+"','"+p+"')";
        System.out.println(sql);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    conn = DBUtil.getConn();
                    if(conn!=null){
                        ps= (PreparedStatement) conn.prepareStatement(sql);
                        if(ps!=null){
                            ps.execute();
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void initMsg(){
        mainList.clear();
        System.out.println("执行initMsg");
        Uri uri = Uri.parse("content://sms/");
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{"_id", "address", "body", "date", "type"}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            List<ContactsInfo> linkMan = GetLinkManUtil.getLinkMan(getApplicationContext());
            System.out.println("判断不为空执行");
            int _id;
            String address;
            String body;
            String date;
            String fName = null;
            String name = null;
            List<String> l = new ArrayList();
            System.out.println("准备执行循环短信内容");
            while (cursor.moveToNext()) {
                _id = cursor.getInt(0);
                address = cursor.getString(1);
                body = cursor.getString(2);
                date = cursor.getString(3);
                double t = Double.parseDouble(date);
                String result2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(t);
                for (int i3 = 0; i3 < linkMan.size(); i3++) {
                    String phone = linkMan.get(i3).getPhone();
                    name = linkMan.get(i3).getName();
                    fName = linkMan.get(i3).getfName();
                    if (phone.equals(address) || phone.equals("+86"+address) || ("+86"+phone).equals(address)){
                        break;
                    }else {
                        fName = null;
                    }
                }
                if(l.contains(address)){
                    continue;
                }else {
                    l.add(address);
                    l.add("+86"+address);
                }
                mainList.add(new MainInfo(address,body,result2,fName,name,_id));
            }
        }
    }
}