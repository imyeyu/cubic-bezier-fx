package net.imyeyu.cbfx.component;

import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import net.imyeyu.betterfx.BetterFX;
import net.imyeyu.betterfx.extend.BgFill;
import net.imyeyu.betterfx.extend.BorderX;
import net.imyeyu.cb.BezierPoint;
import net.imyeyu.cb.CubicBezier;

/**
 * 测试块
 * 
 * 夜雨 创建于 2021/3/12 21:06
 */
public class Cube extends StackPane {

	private static final Border BORDER = new BorderX(BetterFX.GRAY).build();
	private static final Insets PADDING = new Insets(6);
	private static final Background BG = new BgFill(BetterFX.LIGHT_GRAY).build();

	private float f0, f1, f2, f3;
	private final Canvas canvas;
	private final GraphicsContext g;

	public Cube() {
		canvas = new Canvas(48, 64);
		g = canvas.getGraphicsContext2D();
		
		setCursor(Cursor.HAND);
		setBorder(BORDER);
		setPadding(PADDING);
		setBackground(BG);
		getChildren().add(canvas);
	}

	public void draw(float f0, float f1, float f2, float f3) {
		this.f0 = f0;
		this.f1 = f1;
		this.f2 = f2;
		this.f3 = f3;

		List<BezierPoint> datas = new CubicBezier(f0, f1, f2, f3).precision(48).boost(48).build();

		g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for (BezierPoint data : datas) {
			g.fillOval(data.x - .7, 56 - data.y - .7, 1.5, 1.5);
		}
	}

	public float getF0() {
		return f0;
	}

	public float getF1() {
		return f1;
	}

	public float getF2() {
		return f2;
	}

	public float getF3() {
		return f3;
	}
}