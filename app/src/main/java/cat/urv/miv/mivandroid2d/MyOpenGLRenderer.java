package cat.urv.miv.mivandroid2d;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.view.MotionEvent;
import android.view.View;


public class MyOpenGLRenderer implements Renderer {
    private Square square;
    private View view;
    private Background bg;
    private Bird bird;
    private Building bl;
    private End scream;
    private Score score;
    private int angle = 0;
    private Context context;
    private float v0 = 4;
    private float y0 = 0, y;
    private boolean up;
    private int hueco;
    private float t1 = 0.0f;
    private float speed = 1;
    private int mode;
    private boolean restart = false;
    private float despl;
    private MediaPlayer mp;
    private int level = 0;
    private boolean added = false;

    public MyOpenGLRenderer(View view, Context context) {
        this.view = view;
        this.context = context;
        this.up = false;
        this.mode = 0;
    }

    public void onSurfaceCreated(final GL10 gl, EGLConfig config) {

        // Image Background color
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
        Background b = new Background(gl, context);

        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);

        // Object's creation
        square = new Square(gl, context);
        bg = new Background(gl, context);
        bird = new Bird(gl, context);
        bl = new Building(gl, context);
        hueco = (int) (Math.random() * 5) + 1;
        scream = new End(gl, context);
        score = new Score(gl, context);

        // Touch listener
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                up = true;
                if (mode == 1) restart = true;
                return true;//always return true to consume event
            }
        });
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        // Clears the screen and depth buffer.
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, -3.0f);

        if (mode == 0) {
            gl.glPushMatrix();
            bg.draw(gl);
            bl.draw(gl);
            gl.glScalef(0.25f, 0.25f, 1.0f);
            despl = 5.0f - (angle / 50.0f * speed);
            if (despl <= -5.0f) {
                added = false;
                angle = 0;
                speed *= 1.2f;
                hueco = (int) (Math.random() * 4) + 1;
            }
            gl.glPushMatrix();
            gl.glTranslatef(despl, 0.0f, 0.0f);
            float f = -6;
            for (int i = 0; i < 7; i++) {
                if (i != hueco && i != hueco + 1) {
                    gl.glPushMatrix();
                    gl.glTranslatef(0.0f, f, 0.0f);
                    square.draw(gl);
                    gl.glPopMatrix();
                }
                f += 2;
            }

            gl.glPopMatrix();
            gl.glScalef(0.5f, 0.5f, 1.0f);
            gl.glPushMatrix();
            gl.glTranslatef(0.0f, 11.0f, 0.0f);
            gl.glScalef(0.75f, 0.75f, 0.0f);
            score.draw(gl, level);
            gl.glPopMatrix();

            if (up) {
                up = false;
                y0 = y;
                t1 = 0;
            }
            float t = t1 / (float) 60;
            y = (float) (y0 + v0 * t - 3f * Math.pow(t, 2));
            if (y > 0)
                y = Math.min(y, 13.0f);
            else
                y = Math.max(y, -13.0f);
            gl.glTranslatef(0.0f, y, 0.0f);
            bird.draw(gl);
            gl.glPopMatrix();

            if (despl <= 1.25f && despl >= -1.25f) {
                if (!added) {
                    added = true;
                    level++;
                }
                if (y / 2 <= -6.0f + (2.0f * hueco) - 1 || y / 2 >= -6.0f + (2.0f * hueco + 3)) {
                    mp = MediaPlayer.create(context, R.raw.grito);
                    mp.start();
                    mode = 1;
                }
                if (level == 10) mode = 1;
            }
            t1 += 1.0f;
            angle += 1.0f;
        } else {
            scream.draw(gl);
            if (restart) {
                y0 = 0;
                t1 = 0.0f;
                speed = 1;
                y = 0;
                hueco = (int) (Math.random() * 4) + 1;
                mode = 0;
                restart = false;
                despl = 5.0f;
                angle = 0;
                level = 0;
                added = false;
                mp.stop();
            }
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Define the Viewport
        gl.glViewport(0, 0, width, height);
        // Select the projection matrix
        gl.glMatrixMode(GL10.GL_PROJECTION);
        // Reset the projection matrix
        gl.glLoadIdentity();
        // Calculate the aspect ratio of the window
        GLU.gluPerspective(gl, 60.0f, (float) width / (float) height, 0.1f, 100.0f);

        // Select the modelview matrix
        gl.glMatrixMode(GL10.GL_MODELVIEW);
    }

}
