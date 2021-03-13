package net.imyeyu.cbfx.view;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import net.imyeyu.betterfx.BetterFX;
import net.imyeyu.betterfx.extend.AnchorPaneX;
import net.imyeyu.betterfx.extend.BorderX;
import net.imyeyu.cbfx.component.Cube;
import net.imyeyu.cbfx.component.Handle;
import net.imyeyu.pixelfx.PixelFX;
import net.imyeyu.pixelfx.Zpix;
import net.imyeyu.pixelfx.component.PixelButton;
import net.imyeyu.pixelfx.component.Switch;

/**
 * 主界面视图层
 * 
 * 夜雨 创建于 2021/3/12 22:27
 */
public abstract class ViewMain extends Application {
	
	protected HBox libs;
	protected Cube cubeUser, cubeSystem;
	protected Label progression, time, precisionV, durationV;
	protected Slider precision, duration;
	protected Canvas canvas;
	protected Switch keepInt;
	protected Handle ctrl0, ctrl1;
	protected TextField value, valueLib;
	protected Hyperlink source;
	protected AnchorPane canvasBox, trackBox;
	protected PixelButton test;
	
	public void start(Stage stage) throws Exception {
		// 可视化
		ctrl0 = new Handle(BetterFX.RED);
		ctrl0.setTranslateX(100);
		ctrl0.setTranslateY(300);
		ctrl1 = new Handle(BetterFX.BLUE);
		ctrl1.setTranslateX(200);
		ctrl1.setTranslateY(200);
		
		progression = new Label("进展");
		progression.setPrefSize(80, 20);
		progression.setRotate(-90);
		time = new Label("时间");
		
		canvas = new Canvas(300, 500);
		
		AnchorPaneX.def(canvas);
		AnchorPaneX.def(progression, null, null, 132, -52);
		AnchorPaneX.def(time, null, null, 84, 4);
		canvasBox = new AnchorPane(canvas, ctrl0, ctrl1, progression, time);
		canvasBox.setBorder(new BorderX(BetterFX.GRAY).build());
		canvasBox.setPrefSize(300, 500);
		canvasBox.setMaxSize(300, 500);
		
		// 参数
		value = new TextField();
		value.setMaxWidth(290);
		value.setEditable(false);
		Zpix.css(value, Zpix.M);
		
		Label labelKeepInt = new Label("保留整数: ");
		keepInt = new Switch();
		
		Label labelPrecision = new Label("精度: ");
		precision = new Slider(12, 512, 100);
		precision.setPrefWidth(380);
		precisionV = new Label("100");
		HBox pBox = new HBox(precision, precisionV);
		pBox.setSpacing(12);
		
		Label tips = new Label("本程序只是 CubicBezier.jar 的可视化演示，使用方法见");
		source = new Hyperlink("源码");
		TextFlow tipsBox = new TextFlow(tips, source);

		ColumnConstraints colLabel = new ColumnConstraints();
		colLabel.setHalignment(HPos.RIGHT);
		
		final Insets insets24 = new Insets(24);
		final Insets insets8 = new Insets(6, 10, 6, 10);
		GridPane gp = new GridPane();
		gp.setVgap(12);
		gp.setPadding(insets24);
		gp.getColumnConstraints().add(colLabel);
		gp.addColumn(0, new Label(), labelKeepInt, labelPrecision, new Label());
		gp.addColumn(1, value, keepInt, pBox, tipsBox);
		
		// 测试
		Label labelDuration = new Label("持续时间: ");
		duration = new Slider(0.1, 10, 1);
		duration.setPrefWidth(260);
		durationV = new Label("1.0 秒");
		HBox testParm = new HBox(labelDuration, duration, durationV);
		testParm.setAlignment(Pos.CENTER_LEFT);
		testParm.setSpacing(8);
		
		test = new PixelButton("开始");
		BorderPane testCtrl = new BorderPane();
		testCtrl.setBorder(new BorderX(BetterFX.GRAY).width(1, 0, 1, 0).build());
		testCtrl.setPadding(insets8);
		testCtrl.setCenter(testParm);
		testCtrl.setRight(test);
		
		// 测试区
		cubeUser = new Cube();
		cubeUser.setLayoutY(16);
		cubeSystem = new Cube();
		cubeSystem.setLayoutY(100);
		
		trackBox = new AnchorPane(cubeUser, cubeSystem);
		trackBox.setBorder(new BorderX(BetterFX.GRAY).width(0, 1, 0, 1).build());
		
		// 测试控制和测试区
		BorderPane testBox = new BorderPane();
		BorderPane.setMargin(trackBox, new Insets(0, 140, 0, 140));
		testBox.setTop(testCtrl);
		testBox.setCenter(trackBox);

		// 系统预设
		Label libsTitle = new Label("系统预设");
		valueLib = new TextField();
		valueLib.setPrefWidth(160);
		valueLib.setEditable(false);
		HBox libsTitleBox = new HBox(libsTitle, valueLib);
		libsTitleBox.setAlignment(Pos.CENTER_LEFT);
		libsTitleBox.setPadding(insets8);
		libsTitleBox.setSpacing(8);
		
		libs = new HBox();
		libs.setSpacing(16);
		libs.setPadding(insets8);
		libs.setAlignment(Pos.CENTER_LEFT);
		
		ScrollPane sp = new ScrollPane(libs);
		
		BorderPane libsBox = new BorderPane();
		libsBox.setPrefHeight(160);
		libsBox.setBorder(new BorderX(BetterFX.GRAY).top().build());
		libsBox.setTop(libsTitleBox);
		libsBox.setCenter(sp);
		
		// 配置、测试、系统预设
		BorderPane main = new BorderPane();
		main.setBorder(new BorderX(BetterFX.GRAY).left().build());
		main.setTop(gp);
		main.setCenter(testBox);
		main.setBottom(libsBox);
		
		BorderPane root = new BorderPane();
		BorderPane.setMargin(canvasBox, insets24);
		BorderPane.setAlignment(canvasBox, Pos.CENTER);
		root.setBorder(new BorderX(BetterFX.LIGHT_GRAY).top().build());
		root.setLeft(canvasBox);
		root.setCenter(main);
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(PixelFX.CSS);
		stage.setTitle("贝塞尔曲线动画帧生成器 - 夜雨");
		stage.getIcons().add(new Image("/icon.png"));
		stage.setMinWidth(1200);
		stage.setMinHeight(620);
		stage.setWidth(1200);
		stage.setHeight(620);
		stage.setScene(scene);
		stage.show();
	}
}