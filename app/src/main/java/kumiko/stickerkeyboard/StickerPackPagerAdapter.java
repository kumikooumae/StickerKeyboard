package kumiko.stickerkeyboard;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;


class StickerPackPagerAdapter extends PagerAdapter {

    private List<PackView> packViews;

    StickerPackPagerAdapter(List<PackView> packViews) {
        this.packViews = packViews;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        PackView packView = packViews.get(position);
        container.addView(packView);
        return packView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    private void update(List<PackView> packViews) {
        this.packViews = packViews;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return packViews.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
}
