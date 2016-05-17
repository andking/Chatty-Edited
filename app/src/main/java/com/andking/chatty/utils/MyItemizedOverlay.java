package com.andking.chatty.utils;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.Toast;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.List;


public class MyItemizedOverlay extends ItemizedIconOverlay<OverlayItem> {

	protected Context mContext;

	public MyItemizedOverlay(final Context context,
							 final List<OverlayItem> aList) {
		super(context, aList, new OnItemGestureListener<OverlayItem>() {
			@Override
			public boolean onItemSingleTapUp(final int index,
					final OverlayItem item) {
				return false;
			}

			@Override
			public boolean onItemLongPress(final int index,
					final OverlayItem item) {
				return false;
			}
		});
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	@Override
	public boolean addItem(OverlayItem item) {
		// TODO Auto-generated method stub
		return super.addItem(item);
	}
	@Override
	protected boolean onSingleTapUpHelper(final int index,
			final OverlayItem item, final MapView mapView) {
		Toast.makeText(mContext, "" + item.getTitle() + " " + item.getSnippet(),
				Toast.LENGTH_SHORT).show();

		return true;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e, MapView mapView) {
//		float x = e.getRawX();
//		float y = e.getRawY();
//		GeoPoint gp = (GeoPoint) mapView.getProjection().fromPixels((int) x, (int) y);
//		 Toast.makeText(mContext, "" + gp.getLatitude() + " " + gp.getLongitude(),
//				 Toast.LENGTH_SHORT).show();
		return super.onSingleTapUp(e, mapView);
	}


}