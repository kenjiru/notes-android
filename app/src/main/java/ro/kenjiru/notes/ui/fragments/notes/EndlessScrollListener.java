package ro.kenjiru.notes.ui.fragments.notes;

import android.widget.AbsListView;

public abstract class EndlessScrollListener implements AbsListView.OnScrollListener {
    private int itemsPerPage;
    private int currentPage = -1;

    private int visibleThreshold = 3;
    private int previousTotalItemCount = -1;
    private boolean loading = true;

    public EndlessScrollListener(int itemsPerPage, int totalItemCount) {
        this.itemsPerPage = itemsPerPage;

        if (totalItemCount > 0) {
            this.currentPage = totalItemCount / itemsPerPage;
        }
    }

    // This happens many times a second during a scroll, so be wary of the code you place here.
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        if (loading) {
            if (totalItemCount > previousTotalItemCount) {
                loading = false;
                previousTotalItemCount = totalItemCount;
                currentPage++;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            onLoadMore(currentPage, totalItemCount);
            loading = true;
        }
    }

    // Defines the process for actually loading more data based on page
    public abstract void onLoadMore(int page, int totalItemsCount);

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // Don't take any action on changed
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }
}