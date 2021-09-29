package local.hal.st42.android.taskreports80483.dataaccess;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;

/**
 *
 * ST42 Android　課題6 作業報告管理アプリ
 *
 * タスク情報を格納するエンティティクラス。
 *
 * @author Seima Yonesho
 */
@Entity
public class Report {
    /**
     * 主キーのid
     */
    @PrimaryKey(autoGenerate = true)
    public int id;
    /**
     * 作業年月日
     */
    @NonNull
    public String workdate;
    /**
     * 作業開始時刻(時分)。
     */
    @NonNull
    public String starttime;
    /**
     * 作業終了時刻(時分)
     */
    @NonNull
    public String endtime;
    /**
     * 作業内容
     */
    public String workin;
    /**
     * 作業種類
     */
    public int workkind;
    /**
     * 登録日時。
     */
    @NonNull
    public Timestamp insertAt;
    /**
     * 更新日時。
     */
    @NonNull
    public Timestamp updatedAt;

}