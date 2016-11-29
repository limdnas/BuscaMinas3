package com.sandamil.buscaminas.game;

import com.sandamil.buscaminas.Times;
import com.sandamil.buscaminas.activities.GameActivity;
import com.sandamil.buscaminas.game.Neighborhood.Iterator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ZoomControls;

/**
 * Dos t�rminos importantes a tener en cuenta en esta clase: 1) Parte visible:
 * es aquella parte del view que es visible en pantalla. 2) View completo: parte
 * visible + parte no visible.
 * 
 * @author Daniel
 */
public class BoardView extends SurfaceView implements SurfaceHolder.Callback {
	private GameActivity activity;
	private Point down;
	// private int movement;
	private long downTime;
	private Point previous;
	private ZoomControls zoom = null;
	private Paint[] actualPaint;
	private boolean userInteractionEnabled;
	private boolean movementEnabled;
	private boolean zoomEnabled;
	private int scrollX;
	private int scrollY;
	private int borderMax;
	private int borderMin;
	private int borderCurrent;
	private int cellMax;
	private int cellMin;
	private int cellCurrent;
	private int totalMin;
	private int totalCurrent;
	private Bitmap[] bitmapsCurrent;
	private Canvas cellCanvas;
	private CellState[] cells;
	private int rows;
	private Paint filterPaint;
	private Paint noFilterPaint;
	private Paint alphaPaint;
	private Rect cellRect;
	private int[] recreated;
	private Rect destRect;
	private int[] currentCell = { -1, -1 };
	private int lastPlayer;
	private Neighborhood neighbor;

	public final static int OPAQUE = 0xFF;

	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		requestFocus();
		activity = (GameActivity) context;
		// TypedArray a = context.obtainStyledAttributes(attrs,
		// R.styleable.BoardView);
		// rows = a.getInt(R.styleable.BoardView_rows, 16);
		rows = activity.rows;
		neighbor = new Neighborhood(rows, rows);
		getHolder().addCallback(this);
		filterPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
		noFilterPaint = new Paint();
		alphaPaint = new Paint();
		actualPaint = new Paint[2];
		actualPaint[0] = new Paint();
		actualPaint[0].setColor(0xEE0000);
		actualPaint[0].setFilterBitmap(true);
		actualPaint[0].setAntiAlias(true);
		actualPaint[1] = new Paint();
		actualPaint[1].setColor(0xFF0000FF);
		actualPaint[1].setFilterBitmap(true);
		actualPaint[1].setAntiAlias(true);
		cellCanvas = new Canvas();
		cellRect = new Rect();
		destRect = new Rect();
		initializeBitmaps();
		cells = new CellState[rows * rows];
		for (int i = 0; i < cells.length; i++) {
			cells[i] = new CellState(CellEnum.CLOSE, 0, 0);
		}
		borderCurrent = -1;
		userInteractionEnabled = false;
		movementEnabled = false;
		zoomEnabled = false;
	}

	private void initializeSurface(int width) {
		borderMin = width / ((rows + 1) * 2);
		cellMin = borderMin * 2;
		totalMin = cellMin * (rows + 1);
		borderMax = totalMin / 8;
		cellMax = borderMax * 2;
		if (borderCurrent == -1) {
			borderCurrent = borderMin;
			cellCurrent = cellMin;
			configureNewSize();
		}
	}

	private void configureNewSize() {
		totalCurrent = cellCurrent * (rows + 1);
		cellRect.set(0, 0, cellCurrent, cellCurrent);
		scrollX = 0;
		scrollY = 0;
	}

	private void increaseSize() {
		borderCurrent += 1;
		cellCurrent += 2;
		configureNewSize();
	}

	private void decreaseSize() {
		borderCurrent -= 1;
		cellCurrent -= 2;
		configureNewSize();
	}

	private void initializeBitmaps() {
		bitmapsCurrent = new Bitmap[CellEnum.values().length];
		recreated = new int[CellEnum.values().length];
		for (int i = 0; i < bitmapsCurrent.length; i++) {
			bitmapsCurrent[i] = null;
			recreated[i] = -1;
		}
	}

	private Bitmap getCurrentBitmap(CellEnum s) {
		if (bitmapsCurrent[s.getIndex()] == null) {
			bitmapsCurrent[s.getIndex()] = Bitmap.createBitmap(cellMax, cellMax, Bitmap.Config.ARGB_8888);
		}
		if (recreated[s.getIndex()] != cellCurrent) {
			recreated[s.getIndex()] = cellCurrent;
			cellCanvas.setBitmap(bitmapsCurrent[s.getIndex()]);
			bitmapsCurrent[s.getIndex()].eraseColor(0x00);
			filterPaint.setAlpha(OPAQUE);
			s.paintBitmap(getContext(), cellCanvas, cellRect);
		}
		return bitmapsCurrent[s.getIndex()];
	}

	private int getInnerBorderSize() {
		return (int) (borderCurrent * 0.25);
	}

	private int getOuterBorderSize() {
		return borderCurrent - getInnerBorderSize();
	}

	private void drawBorder(Canvas c, int alpha) {
		int left = scrollX - borderCurrent;
		int top = scrollY - borderCurrent;
		int right = getWidth() + scrollX;
		int bottom = getWidth() + scrollY;
		int border = cellCurrent * rows + borderCurrent;
		if (left >= 0 && top >= 0 && right <= border && bottom <= border) {
			return;
		}
		c.drawColor(0xFF000000);
		int w = getInnerBorderSize();
		RectF outside = new RectF(0, 0, totalCurrent, totalCurrent);
		RectF inside = new RectF(borderCurrent - w, borderCurrent - w, totalCurrent - borderCurrent + w, totalCurrent - borderCurrent + w);
		outside.offset(-scrollX, -scrollY);
		inside.offset(-scrollX, -scrollY);
		Path p = new Path();
		p.addRoundRect(outside, borderCurrent, borderCurrent, Path.Direction.CW);
		p.addRect(inside, Path.Direction.CCW);
		filterPaint.setColor(0xFF666666);
		filterPaint.setAlpha(alpha);
		c.drawPath(p, filterPaint);
		outside.set(borderCurrent - w, borderCurrent - w, totalCurrent - borderCurrent + w, totalCurrent - borderCurrent + w);
		inside.set(borderCurrent, borderCurrent, totalCurrent - borderCurrent, totalCurrent - borderCurrent);
		outside.offset(-scrollX, -scrollY);
		inside.offset(-scrollX, -scrollY);
		p.reset();
		p.addRect(outside, Path.Direction.CW);
		p.addRect(inside, Path.Direction.CCW);
		filterPaint.setColor(0xFFCCCCCC);
		filterPaint.setAlpha(alpha);
		c.drawPath(p, filterPaint);
	}

	private void drawInnerBorder(Canvas c, Rect dirty, int alpha) {
		int left = dirty.left + scrollX - borderCurrent;
		int top = dirty.top + scrollY - borderCurrent;
		int right = dirty.right + scrollX;
		int bottom = dirty.bottom + scrollY;
		int border = cellCurrent * rows + borderCurrent;
		if (left >= 0 && top >= 0 && right <= border && bottom <= border) {
			return;
		}
		int w = (int) (borderCurrent * 0.25);
		RectF outside = new RectF(borderCurrent - w, borderCurrent - w, totalCurrent - borderCurrent + w, totalCurrent - borderCurrent + w);
		RectF inside = new RectF(borderCurrent, borderCurrent, totalCurrent - borderCurrent, totalCurrent - borderCurrent);
		outside.offset(-scrollX, -scrollY);
		inside.offset(-scrollX, -scrollY);
		Path p = new Path();
		p.addRect(outside, Path.Direction.CW);
		p.addRect(inside, Path.Direction.CCW);
		filterPaint.setColor(0xFFCCCCCC);
		filterPaint.setAlpha(alpha);
		c.drawPath(p, filterPaint);
	}

	private void drawCells(Canvas c, Rect dirty, int alpha) {
		noFilterPaint.setAlpha(alpha);
		int left = dirty.left + scrollX - borderCurrent;
		int top = dirty.top + scrollY - borderCurrent;
		int firstCol = 0;
		if (left < 0) {
			destRect.left = -left;
		} else if (left % cellCurrent != 0) {
			firstCol = left / cellCurrent;
			destRect.left = -(left % cellCurrent);
		} else {
			destRect.left = 0;
			firstCol = left / cellCurrent;
		}
		int firstRow = 0;
		if (top < 0) {
			destRect.top = -top;
		} else if (top % cellCurrent != 0) {
			firstRow = top / cellCurrent;
			destRect.top = -(top % cellCurrent);
		} else {
			destRect.top = 0;
			firstRow = top / cellCurrent;
		}
		destRect.left += dirty.left;
		destRect.right = destRect.left + cellCurrent;
		int destTop = destRect.top + dirty.top;
		int col = firstCol;
		while (destRect.left < dirty.right && col < rows) {
			int row = firstRow;
			destRect.top = destTop;
			destRect.bottom = destRect.top + cellCurrent;
			while (destRect.top < dirty.bottom && row < rows) {
				drawCell(c, cellRect, destRect, cells[getIndex(col, row)], noFilterPaint);
				row++;
				destRect.offset(0, cellCurrent);
			}
			col++;
			destRect.offset(cellCurrent, 0);
		}
	}

	private void drawCell(Canvas c, Rect src, Rect dest, CellState cell, Paint p) {
		if (cell.state != null) {
			if (cell.state == CellEnum.CURSE || cell.state == CellEnum.MINE1 || cell.state == CellEnum.MINE2) {
				c.drawBitmap(getCurrentBitmap(CellEnum.CLOSE), src, dest, p);
			}
			c.drawBitmap(getCurrentBitmap(cell.state), src, dest, p);
			return;
		}
		if (cell.cursesAround == 0 && cell.minesAround == 0) {
			c.drawBitmap(getCurrentBitmap(CellEnum.EMPTY), src, dest, p);
			return;
		}
		if (cell.cursesAround == 0) {
			c.drawBitmap(getCurrentBitmap(CellEnum.EMPTY), src, dest, p);
			c.drawBitmap(getCurrentBitmap(CellEnum.fromNumber(cell.minesAround)), src, dest, p);
			return;
		}
		// c.drawBitmap(getCurrentBitmap(State.EMPTY2), src, dest, p);
		// p.setTextSize(dest.width() * 0.5f);
		// p.setAntiAlias(true);
		// p.setColor(colorNumber(-1));
		// p.setAlpha(alpha);
		// c.drawText(Integer.toString(cell.minesAround), dest.left +
		// dest.width()* 0.15f, dest.top + dest.height()* 0.6f, p);
		// c.drawText(Integer.toString(cell.cursesAround), dest.left +
		// dest.width()* 0.55f, dest.top + dest.height()* 0.85f, p);
	}

	private void drawPlayerPosition(Canvas c, int player, Rect dirty, int alpha) {
		Rect p = getRectangle(currentCell[player]);
		if (currentCell[player] != -1 && isPartiallyVisible(p, dirty)) {
			drawRectangleSelected(c, p, player, alpha);
		}
	}

	void drawRectangleSelected(Canvas c, Rect dest, int player) {
		drawRectangleSelected(c, dest, player, OPAQUE);
	}

	void drawRectangleSelected(Canvas c, Rect dest, int player, int alpha) {
		Path p = new Path();
		int radius = cellCurrent / 6;
		int lng = (int) (cellCurrent / 2.75);
		actualPaint[player].setAlpha(alpha);
		actualPaint[player].setAntiAlias(true);
		actualPaint[player].setStyle(Paint.Style.STROKE);
		actualPaint[player].setStrokeWidth((float) (cellCurrent * 0.09));
		actualPaint[player].setPathEffect(new CornerPathEffect(radius));
		p.moveTo(dest.left, dest.top + lng);
		p.lineTo(dest.left, dest.top);
		p.lineTo(dest.left + lng + radius, dest.top);
		c.drawPath(p, actualPaint[player]);
		p.reset();
		p.moveTo(dest.right, dest.top + lng);
		p.lineTo(dest.right, dest.top);
		p.lineTo(dest.right - lng - radius, dest.top);
		c.drawPath(p, actualPaint[player]);
		p.reset();
		p.moveTo(dest.right, dest.bottom - lng);
		p.lineTo(dest.right, dest.bottom);
		p.lineTo(dest.right - lng - radius, dest.bottom);
		c.drawPath(p, actualPaint[player]);
		p.reset();
		p.moveTo(dest.left, dest.bottom - lng);
		p.lineTo(dest.left, dest.bottom);
		p.lineTo(dest.left + lng + radius, dest.bottom);
		c.drawPath(p, actualPaint[player]);
	}

	private boolean drawBoard() {
		return drawBoard(OPAQUE);
	}

	private boolean drawBoard(int alpha, Rect dirty, boolean[] drawPlayer, int[] alphaPlayer) {
		if (!activity.ready) {
			return false;
		}
		Canvas c = null;
		SurfaceHolder h = getHolder();
		try {
			c = h.lockCanvas(dirty);
			if (c == null) {
				return false;
			}
			synchronized (h) {
				drawInnerBorder(c, dirty, alpha);
				drawCells(c, dirty, alpha);
				int firstPlayer = (lastPlayer + 1) % 2;
				if (drawPlayer[firstPlayer]) {
					drawPlayerPosition(c, firstPlayer, dirty, alphaPlayer[firstPlayer]);
				}
				if (drawPlayer[lastPlayer]) {
					drawPlayerPosition(c, lastPlayer, dirty, alphaPlayer[lastPlayer]);
				}
			}
		} finally {
			h.unlockCanvasAndPost(c);
		}
		return true;
	}

	private boolean drawBoard(int alpha) {
		if (!activity.ready) {
			return false;
		}
		Rect r = new Rect(0, 0, getWidth(), getWidth());
		Canvas c = null;
		SurfaceHolder h = getHolder();
		try {
			c = h.lockCanvas();
			if (c == null) {
				return false;
			}
			synchronized (h) {
				drawBorder(c, alpha);
				drawCells(c, r, alpha);
				int firstPlayer = (lastPlayer + 1) % 2;
				drawPlayerPosition(c, firstPlayer, r, alpha);
				drawPlayerPosition(c, lastPlayer, r, alpha);
			}
		} finally {
			h.unlockCanvasAndPost(c);
		}
		return true;
	}

	private boolean drawOpeningCells(OpeningCells opening, int alpha, int player, Rect dirty) {
		if (!activity.ready) {
			return false;
		}
		Canvas c = null;
		SurfaceHolder h = getHolder();
		try {
			c = h.lockCanvas(new Rect(dirty));
			if (c == null) {
				return false;
			}
			synchronized (h) {
				drawInnerBorder(c, dirty, OPAQUE);
				drawCells(c, dirty, OPAQUE);
				alphaPaint.setAlpha(alpha);
				for (int i = 0; i < opening.size(); i++) {
					Rect r = getRectangle(opening.getCell(i));
					if (isPartiallyVisible(r)) {
						drawCell(c, cellRect, r, opening.getState(i), alphaPaint);
					}
				}
				drawPlayerPosition(c, (player + 1) % 2, dirty, OPAQUE);
				drawPlayerPosition(c, player, dirty, alpha);
			}
		} finally {
			h.unlockCanvasAndPost(c);
		}
		return true;
	}

	public boolean openCells(OpeningCells opening, int player) {
		movementEnabled = false;
		zoomEnabled = false;
		int cell = currentCell[player];
		if (cell != -1) {
			Rect r = getRectangle(cell);
			addNeighbors(cell, r);
			cell = currentCell[(player + 1) % 2];
			if (cell != -1) {
				for (Iterator it = neighbor.iterator(cell); it.hasNext();) {
					if (belongToRect(it.get(), r)) {
						addCellToRect(cell, r);
						addNeighbors(cell, r);
						break;
					}
				}
			}
			fixRectToSurface(r);
			if (player == 0) {
				drawBoard(OPAQUE, r, new boolean[] { false, true }, new int[] { 0, OPAQUE });
			} else {
				drawBoard(OPAQUE, r, new boolean[] { true, false }, new int[] { OPAQUE, 0 });
			}
		}
		int previousCurrentCell = currentCell[player];
		int previuosLastPlayer = lastPlayer;
		currentCell[player] = opening.getCell(0);
		lastPlayer = player;
		Rect r = getRectangle(currentCell[player]);
		addNeighbors(currentCell[player], r);
		for (int i = 1; i < opening.size(); i++) {
			addCellToRect(opening.getCell(i), r);
		}
		cell = currentCell[(player + 1) % 2];
		if (cell != -1) {
			for (Iterator it = neighbor.iterator(cell); it.hasNext();) {
				if (belongToRect(it.get(), r)) {
					addCellToRect(cell, r);
					addNeighbors(cell, r);
					break;
				}
			}
		}
		fixRectToSurface(r);
		openCellsOnTime(opening, r, player);
		if (!activity.ready) {
			currentCell[player] = previousCurrentCell;
			lastPlayer = previuosLastPlayer;
			return false;
		}
		for (int i = 0; i < opening.size(); i++) {
			cells[opening.getCell(i)] = opening.getState(i);
		}
		movementEnabled = true;
		zoomEnabled = true;
		return true;
	}

	private void openCellsOnTime(OpeningCells opening, Rect r, int player) {
		if (!drawOpeningCells(opening, 5, player, r)) {
			return;
		}
		long startTime = System.currentTimeMillis();
		long totalTime = Times.CHANGE_CELL;
		if (opening.size() > 1) {
			totalTime = Times.CHANGE_GAP;
		}
		float alpha = 10;
		long remaining = totalTime - (System.currentTimeMillis() - startTime);
		long totalDrawTime = 0;
		int draws = 0;
		float meanDrawTime = 0;
		float futureDraws = 2;
		int intAlpha = 9;
		int oldAlpha;
		while (futureDraws > 1) {
			oldAlpha = intAlpha;
			intAlpha = (int) alpha;
			if (intAlpha > oldAlpha) {
				long drawTime = System.currentTimeMillis();
				if (!drawOpeningCells(opening, intAlpha, player, r)) {
					return;
				}
				drawTime = System.currentTimeMillis() - drawTime;
				if (alpha >= 254.0) {
					break;
				}
				totalDrawTime += drawTime;
				draws++;
				meanDrawTime = (float) totalDrawTime / (float) draws;
			} else {
				Times.Wait(this, (long) meanDrawTime);
			}
			remaining = totalTime - (System.currentTimeMillis() - startTime);
			futureDraws = (int) (remaining / meanDrawTime);
			alpha += (254.0f - alpha) / futureDraws;
		}
		drawOpeningCells(opening, OPAQUE, player, r);
	}

	public void showBoard() {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				setVisibility(View.VISIBLE);
			}
		});
		while (getVisibility() != View.VISIBLE) {
		}
		long startTime = System.currentTimeMillis();
		float alpha = 5;
		long remaining = Times.SHOW_BOARD - (System.currentTimeMillis() - startTime);
		long totalDrawTime = 0;
		int draws = 0;
		float meanDrawTime = 0;
		float futureDraws = 2;
		int intAlpha = 4;
		int oldAlpha;
		while (futureDraws > 1) {
			oldAlpha = intAlpha;
			intAlpha = (int) alpha;
			if (intAlpha > oldAlpha) {
				long drawTime = System.currentTimeMillis();
				if (!drawBoard(intAlpha)) {
					return;
				}
				drawTime = System.currentTimeMillis() - drawTime;
				if (alpha >= 254.0) {
					break;
				}
				totalDrawTime += drawTime;
				draws++;
				meanDrawTime = (float) totalDrawTime / (float) draws;
			} else {
				Times.Wait(this, (long) meanDrawTime);
			}
			remaining = Times.SHOW_BOARD - (System.currentTimeMillis() - startTime);
			futureDraws = (int) (remaining / meanDrawTime);
			alpha += (254.0f - alpha) / futureDraws;
		}
		drawBoard();
	}

	private void addCellToRect(int cell, Rect r) {
		addRect(getRectangle(cell), r);
	}

	private void addRect(Rect added, Rect r) {
		if (isPartiallyVisible(added)) {
			if (added.left < r.left) {
				r.left = added.left;
			}
			if (added.top < r.top) {
				r.top = added.top;
			}
			if (added.right > r.right) {
				r.right = added.right;
			}
			if (added.bottom > r.bottom) {
				r.bottom = added.bottom;
			}
		}
	}

	private boolean belongToRect(int cell, Rect r) {
		Rect cellRect = getRectangle(cell);
		if (cellRect.left >= r.left && cellRect.top >= r.top && cellRect.right <= r.right && cellRect.bottom <= r.bottom) {
			return true;
		}
		return false;
	}

	private void fixRectToSurface(Rect r) {
		int border = getOuterBorderSize();
		if (r.left + scrollX < border) {
			r.left = border - scrollX;
		}
		if (r.top + scrollY < border) {
			r.top = border - scrollY;
		}
		int border2 = totalCurrent - border;
		if (r.right + scrollX > border2) {
			r.right = border2 - scrollX;
		}
		if (r.bottom + scrollY > border2) {
			r.bottom = border2 - scrollY;
		}
	}

	private boolean checkCellSizeMax() {
		if (borderCurrent == borderMax) {
			activity.runOnUiThread(new Runnable() {
				public void run() {
					zoom.setIsZoomInEnabled(false);
					zoom.setIsZoomOutEnabled(true);
				}
			});
			return false;
		}
		activity.runOnUiThread(new Runnable() {
			public void run() {
				zoom.setIsZoomOutEnabled(true);
			}
		});
		return true;
	}

	private boolean checkCellSizeMin() {
		if (totalCurrent == totalMin) {
			activity.runOnUiThread(new Runnable() {
				public void run() {
					zoom.setIsZoomOutEnabled(false);
					zoom.setIsZoomInEnabled(true);
				}
			});
			return false;
		}
		activity.runOnUiThread(new Runnable() {
			public void run() {
				zoom.setIsZoomInEnabled(true);
			}
		});
		return true;
	}

	private void zoomInMove(final long time) {
		activity.game.gamePost(new Runnable() {
			public void run() {
				movementEnabled = false;
				long initial = System.currentTimeMillis();
				if (!checkCellSizeMax()) {
					movementEnabled = true;
					return;
				}
				FloatPoint currentCenter = getCenter();
				while (System.currentTimeMillis() - initial < time) {
					long t = System.currentTimeMillis();
					increaseSize();
					setCenter(currentCenter);
					drawBoard();
					if (!checkCellSizeMax()) {
						movementEnabled = true;
						return;
					}
					t = System.currentTimeMillis() - t;
					if (t < Times.ZOOM_STEP) {
						Times.Wait(this, Times.ZOOM_STEP - t);
					}
				}
				movementEnabled = true;
			}
		});
	}

	private void zoomOutMove(final long time) {
		activity.game.gamePost(new Runnable() {
			public void run() {
				movementEnabled = false;
				long initial = System.currentTimeMillis();
				if (!checkCellSizeMin()) {
					movementEnabled = true;
					return;
				}
				FloatPoint currentCenter = getCenter();
				// int count = 0;
				while (System.currentTimeMillis() - initial < time) {
					long t = System.currentTimeMillis();
					// count++;
					decreaseSize();
					setCenter(currentCenter);
					drawBoard();
					if (!checkCellSizeMin()) {
						movementEnabled = true;
						return;
					}
					t = System.currentTimeMillis() - t;
					if (t < Times.ZOOM_STEP) {
						Times.Wait(this, Times.ZOOM_STEP - t);
					}
				}
				movementEnabled = true;
			}
		});
	}

	private void addNeighbors(int cell, Rect r) {
		int col = getColumn(cell);
		int row = getRow(cell);
		addRect(getRectangle(col - 1, row), r);
		addRect(getRectangle(col + 1, row), r);
		addRect(getRectangle(col, row - 1), r);
		addRect(getRectangle(col, row + 1), r);
	}

	public void disableInteraction() {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				if (zoom != null) {
					zoom.setIsZoomInEnabled(false);
					zoom.setIsZoomOutEnabled(false);
				}
				userInteractionEnabled = false;
			}
		});
	}

	public void enableInteraction() {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				checkZoomButtons();
				userInteractionEnabled = true;
			}
		});
	}

	private void checkZoomButtons() {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				if (borderCurrent == borderMax) {
					zoom.setIsZoomInEnabled(false);
				} else {
					zoom.setIsZoomInEnabled(true);
				}
				if (totalCurrent == totalMin) {
					zoom.setIsZoomOutEnabled(false);
				} else {
					zoom.setIsZoomOutEnabled(true);
				}
			}
		});
	}

	public void setZoomControls(final ZoomControls z) {
		this.zoom = z;
		z.setZoomSpeed(Times.ZOOM);
		z.setOnZoomInClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!zoomEnabled) {
					return;
				}
				zoomInMove(Times.ZOOM);
			}
		});
		z.setOnZoomOutClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!zoomEnabled) {
					return;
				}
				zoomOutMove(Times.ZOOM);
			}
		});
	}

	private void lateralStep(final FloatPoint p) {
		setCenter(p);
		drawBoard();
	}

	public void lateralMove(int cell) {
		lateralMove(getBoardPos(cell));
	}

	private void lateralMove(final FloatPoint destiny) {
		FloatPoint center = getCenter();
		Point before = getPixelFromPercent(center);
		Point after = getPixelFromPercent(destiny);
		float distance = (float) Math.sqrt(Math.pow(after.x - before.x, 2) + Math.pow(after.y - before.y, 2));
		int iterations = (int) Math.ceil(distance / 16);
		if (iterations == 0) {
			return;
		}
		FloatPoint stepCenter = new FloatPoint((destiny.x - center.x) / iterations, (destiny.y - center.y) / iterations);
		for (int j = 0; j < iterations; j++) {
			center.sum(stepCenter);
			lateralStep(new FloatPoint(center));
		}
	}

	/**
	 * Obtiene el indice de columna en la que se encuentra la celda
	 */
	private int getColumn(int cell) {
		return cell % rows;
	}

	/**
	 * Obtiene el indice de fila en la que se encuentra la celda
	 */
	private int getRow(int cell) {
		return cell / rows;
	}

	private int getIndex(int col, int row) {
		return row * rows + col;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// Keep the view squared
		int w = MeasureSpec.getSize(widthMeasureSpec);
		int h = MeasureSpec.getSize(heightMeasureSpec);
		int d = w == 0 ? h : h == 0 ? w : w < h ? w : h;
		setMeasuredDimension(d, d);
	}

	/**
	 * 
	 * @param x
	 *            Cantidad de pixels en el eje X para los que se pretende hacer
	 *            la llamada a scrollBy
	 * @return Devuelve la cantidad correcta de scroll en el eje X para que no
	 *         se salga del tablero
	 */
	private int checkXScrollBy(int x) {
		if (x + scrollX < 0) {
			return -scrollX;
		}
		if (totalCurrent - x - scrollX < getWidth()) {
			return (int) (totalCurrent - getWidth() - scrollX);
		}
		return x;
	}

	/**
	 * 
	 * @param y
	 *            Cantidad de pixels en el eje Y para los que se pretende hacer
	 *            la llamada a scrollBy
	 * @return Devuelve la cantidad correcta de scroll en el eje Y para que no
	 *         se salga del tablero
	 */
	private int checkYScrollBy(int y) {
		if (y + scrollY < 0) {
			return -scrollY;
		}
		if (totalCurrent - y - scrollY < getWidth()) {
			return (int) (totalCurrent - getWidth() - scrollY);
		}
		return y;
	}

	/**
	 * 
	 * @param x
	 *            Cantidad de pixels en el eje X para los que se pretende hacer
	 *            la llamada a scrollTo
	 * @return Devuelve la cantidad correcta de scroll en el eje X para que no
	 *         se salga del tablero
	 */
	private int checkXScrollTo(int x) {
		if (x < 0) {
			return 0;
		}
		if (totalCurrent - x < getWidth()) {
			return (int) (totalCurrent - getWidth());
		}
		return x;
	}

	/**
	 * 
	 * @param y
	 *            Cantidad de pixels en el eje Y para los que se pretende hacer
	 *            la llamada a scrollTo
	 * @return Devuelve la cantidad correcta de scroll en el eje Y para que no
	 *         se salga del tablero
	 */
	private int checkYScrollTo(int y) {
		if (y < 0) {
			return 0;
		}
		if (totalCurrent - y < getWidth()) {
			return (int) (totalCurrent - getWidth());
		}
		return y;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (w != h || w == 0) {
			return;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!userInteractionEnabled) {
			return true;
		}
		if (!movementEnabled) {
			previous = null;
		}
		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			down = new Point(event.getX(), event.getY());
			previous = down;
			downTime = System.currentTimeMillis();
			// movement = 0;
			return true;
		}
		if (action == MotionEvent.ACTION_UP) {
			Integer cellDown = getCell(down.x, down.y);
			final Integer cellUp = getCell((int) event.getX(), (int) event.getY());
			downTime = System.currentTimeMillis() - downTime;
			// if (cellUp == null || cellDown == null
			// || cellUp.compareTo(cellDown) != 0 || movement > 5
			// || downTime > Times.MAX_OPEN_CELL) {
			// return true;
			// }
			if (cellUp == null || cellDown == null || cellUp.compareTo(cellDown) != 0) {
				return true;
			}
			activity.game.cellPressed(cellUp);
			return true;
		}
		if (action == MotionEvent.ACTION_MOVE) {
			if (previous == null) {
				return true;
			}
			// movement++;
			Point newp = new Point(event.getX(), event.getY());
			int offsetX = checkXScrollBy(previous.x - newp.x);
			int offsetY = checkYScrollBy(previous.y - newp.y);
			if (offsetX != 0 || offsetY != 0) {
				myScrollBy(offsetX, offsetY);
				drawBoard();
			}
			previous = newp;
		}
		return true;
	}

	/**
	 * Devuelve el �ndice de una celda a partir de las coordenadas de un pixel
	 * de la parte visible
	 */
	private Integer getCell(int x, int y) {
		float ix = (x + scrollX - borderCurrent) / cellCurrent;
		if (ix >= rows || ix < 0)
			return null;
		float iy = (y + scrollY - borderCurrent) / cellCurrent;
		if (iy >= rows || iy < 0)
			return null;
		return (int) iy * rows + (int) ix;
	}

	/**
	 * Devuelve coordenadas del view completo a partir de coordenadas de la
	 * parte visible
	 */
	private FloatPoint getBoardPos(int x, int y) {
		return new FloatPoint((x + scrollX) / (float) totalCurrent, (y + scrollY) / (float) totalCurrent);
	}

	/**
	 * Devuelve las coordenadas del tablero completo a partir del indice de
	 * celda
	 */
	private FloatPoint getBoardPos(int cell) {
		return new FloatPoint(((getColumn(cell) + 1) * cellCurrent) / (float) totalCurrent, ((getRow(cell) + 1) * cellCurrent) / (float) totalCurrent);
	}

	private Rect getRectangle(int cell) {
		int x = borderCurrent + getColumn(cell) * cellCurrent - scrollX;
		int y = borderCurrent + getRow(cell) * cellCurrent - scrollY;
		return new Rect(x, y, x + cellCurrent, y + cellCurrent);
	}

	private Rect getRectangle(int col, int row) {
		int x = borderCurrent + col * cellCurrent - scrollX;
		int y = borderCurrent + row * cellCurrent - scrollY;
		return new Rect(x, y, x + cellCurrent, y + cellCurrent);
	}

	public boolean isCompletelyVisible(int cell) {
		return isCompletelyVisible(getRectangle(cell));
	}

	private boolean isCompletelyVisible(Rect r) {
		if (r.left >= 0 && r.top >= 0 && r.right <= getWidth() && r.bottom <= getWidth()) {
			return true;
		}
		return false;
	}

	private boolean isPartiallyVisible(Rect r) {
		if (r.left >= getWidth() || r.top >= getWidth() || r.right <= 0 || r.bottom <= 0) {
			return false;
		}
		return true;
	}

	private boolean isPartiallyVisible(Rect r, Rect source) {
		if (r.left >= source.right || r.top >= source.bottom || r.right <= source.left || r.bottom <= source.top) {
			return false;
		}
		return true;
	}

	private void myScrollTo(int x, int y) {
		scrollX = x;
		scrollY = y;
	}

	private void myScrollBy(int x, int y) {
		scrollX += x;
		scrollY += y;
	}

	/**
	 * Establece el centro del tablero a partir de unos porcentajes respecto al
	 * view completo.
	 * 
	 * @return True si la cantidad de scroll ha cambiado. False si es la misma.
	 *         (Si la cantidad de scroll NO ha cambiado, no se realiza un
	 *         invalidate)
	 */
	private void setCenter(FloatPoint p) {
		Point pixel = getPixelFromPercent(p);
		myScrollTo(checkXScrollTo(pixel.x - getWidth() / 2), checkYScrollTo(pixel.y - getWidth() / 2));
	}

	/**
	 * Devuelve que coordenada del view completo (en porcentaje) se encuentra en
	 * el centro de la parte visible
	 */
	private FloatPoint getCenter() {
		return getBoardPos(getWidth() / 2, getWidth() / 2);
	}

	/**
	 * Obtiene los indices del pixel a partir del porcentaje de ubicacion en
	 * view completo
	 */
	private Point getPixelFromPercent(FloatPoint p) {
		return new Point((int) (p.x * totalCurrent), (int) (p.y * totalCurrent));
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle();
		bundle.putParcelable("super", super.onSaveInstanceState());
		bundle.putParcelableArray("cells", cells);
		bundle.putIntArray("currentCell", currentCell);
		bundle.putInt("lastPlayer", lastPlayer);
		return bundle;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		Bundle bundle = (Bundle) state;
		cells = (CellState[]) bundle.getParcelableArray("cells");
		currentCell = bundle.getIntArray("currentCell");
		lastPlayer = bundle.getInt("lastPlayer");
		super.onRestoreInstanceState(bundle.getParcelable("super"));
	}

	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		if (totalMin == getWidth() && !activity.ready) {
			activity.ready = true;
			userInteractionEnabled = true;
			movementEnabled = true;
			zoomEnabled = true;
			zoom.setVisibility(View.VISIBLE);
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		initializeSurface(getWidth());
		android.view.ViewGroup.LayoutParams lp = getLayoutParams();
		lp.width = totalMin;
		lp.height = totalMin;
		setLayoutParams(lp);
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		activity.ready = false;
		userInteractionEnabled = false;
		movementEnabled = false;
		zoomEnabled = false;
	}
}