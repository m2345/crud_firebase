package com.development.buccola.crudfirebase;
/**
 *
 * Created by megan on 5/30/2016.
 * ListViewAdapter
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

    public class ListViewAdapter extends ArrayAdapter<Person>{
        customButtonListener customListener;
        HashMap<Person, Integer> mIdMap = new HashMap<>();

        public class ViewHolder {
            TextView text;
            Button buttonEdit;
            Button btnDelete;
        }

        public ListViewAdapter(Context context, int textViewResourceId,
                                    ArrayList<Person> objects) {
            super(context, textViewResourceId, objects);

            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            Person item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        public interface customButtonListener {
            void deletePerson(int position, Person person);
            void editPerson(int position, Person person);
        }

        public void setCustomButtonListener(customButtonListener listener) {
            this.customListener = listener;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            ViewHolder viewHolder;
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                view = inflater.inflate(R.layout.item_text_two_buttons, null);
                viewHolder = new ViewHolder();
                viewHolder.text = (TextView) view.findViewById(R.id.tvInfo);
                viewHolder.buttonEdit = (Button) view.findViewById(R.id.btnEdit);
                viewHolder.btnDelete = (Button) view.findViewById(R.id.btnDelete);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            final Person temp = getItem(position);

            String info = temp.getFirstName() + " " + temp.getLastName() + "\n" + temp.getDob() + "\n" + temp.getZip();
            viewHolder.text.setText(info);
            //delete person
            viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customListener.deletePerson(position, temp);
                }
            });
            //edit person
            viewHolder.buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customListener.editPerson(position, temp);
                }
            });

            return view;
        }
}
