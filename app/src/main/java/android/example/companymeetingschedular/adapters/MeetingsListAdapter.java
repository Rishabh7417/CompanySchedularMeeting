package android.example.companymeetingschedular.adapters;

import android.example.companymeetingschedular.R;
import android.example.companymeetingschedular.Utils;
import android.example.companymeetingschedular.model.MeetingInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MeetingsListAdapter extends RecyclerView.Adapter<MeetingsListAdapter.ViewHolder> {

    private ArrayList<MeetingInfo> meetingInfoArrayList;

    public MeetingsListAdapter(ArrayList<MeetingInfo> meetingInfoArrayList)
    {
        this.meetingInfoArrayList = meetingInfoArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meeting_list_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MeetingInfo meetingInfo = meetingInfoArrayList.get(position);
        holder.timing_tv.setText(Utils.convertTimeToShow(meetingInfo.getStart_time())+ " - "+Utils.convertTimeToShow(meetingInfo.getEnd_time()));

        holder.desc_tv.setText(meetingInfo.getDescription());
    }

    @Override
    public int getItemCount() {
        return meetingInfoArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView timing_tv;
        TextView desc_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timing_tv = itemView.findViewById(R.id.timing_tv);
            desc_tv = itemView.findViewById(R.id.desc_tv);
        }
    }
}
