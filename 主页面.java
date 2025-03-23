package Xp.YuJian.App.光遇;

import Xp.YuJian.App.光遇.Hook.三服配置区;
import Xp.YuJian.App.光遇.悬浮窗.页面.主题设置_页面;
import Xp.YuJian.App.光遇.悬浮窗.页面.云端账号_页面;
import Xp.YuJian.App.光遇.悬浮窗.页面.任意传送_页面;
import Xp.YuJian.App.光遇.悬浮窗.页面.娱乐功能_页面;
import Xp.YuJian.App.光遇.悬浮窗.页面.实用功能_页面;
import Xp.YuJian.App.光遇.悬浮窗.页面.带人跑图_页面;
import Xp.YuJian.App.光遇.悬浮窗.页面.新号系列_页面;
import Xp.YuJian.App.光遇.悬浮窗.页面.景点系统_页面;
import Xp.YuJian.App.光遇.悬浮窗.页面.设备模拟_页面;
import Xp.YuJian.App.光遇.悬浮窗.页面.跑图功能_页面;
import Xp.YuJian.App.光遇.悬浮窗.页面.辅助设置_页面;
import Xp.YuJian.App.工具类.游戏检测;
import Xp.YuJian.App.工具类.自定义提示;
import Xp.YuJian.App.工具类.音频播放;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import tyrantgit.explosionfield.ExplosionField;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import android.graphics.drawable.PictureDrawable;
import android.widget.ImageView;


public class 主页面 {
    public Activity activity;
	public Context context;
	private 音频播放 audioPlayer;
    public static ImageView floatingButton;

    public static FrameLayout parentLayout;
    public static FrameLayout floatingLayout;
	private static boolean isManualClick = true;

    public static LinearLayout layout2;
	public static TextView closeButton;
	public static GradientDrawable backgroundDrawable;
	private String 悬浮窗图片颜色="#" + 三服配置区.sec;
	private static 主页面 floatingDialog;


    public static void setFloatingDialog(主页面 dialog) {
        floatingDialog = dialog;
    }

    public 主页面(Activity activity) {
        this.activity = activity;
		this.context = activity.getApplicationContext();
    }


	public static void showCustomDialog(final Activity activity) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle("版本不匹配");
		builder.setMessage("游戏已经更新\n开发者正在努力适配最新版\n请等待更新即可");
		builder.setCancelable(false); // 设置对话框点击外部不可关闭

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 点击确定按钮后执行的操作
					// 启动浏览器并访问百度网站
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(三服配置区.URL));
					activity.startActivity(intent);
				}
			});

		AlertDialog dialog = builder.create();
		dialog.show();
	}


    public void showFloatingButton() {

		String versionName = 游戏检测.getAppVersionName(activity);
		int versionCode = 游戏检测.getAppVersionCode(activity);
		if (versionName != null && versionName.equals(三服配置区.游戏版本) && versionCode == 三服配置区.游戏版本号) {

		} else {
			// 版本名称或版本号不满足条件时执行的操作

		    showCustomDialog(activity);
		}
		parentLayout = activity.findViewById(android.R.id.content);

		floatingLayout = createFloatingLayout();
		floatingLayout.setVisibility(View.GONE);

		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.CENTER;

		floatingLayout.setVisibility(View.GONE);
		parentLayout.addView(floatingLayout, layoutParams);

		floatingButton = createFloatingButton();
		floatingButton.setVisibility(View.GONE);

		FrameLayout.LayoutParams floatingButtonLayoutParams = new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT);
		floatingButtonLayoutParams.gravity = Gravity.START | Gravity.TOP;
		floatingButtonLayoutParams.setMargins(0, 0, 0, 0);
		floatingButton.setLayoutParams(floatingButtonLayoutParams);

		FrameLayout.LayoutParams buttonLayoutParams = (FrameLayout.LayoutParams) floatingButton.getLayoutParams();

		int buttonSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());
		buttonLayoutParams.height = buttonSize;
		buttonLayoutParams.width = buttonSize;

		floatingButton.setLayoutParams(buttonLayoutParams);

		setupRotationAnimation(floatingButton);


		floatingButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (floatingLayout.getVisibility() == View.VISIBLE) {
						handleClickEvent();
					}
				}
			});

		floatingButton.setOnTouchListener(new View.OnTouchListener() {
				private float lastX;
				private float lastY;
				private float startX;
				private float startY;
				private boolean isMoved;

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							lastX = event.getRawX();
							lastY = event.getRawY();
							startX = lastX;
							startY = lastY;
							isMoved = false;
							return true;
						case MotionEvent.ACTION_MOVE:
							float deltaX = event.getRawX() - lastX;
							float deltaY = event.getRawY() - lastY;
							floatingButton.setTranslationX(floatingButton.getTranslationX() + deltaX);
							floatingButton.setTranslationY(floatingButton.getTranslationY() + deltaY);
							lastX = event.getRawX();
							lastY = event.getRawY();
							isMoved = true;
							return true;
						case MotionEvent.ACTION_UP:
							if (Math.abs(event.getRawX() - startX) < 20 && Math.abs(event.getRawY() - startY) < 20) {
								handleClickEvent();
								return floatingLayout.getVisibility() == View.VISIBLE;
							} else {
								snapToEdge();
								return true;
							}
					}
					return false;
				}

				private void snapToEdge() {
					int parentWidth = parentLayout.getWidth();
					float currentX = floatingButton.getX();
					if (currentX < (parentWidth / 2)) {
						floatingButton.animate().x(0).setDuration(500).start();
					} else {
						floatingButton.animate().x(parentWidth - floatingButton.getWidth()).setDuration(500).start();
					}
				}
			});

		parentLayout.addView(floatingButton);

	}

	private void setupRotationAnimation(View view) {
		ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
		rotationAnimator.setDuration(15000); // 15秒
		rotationAnimator.setRepeatCount(ObjectAnimator.INFINITE); // 无限重复
		rotationAnimator.setInterpolator(new LinearInterpolator()); // 线性时间插值
		rotationAnimator.start();
	}

	private void handleClickEvent() {
        if (floatingLayout.getVisibility() == View.GONE) { 
			audioPlayer = new 音频播放(activity, 三服配置区.音效);
			audioPlayer.playAudio();
            showFloatingWindow();
        } else {
			audioPlayer = new 音频播放(activity, 三服配置区.音效);
			audioPlayer.playAudio();
            hideFloatingWindow();
        }
    }

    public void hideFloatingWindow() {

		ExplosionField explosionField = ExplosionField.attach2Window(activity);

		// 获取 floatingLayout 的原始布局参数
		final ViewGroup.LayoutParams originalLayoutParams = floatingLayout.getLayoutParams();

		// 对 floatingLayout 应用爆炸效果
		explosionField.explode(floatingLayout);

		// 设置一个与动画持续时间相同的延迟，然后隐藏视图
		floatingLayout.postDelayed(new Runnable() {
				@Override
				public void run() {
					// 动画完成后的操作
					// 确保 floatingLayout 未被从其父视图中移除
					if (floatingLayout.getParent() == null) {
						// 如果 floatingLayout 被移除了，将它再次添加到父视图
						ViewGroup parent = (ViewGroup) activity.findViewById(android.R.id.content);
						parent.addView(floatingLayout, originalLayoutParams);
					}

					// 恢复 floatingLayout 的可见性
					floatingLayout.setVisibility(View.GONE);

					// 重置视图的透明度和缩放
					floatingLayout.setAlpha(1.0f);
					floatingLayout.setScaleX(1.0f);
					floatingLayout.setScaleY(1.0f);

					floatingButton.setVisibility(View.VISIBLE);
				}
			}, 300); // 延迟时间应与爆炸动画的持续时间相匹配
	}
	public void hideFloatingWindowf() {
		float startScale = 1f;
		float endScale = 0f; 
		ScaleAnimation scaleAnimation = new ScaleAnimation(startScale, endScale, startScale, endScale,
														   Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(300); // 设置最小300
		AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
		alphaAnimation.setDuration(300); // 设置最小300

		AnimationSet animationSet = new AnimationSet(true);
		animationSet.addAnimation(scaleAnimation);
		animationSet.addAnimation(alphaAnimation);

		floatingLayout.startAnimation(animationSet);
		floatingLayout.postDelayed(new Runnable() {
				public void run() {
					floatingLayout.setVisibility(View.GONE);
					floatingButton.setVisibility(View.VISIBLE);
				}
			}, 300);
	}



	public void animateFloatingButton() {
		// 设置缩放的起始和结束值
		float startScale = 1f; // 开始缩放比例 (原始大小)
		float midScale = 0.95f; // 中间缩放比例 (稍微缩小)
		float endScale = 1f; // 结束缩放比例 (恢复原始大小)

		// 创建缩放动画，从原始大小缩小到稍微小一点，然后再放大回原始大小
		ScaleAnimation scaleDownAnimation = new ScaleAnimation(startScale, midScale, startScale, midScale,
															   Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scaleDownAnimation.setDuration(150); // 前半段动画时间，150毫秒

		ScaleAnimation scaleUpAnimation = new ScaleAnimation(midScale, endScale, midScale, endScale,
															 Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scaleUpAnimation.setDuration(150); // 后半段动画时间，150毫秒

		// 创建动画集合以便顺序播放动画
		AnimationSet animationSet = new AnimationSet(true);
		animationSet.addAnimation(scaleDownAnimation);
		animationSet.addAnimation(scaleUpAnimation);

		// 为floatingButton设置动画
		floatingButton.startAnimation(animationSet);
	}


	private void showFloatingWindow() {
		floatingLayout.setVisibility(View.VISIBLE);
		floatingButton.setVisibility(View.GONE);

		float startScale = 0f; // 初始缩放比例为0，即不可见
		float endScale = 1f; // 最终缩放比例为1，即正常大小

		ScaleAnimation scaleAnimation = new ScaleAnimation(startScale, endScale, startScale, endScale,
														   Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(300); // 设置缩放动画持续时间为500毫秒

		AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
		alphaAnimation.setDuration(300); // 设置透明度渐变动画持续时间为500毫秒

		AnimationSet animationSet = new AnimationSet(true);
		animationSet.addAnimation(scaleAnimation);
		animationSet.addAnimation(alphaAnimation);

		floatingLayout.startAnimation(animationSet);
	}


    public void hideFloatingButton() {
        parentLayout.removeView(floatingButton);
        parentLayout.removeView(floatingLayout);
    }

	private FrameLayout createFloatingLayout() {
		final FrameLayout floatingLayout = new FrameLayout(activity);
		floatingLayout.setBackgroundColor(Color.WHITE);
		floatingLayout.setPadding(4, 0, 12, 12);
		// 在布局中添加一个半透明的蒙版视图

		final View resizeOverlay = new View(context);

		// 使用GradientDrawable创建圆角背景
		GradientDrawable drawable = new GradientDrawable();
		drawable.setShape(GradientDrawable.RECTANGLE);
		drawable.setCornerRadius(dpToPx(三服配置区.圆角 * 2)); // 设置圆角半径为12dp
		drawable.setColor(Color.parseColor("#80000000")); // 半透明的黑色蒙版

		resizeOverlay.setBackground(drawable); // 设置背景为圆角矩形
		resizeOverlay.setLayoutParams(new FrameLayout.LayoutParams(
										  FrameLayout.LayoutParams.MATCH_PARENT,
										  FrameLayout.LayoutParams.MATCH_PARENT));
		resizeOverlay.setVisibility(View.GONE); // 初始时隐藏蒙版
		floatingLayout.addView(resizeOverlay);



		LinearLayout mainLayout = new LinearLayout(activity);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		mainLayout.setGravity(Gravity.CENTER);

		LinearLayout buttonLayout = new LinearLayout(activity);
		buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
		buttonLayout.setGravity(Gravity.CENTER);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT,
			LinearLayout.LayoutParams.WRAP_CONTENT
		);
		layoutParams.setMargins(dpToPx(16), dpToPx(16), dpToPx(16), 0);
		buttonLayout.setLayoutParams(layoutParams);

		TextView titleTextView = new TextView(activity);
		titleTextView.setText(" " + 三服配置区.应用名称);
		titleTextView.setTextColor(Color.parseColor("#" + 三服配置区.ztys));
		LinearLayout.LayoutParams titleLayoutParams = new LinearLayout.LayoutParams(
			ViewGroup.LayoutParams.WRAP_CONTENT,
			ViewGroup.LayoutParams.WRAP_CONTENT);
		titleLayoutParams.gravity = Gravity.CENTER;
		titleLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
		titleLayoutParams.weight = 1;
		titleLayoutParams.setMargins(dpToPx(8), 0, 0, 0);
		titleTextView.setLayoutParams(titleLayoutParams);
		titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

		backgroundDrawable = new GradientDrawable();
		backgroundDrawable.setShape(GradientDrawable.RECTANGLE);
		backgroundDrawable.setCornerRadius(dpToPx(三服配置区.圆角 * 2));
		backgroundDrawable.setColor(Color.parseColor("#" + 三服配置区.beijingtou + 三服配置区.sec));


	    closeButton = new TextView(activity);
		closeButton.setText("✖");
		closeButton.setTextColor(Color.WHITE);
		LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
			dpToPx(16), // 设置按钮宽度为16dp
			dpToPx(22)); // 设置按钮高度为22dp
		buttonLayoutParams.gravity = Gravity.CENTER; // 设置按钮在父容器中居中
		buttonLayoutParams.setMargins(dpToPx(12), dpToPx(4), dpToPx(12), dpToPx(6));
		closeButton.setLayoutParams(buttonLayoutParams);
		closeButton.setGravity(Gravity.CENTER); // 将文本对齐方式设置为居中
		closeButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (isManualClick) {
						// 处理手动点击
						audioPlayer = new 音频播放(activity, "posui.mp3");
						audioPlayer.playAudio();

						// 创建 ExplosionField 实例
						hideFloatingWindow();
					} else {
						// 处理performClick调用的点击
						// 这里可以放置特定的逻辑，或者什么都不做
						audioPlayer = new 音频播放(activity, 三服配置区.音效);
						audioPlayer.playAudio();
						hideFloatingWindowf();
					}
					// 重置标志为手动点击
					isManualClick = true;
				}
			});


		FrameLayout closeButtonContainer = new FrameLayout(activity);
		closeButtonContainer.addView(closeButton);

		GradientDrawable closeButtonBackground = new GradientDrawable();
		closeButtonBackground.setShape(GradientDrawable.RECTANGLE);
		closeButtonBackground.setCornerRadius(dpToPx(三服配置区.圆角 * 2));
		closeButtonBackground.setColor(Color.parseColor("#" + 三服配置区.beijingtou + 三服配置区.sec));
		closeButtonContainer.setBackground(closeButtonBackground);

		buttonLayout.addView(titleTextView);
		buttonLayout.addView(closeButtonContainer);

		LinearLayout layoutBelowDivider = new LinearLayout(activity);
		layoutBelowDivider.setOrientation(LinearLayout.HORIZONTAL);
		layoutBelowDivider.setGravity(Gravity.CENTER);
		layoutBelowDivider.setWeightSum(1);

		LinearLayout layout1 = new LinearLayout(activity);
		LinearLayout.LayoutParams layout1Params = new LinearLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.MATCH_PARENT);
		layout1.setLayoutParams(layout1Params);
		layout1.setOrientation(LinearLayout.VERTICAL);
		layout1.setPadding(0, dpToPx(8), 0, 0);

		final 主页面左侧功能按钮加载 customView = new 主页面左侧功能按钮加载(activity, "实用功能", "跑图功能", "娱乐功能", "云端账号", "任意传送", "景点系统", "带人跑图", "新号系列", "辅助设置", "主题设置", "设备模拟");
		LinearLayout.LayoutParams customViewParams = new LinearLayout.LayoutParams(
			ViewGroup.LayoutParams.WRAP_CONTENT,
			ViewGroup.LayoutParams.WRAP_CONTENT);
		layout1.addView(customView, customViewParams);

	    layout2 = new LinearLayout(activity);
		LinearLayout.LayoutParams layout2Params = new LinearLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.MATCH_PARENT);
		layout2Params.gravity = Gravity.CENTER;
		layout2.setLayoutParams(layout2Params);
		layout2.setGravity(Gravity.CENTER);
		layout2.setOrientation(LinearLayout.VERTICAL);
		layout2.setPadding(dpToPx(8), dpToPx(4), dpToPx(10), dpToPx(10)); 

		if (主页面左侧功能按钮加载.首次加载.equals("是")) {
			主页面左侧功能按钮加载.首次加载 = "否";
			实用功能_页面 layoutx1 = new 实用功能_页面(activity);
			layout2.addView(layoutx1.createLayout());
		}

		customView.setOnButtonClickListener(new 主页面左侧功能按钮加载.OnButtonClickListener() {
				@Override
				public void onButtonClicked(final int buttonNumber) {
					// 触发按钮的缩放动画


					// 同时开始播放音频
					audioPlayer = new 音频播放(activity, 三服配置区.音效);
					audioPlayer.playAudio();

					// 同时执行布局切换
					layout2.removeAllViews();
					switchLayouts(buttonNumber); // 将原来的switch语句移到一个单独的方法中
				}
			});

		// 创建一个GradientDrawable对象
		GradientDrawable gradientDrawable = new GradientDrawable();
		// 创建一个GradientDrawable对象
		// 设置每个角的圆角半径
		gradientDrawable.setCornerRadii(new float[]{
											0, 0, // 左上圆角0dp
											dpToPx(12), dpToPx(12), // 右上圆角14dp
											dpToPx(12), dpToPx(12), // 左底圆角0dp // 右底圆角14dp
											dpToPx(0),dpToPx(0)
										});
		gradientDrawable.setColor(Color.parseColor("#26000000")); // 设置背景颜色为#334466
		// 创建ScrollView对象
		ScrollView scrollView1 = new ScrollView(activity);
		// 设置ScrollView的布局参数
		LinearLayout.LayoutParams scrollView1Params = new LinearLayout.LayoutParams(
			dpToPx(90), // 宽度为90dp
			ViewGroup.LayoutParams.MATCH_PARENT); // 高度为MATCH_PARENT
		scrollView1.setLayoutParams(scrollView1Params);
		// 禁用垂直滚动条
		scrollView1.setVerticalScrollBarEnabled(false);
		// 设置ScrollView的顶部和底部内边距为8dp
		scrollView1.setPadding(0, dpToPx(4), 0, dpToPx(8));
		scrollView1Params.setMargins(0, dpToPx(8), 0, dpToPx(8));
		// 将GradientDrawable应用到ScrollView的背景上
		scrollView1.setBackground(gradientDrawable);
		// 将layout1添加到ScrollView中
		scrollView1.addView(layout1);



		ScrollView scrollView2 = new ScrollView(activity);
		LinearLayout.LayoutParams scrollView2Params = new LinearLayout.LayoutParams(
			0, 
			ViewGroup.LayoutParams.MATCH_PARENT);
		scrollView2Params.weight = 1; 
		scrollView2Params.setMargins(0, dpToPx(8), 0, dpToPx(8));
		scrollView2.setLayoutParams(scrollView2Params);
		scrollView2.addView(layout2);
		scrollView2.setVerticalScrollBarEnabled(false);
		layoutBelowDivider.addView(scrollView1);
		layoutBelowDivider.addView(scrollView2);
		mainLayout.addView(buttonLayout);

		mainLayout.addView(layoutBelowDivider);
		GradientDrawable layoutBackground = new GradientDrawable();
		layoutBackground.setShape(GradientDrawable.RECTANGLE);
		layoutBackground.setCornerRadius(dpToPx(三服配置区.圆角 * 2));
		layoutBackground.setColor(Color.parseColor("#" + 三服配置区.beijingtou + 三服配置区.bjys));
		floatingLayout.setBackground(layoutBackground);
		int maxLayoutHeight = (int) (getScreenHeight() * 0.82);
		final FrameLayout.LayoutParams mainLayoutParams = new FrameLayout.LayoutParams(
			(int) (getScreenWidth() * 0.50),
			maxLayoutHeight);
		mainLayoutParams.gravity = Gravity.CENTER;
		floatingLayout.addView(mainLayout, mainLayoutParams);

		ImageView imageView = crxeateFloatingButtonp(); 
		FrameLayout.LayoutParams imageLayoutParams = new FrameLayout.LayoutParams(
			FrameLayout.LayoutParams.WRAP_CONTENT,
			FrameLayout.LayoutParams.WRAP_CONTENT);
		imageLayoutParams.gravity = Gravity.BOTTOM | Gravity.END;
		imageLayoutParams.setMargins(0, 0, dpToPx(8), dpToPx(8)); 

		int imageWidth = dpToPx(30);
		int imageHeight = dpToPx(26);
		imageLayoutParams.width = imageWidth;
		imageLayoutParams.height = imageHeight;

		imageView.setBackgroundColor(Color.TRANSPARENT); 
		floatingLayout.addView(imageView, imageLayoutParams);

		imageView.setOnTouchListener(new View.OnTouchListener() {
				private float startX;
				private float startY;

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							startX = event.getX();
							startY = event.getY();
							resizeOverlay.setVisibility(View.VISIBLE); // 显示蒙版
							floatingLayout.setPadding(0, 0, 0, 0);
							return true;
						case MotionEvent.ACTION_MOVE:
							float newX = event.getX();
							float newY = event.getY();
							float deltaX = newX - startX;
							float deltaY = newY - startY;

							float newWidth = calculateNewWidth(deltaX);
							float newHeight = calculateNewHeight(deltaY);

							mainLayoutParams.width = (int) newWidth;
							mainLayoutParams.height = (int) newHeight;
							floatingLayout.setLayoutParams(mainLayoutParams);

							startX = newX;
							startY = newY;
							return true;
						case MotionEvent.ACTION_UP:
							floatingLayout.setPadding(4, 0, 12, 12);
							resizeOverlay.setVisibility(View.GONE); // 隐藏蒙版
							return true;
					}
					return false;
				}

				private float calculateNewWidth(float deltaX) {
					float newWidth = mainLayoutParams.width + deltaX;
					return Math.max(300, newWidth); // 限制宽度最小为300
				}

				private float calculateNewHeight(float deltaY) {
					float newHeight = mainLayoutParams.height + deltaY;
					return Math.max(300, newHeight); // 限制高度最小为300
				}
			});



		return floatingLayout;
	}



	private void switchLayouts(int buttonNumber) {
		// 这里是原来的switch语句
		switch (buttonNumber) {
			case 1:
				实用功能_页面 实用功能 = new 实用功能_页面(activity);
				layout2.addView(实用功能.createLayout());
				break;
			case 2:
				跑图功能_页面 跑图功能 = new 跑图功能_页面(activity);
				layout2.addView(跑图功能.createLayout());
				break;
			case 3:
				娱乐功能_页面 娱乐功能 = new 娱乐功能_页面(activity);
				layout2.addView(娱乐功能.createLayout());
				break;
			case 4:
				云端账号_页面 云端账号 = new 云端账号_页面(activity);
				layout2.addView(云端账号.createLayout());
				break;
			case 5:
				任意传送_页面 任意传送 = new 任意传送_页面(activity);
				layout2.addView(任意传送.createLayout());
				break;
			case 6:
				景点系统_页面 景点系统 = new 景点系统_页面(activity);
				layout2.addView(景点系统.createLayout());
				break;
			case 7:
				带人跑图_页面 带人跑图 = new 带人跑图_页面(activity);
				layout2.addView(带人跑图.createLayout());
				break;
			case 8:
				新号系列_页面 新号功能 = new 新号系列_页面(activity);
				layout2.addView(新号功能.createLayout());
				break;
			case 9:
				辅助设置_页面 辅助设置 = new 辅助设置_页面(activity);
				layout2.addView(辅助设置.createLayout());
				break;
			case 10:
				主题设置_页面 主题设置 = new 主题设置_页面(activity);
				layout2.addView(主题设置.createLayout());
				break;
			case 11:
				设备模拟_页面 设备模拟 = new 设备模拟_页面(activity);
				layout2.addView(设备模拟.createLayout());
				break;

			default:
				自定义提示.showToast(activity, "未知按钮错误", Toast.LENGTH_SHORT);
				break;
		}
	}
	public static void 关闭() {
        isManualClick = false;
        closeButton.performClick();
    }

    public void selectButton(int p0) {
        int clickedButtonNumber = p0;
        invalidate();
    }

    private void invalidate() {
    }

    private int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    private int getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private int dpToPx(int dp) {
        float scale = activity.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


	private ImageView createFloatingButton() {
		ImageView floatingButton = new ImageView(activity);

		try {
			String svgCode = "<svg t=\"1705880156677\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"1578\" width=\"256\" height=\"256\"><path d=\"M296 440c-44.1 0-80 35.9-80 80s35.9 80 80 80 80-35.9 80-80-35.9-80-80-80z\" fill=\"" + 悬浮窗图片颜色 + "\" p-id=\"1579\"></path><path d=\"M960 512c0-247-201-448-448-448S64 265 64 512c0 1.8 0.1 3.5 0.1 5.3 0 0.9-0.1 1.8-0.1 2.7h0.2C68.5 763.3 267.7 960 512 960c236.2 0 430.1-183.7 446.7-415.7 0.1-0.8 0.1-1.6 0.2-2.3 0.4-4.6 0.5-9.3 0.7-13.9 0.1-2.7 0.4-5.3 0.4-8h-0.2c0-2.8 0.2-5.4 0.2-8.1z m-152 8c0 44.1-35.9 80-80 80s-80-35.9-80-80 35.9-80 80-80 80 35.9 80 80zM512 928C284.4 928 99 744.3 96.1 517.3 97.6 408.3 186.6 320 296 320c110.3 0 200 89.7 200 200 0 127.9 104.1 232 232 232 62.9 0 119.9-25.2 161.7-66-66 142.7-210.4 242-377.7 242z\" fill=\"" + 悬浮窗图片颜色 + "\" p-id=\"1580\"></path></svg>";
			SVG svg = SVG.getFromString(svgCode);
			Drawable drawable = new PictureDrawable(svg.renderToPicture());
			floatingButton.setImageDrawable(drawable);
		} catch (SVGParseException e) {
			e.printStackTrace();
		}

		floatingButton.setAdjustViewBounds(true);
		floatingButton.setMaxWidth(dpToPx(72));
		floatingButton.setMaxHeight(dpToPx(72));
		floatingButton.setBackgroundColor(Color.TRANSPARENT);

		return floatingButton;
	}




	private ImageView crxeateFloatingButtonp() {
		ImageView floatingButton = new ImageView(activity);

        try {
            String svgCode = "<svg t=\"1705826827756\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"2470\" width=\"128\" height=\"128\"><path d=\"M814.933333 512l-98.133333-98.133333 29.866667-29.866667 149.333333 149.333333-149.333333 149.333334-29.866667-29.866667 98.133333-98.133333H554.666667v-42.666667h260.266666zM209.066667 512H469.333333v42.666667H209.066667l98.133333 98.133333-29.866667 29.866667L128 533.333333 277.333333 384l29.866667 29.866667L209.066667 512z\" fill=\"" + 悬浮窗图片颜色 + "\" p-id=\"2471\"></path></svg>";
			SVG svg = SVG.getFromString(svgCode);
            Drawable drawable = new PictureDrawable(svg.renderToPicture());
            floatingButton.setImageDrawable(drawable);
        } catch (SVGParseException e) {
            e.printStackTrace();
        }

        floatingButton.setAdjustViewBounds(true);
        floatingButton.setMaxWidth(dpToPx(44));
        floatingButton.setMaxHeight(dpToPx(24));
        floatingButton.setBackgroundColor(Color.TRANSPARENT);

		return floatingButton;
	}




}

