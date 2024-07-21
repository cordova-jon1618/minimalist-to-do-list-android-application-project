package afinal.jcordova.com.todolistapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ToDoItemAdapter extends ArrayAdapter<ToDoItem>{

    private int layoutResourceId;
    public final static String TAG = "ToDoAdapter";
    private LayoutInflater inflater;
    private List<ToDoItem> toDoItems;

    public ToDoItemAdapter(Context context, int layoutResourceId, List<ToDoItem> toDoItems) {
        super(context, layoutResourceId, toDoItems);
        this.layoutResourceId = layoutResourceId;
        this.toDoItems = toDoItems;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }//end of constructor


    static class TodoHolder {
        ImageView imgIcon;
        TextView txtTitle;
        TextView txtShortDesc;
        TextView txtDueDate;
        //TextView txtAddtlInfo;

    }//static class TodoHolder


    public View getView(int position, View convertView, ViewGroup parent){
        TodoHolder holder = null;

        if(null == convertView){
            Log.d(TAG, "getView: rowView null: position" + position);

            convertView = inflater.inflate(layoutResourceId, parent, false);

            holder = new TodoHolder();
            holder.imgIcon = (ImageView) convertView.findViewById(R.id.icon1);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.tasktitleLabel);
            holder.txtShortDesc = (TextView) convertView.findViewById(R.id.shortdesc_label);
            holder.txtDueDate= (TextView) convertView.findViewById(R.id.duedate_label);

            //We won't display Additional information unless the user clicks on the list item
            //holder.txtAddtlInfo = (TextView) convertView.findViewById(R.id.addtlinfo_label);


            //Using tags to store data
            convertView.setTag(holder);
        }
        else{
            Log.d(TAG, "getView: rowView !null - reuse holder: position" + position);
            holder = (TodoHolder)convertView.getTag(); //reuse holder position
        }

        Log.d(TAG, " getView toDoItems " + toDoItems.size());

        try {
            ToDoItem todo = toDoItems.get(position);
            holder.txtTitle.setText(todo.getTitle());

            //Updated to always display "Note" instead of the short description
            holder.txtShortDesc.setText("Note");
            //holder.txtShortDesc.setText(todo.getShortdesc());

            holder.txtDueDate.setText(todo.getDuedate());
            //holder.txtAddtlInfo.setText(todo.getAddtlinfo());
            holder.imgIcon.setImageResource(R.drawable.ic_clipboard0);

        } catch(Exception e) {
            Log.e(TAG, " getView toDoItems " + e + " position was : " + position +
                    " toDoItems.size: " + toDoItems.size());
        }
        return convertView;
    }//end of getView
}//end of class
