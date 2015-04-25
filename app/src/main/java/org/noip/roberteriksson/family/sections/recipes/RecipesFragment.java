package org.noip.roberteriksson.family.sections.recipes;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.robert.family.R;

import org.noip.roberteriksson.family.main.MainActivity;
import org.noip.roberteriksson.family.sections.FragmentNumbers;
import org.noip.roberteriksson.family.sections.SectionFragment;

public class RecipesFragment extends Fragment implements SectionFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);

        GridView gridview = (GridView) view.findViewById(R.id.recipes_categoryGrid);
        gridview.setAdapter(new RecipeCategoriesAdapter(getActivity()));

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    @Override
    public void refresh() {
        //TODO: Implement this.
    }

    @Override
    public void goBack() {
        ((MainActivity) getActivity()).setCurrentlyLiveFragment(FragmentNumbers.HOME);
    }

    public class RecipeCategoriesAdapter extends BaseAdapter {
        private Context context;

        public class SquareImageView extends ImageView {

            public SquareImageView(Context context) {
                super(context);
            }

            @Override
            public void onMeasure(int width, int height) {
                super.onMeasure(width, width);
            }
        }

        public RecipeCategoriesAdapter(Context context) {
            this.context = context;
        }

        public int getCount() {
            return images.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            SquareImageView imageView;
            if (convertView == null) {
                imageView = new SquareImageView(context);
                imageView.setPadding(30, 30, 30, 30);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else {
                imageView = (SquareImageView) convertView;
            }

            imageView.setImageResource(images[position]);
            return imageView;
        }

        private Integer[] images = {
                R.drawable.abc_btn_check_material, R.drawable.common_signin_btn_icon_dark,
                R.drawable.common_signin_btn_icon_pressed_light, R.drawable.powered_by_google_dark,
                R.drawable.powered_by_google_light, R.drawable.abc_ab_share_pack_mtrl_alpha
        };
    }
}