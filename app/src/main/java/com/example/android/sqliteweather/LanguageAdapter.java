package com.example.android.sqliteweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.sqliteweather.data.LanguageData;

import java.util.ArrayList;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageItemViewHolder> {
    private ArrayList<LanguageData> languageList;
    private OnLanguageItemClickListener onLanguageItemClickListener;

    public interface OnLanguageItemClickListener {
        void onLanguageItemClick(LanguageData languageData);
    }

    public LanguageAdapter(OnLanguageItemClickListener onLanguageItemClickListener) {
        this.onLanguageItemClickListener = onLanguageItemClickListener;
    }

    @NonNull
    @Override
    public LanguageItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.language_list_item, parent, false);
        return new LanguageItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageItemViewHolder holder, int position) {
        holder.bind(this.languageList.get(position));
    }

    public void updateLanguageData(ArrayList<LanguageData> languageList) {
        this.languageList = languageList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (this.languageList == null) {
            return 0;
        } else {
            return this.languageList.size();
        }
    }

    class LanguageItemViewHolder extends RecyclerView.ViewHolder {
        final private TextView langTV;

        public LanguageItemViewHolder(@NonNull View itemView) {
            super(itemView);
            langTV = itemView.findViewById(R.id.tv_lang);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLanguageItemClickListener.onLanguageItemClick(
                            languageList.get(getAdapterPosition())
                    );
                }
            });
        }

        public void bind(LanguageData languageData) {
            Context ctx = this.itemView.getContext();
            langTV.setText(languageData.getEnglish_name());

        }
    }
}
