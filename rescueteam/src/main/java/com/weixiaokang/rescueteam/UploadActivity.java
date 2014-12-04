package com.weixiaokang.rescueteam;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.weixiaokang.rescueteam.util.UploadUtil;

public class UploadActivity extends ActionBarActivity
{
    // 要上传的文件路径，理论上可以传输任何文件，实际使用时根据需要处理
    private String uploadFile = Environment.getExternalStorageDirectory().getPath() + File.separator + "OpenCamera" + File.separator + "IMG_20141202215809.jpg";
    private String srcPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "OpenCamera" + File.separator + "IMG_20141202215809.jpg";
    // 服务器上接收文件的处理页面，这里根据需要换成自己的
    private String actionUrl = "http://www.tingyuge.me/upimg.php";
    private TextView mText1;
    private TextView mText2;
    private Button mButton;
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        mText1 = (TextView) findViewById(R.id.myText2);
        mText1.setText("文件路径：\n" + uploadFile);
        mText2 = (TextView) findViewById(R.id.myText3);
        mText2.setText("上传网址：\n" + actionUrl);
    /* 设置mButton的onClick事件处理 */
        mButton = (Button) findViewById(R.id.myButton);
        mButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        uploadFile(actionUrl);
                    }
                }).start();
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 13041309) {
                    Toast.makeText(UploadActivity.this, msg.getData().getString("result"), Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    /* 上传文件至Server，uploadUrl：接收文件的处理页面 */
    private void uploadFile(String uploadUrl)
    {
        Log.i("upload", "-->uploadFile");
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        try
        {
            URL url = new URL(uploadUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            // 设置每次传输的流大小，可以有效防止手机因为内存不足崩溃
            // 此方法用于在预先不知道内容长度时启用没有进行内部缓冲的 HTTP 请求正文的流。
            httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
            // 允许输入输出流
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            // 使用POST方法
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            Log.i("upload", "httpurlconnection");
            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
                    + srcPath.substring(srcPath.lastIndexOf("/") + 1)
                    + "\""
                    + end);
            dos.writeBytes(end);
            Log.i("upload", "dos end");
            FileInputStream fis = new FileInputStream(srcPath);
            byte[] buffer = new byte[8192]; // 8k
            int count = 0;
            Log.i("upload", "preupload");
            // 读取文件
            while ((count = fis.read(buffer)) != -1)
            {
                dos.write(buffer, 0, count);
            }
            fis.close();
            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            Log.i("upload", "dos flush");
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String result = br.readLine();
            Bundle bundle = new Bundle();
            bundle.putString("result", result);
            Message message = new Message();
            message.what = 13041309;
            message.setData(bundle);
            handler.sendMessage(message);
            Log.i("upload", "presuccess");
            dos.close();
            is.close();
        } catch (Exception e)
        {
            e.printStackTrace();
            Log.i("upload", "error");
        }
    }
}