package local.hal.st42.android.taskreports80483.dataaccess;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * ST42 Android
 * データベースオブジェクトクラス
 *
 * @author Seima Yonesho
 */
@Database(entities = {Report.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    /**
     * データベースインスタンス。
     */
    private static AppDatabase _instance;

    /**
     * データベースインスタンスを得るメソッド。
     * シングルトンパターンに従ってインスタンスを生成する。
     *
     * @param context コンテキスト。
     * @return データベースインスタンス。
     */
    public static AppDatabase getDatabase(Context context) {
        if (_instance == null) {
            _instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "report_db").build();
        }
        return _instance;
    }

    /**
     * TaskDAOオブジェクトを生成するメソッド。
     *
     * @return TaskDAOオブジェクト。
     */
    public abstract ReportDAO reportDAO();

}