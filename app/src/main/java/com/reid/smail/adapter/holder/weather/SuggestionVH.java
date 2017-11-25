package com.reid.smail.adapter.holder.weather;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.reid.smail.R;
import com.reid.smail.adapter.holder.BaseVH;
import com.reid.smail.model.weather.Weather;
import com.reid.smail.util.WeatherUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by reid on 2017/11/25.
 */

public class SuggestionVH extends BaseVH<Weather> {

    private RecyclerView mRecycler;
    private SuggestionAdapter mAdapter;

    public SuggestionVH(View itemView) {
        super(itemView);
        mRecycler = itemView.findViewById(R.id.suggestion_recycler_view);
        mRecycler.setFocusable(false);
        mRecycler.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
    }

    @Override
    public void onBindData(Weather data) {
        mAdapter = new SuggestionAdapter(data.suggestion);
        mRecycler.setAdapter(mAdapter);
    }

    private class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.VH> {

        private List<String> keys = new ArrayList<>();
        private Map<String, Weather.Suggestion> suggestions;

        public SuggestionAdapter(Map<String, Weather.Suggestion> suggestionMap){
            if (suggestionMap != null && !suggestionMap.isEmpty()){
                keys.clear();
                keys.addAll(suggestionMap.keySet());
                suggestions = suggestionMap;
            }
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_weather_suggestion_item_view, parent, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            String key = keys.get(position);
            Weather.Suggestion suggestion = suggestions.get(key);
            holder.onBindData(key, suggestion);
        }

        @Override
        public int getItemCount() {
            return keys != null? keys.size():0;
        }

        public class VH extends RecyclerView.ViewHolder{

            public ImageView mIcon;
            public TextView mBrief;
            public TextView mTxt;

            public VH(View itemView) {
                super(itemView);

                mIcon = itemView.findViewById(R.id.suggestion_icon);
                mBrief = itemView.findViewById(R.id.suggestion_brief);
                mTxt = itemView.findViewById(R.id.suggestion_txt);
            }

            public void onBindData(String key, Weather.Suggestion suggestion){
//                GlideApp.with(itemView.getContext()).load(WeatherUtils.getSuggestionIcon(key)).into(mIcon);
                mIcon.setImageResource(WeatherUtils.getSuggestionIcon(key));
                mBrief.setText(WeatherUtils.getSuggestionBrief(key) + suggestion.brf);
                mTxt.setText(suggestion.txt);
            }
        }
    }
}
