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

public class Bird {
    private int frame;

    private float texCoords[][] = {
            {0.0f,0.5f,0.0f,0.0f,0.5f,0.0f,0.5f,0.5f},
            {0.5f,0.5f,0.5f,0.0f,1.0f,0.0f,1.0f,0.5f},
            {0.0f,1.0f,0.0f,0.5f,0.5f,0.5f,0.5f,1.0f},
            {0.5f,1.0f,0.5f,0.5f,1.0f,0.5f,1.0f,1.0f}
    };
    private float vertices[] = {-1.0f, -1.0f, 0.0f,
            -1.0f,  1.0f, 0.0f,
            1.0f,  1.0f, 0.0f,
            1.0f, -1.0f, 0.0f };

    private short faces[] = { 0, 1, 2, 0, 2, 3 };

    private int[] texture;

    // Our vertex buffer.
    private FloatBuffer vertexBuffer;

    // Our index buffer.
    private ShortBuffer indexBuffer;

    private FloatBuffer texCoordsBuffer[];

    public Bird(GL10 gl, Context context) {
        frame=0;
        texCoordsBuffer = new FloatBuffer[4];
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

        @SuppressLint("ResourceType") InputStream is = context.getResources().openRawResource(R.drawable.bird2);
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        texture = new int[1];

        gl.glGenTextures(
                1, texture, 0);
        System.out.println("BIRD");
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[0]);
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[1]);
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[2]);
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[3]);

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_CLAMP_TO_EDGE, GL10.GL_REPEAT);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        for(int i=0;i<4;i++) {
            ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords[i].length * 4);
            tbb.order(ByteOrder.nativeOrder());
            texCoordsBuffer[i] = tbb.asFloatBuffer();
            texCoordsBuffer[i].put(texCoords[i]);
            texCoordsBuffer[i].position(0);
        }
    }


    public void draw(GL10 gl) {
        if(frame==40)frame=0;
        // Enabled the vertices buffer for writing and to be used during rendering.
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        // Specifies the location and data format of an array of vertex
        // coordinates to use when rendering.
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordsBuffer[(int)(frame/10)]);
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[(int)(frame/10)]);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[0]);
        gl.glDrawElements(GL10.GL_TRIANGLES, faces.length, GL10.GL_UNSIGNED_SHORT, indexBuffer);

        // Disable the vertices buffer.
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        frame++;
    }
}
