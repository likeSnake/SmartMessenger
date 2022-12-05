package com.jcl.test2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jcl.test2.pojo.ContactsInfo;
import com.jcl.test2.MainActivity2;
import com.jcl.test2.R;
import com.jcl.test2.Util.RandomColor;

import java.util.List;
import java.util.Random;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder>{
    private List<ContactsInfo> cContactsList;
    private Context context;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView contactsListPhone;
        TextView contactsName;
        TextView contactsFirstName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactsListPhone = itemView.findViewById(R.id.person_phone);
            contactsName = itemView.findViewById(R.id.person_name);
            contactsFirstName = itemView.findViewById(R.id.name_tag);
        }
    }

    public ContactsAdapter(List<ContactsInfo> contactsList,Context context1) {
        cContactsList = contactsList;
        context = context1;
    }
    @NonNull
    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item,parent,false);
        return new ContactsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.ViewHolder holder, int position) {
        ContactsInfo contactsInfo = cContactsList.get(position);
        holder.contactsName.setText(contactsInfo.getName());
        holder.contactsListPhone.setText(contactsInfo.getPhone());
        holder.contactsFirstName.setText(contactsInfo.getfName());
        Random random = new Random();
        int r = random.nextInt(RandomColor.getI().length);
        holder.contactsFirstName.setBackgroundColor(RandomColor.i[r]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // left.setVisibility(View.GONE);
                String s = contactsInfo.getPhone();
                intent.setClass(context, MainActivity2.class);
                intent.putExtra("phone",s);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cContactsList.size();
    }


}
