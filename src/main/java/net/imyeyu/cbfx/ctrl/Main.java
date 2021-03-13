package net.imyeyu.cbfx.ctrl;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import net.imyeyu.betterfx.BetterFX;
import net.imyeyu.betterjava.Network;
import net.imyeyu.cb.BezierPoint;
import net.imyeyu.cb.CubicBezier;
import net.imyeyu.cbfx.component.Cube;
import net.imyeyu.cbfx.view.ViewMain;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * 主界面控制层
 * 
 * 夜雨 创建于 2021/3/12 22:31
 */
public class Main extends ViewMain {
	
	private int frame = 0;
	private double bx = 0, by = 0, cx = 0, cy = 0;
	private double testWidth = 0;
	private AnimationTimer timer;
	private GraphicsContext g;
	private List<BezierPoint> dataUser, dataSystem;

	public void start(Stage stage) throws Exception {
		super.start(stage);

		// 拖拽
		ctrl0.setOnMousePressed(event -> {
			bx = canvasBox.getLayoutX();
			by = canvasBox.getLayoutY();
		});
		ctrl0.setOnMouseDragged(event -> {
			cx = event.getSceneX() - bx;
			cy = event.getSceneY() - by;
			cx = cx < 0 ? 0 : cx;
			cx = 300 < cx ? 300 : cx;
			cy = cy < 0 ? 0 : cy;
			cy = 500 < cy ? 500 : cy;
			ctrl0.setTranslateX(cx);
			ctrl0.setTranslateY(cy);
			draw(getBezierPoints());
		});
		ctrl1.setOnMousePressed(event -> {
			bx = canvasBox.getLayoutX();
			by = canvasBox.getLayoutY();
		});
		ctrl1.setOnMouseDragged(event -> {
			cx = event.getSceneX() - bx;
			cy = event.getSceneY() - by;
			cx = cx < 0 ? 0 : cx;
			cx = 300 < cx ? 300 : cx;
			cy = cy < 0 ? 0 : cy;
			cy = 500 < cy ? 500 : cy;
			ctrl1.setTranslateX(cx);
			ctrl1.setTranslateY(cy);
			draw(getBezierPoints());
		});
		// 保留整数
		keepInt.selectedProperty().addListener((obs, o, n) -> {
			draw(getBezierPoints());
			StringBuilder sb = new StringBuilder();
			sb.append(toFixed(cubeSystem.getF0())).append(", ");
			sb.append(toFixed(cubeSystem.getF1())).append(", ");
			sb.append(toFixed(cubeSystem.getF2())).append(", ");
			sb.append(toFixed(cubeSystem.getF3()));
			valueLib.setText(sb.toString());
		});
		// 精度调整
		precision.valueProperty().addListener((obs, o, n) -> {
			precisionV.setText(String.valueOf(n.intValue()));
			draw(getBezierPoints());
		});
		// 导出源码
		source.setOnAction(event -> {
			try {
				Network.openURIInBrowser(new URL("https://www.imyeyu.net/article/public/aid138.html").toURI());
			} catch (URISyntaxException | MalformedURLException e) {
				e.printStackTrace();
			}
		});
		// 测试持续时间
		duration.valueProperty().addListener((obs, o, second) -> durationV.setText(String.format("%.1f", second.doubleValue()) + " 秒"));
		// 测试
		test.setOnAction(event -> {
			timer.stop();
			frame = 0;
			
			testWidth = trackBox.getWidth() - cubeUser.getWidth();
			final int p = (int) (duration.getValue() * 60); // 预计帧
			dataUser = new CubicBezier(cubeUser.getF0(), cubeUser.getF1(), cubeUser.getF2(), cubeUser.getF3()).precision(p).build();
			dataSystem = new CubicBezier(cubeSystem.getF0(), cubeSystem.getF1(), cubeSystem.getF2(), cubeSystem.getF3()).precision(p).build();
			timer.start();
		});
		
		// 帧运算
		timer = new AnimationTimer() {
			public void handle(long now) {
				if (frame < dataUser.size()) {
					cubeUser.setTranslateX(dataUser.get(frame).y * testWidth);
				}
				if (frame < dataSystem.size()) {
					cubeSystem.setTranslateX(dataSystem.get(frame).y * testWidth);
				}
				if (dataUser.size() < frame && dataSystem.size() < frame) {
					dataUser = null;
					dataSystem = null;
					frame = 0;
					timer.stop();
				} else {
					frame++;
				}
				if (dataUser == null || dataSystem == null) {
					frame = 0;
					timer.stop();
				}
			}
		};
		
		g = canvas.getGraphicsContext2D();
		// 绘制
		draw(getBezierPoints());
		// 生成系统预设
		setLibs();
	}
	
	/**
	 * 生成贝塞尔点
	 * 
	 * @return 数据点
	 */
	private List<BezierPoint> getBezierPoints() {
		// y 取反
		float f0 = (float) (ctrl0.getTranslateX() / 300);
		float f1 = (float) ((400 - ctrl0.getTranslateY()) / 300);
		float f2 = (float) (ctrl1.getTranslateX() / 300);
		float f3 = (float) ((400 - ctrl1.getTranslateY()) / 300);
		
		// 测试方块
		cubeUser.draw(f0, f1, f2, f3);
		
		// 控制点坐标
		value.setText(toFixed(f0) + ", " + toFixed(f1) + ", " + toFixed(f2) + ", " + toFixed(f3));
		
		return new CubicBezier(f0, f1, f2, f3).precision((int) precision.getValue()).boost(300).build();
	}
	
	/**
	 * 保留两位小数，去整数
	 * 
	 * @param v 值
	 * @return 结果
	 */
	private String toFixed(float v) {
		if (v == 0) return "0";
		if (v == 1) return "1";
		
		if (keepInt.isSelected() || 1 < v) return String.format("%.2f", v);
		
		if (v < 0) {
			return String.format("%.2f", v);
		}
		return String.format("%.2f", v).substring(1);
		
	}
	
	/**
	 * 绘制
	 * 
	 * @param l 数据点
	 */
	private void draw(List<BezierPoint> l) {
		g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		// 中线
		g.setStroke(BetterFX.GRAY);
		g.setLineWidth(4);
		g.strokeLine(0, 400, 300, 100);
		// 中轴
		g.setLineWidth(2);
		g.strokeLine(0, 250, 300, 250);
		g.strokeLine(150, 100, 150, 400);
		// 边界
		g.setLineWidth(1);
		g.setStroke(BetterFX.PINK);
		g.strokeLine(0, 100, 300, 100);
		g.strokeLine(0, 400, 300, 400);
		// 曲线
		g.setFill(BetterFX.RED);
		for (BezierPoint bp : l) {
			g.fillOval(bp.x - 2, 400 - bp.y - 2, 4, 4);
		}
		// 柄
		g.setLineWidth(4);
		g.setStroke(BetterFX.BROWN);
		g.strokeLine(1, 400, ctrl0.getTranslateX(), ctrl0.getTranslateY());
		g.strokeLine(300, 100, ctrl1.getTranslateX(), ctrl1.getTranslateY());
	}
	
	/**
	 * 生成系统预设
	 * 
	 */
	private void setLibs() {
		float[] a = {
			.333f, .333f, .666f, .666f,
			.55f, .055f, .675f, .19f,
			.755f, .05f, .855f, .06f,
			.6f, .04f, .98f, .335f,
			.215f, .61f, .355f, 1,
			.23f, 1, .32f, 1,
			.075f, .82f, .165f, 1,
			.645f, .045f, .355f, 1,
			.86f, 0f, .07f, 1,
			.785f, .135f, .15f, .86f,
			.6f, -.28f, .735f, .045f,
			.175f, .885f, .32f, 1.275f,
			.68f, -.55f, .265f, 1.55f,
		};
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < a.length;) {
			Cube cube = new Cube();
			cube.draw(a[i++], a[i++], a[i++], a[i++]);
			cube.setOnMouseClicked(event -> {
				sb.setLength(0);
				sb.append(toFixed(cube.getF0())).append(", ");
				sb.append(toFixed(cube.getF1())).append(", ");
				sb.append(toFixed(cube.getF2())).append(", ");
				sb.append(toFixed(cube.getF3()));
				valueLib.setText(sb.toString());
				
				cubeSystem.draw(cube.getF0(), cube.getF1(), cube.getF2(), cube.getF3());
			});
			libs.getChildren().add(cube);
		}
		// 默认线性预设
		cubeSystem.draw(a[0], a[1], a[2], a[3]);
		sb.setLength(0);
		sb.append(toFixed(a[0])).append(", ");
		sb.append(toFixed(a[1])).append(", ");
		sb.append(toFixed(a[2])).append(", ");
		sb.append(toFixed(a[3]));
		valueLib.setText(sb.toString());
	}
}