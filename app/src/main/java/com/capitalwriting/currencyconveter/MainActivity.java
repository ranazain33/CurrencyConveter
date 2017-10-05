package com.capitalwriting.currencyconveter;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AlbumsAdapter adapter;
    private List<Album> albumList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initCollapsingToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        albumList = new ArrayList<>();
        adapter = new AlbumsAdapter(this, albumList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAlbums();

        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.convert));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.bitcoin,
                R.drawable.usd,
                R.drawable.euro,
                R.drawable.cny,
                R.drawable.ethereum,
                R.drawable.cad,
                R.drawable.krw,
                R.drawable.naira,
                R.drawable.ltcjpg,
                R.drawable.rub,
                R.drawable.rub,
                R.drawable.bitcoin,
                R.drawable.usd,
                R.drawable.euro,
                R.drawable.cny,
                R.drawable.ethereum,
                R.drawable.cad,
                R.drawable.krw,
                R.drawable.naira,
                R.drawable.ltcjpg};

        Album a = new Album("BTC", 13, covers[0]);
        albumList.add(a);

        a = new Album("USD", 8, covers[1]);
        albumList.add(a);

        a = new Album("EUR", 11, covers[2]);
        albumList.add(a);

        a = new Album("CNY", 12, covers[3]);
        albumList.add(a);

        a = new Album("ETH", 14, covers[4]);
        albumList.add(a);

        a = new Album("CAD", 1, covers[5]);
        albumList.add(a);

        a = new Album("KTW", 11, covers[6]);
        albumList.add(a);

        a = new Album("N", 14, covers[7]);
        albumList.add(a);

        a = new Album("LTC", 11, covers[8]);
        albumList.add(a);

        a = new Album("RUB", 17, covers[9]);
        albumList.add(a);
        a = new Album("XMR", 8, covers[10]);
        albumList.add(a);

        a = new Album("GOLD", 11, covers[11]);
        albumList.add(a);

        a = new Album("GBP", 12, covers[12]);
        albumList.add(a);

        a = new Album("CHF", 14, covers[13]);
        albumList.add(a);

        a = new Album("DASH", 1, covers[14]);
        albumList.add(a);

        a = new Album("ZEC", 11, covers[15]);
        albumList.add(a);

        a = new Album("CLP", 14, covers[16]);
        albumList.add(a);

        a = new Album("UGX", 11, covers[17]);
        albumList.add(a);

        a = new Album("KGS", 17, covers[18]);
        albumList.add(a);

        a = new Album("UZS", 17, covers[19]);
        albumList.add(a);

        adapter.notifyDataSetChanged();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
