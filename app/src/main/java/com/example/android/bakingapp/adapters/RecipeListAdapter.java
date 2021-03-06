package com.example.android.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.utils.Utils;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * {@link RecipeListAdapter} creates a list of recipe items to a {@link android.support.v7.widget.RecyclerView}
 * Created by aditibhattacharya on 31/01/2018.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    private RecipeListOnClickHandler mOnClickHandler;
    private Context mContext;
    private ArrayList<Recipe> mRecipeList;

    /**
     * Interface to receive onClick messages
     */
    public interface RecipeListOnClickHandler {

        void onClick(Recipe recipe);
    }

    /**
     * OnClick handler for the adapter that handles situation when a single item is clicked
     */
    public RecipeListAdapter(RecipeListOnClickHandler onClickHandler) {
        mOnClickHandler = onClickHandler;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageview_recipe_icon)
        ImageView imageViewRecipeIcon;
        @BindView(R.id.textview_recipe_name)
        TextView textViewRecipeName;
        @BindView(R.id.textview_recipe_servings)
        TextView textViewRecipeServings;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        /**
         * This method gets called when child view is clicked
         */
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Recipe recipe = mRecipeList.get(adapterPosition);
            mOnClickHandler.onClick(recipe);
        }
    }

    /**
     * Method called when a new ViewHolder gets created in the event of RecyclerView being laid out.
     * This creates enough ViewHolders to fill up the screen and allow scrolling
     *
     * @return A new ViewHolder that holds the View for each list item
     */
    @Override
    public RecipeListAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int listItemLayoutId = R.layout.list_item_recipe;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(listItemLayoutId, parent, false);
        return new RecipeViewHolder(view);
    }

    /**
     * Method used by RecyclerView to list the recipes
     */
    @Override
    public void onBindViewHolder(RecipeListAdapter.RecipeViewHolder holder, int position) {
        if (position < getItemCount()) {
            Recipe recipe = mRecipeList.get(position);
            String recipeName = recipe.getRecipeName();
            int servings = recipe.getRecipeServings();
            String imageUri = recipe.getRecipeImage();

            if (!Utils.isEmptyString(recipeName)) {

                // set recipe name
                holder.textViewRecipeName.setText(recipeName);

                // set recipe servings
                if (!Utils.isNumZero(servings)) {
                    holder.textViewRecipeServings.setText(mContext.getString(R.string.display_servings, servings));
                } else {
                    holder.textViewRecipeServings.setText(mContext.getString(R.string.display_no_servings));
                }

                // set recipe image if available, else display a default image
                if (!Utils.isEmptyString(imageUri)) {
                    Picasso.with(mContext)
                            .load(imageUri)
                            .placeholder(getImageResourceId(recipeName))
                            .error(getImageResourceId(recipeName))
                            .into(holder.imageViewRecipeIcon);
                } else {
                    holder.imageViewRecipeIcon.setImageResource(getImageResourceId(recipeName));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return (mRecipeList == null) ? 0 : mRecipeList.size();
    }

    /**
     * Method used to refresh the list once the adapter is already created, to avoid creating a new one
     */
    public void setRecipeData(ArrayList<Recipe> recipeList) {
        mRecipeList = recipeList;
        notifyDataSetChanged();
    }

    /**
     * Add icon resource to list item depending on the recipe name
     */
    public int getImageResourceId(String recipeName) {
        int imageResId;

        if (recipeName.contains(mContext.getString(R.string.recipe_pie))) {
            imageResId = R.drawable.ic_pie;
        } else if (recipeName.contains(mContext.getString(R.string.recipe_brownie))) {
            imageResId = R.drawable.ic_brownie;
        } else if (recipeName.contains(mContext.getString(R.string.recipe_cheesecake))) {
            imageResId = R.drawable.ic_cheesecake;
        } else if (recipeName.contains(mContext.getString(R.string.recipe_cake))) {
            imageResId = R.drawable.ic_cake;
        } else { // default if recipe name does not match anything else
            imageResId = R.drawable.ic_chef_hat;
        }

        return imageResId;
    }
}
