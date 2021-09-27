package local.hal.st42.android.taskreports80483.dataaccess;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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

    /**
     * レポート情報を新規登録するメソッド。
     *
     * @param report 登録レポート情報が格納されたReportオブジェクト。
     * @return 新規登録された主キー値に関連するListenableFutureオブジェクト。
     */
    @Insert
    ListenableFuture<Long> insert(Report report);

    /**
     * レポート情報を更新するメソッド。
     *
     * @param report 更新レポート情報が格納されたReportオブジェクト。
     * @return 更新件数を表す値に関連するListenableFutureオブジェクト。
     */
    @Update
    ListenableFuture<Integer> update(Report report);

    /**
     * タスク情報を削除するメソッド。
     *
     * @param report 削除レポート情報が格納されたReportオブジェクト。
     * @return 削除件数を表す値に関連するListenableFutureオブジェクト。
     */
    @Delete
    ListenableFuture<Integer> delete(Report report);
}
