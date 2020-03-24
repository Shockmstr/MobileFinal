package hieubd.mobilefinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ManageGroupActivity extends AppCompatActivity {
    private ListView groupListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_group);
        createListView();
    }

    private void createListView(){
        List<String> groupList = new ArrayList<>();
        groupList.add("Momentum Group");
        groupListView = findViewById(R.id.listGroup);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, groupList);
        groupListView.setAdapter(adapter);
        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ManageGroupActivity.this, GroupDetailActivity.class);
                String name = groupListView.getItemAtPosition(i).toString();
                intent.putExtra("NAME", name);
                startActivity(intent);
            }
        });
    }
}
