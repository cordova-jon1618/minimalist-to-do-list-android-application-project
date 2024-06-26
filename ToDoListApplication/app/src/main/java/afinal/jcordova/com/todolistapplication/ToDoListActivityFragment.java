package afinal.jcordova.com.todolistapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class ToDoListActivityFragment extends Fragment {

    List<ToDoItem> todolist = new ArrayList<ToDoItem>();
    ArrayAdapter<ToDoItem> adapter = null;
    private DBHelper dbHelper = null;
    public final static String TAG = "ToDoList";

    public ToDoListActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_to_do_list, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        try{
            dbHelper = new DBHelper(getActivity());
            todolist = dbHelper.selectAll();
        } catch (Exception e){
            Log.d(TAG, "onCreate: DBHelper threw exception: " + e);
            e.printStackTrace();
        }

        ListView list = (ListView) getActivity().findViewById(R.id.todoItems);

        adapter = new ToDoItemAdapter(getActivity(), R.layout.row, todolist);
        // adapter=new ArrayAdapter<Animal>(getActivity(),android.R.layout.simple_list_item_1, animals);
        list.setAdapter(adapter);


        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                onDelete(view, position);
                return true;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                clickListItem(view, position);
            }
        });

        Button saveButton = (Button)getActivity().findViewById(R.id.postButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onSave();
                //onClearEditBox();
            }
        });
    }//end of onActivityCreated

    private void onSave(){
        ToDoItem todo = new ToDoItem();
        EditText title = (EditText)getActivity().findViewById(R.id.edit_tasktitle);
        EditText shortdesc = (EditText)getActivity().findViewById(R.id.edit_short_desc);
        EditText duedate = (EditText)getActivity().findViewById(R.id.edit_duedate);
        EditText addtlinfo = (EditText)getActivity().findViewById(R.id.edit_additionalinfo);


        String todoTitle = title.getText().toString();
        String todoDate = duedate.getText().toString();
        if(TextUtils.isEmpty(todoTitle) || TextUtils.isEmpty(todoDate)){
            showMissingInfoAlert();
        }else {

            todo.setTitle(todoTitle);
            todo.setDuedate(todoDate);
            todo.setShortdesc(shortdesc.getText().toString());
            todo.setAddtlinfo(addtlinfo.getText().toString());

            long todoID = 0;
            if (dbHelper != null) {
                todoID = dbHelper.insert(todo);
                todo.setId(todoID);
            }
            adapter.add(todo); //add todo to listView
            adapter.notifyDataSetChanged(); //listView refreshes

            onClearEditBox();
        }
        //Hides the keyboard after the user hits the save button
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }//end of onSave method

    private void onDelete(View view, int position){
        ToDoItem itemobj = adapter.getItem(position);

        if(itemobj != null){
            String item = "Deleting Task Title: " + itemobj.getTitle();
            Toast.makeText(getActivity(),item, Toast.LENGTH_SHORT).show();
            Log.d(TAG, " onItemClick: " + itemobj.getTitle());
            if(dbHelper != null)
            {
                dbHelper.deleteRecord(itemobj.getId());
            }
            adapter.remove(itemobj);
            adapter.notifyDataSetChanged();
        }
    }//end of onDelete method

    public void showMissingInfoAlert(){

        ContextThemeWrapper ctw =  new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
        alertDialogBuilder.setTitle(getResources().getString(R.string.alert_title));
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);

        alertDialogBuilder
                .setMessage(getResources().getString(R.string.alert_message))
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();  //when clicked close dialog box
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }//end of method

    private void clickListItem(View view, int position) {

        ToDoItem itemobj2 = adapter.getItem(position);

        if (itemobj2 != null) {
            String iteminformation = itemobj2.getAddtlinfo();

            if (iteminformation.isEmpty()){
                Toast.makeText(getActivity(), "No Additional Information Entered.",
                        Toast.LENGTH_SHORT).show();
                Log.d(TAG, " onItemClick: " + itemobj2.getAddtlinfo() +
                        "No Additional Information Entered.");
            } else{
                String consolidated = "Additional Information: " + iteminformation;
                Toast.makeText(getActivity(), consolidated, Toast.LENGTH_SHORT).show();
                Log.d(TAG, " onItemClick: " + itemobj2.getAddtlinfo());
            }
        }
    }//end of clickListItem

    //method to clear editbox after the save button has been pushed
    private void onClearEditBox() {

        EditText title1 = (EditText)getActivity().findViewById(R.id.edit_tasktitle);
        EditText shortdesc1 = (EditText)getActivity().findViewById(R.id.edit_short_desc);
        EditText duedate1 = (EditText)getActivity().findViewById(R.id.edit_duedate);
        EditText addtlinfo1 = (EditText)getActivity().findViewById(R.id.edit_additionalinfo);

        title1.setText("");
        shortdesc1.setText("");
        duedate1.setText("");
        addtlinfo1.setText("");

    }//end of onClearEditBox()


}//end of class
