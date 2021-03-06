import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.*;
import static org.junit.Assert.*;

public class CategoryTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
   assertEquals(Category.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfNamesAretheSame() {
    Category firstCategory = new Category("Household chores");
    Category secondCategory = new Category("Household chores");
    assertTrue(firstCategory.equals(secondCategory));
  }

  @Test
  public void save_savesIntoDatabase_true() {
    Category myCategory = new Category("Household chores");
    myCategory.save();
    assertTrue(Category.all().get(0).equals(myCategory));
  }

  @Test
  public void find_findCategoryInDatabase_true() {
    Category myCategory = new Category("Household chores");
    myCategory.save();
    Category savedCategory = Category.find(myCategory.getId());
    assertTrue(myCategory.equals(savedCategory));
  }

  @Test
  public void getName_returnsName_true() {
    Category testCategory = new Category("Home");
    assertEquals("Home", testCategory.getName());
  }

  @Test
  public void getId_returnsCategoryId() {
    Category testCategory = new Category("Home");
    assertTrue(Category.all().size() == testCategory.getId());
  }

  @Test
  public void all_returnsAllInstancesOfTask_true() {
    Category firstCategory = new Category("Home");
    Category secondCategory = new Category("Home");
    firstCategory.save();
    secondCategory.save();
    assertTrue(Category.all().contains(firstCategory));
    assertTrue(Category.all().contains(secondCategory));
  }

  @Test
  public void addTask_addsTaskToCategory() {
    Category myCategory = new Category("Household chores");
    myCategory.save();

    Task myTask = new Task("Mow the lawn");
    myTask.save();

    myCategory.addTask(myTask);
    Task savedTask = myCategory.getTasks().get(0);
    assertTrue(myTask.equals(savedTask));
  }

  @Test
  public void getTasks_returnsAllTasks_ArrayList() {
    Category myCategory = new Category("Household chores");
    myCategory.save();

    Task myTask = new Task("Mow the lawn");
    myTask.save();

    myCategory.addTask(myTask);
    List savedTasks = myCategory.getTasks();
    assertEquals(savedTasks.size(), 1);
  }

  @Test
  public void delete_deletesAllTasksAndListsAssociations() {
    Category myCategory = new Category("Household chores");
    myCategory.save();

    Task myTask = new Task("Mow the lawn");
    myTask.save();

    myCategory.addTask(myTask);
    myCategory.delete();
    assertEquals(myTask.getCategories().size(), 0);
  }
}
