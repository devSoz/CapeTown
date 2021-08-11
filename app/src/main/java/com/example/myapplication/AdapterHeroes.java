package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Database.interfaceDb;
import com.example.myapplication.Models.ModelHero;
import com.example.myapplication.Models.favHero;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterHeroes extends RecyclerView.Adapter<AdapterHeroes.HeroViewHolder> implements Filterable {

    private static List<ModelHero> heroList = new ArrayList<>();
    private Integer rowLayout;
    private CreateDb createDb;
    private interfaceDb dao;
    private String filterPattern="";
    List<favHero> favHeroList;
    List<ModelHero> heroListFull= new ArrayList<>();
    Context context;
    private List<Integer> heroIdList = new ArrayList<>();
    //List<ModelHero> heroList,
    public AdapterHeroes(Integer rowLayout, Context context)
    {
        this.rowLayout = rowLayout;
        this.context = context;

        createDb = CreateDb.getInstance(context);
        dao = createDb.Dao();
        favHeroList =  dao.getFavHero();

        for (favHero i : favHeroList) {
            heroIdList.add(i.getHeroId());
        }
    }

    public static class HeroViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout layoutHero;
        TextView bredFor;
        TextView name, slug, weight, fullname, birthplace, color,firstappearance, work, connection1, connection2;

        ImageView imageView;
        ImageButton ibtnFavHero;
        CardView cardView;
        LinearLayout linearLayoutBack, linearLayoutFront ;

        public HeroViewHolder(View view)
        {
            super(view);

            imageView = (ImageView) view.findViewById(R.id.imageView);
            layoutHero = (LinearLayout) view.findViewById(R.id.layoutHero);
            slug = (TextView) view.findViewById(R.id.slug);
            weight   = (TextView) view.findViewById(R.id.weight);
            fullname = (TextView) view.findViewById(R.id.fullname);
            birthplace = (TextView) view.findViewById(R.id.birthplace);
            color = (TextView) view.findViewById(R.id.color);
            firstappearance = (TextView) view.findViewById(R.id.firstappearance);
            work = (TextView) view.findViewById(R.id.work);
            connection1 = (TextView) view.findViewById(R.id.connection1);
            connection2 = (TextView) view.findViewById(R.id.connection2);

            linearLayoutBack = (LinearLayout) view.findViewById(R.id.layoutBack);
            linearLayoutFront = (LinearLayout) view.findViewById(R.id.layoutFront);
            ibtnFavHero = (ImageButton) view.findViewById(R.id.btnFavHero);

          }
    }

    @Override
    public AdapterHeroes.HeroViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new HeroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HeroViewHolder holder, final int position)
    {
        try {
            //position = holder.getAdapterPosition();
            Picasso.get()
                    .load(heroList.get(position).getImages().getSm())
                    .placeholder(R.color.white)
                    .into(holder.imageView);
            holder.name.setText(heroList.get(position).getName());

            holder.slug.setText("Slug: " + emptyifNull(heroList.get(position).getSlug()));

            holder.fullname.setText("Full Name: "+emptyifNull( heroList.get(position).getBiography().getFullName())
                    + "Race - " + emptyifNull( heroList.get(position).getAppearance().getRace())
                    + "(" + emptyifNull( heroList.get(position).getAppearance().getGender()) + ")");


            holder.birthplace.setText("Birth Place : "+emptyifNull( heroList.get(position).getBiography().getPlaceOfBirth()));

            holder.color.setText("Hair Color : "+emptyifNull( heroList.get(position).getAppearance().getHairColor())
                            + "  Eye Color : "+ emptyifNull( heroList.get(position).getAppearance().getEyeColor()));


            holder.firstappearance.setText("First Appearance : "+emptyifNull( heroList.get(position).getBiography().getFirstAppearance()));
            holder.weight.setText("Weight: "+heroList.get(position).getAppearance().getWeight().get(0)
                    +"\n   Height: " +heroList.get(position).getAppearance().getHeight().get(0));
            holder.work.setText("Work : "+emptyifNull( heroList.get(position).getWork().getOccupation()));

            holder.work.setText("Occupation :\n Group Affiliation -"+emptyifNull( heroList.get(position).getConnections().getGroupAffiliation()));
            holder.work.setText("Relatives - "+emptyifNull( heroList.get(position).getConnections().getRelatives()));

            holder.linearLayoutBack.setVisibility(View.GONE);
            holder.linearLayoutFront.setVisibility(View.VISIBLE);

            if(heroIdList.contains(heroList.get(position).getId()))
            {
                holder.ibtnFavHero.setImageResource(R.drawable.fav);
            }
            else
            {
                holder.ibtnFavHero.setImageResource(R.drawable.un_fav);
            }

            holder.linearLayoutFront.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.linearLayoutFront.setVisibility(View.GONE);
                    holder.linearLayoutBack.setVisibility(View.VISIBLE);

                }
            });

            holder.linearLayoutBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.linearLayoutBack.setVisibility(View.GONE);
                    holder.linearLayoutFront.setVisibility(View.VISIBLE);
                }
            });

            holder.ibtnFavHero.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer pos = holder.getAdapterPosition();
                    String imageUrl = heroList.get(pos).getImages().getSm();
                    Integer heroId = heroList.get(pos).getId();
                    String heroname = heroList.get(pos).getName();

                    if(heroIdList.contains(heroId))
                    {
                        holder.ibtnFavHero.setImageResource(R.drawable.un_fav);
                        deleteData(heroId, imageUrl,heroname);
                        heroIdList.remove(heroId);
                    }
                    else {
                        holder.ibtnFavHero.setImageResource(R.drawable.fav);
                        heroIdList.add(heroId);
                        insertData(heroId, imageUrl,heroname);
                    }
                }
            });
        }
        catch (Exception e)
        {
            Log.d("Mmkayy", e.getMessage());
        }
    }

    public void insertData(Integer heroId, String imageUrl, String heroName)
    {
        favHero myDataList = new favHero(heroId, imageUrl, heroName);
        createDb = CreateDb.getInstance(context);
        dao = createDb.Dao();
        dao.insert(myDataList);

        Toast.makeText(context,"Added to favourites!",Toast.LENGTH_LONG).show();
    }

    public void deleteData(Integer heroId, String imageUrl, String heroName)
    {
        //favHero myDataList = new favHero(heroId, imageUrl, heroName);
        createDb = CreateDb.getInstance(context);
        dao = createDb.Dao();
        dao.deleteById(heroId);

        Toast.makeText(context,"Removed from favourites!",Toast.LENGTH_LONG).show();
    }

    public String emptyifNull(String s)
    {
        if (s==null)
            return "";
        else
            return s;
    }

    @Override
    public Filter getFilter() {
        return searchHeroFilter;
    }
    private Filter searchHeroFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ModelHero> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(heroListFull);
            } else {
                filterPattern = constraint.toString().toLowerCase().trim();
                for (ModelHero item : heroList) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                    if (String.valueOf(item.getId()).contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            heroList.clear();
            heroList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public void addAll(List<ModelHero> tempResults) {

        for (ModelHero result : tempResults) {
            heroListFull.add(result);
            heroList.add(result);

        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return heroList.size();
    }
}
