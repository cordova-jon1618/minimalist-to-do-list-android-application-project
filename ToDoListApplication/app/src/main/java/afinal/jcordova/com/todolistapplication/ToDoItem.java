package afinal.jcordova.com.todolistapplication;



public class ToDoItem {

    public final static String TITLE = "title";
    public final static String SHORTDESC = "short description";
    public final static String DUEDATE = "title";
    public final static String ADDTLINFO = "additional information";

    private String title ="";
    private String shortdesc = "";
    private String duedate ="";
    private String addtlinfo ="";
    protected long id = 0;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    public void setShortdesc(String shortdesc) {
        this.shortdesc = shortdesc;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public String getAddtlinfo() {
        return addtlinfo;
    }

    public void setAddtlinfo(String addtlinfo) {
        this.addtlinfo = addtlinfo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}//end of class
