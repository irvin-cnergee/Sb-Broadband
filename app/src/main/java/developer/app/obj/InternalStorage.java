package developer.app.obj;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cnergee.sbbroadband.utils.MyUtils;


/**
 * Created by ccadmin on 4/19/2016.
 */
public class InternalStorage {

    public static void writeObject(Context ctx,String key,Object obj)throws IOException {

        MyUtils.l("Internal Storage","Internal Storage Executed");
        FileOutputStream fos = ctx.openFileOutput(key, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(obj);
        oos.close();
        fos.close();
    }

    public static  Object readObject(Context ctx,String key)throws IOException, ClassNotFoundException, FileNotFoundException,Exception{
        FileInputStream fis = ctx.openFileInput(key);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object s = ois.readObject();
        return s;
    }


    public static void deleteFile(Context context, String key)
            throws IOException, ClassNotFoundException, FileNotFoundException,Exception {
        File path=context.getFileStreamPath(key);
        if(path.delete())
        {
            MyUtils.l("File", "Deleted");
        }
        else{
            MyUtils.l("File", "Not exist");
        }
    }


}
