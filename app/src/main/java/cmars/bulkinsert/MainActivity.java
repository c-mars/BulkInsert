package cmars.bulkinsert;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String SAMPLE_DB_NAME = "MathNerdDB";
    private static final String SAMPLE_TABLE_NAME = "MulitplicationTable";
    private SQLiteDatabase sampleDB;

    @Bind(R.id.t)
    TextView t;

    @OnClick(R.id.std) void std(){
        sampleDB.delete(SAMPLE_TABLE_NAME, null, null);
        long startTime = System.currentTimeMillis();

        insertOneHundredRecords();

        long diff = System.currentTimeMillis() - startTime;
        t.setText("Exec Time: "+Long.toString(diff)+"ms");
    }

    @OnClick(R.id.bulk) void bulk(){
        sampleDB.delete(SAMPLE_TABLE_NAME, null, null);
        long startTime = System.currentTimeMillis();

        bulkInsertOneHundredRecords();

        long diff = System.currentTimeMillis() - startTime;
        t.setText("Exec Time: "+Long.toString(diff)+"ms");
    }

    private void insertOneHundredRecords() {
        for (int i = 0; i<100; i++) {
            ContentValues values = new ContentValues();
            values.put("FirstNumber", i);
            values.put("SecondNumber", i);
            values.put("Result", i*i);
            sampleDB.insert(SAMPLE_TABLE_NAME,null,values);
        }
    }

    void bulkInsertOneHundredRecords(){
        String sql = "INSERT INTO "+ SAMPLE_TABLE_NAME +" VALUES (?,?,?);";
        SQLiteStatement statement = sampleDB.compileStatement(sql);
        sampleDB.beginTransaction();
        for (int i = 0; i<100; i++) {
            statement.clearBindings();
            statement.bindLong(1, i);
            statement.bindLong(2, i);
            statement.bindLong(3, i*i);
            statement.execute();
        }
        sampleDB.setTransactionSuccessful();
        sampleDB.endTransaction();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDB();
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        sampleDB.close();
        super.onDestroy();
    }

    private void initDB() {
        sampleDB =  this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
        sampleDB.execSQL("CREATE TABLE IF NOT EXISTS " +
                SAMPLE_TABLE_NAME +
                " (FirstNumber INT, SecondNumber INT," +
                " Result INT);");
        sampleDB.delete(SAMPLE_TABLE_NAME, null, null);
    }
}
