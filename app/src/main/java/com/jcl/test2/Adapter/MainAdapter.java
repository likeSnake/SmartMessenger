package com.jcl.test2.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.role.RoleManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Telephony;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jcl.test2.MainActivity2;
import com.jcl.test2.Util.MsgDbUtil;
import com.jcl.test2.pojo.MainInfo;
import com.jcl.test2.R;
import com.jcl.test2.Util.RandomColor;

import java.util.List;
import java.util.Random;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private List<MainInfo> mMsgList;
    Context context;
    Random random = new Random();

    public MainAdapter(List<MainInfo> msgList, Context context) {
        mMsgList = msgList;
        this.context = context;
        //linkMan = GetLinkManUtil.getLinkMan(context);

    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView sent_name;
        TextView sent_content;
        TextView sent_date;
        TextView MsgName_tag;
        ImageView MsgTouX;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sent_name = itemView.findViewById(R.id.sent_name);
            sent_content = itemView.findViewById(R.id.sent_content);
            sent_date = itemView.findViewById(R.id.sent_date);
            MsgName_tag = itemView.findViewById(R.id.MsgName_tag);
            MsgTouX = itemView.findViewById(R.id.MsgTouX);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sms_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int r = random.nextInt(RandomColor.getI().length);
        MainInfo mainInfo = mMsgList.get(position);
        String address = mainInfo.getAddress();
        String fName = mainInfo.getfName();
        String time = mainInfo.getDate();
        String body = mainInfo.getBody();
        holder.MsgName_tag.setBackgroundColor(RandomColor.i[r]);
        holder.MsgTouX.setBackgroundColor(RandomColor.i[r]);
        if (fName == null){
            holder.MsgName_tag.setVisibility(View.GONE);
            holder.sent_name.setText(address);
        } else{

            holder.MsgName_tag.setVisibility(View.VISIBLE);
            holder.MsgName_tag.setText(fName);
            holder.sent_name.setText(mainInfo.getName());
        }

        holder.sent_date.setText(time);
        holder.sent_content.setText(body);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // left.setVisibility(View.GONE);
                intent.setClass(context, MainActivity2.class);
                intent.putExtra("phone",address);
                intent.putExtra("FName",fName);
                context.startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
              //  Toast.makeText(context, "长按" + position, Toast.LENGTH_SHORT).show();
                AlertDialog alertDialog2 = new AlertDialog.Builder(context)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                System.out.println("当前默认包名："+ Telephony.Sms.getDefaultSmsPackage(context)+"当前包名:"+context.getPackageName());
                                if (Telephony.Sms.getDefaultSmsPackage(context) == null || Telephony.Sms.getDefaultSmsPackage(context).equals(context.getPackageName())){
                                    mMsgList.remove(position);
                                    MsgDbUtil msgDbUtil = new MsgDbUtil(context);
                                    msgDbUtil.deleteSmsById(mainInfo.getId());
                                    // ScheduleSmsHelper.getInstance(context).deleteSms(msg.getId());
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                }else {
                                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P ) {
                                        System.out.println("运行第一个");
                                        RoleManager roleManager = context.getSystemService(RoleManager.class);
                                        Intent roleRequestIntent = roleManager.createRequestRoleIntent(RoleManager.ROLE_SMS);
                                        ((Activity) context).startActivityForResult(roleRequestIntent, 12);
                                    } else {//获取当前程序包名
                                        System.out.println("运行第二个");
                                        Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                                        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, context.getPackageName());
                                        ((Activity) context).startActivityForResult(intent, 12);
                                    }
                                }
                            }
                        })

                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {//添加取消
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create();
                alertDialog2.show();
                setDialog(alertDialog2);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
    }
    public static void setDialog(Dialog dialog) {
        Display display = dialog.getWindow().getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int)(display.getWidth() * 0.5);
        dialog.getWindow().setAttributes(params);
    }

}
