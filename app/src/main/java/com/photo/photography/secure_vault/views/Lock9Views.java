package com.photo.photography.secure_vault.views;

/**
 * Created by Admin on 16-08-2016.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.drawable.Drawable;
import android.media.SoundPool;
import android.os.Build;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.photo.photography.R;

import java.util.ArrayList;
import java.util.List;

public class Lock9Views extends ViewGroup {

    /**
     * Vibration Manager
     */
    public static Vibrator vibrator;
    /**
     * Node definitions
     */
    private final List<NodeView> nodeList = new ArrayList<>(); // Already wired node list
    private final Context context;
    /**
     * Password builder
     */
    private final StringBuilder passwordBuilder = new StringBuilder();
    PathEffect[] mEffects;
    boolean isGlow;
    boolean isSound = false;
    private float x; // The current finger coordinate x
    private float y; // The current finger coordinate y
    /**
     * The layout and style of nodes
     */
    private Drawable nodeSrc;
    private Drawable nodeOnSrc;
    private float nodeSize; // Node size , if not zero , ignore padding and spacing attributes
    private float nodeAreaExpand; // The touch area node expansion
    private int nodeOnAnim; // Animation lit node
    private int lineColor;
    private float lineWidth;
    private float padding; // Padding
    private float spacing; // Node spacing
    /**
     * Automatic connection intermediate node
     */
    private boolean autoLink;
    private boolean enableVibrate;
    private int vibrateTime;
    /**
     * Line drawing brush
     */
    private Paint paint, brushPaint;
    /**
     * Results callback listener interface
     */
    private CallBack callBack;
    private SoundPool soundPool;
    private int musicId;

    /**
     * Constructor
     */

    public Lock9Views(Context context) {
        super(context);
        this.context = context;
        init(context, null, 0, 0);
    }

    public Lock9Views(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs, 0, 0);
    }

    public Lock9Views(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Lock9Views(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private static void makeEffects(PathEffect[] e, float phase) {
        e[0] = null;     // no effect
        e[1] = new CornerPathEffect(10);


        e[2] = new DashPathEffect(new float[]{10, 5, 5, 5}, phase);
        e[3] = new PathDashPathEffect(makePathDash(), 12, phase,
                PathDashPathEffect.Style.MORPH);
        e[4] = new ComposePathEffect(e[2], e[1]);
        e[5] = new ComposePathEffect(e[3], e[1]);
    }

    private static Path makePathDash() {
        Path p = new Path();
        p.moveTo(-6, 4);
        p.lineTo(6, 4);
        p.lineTo(6, 3);
        p.lineTo(-6, 3);
        p.close();
        p.moveTo(-6, -4);
        p.lineTo(6, -4);
        p.lineTo(6, -3);
        p.lineTo(-6, -3);
        return p;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public Drawable getNode() {
        return nodeSrc;
    }

    public void setNode(Drawable d) {
        this.nodeSrc = d;
//        foregroundPaint.setColor(color);
        invalidate();
        requestLayout();
    }

    /**
     * initialization
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        // Being defined attributes
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Lock9View, defStyleAttr, defStyleRes);

        nodeSrc = a.getDrawable(R.styleable.Lock9View_lock9_nodeSrc);
        nodeOnSrc = a.getDrawable(R.styleable.Lock9View_lock9_nodeOnSrc);
        nodeSize = a.getDimension(R.styleable.Lock9View_lock9_nodeSize, 0);
        nodeAreaExpand = a.getDimension(R.styleable.Lock9View_lock9_nodeAreaExpand, 0);
        nodeOnAnim = a.getResourceId(R.styleable.Lock9View_lock9_nodeOnAnim, 0);
        lineColor = a.getColor(R.styleable.Lock9View_lock9_lineColor, Color.argb(0, 0, 0, 0));
        lineWidth = a.getDimension(R.styleable.Lock9View_lock9_lineWidth, 0);
        padding = a.getDimension(R.styleable.Lock9View_lock9_padding, 0);
        spacing = a.getDimension(R.styleable.Lock9View_lock9_spacing, 0);

        autoLink = a.getBoolean(R.styleable.Lock9View_lock9_autoLink, true);
        autoLink = true;
        enableVibrate = a.getBoolean(R.styleable.Lock9View_lock9_enableVibrate, false);
        vibrateTime = a.getInt(R.styleable.Lock9View_lock9_vibrateTime, 20);

        a.recycle();

        // Initialization vibrator
        if (enableVibrate && !isInEditMode()) {
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }

        // Initialization brush
        paint = new Paint(Paint.DITHER_FLAG);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(lineWidth);
        paint.setColor(lineColor);


        paint.setAntiAlias(true); // Antialiasing

        // Build node
        if (nodeSrc != null) {
            for (int n = 0; n < 9; n++) {
                NodeView node = new NodeView(getContext(), n + 1);
                addView(node);
            }
        }

        mEffects = new PathEffect[6];
        // Clear FLAG, otherwise onDraw () is not called , because ViewGroup default transparent background do not need to call onDraw ()
        setWillNotDraw(false);
    }

    /**
     * TODO We let the height equal to the width - method to be verified
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = measureSize(widthMeasureSpec); // Measure the width
        setMeasuredDimension(size, size);
    }

    /**
     * TODO Measuring length
     */
    private int measureSize(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec); // Receive mode
        int specSize = MeasureSpec.getSize(measureSpec); // Get size
        switch (specMode) {
            case MeasureSpec.EXACTLY:
            case MeasureSpec.AT_MOST:
                return specSize;
            default:
                return 0;
        }
    }

    /**
     * In the here node layout
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            if (nodeSize > 0) { // If nodeSize value, the nodes draw nine subregional centers
                float areaWidth = (right - left) / 3;
                for (int n = 0; n < 9; n++) {
                    NodeView node = (NodeView) getChildAt(n);
                    // Being inside the palace 3 * 3 grid coordinates
                    int row = n / 3;
                    int col = n % 3;
                    // Calculate actual coordinates
                    int l = (int) (col * areaWidth + (areaWidth - nodeSize) / 2);
                    int t = (int) (row * areaWidth + (areaWidth - nodeSize) / 2);
                    int r = (int) (l + nodeSize);
                    int b = (int) (t + nodeSize);
                    node.layout(l, t, r, b);
                }
            } else { // Otherwise margins by dividing the layout , the size of the compute nodes manually
                float nodeSize = (right - left - padding * 2 - spacing * 2) / 3;
                for (int n = 0; n < 9; n++) {
                    NodeView node = (NodeView) getChildAt(n);
                    // Being inside the palace 3 * 3 grid coordinates
                    int row = n / 3;
                    int col = n % 3;
                    // Actual coordinates calculated to include padding and margins segmentation
                    int l = (int) (padding + col * (nodeSize + spacing));
                    int t = (int) (padding + row * (nodeSize + spacing));
                    int r = (int) (l + nodeSize);
                    int b = (int) (t + nodeSize);
                    node.layout(l, t, r, b);
                }
            }
        }
    }

    /**
     * Here processing gesture
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                x = event.getX(); // Here to coordinate finger real time recording
                y = event.getY();
                NodeView currentNode = getNodeAt(x, y);
                if (currentNode != null && !currentNode.isHighLighted()) { // Touch the new node is not lit.
                    if (nodeList.size() > 0) { // Preceded lit node
                        if (autoLink) { // Open the intermediate node to automatically connect
                            NodeView lastNode = nodeList.get(nodeList.size() - 1);
                            NodeView middleNode = getNodeBetween(lastNode, currentNode);
                            if (middleNode != null && !middleNode.isHighLighted()) { // An intermediate node exists not lit.
                                // Lighting intermediate node
                                middleNode.setHighLighted(true, true);
                                nodeList.add(middleNode);
                            }
                        }
                    }
                    // Lit touch current node
                    currentNode.setHighLighted(true, false);
                    nodeList.add(currentNode);
                }
                // There are only lit node redraw
                if (nodeList.size() > 0) {
                    requestDisallowInterceptTouchEvent(true);
                    invalidate();
                } else {
                    requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (nodeList.size() > 0) { // It has lit node
                    // Callback Results
                    if (callBack != null) {
                        // Generated passwords
                        passwordBuilder.setLength(0);
                        for (NodeView nodeView : nodeList) {
                            passwordBuilder.append(nodeView.getNum());
                        }
                        // callback
                        callBack.onFinish(passwordBuilder.toString());
                    }
                    // Clear Status
                    nodeList.clear();
                    for (int n = 0; n < getChildCount(); n++) {
                        NodeView node = (NodeView) getChildAt(n);
                        node.setHighLighted(false, false);
                    }
                    // Notify redraw
                    invalidate();
                }
                break;
        }
        return true;
    }

    /**
     * Draw callback system - the main draw lines
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // Has drawn first connection
        for (int n = 1; n < nodeList.size(); n++) {
            NodeView firstNode = nodeList.get(n - 1);
            NodeView secondNode = nodeList.get(n);

            if (isGlow)
                canvas.drawLine(firstNode.getCenterX(), firstNode.getCenterY(), secondNode.getCenterX(), secondNode.getCenterY(), brushPaint);
            canvas.drawLine(firstNode.getCenterX(), firstNode.getCenterY(), secondNode.getCenterX(), secondNode.getCenterY(), paint);
        }
        // If you already have a lit spot , then draw lines between points finger position and highlights
        if (nodeList.size() > 0) {
            NodeView lastNode = nodeList.get(nodeList.size() - 1);
            if (isGlow)
                canvas.drawLine(lastNode.getCenterX(), lastNode.getCenterY(), x, y, brushPaint);
            canvas.drawLine(lastNode.getCenterX(), lastNode.getCenterY(), x, y, paint);
        }
    }

    /**
     * Get to the point given coordinates Node , or null indicating the current finger between two Node
     */
    private NodeView getNodeAt(float x, float y) {
        for (int n = 0; n < getChildCount(); n++) {
            NodeView node = (NodeView) getChildAt(n);
            if (!(x >= node.getLeft() - nodeAreaExpand && x < node.getRight() + nodeAreaExpand)) {
                continue;
            }
            if (!(y >= node.getTop() - nodeAreaExpand && y < node.getBottom() + nodeAreaExpand)) {
                continue;
            }
            return node;
        }
        return null;
    }

    /**
     * Being in the middle of two Node Node, or null means no intermediate node
     */
    private NodeView getNodeBetween(NodeView na, NodeView nb) {
        if (na.getNum() > nb.getNum()) { // Ensure na less than nb
            NodeView nc = na;
            na = nb;
            nb = nc;
        }
        if (na.getNum() % 3 == 1 && nb.getNum() - na.getNum() == 2) { // Case level
            return (NodeView) getChildAt(na.getNum());
        } else if (na.getNum() <= 3 && nb.getNum() - na.getNum() == 6) { // Vertical case
            return (NodeView) getChildAt(na.getNum() + 2);
        } else if ((na.getNum() == 1 && nb.getNum() == 9) || (na.getNum() == 3 && nb.getNum() == 7)) { // Slanting
            return (NodeView) getChildAt(4);
        } else {
            return null;
        }
    }

    public interface CallBack {

        void onFinish(String password);

    }

    /**
     * Description node class
     */
    private class NodeView extends View {

        private final int num;
        private boolean highLighted = false;

        @SuppressWarnings("deprecation")
        public NodeView(Context context, int num) {
            super(context);
            this.num = num;
            setBackgroundDrawable(nodeSrc);
        }

        public boolean isHighLighted() {
            return highLighted;
        }

        @SuppressWarnings("deprecation")
        public void setHighLighted(boolean highLighted, boolean isMid) {
            if (this.highLighted != highLighted) {
                this.highLighted = highLighted;
                if (nodeOnSrc != null) { // Not Set Highlight picture will not change
                    setBackgroundDrawable(highLighted ? nodeOnSrc : nodeSrc);
                }
                if (nodeOnAnim != 0) { // Play the animation
                    if (highLighted) {
                        startAnimation(AnimationUtils.loadAnimation(getContext(), nodeOnAnim));
                    } else {
                        clearAnimation();
                    }
                }
                if (enableVibrate && !isMid) { // shock
                    if (highLighted) {
                        vibrator.vibrate(vibrateTime);
                    }
                }

                if (isSound && !isMid) {
                    if (highLighted) {
                        soundPool.play(musicId, 1, 1, 0, 0, 1);
                    }
                }
            }
        }

        public int getCenterX() {
            return (getLeft() + getRight()) / 2;
        }

        public int getCenterY() {
            return (getTop() + getBottom()) / 2;
        }

        public int getNum() {
            return num;
        }

    }

}