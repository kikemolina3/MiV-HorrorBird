package cat.urv.miv.mivandroid2d;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Score{
    private float vertices[] = {-1.0f, -2.0f, 0.0f,
            -1.0f,  2.0f, 0.0f,
            1.0f,  2.0f, 0.0f,
            1.0f, -2.0f, 0.0f};

    private short faces[] = { 0, 1, 2, 0, 2, 3 };

    private float texCoords[][] = {
            {0.0f, 1.0f, 0.0f, 0.0f, 0.1f, 0.0f,0.1f,1.0f},
            {0.1f, 1.0f, 0.1f, 0.0f, 0.2f, 0.0f,0.2f,1.0f},
            {0.2f, 1.0f, 0.2f, 0.0f, 0.3f, 0.0f,0.3f,1.0f},
            {0.3f, 1.0f, 0.3f, 0.0f, 0.4f, 0.0f,0.4f,1.0f},
            {0.4f, 1.0f, 0.4f, 0.0f, 0.5f, 0.0f,0.5f,1.0f},
            {0.5f, 1.0f, 0.5f, 0.0f, 0.6f, 0.0f,0.6f,1.0f},
            {0.6f, 1.0f, 0.6f, 0.0f, 0.7f, 0.0f,0.7f,1.0f},
            {0.7f, 1.0f, 0.7f, 0.0f, 0.8f, 0.0f,0.8f,1.0f},
            {0.8f, 1.0f, 0.8f, 0.0f, 0.9f, 0.0f,0.9f,1.0f},
            {0.9f, 1.0f, 0.9f, 0.0f, 1.0f, 0.0f,1.0f,1.0f}
    };
    private int[] texture;

    // Our vertex buffer.
    private FloatBuffer vertexBuffer;

    // Our index buffer.
    private ShortBuffer indexBuffer;

    private FloatBuffer texCoordsBuffer[];

    public Score(GL10 gl, Context context) {
        texCoordsBuffer = new FloatBuffer[10];
        //Move the vertices list into a buffer
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        //Move the faces list into a buffer
        ByteBuffer ibb = ByteBuffer.allocateDirect(faces.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        indexBuffer = ibb.asShortBuffer();
        indexBuffer.put(faces);
        indexBuffer.position(0);

        @SuppressLint("ResourceType") InputStream is = context.getResources().openRawResource(R.drawable.numbers_tile);
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        texture = new int[1];
        gl.glGenTextures(1, texture, 0);
        System.out.println("SCORE");
            gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[0]);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_CLAMP_TO_EDGE, GL10.GL_REPEAT);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        for(int i=0;i<10;i++){
            ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4);
            tbb.order(ByteOrder.nativeOrder());
            texCoordsBuffer[i] = tbb.asFloatBuffer();
            texCoordsBuffer[i].put(texCoords[i]);
            texCoordsBuffer[i].position(0);
        }
        bitmap.recycle();
    }


    public void draw(GL10 gl, int number) {

        // Enabled the vertices buffer for writing and to be used during rendering.
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        // Specifies the location and data format of an array of vertex
        // coordinates to use when rendering.
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordsBuffer[number]);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[0]);

        gl.glDrawElements(GL10.GL_TRIANGLES, faces.length, GL10.GL_UNSIGNED_SHORT, indexBuffer);

        // Disable the vertices buffer.
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

    }
}