package com.zxc.network_coding.utils;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import static java.io.File.separatorChar;

/**
 * 与文件处理相关的工具类
 */
public class FileUtils {

    public static final String TAG = "FileUtils" ;

    public static byte[] readLines(@NonNull String filename) throws IOException {
        /*FileReader fileReader = new FileReader(filename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<>();
        Log.d(TAG,bufferedReader.readLine()) ;
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
            Log.d(TAG,line) ;
        }
        bufferedReader.close();
        return lines.toArray(new Byte[0]);*/
        FileChannel fc = null;
        try{
            fc = new RandomAccessFile(filename,"r").getChannel();
            MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size()).load();
            System.out.println(byteBuffer.isLoaded());
            byte[] result = new byte[(int)fc.size()];
            if (byteBuffer.remaining() > 0) {
//              System.out.println("remain");
                byteBuffer.get(result, 0, byteBuffer.remaining());
            }
            return result;
        }catch (IOException e) {
            e.printStackTrace();
            throw e;
        }finally{
            try{
                fc.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getName(@NonNull Uri uri){
        String path = uri.getPath() ;
        assert path != null;
        int index = path.lastIndexOf(separatorChar);
        return path.substring(index+1);
    }

}
