package com.example.lakshmimarriagebureau;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context context;
    private List<Person> persons;
    private OnCardListener mOnCardListener;

    public MyAdapter(Context context, List<Person> persons, showBiodata mOnCardListener) {
        this.persons = persons;
        this.context = context;
        this.mOnCardListener = mOnCardListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v, mOnCardListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Person person = persons.get(position);

        if (person.getGender() == null || person.getGender().equals("")) {
            holder.listGender.setText("");
            holder.listGenderText.setText("");
        } else if (person.getGender().equals("M"))
            holder.listGender.setText("Male");
        else if (person.getGender().equals("F"))
            holder.listGender.setText("Female");


        String isMangalik = (!person.isMangalik()) ? "" : "Mangalik";
        holder.listMangalik.setText(isMangalik);

        String status = (!person.isDivorcee()) ? "" : "Mangalik";
        holder.listStatus.setText(status);


        if (person.getQualification() != null && !person.getQualification().equals(""))
            holder.listEducation.setText(person.getQualification());
        if (person.getOccupation() != null && !person.getOccupation().equals(""))
            holder.listOccupation.setText(person.getOccupation());
        if (person.getCurrLocation() != null && !person.getCurrLocation().equals(""))
            holder.listLocation.setText(person.getCurrLocation());
        if (person.getIncome() > 0)
            holder.listIncome.setText(Long.toString(person.getIncome()));
        Log.i("Age of person", Long.toString(person.getAge()));
        if (person.getAge() > 0 && person.getAge() < 100)
            holder.listAge.setText(Integer.toString(person.getAge()));


        if (person.getPhotoUrl1() != null && person.getPhotoUrl1() != "") {
            Log.i("Url", person.getPhotoUrl1());
            Picasso.with(context)
                    .load(person.getPhotoUrl1())
                    .into(holder.listImage);
        } else if (person.getPhotoUrl2() != null && person.getPhotoUrl2() != "")
            Picasso.with(context)
                    .load(person.getPhotoUrl2())
                    .into(holder.listImage);
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView listImage;
        TextView listGender, listGenderText;
        TextView listStatus;
        TextView listEducation;
        TextView listOccupation;
        TextView listLocation;
        TextView listIncome;
        TextView listMangalik;
        TextView listAge;
        ImageView listDelete;
        OnCardListener onCardListener;

        public ViewHolder(View itemView, OnCardListener onCardListener) {
            super(itemView);

            listMangalik = (TextView) itemView.findViewById(R.id.listMangalik);
            listDelete = (ImageView) itemView.findViewById(R.id.listDelete);
            listImage = (ImageView) itemView.findViewById(R.id.listImage);
            listGender = (TextView) itemView.findViewById(R.id.listGender);
            listGenderText = (TextView) itemView.findViewById(R.id.listGenderText);
            listStatus = (TextView) itemView.findViewById(R.id.listStatus);
            listEducation = (TextView) itemView.findViewById(R.id.listEducation);
            listOccupation = (TextView) itemView.findViewById(R.id.listOccupation);
            listLocation = (TextView) itemView.findViewById(R.id.listLocation);
            listIncome = (TextView) itemView.findViewById(R.id.listIncome);
            listAge = (TextView) itemView.findViewById(R.id.listAge);
            this.onCardListener = onCardListener;
            itemView.setOnClickListener(this);
            listDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == listDelete) {
                onCardListener.onDeleteClick(getAdapterPosition());
            } else
                onCardListener.onCardClick(getAdapterPosition());
        }
    }

    public interface OnCardListener {
        void onCardClick(int position);

        void onDeleteClick(int position);
    }
}
