package com.example.mytvs.adapters;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.mytvs.R;
import com.example.mytvs.model.Person;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder>
                           implements Filterable {

    private static List<Person> personList;
    private List<Person> filteredList;

    private static PersonClicked mClickedListener;

    /*
    Method to set new filtered list as per search query
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchString = charSequence.toString();
                if(!searchString.isEmpty())
                {
                    List<Person> filtered = new ArrayList<>();
                    for(Person person : personList){
                        if(person.getName().toLowerCase().contains(searchString.toLowerCase())){
                            filtered.add(person);
                        }
                    }
                    filteredList = filtered;
                }else {
                    filteredList = personList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (List<Person>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    /*
    Interface to detect person item click
     */
    public interface PersonClicked {

        void onPersonClicked(Person person);
    }

    public PersonAdapter(PersonClicked contactClicked){
        mClickedListener = contactClicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(
                LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_layout, viewGroup, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Person person = filteredList.get(i);
        viewHolder.nameView.setText(person.getName());
        viewHolder.occupationView.setText(person.getOccupation());
    }

    /*
    Setting personList list to adapter
     */
    public void setPersonList(List<Person> persons){
        personList = persons;
        this.filteredList = persons;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (filteredList == null) ? 0 : filteredList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nameView;
        TextView occupationView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            nameView = itemView.findViewById(R.id.person_name);
            occupationView = itemView.findViewById(R.id.person_occupation);

            Typeface custom_font = Typeface.createFromAsset(itemView.getContext().getAssets(),  "fonts/logo.ttf");
            occupationView.setTypeface(custom_font);
        }

        @Override
        public void onClick(View view) {
            mClickedListener.onPersonClicked(personList.get(getAdapterPosition()));
        }
    }

}
