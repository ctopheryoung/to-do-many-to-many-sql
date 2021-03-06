import java.util.List;
import java.util.ArrayList;
import org.sql2o.*;

public class Category {
  private String name;
  private int id;

  @Override
  public boolean equals(Object otherCategory){
    if (!(otherCategory instanceof Category)) {
      return false;
    } else {
      Category newCategory = (Category) otherCategory;
      return this.getName().equals(newCategory.getName());
    }
  }

  public Category(String name) {
    this.name = name;
  }

  //GETTER METHODS//
  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

  //CREATE//

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO Categories(name) VALUES (:name);";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("name", this.name)
      .executeUpdate()
      .getKey();
    }
  }

//UPDATE//

  public void addTask(Task task) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO categories_tasks (category_id, task_id) VALUES (:category_id, :task_id);";
      con.createQuery(sql)
        .addParameter("category_id", this.getId())
        .addParameter("task_id", task.getId())
        .executeUpdate();
    }
  }

  public void update(String name) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE categories SET name = :name WHERE id = :id";
      con.createQuery(sql)
        .addParameter("name", name)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  //READ//

  public ArrayList<Task> getTasks() {
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT task_id FROM categories_tasks WHERE category_id = :category_id";
      List<Integer> taskIds = con.createQuery(sql)
        .addParameter("category_id", this.getId())
        .executeAndFetch(Integer.class);

      ArrayList<Task> tasks = new ArrayList<Task>();

      for (Integer taskId : taskIds) {
        String taskQuery = "SELECT * FROM tasks WHERE id = :taskId";
        Task task = con.createQuery(taskQuery)
          .addParameter("taskId", taskId)
          .executeAndFetchFirst(Task.class);
          tasks.add(task);
      }
      return tasks;
    }
  }

  public static Category find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM Categories where id=:id;";
      Category category = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Category.class);
      return category;
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
    String sql = "DELETE FROM categories WHERE id=:id;";
      con.createQuery(sql)
        .addParameter("id", id)
        .executeUpdate();

    String joinDeleteQuery = "DELETE FROM categories_tasks WHERE category_Id = :categoryId";
      con.createQuery(joinDeleteQuery)
        .addParameter("categoryId", this.getId())
        .executeUpdate();
    }
  }

  public static List<Category> all(){
    String sql = "SELECT id, name FROM Categories";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Category.class);
    }
  }
}
