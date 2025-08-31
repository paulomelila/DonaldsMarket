package com.paulomelila.donaldsmarket;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private final List<ProductSection> mSectionsList = new ArrayList<>();

    /* TODO: Have more ideas for home tab
     *
    */

    /* TODO: Contact
     *  Maybe add a way for costumers to send suggestions, complaints, questions...
     *
     *  Thru e-mail (boring)
     *  Thru chat (cool)
     */

    /* TODO: make listview scrolling smoother
     *      Maybe use recyclerview
     */

    /* TODO: Implement onItemClick in ListView
        Maybe open a Dialog window showing in which locations
        the selected section is available
     */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        createSections();

        ListAdapter adapter = new SectionsAdapter(getContext(), mSectionsList);

        final ListView listView = view.findViewById(R.id.home_listView);
        listView.setAdapter(adapter);

        return view;
    }

    private void createSections() {
        mSectionsList.add(new ProductSection(getString(R.string.bakery_name), R.drawable.bakery, getString(R.string.bakery_description)));
        mSectionsList.add(new ProductSection(getString(R.string.bulk_name), R.drawable.bulk, getString(R.string.bulk_description)));
        mSectionsList.add(new ProductSection(getString(R.string.meat_name), R.drawable.meat, getString(R.string.meat_description)));
        mSectionsList.add(new ProductSection(getString(R.string.produce_name), R.drawable.produce, getString(R.string.produce_description)));
        mSectionsList.add(new ProductSection(getString(R.string.organic_produce_name), R.drawable.organic_produce, getString(R.string.organic_produce_description)));
        mSectionsList.add(new ProductSection(getString(R.string.grocery_name), R.drawable.grocery, getString(R.string.grocery_description)));
        mSectionsList.add(new ProductSection(getString(R.string.tea_name), R.drawable.steam_tea_house, getString(R.string.tea_description)));
        mSectionsList.add(new ProductSection(getString(R.string.deli_name), R.drawable.deli, getString(R.string.deli_description)));
        mSectionsList.add(new ProductSection(getString(R.string.organic_products_name), R.drawable.organic_products, getString(R.string.organic_products_description)));
        mSectionsList.add(new ProductSection(getString(R.string.dm_kitchen_name), R.drawable.dm_kitchen, getString(R.string.dm_kitchen_description)));
        mSectionsList.add(new ProductSection(getString(R.string.organics_name), R.drawable.organics, getString(R.string.organics_description)));
        mSectionsList.add(new ProductSection(getString(R.string.dairy_name), R.drawable.dairy, getString(R.string.dairy_description)));

    }

    private static class SectionsAdapter extends ArrayAdapter<ProductSection> {

        public SectionsAdapter(Context context, List<ProductSection> sectionsList) {
            super(context, R.layout.home_list_item, sectionsList);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.home_list_item, parent, false);

            ProductSection productSection = getItem(position); // No need to cast anymore

            ViewHolder holder = new ViewHolder();
            holder.sectionTitle = view.findViewById(R.id.sectionTitle);
            holder.sectionImage = view.findViewById(R.id.sectionImage);
            holder.sectionDescription = view.findViewById(R.id.sectionDescription);

            view.setTag(holder);

            holder.sectionTitle.setText(productSection.getName());
            holder.sectionImage.setImageResource(productSection.getSectionImageResource());
            holder.sectionDescription.setText(productSection.getDescription());

//            TextView sectionTitle = view.findViewById(R.id.sectionTitle);
//            ImageView sectionImage = view.findViewById(R.id.sectionImage);
//            TextView sectionDescription = view.findViewById(R.id.sectionDescription);
//
//            sectionTitle.setText(productSection.getName());
//            sectionImage.setImageResource(productSection.getSectionImageResource());
//            sectionDescription.setText(productSection.getDescription());

            return view;
        }
    }

    private static class ViewHolder {
        TextView sectionTitle;
        ImageView sectionImage;
        TextView sectionDescription;
    }
}
