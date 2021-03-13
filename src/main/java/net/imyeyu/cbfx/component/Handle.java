package net.imyeyu.cbfx.component;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import net.imyeyu.betterfx.BetterFX;

/**
 * 拖拽把柄
 * 
 * 夜雨 创建于 2021/3/12 22:27
 */
public class Handle extends Circle {

	public Handle(Paint color) {
		super(0, 0, 9, color);
		setStroke(BetterFX.BLACK);
		setStrokeWidth(1.5);
	}
}