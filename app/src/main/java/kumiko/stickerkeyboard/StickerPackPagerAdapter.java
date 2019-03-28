package kumiko.stickerkeyboard;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;


class StickerPackPagerAdapter extends PagerAdapter {

    private List<StickerKeyboardAdapter> stickerAdapters;

    StickerPackPagerAdapter(@NonNull List<StickerKeyboardAdapter> stickerAdapters) {
        this.stickerAdapters = stickerAdapters;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        PackView packView = new PackView(container.getContext());
        packView.setAdapter(stickerAdapters.get(position));
        container.addView(packView);
        return packView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return stickerAdapters.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
}
