package local.hal.st42.android.taskreports80483.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

import local.hal.st42.android.taskreports80483.dataaccess.AppDatabase;
import local.hal.st42.android.taskreports80483.dataaccess.Report;
import local.hal.st42.android.taskreports80483.dataaccess.ReportDAO;

public class ReportEditViewModel extends AndroidViewModel {
    /**
     * データベースオブジェクト。
     */
    private AppDatabase _db;

    /**
     * コンストラクタ。
     *
     * @param application アプリケーションオブジェクト。
     */
    public ReportEditViewModel(Application application) {
        super(application);
        _db = AppDatabase.getDatabase(application);
    }

    /**
     * 引数の主キーに該当するレポート情報を取得するメソッド。
     *
     * @param id 主キー値。
     * @return 引数に該当するレポートオブジェクト。該当データが存在しない場合は、空のレポートオブジェクト。
     */
    public Report getReport(int id) {
        ReportDAO reportDAO = _db.reportDAO();
        ListenableFuture<Report> future = reportDAO.findByPK(id);
        Report report = new Report();
        try {
            report = future.get();
        }
        catch(ExecutionException ex) {
            Log.e("ReportEditViewModel", "データ取得処理失敗", ex);
        }
        catch(InterruptedException ex) {
            Log.e("ReportEditViewModel", "データ削除処理失敗", ex);
        }
        return report;
    }

    /**
     * レポート情報登録メソッド。
     *
     * @param report 登録するレポート情報。
     * @return 登録されたレポート情報の主キー値。登録に失敗した場合は0。
     */
    public long insert(Report report) {
        ReportDAO reportDAO = _db.reportDAO();
        ListenableFuture<Long> future = reportDAO.insert(report);
        long result = 0;
        try {
            result = future.get();
        }
        catch(ExecutionException ex) {
            Log.e("ReportEditViewModel", "データ登録処理失敗", ex);
        }
        catch(InterruptedException ex) {
            Log.e("ReportEditViewModel", "データ登録処理失敗", ex);
        }
        return result;
    }

    /**
     * レポート情報更新メソッド。
     *
     * @param report 更新するレポート情報。
     * @return 更新件数。更新に失敗した場合は0。
     */
    public int update(Report report) {
        ReportDAO reportDAO = _db.reportDAO();
        ListenableFuture<Integer> future = reportDAO.update(report);
        int result = 0;
        try {
            result = future.get();
        }
        catch(ExecutionException ex) {
            Log.e("ReportEditViewModel", "データ更新処理失敗", ex);
        }
        catch(InterruptedException ex) {
            Log.e("ReportEditViewModel", "データ更新処理失敗", ex);
        }
        return result;
    }

    /**
     * レポート情報削除メソッド。
     *
     * @param id 削除対象レポートの主キー値。
     * @return 削除件数。削除に失敗した場合は0。
     */
    public int delete(int id) {
        Report report = new Report();
        report.id = id;
        ReportDAO reportDAO = _db.reportDAO();
        ListenableFuture<Integer> future = reportDAO.delete(report);
        int result = 0;
        try {
            result = future.get();
        }
        catch(ExecutionException ex) {
            Log.e("ReportEditViewModel", "データ削除処理失敗", ex);
        }
        catch(InterruptedException ex) {
            Log.e("ReportEditViewModel", "データ削除処理失敗", ex);
        }
        return result;
    }

}
