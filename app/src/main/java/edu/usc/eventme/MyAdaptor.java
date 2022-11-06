package edu.usc.eventme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdaptor extends RecyclerView.Adapter<MyAdaptor.ViewHolder>{
    EventList result;
    Context context;

    public MyAdaptor(Context ct, EventList list){
        context = ct;
        result = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_row, parent, false);
        ArrayList<Event> list = result.getList();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArrayList<Event> list = result.getList();
        holder.eventName.setText(list.get(position).getEventTitle());
        holder.distance.setText(String.format("%.2f", list.get(position).findDis(result.getCurrentlat(), result.getCurrentlon()))+"miles");
        //System.out.println(list.get(position).getEventTitle()+"!!!!!!!!!!!!");
        holder.eventLocation.setText(list.get(position).getLocation());
        holder.eventDate.setText(list.get(position).getStartDate()+" to "+list.get(position).getEndDate());
        holder.eventTime.setText(list.get(position).getStartTime()+" to "+list.get(position).getEndTime());
        holder.eventCost.setText(list.get(position).getCost());
        holder.sponcer.setText(list.get(position).getSponsoringOrganization());
        Picasso.get().load(list.get(position).getPhotoURL()).into(holder.eventImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("click!!!!!!!!!!");
                Intent intent = new Intent(view.getContext(), BottomsheetDetails.class);
                intent.putExtra("Events",result);
                intent.putExtra("position",holder.getAdapterPosition());
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return result.getList().size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView eventName, eventLocation, eventDate, eventTime, eventCost, sponcer, distance;
        ImageView eventImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
            eventLocation = itemView.findViewById(R.id.eventLocation);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventTime = itemView.findViewById(R.id.eventTime);
            eventCost = itemView.findViewById(R.id.eventCost);
            sponcer = itemView.findViewById(R.id.sponceringOrganization);
            eventImage = itemView.findViewById(R.id.eventImage);
            distance = itemView.findViewById(R.id.distance);
        }

        @Override
        public void onClick(View view) {
            System.out.println("click!!!!!!!!!!");
            Intent intent = new Intent(view.getContext(), Details.class);
            intent.putExtra("Events",result);
            intent.putExtra("position",getAdapterPosition());
            view.getContext().startActivity(intent);
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
