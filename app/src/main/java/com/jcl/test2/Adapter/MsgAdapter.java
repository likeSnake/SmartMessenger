package com.jcl.test2.Adapter;
import static com.jcl.test2.Adapter.MainAdapter.setDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.role.RoleManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jcl.test2.Util.MsgDbUtil;
import com.jcl.test2.pojo.MmsInfo;
import com.jcl.test2.pojo.MsgInfo;
import com.jcl.test2.R;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    private List<MsgInfo> mMsgList;
    private  List<Integer> li;
    private Context context;
    private List<MmsInfo> mmsInfo;

    public MsgAdapter(List<MsgInfo> msgList,List<MmsInfo> mmsInfo, Context context) {
        mMsgList = msgList;
        this.context = context;
        this.mmsInfo = mmsInfo;

    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView  address;
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
        ImageView isSend;
        ImageView default_avatar;
        TextView user_time;
        TextView me_time;
        TextView name_tag;
        TextView time_tag;
        ImageView mms_left;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.send_name);
            leftLayout = itemView.findViewById(R.id.chat_left);
            rightLayout = itemView.findViewById(R.id.chat_right);
            leftMsg = itemView.findViewById(R.id.user_content);
            rightMsg = itemView.findViewById(R.id.me_content);
            isSend = itemView.findViewById(R.id.send_status);
            name_tag = itemView.findViewById(R.id.name_tag);
            default_avatar = itemView.findViewById(R.id.default_avatar);
            user_time = itemView.findViewById(R.id.user_time);
            me_time = itemView.findViewById(R.id.me_time);
            time_tag = itemView.findViewById(R.id.time_tag);
            mms_left = itemView.findViewById(R.id.mms_left);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MsgInfo msg = mMsgList.get(position);
        if (msg.getType()==(MsgInfo.TYPE_RECEIVED) || msg.getType() == 5){
            if (false){
                holder.user_time.setText("");
                holder.time_tag.setVisibility(View.GONE);
            }else {
                holder.user_time.setText(String.valueOf(msg.getDate()));
            }
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.leftMsg.setText(msg.getBody());
            if (msg.getType() == 5){
                holder.isSend.setVisibility(View.VISIBLE);
            }else {
                holder.isSend.setVisibility(View.GONE);
            }

            if (msg.getfName() != null){
                holder.name_tag.setText(msg.getfName());
            }else{
                holder.name_tag.setVisibility(View.GONE);
                holder.default_avatar.setVisibility(View.VISIBLE);
            }
        }else if (msg.getType()==(MsgInfo.TYPE_SENT)){
            if (false){
                holder.me_time.setText("");
                holder.time_tag.setVisibility(View.GONE);
            }else {
                holder.me_time.setText(String.valueOf(msg.getDate()));
            }
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightMsg.setText(msg.getBody());
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //  Toast.makeText(context, "长按" + position, Toast.LENGTH_SHORT).show();
                AlertDialog alertDialog2 = new AlertDialog.Builder(context)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                System.out.println("当前默认包名："+Telephony.Sms.getDefaultSmsPackage(context)+"当前包名:"+context.getPackageName());
                                if (Telephony.Sms.getDefaultSmsPackage(context) == null || Telephony.Sms.getDefaultSmsPackage(context).equals(context.getPackageName())){
                                    mMsgList.remove(position);
                                    MsgDbUtil msgDbUtil = new MsgDbUtil(context);
                                    msgDbUtil.deleteSmsById(msg.getId());
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
}
