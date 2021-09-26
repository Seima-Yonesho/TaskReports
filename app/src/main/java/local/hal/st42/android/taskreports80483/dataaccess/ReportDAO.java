package local.hal.st42.android.taskreports80483.dataaccess;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;


/**
 * ST42 Android 課題6 作業報告管理アプリ
 *
 * レポート情報を操作するDAOインターフェース。
 *
 * @author Seima Yonesho
 */
@Dao
public interface ReportDAO {
    /**
     * 全データ検索メソッド。
     *
     * @return 検索結果のListオブジェクトに関連するLiveDataオブジェクト。
     */
    @Query("SELECT * FROM report ORDER BY workdate DESC")
    LiveData<List<Report>> findAll();

    /**
     * 主キーによる検索メソッド。
     *
     * @param id 主キー値。
     * @return 主キーに対応するデータを格納したTaskオブジェクトに関連するListenableFutureオブジェクト。
     */
    @Query("SELECT * FROM report WHERE id = :id")
    ListenableFuture<Report> findByPK(int id);

    /**
     * メニューカテゴリによる検索メソッド。
     *
     * @param workkind カテゴリ値。
     * @return 検索結果のListオブジェクトに関連するLiveDataオブジェクト。
     */
    @Query("SELECT * FROM report WHERE workkind = :workkind")
    LiveData<List<Report>> findByWorkCategory(int workkind);
}
