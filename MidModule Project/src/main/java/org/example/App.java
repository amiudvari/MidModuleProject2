package org.example;

import org.apache.commons.dbcp2.BasicDataSource;
import org.example.daos.TaskDao;
import org.example.models.Task;
import org.springframework.cglib.core.Local;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.w3c.dom.ls.LSOutput;

import java.time.LocalDate;
import java.time.Year;
import java.util.*;

import java.util.List;


public class App {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\033[1;31m";
    public static final String BLUE = "\033[1;34m";
    public static final String CYAN = "\033[1;36m";
    public static final String PURPLE = "\033[1;35m";
    public static final String YELLOW = "\033[1;33m";
    public BasicDataSource basicDataSource;
    public TaskDao taskDao;
    public Scanner scanner;
    public String space = " ";

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    public void run() {
        scanner = new Scanner(System.in);
        setupDataSource();
        updatePriorityForTasks();
        mainMenu();
    }

    public void setupDataSource() {
        basicDataSource = new BasicDataSource();
        basicDataSource.setUrl("jdbc:postgresql://localhost:5432/TaskManager");
        basicDataSource.setUsername("postgres");
        basicDataSource.setPassword("postgres1");
        taskDao = new TaskDao(basicDataSource);
    }

    public void updatePriorityForTasks() {
        LocalDate todaysDate = LocalDate.now();
        LocalDate nextWeek = todaysDate.plusDays(7);
        taskDao.updatePriorityForTasks();
    }

    public void mainMenu() {

        while (true) {
            System.out.println(space);
            System.out.println("Please select from the menu below:");
            System.out.println("1. View All Current Tasks");
            System.out.println("2. Add a Task");
            System.out.println("3. Make a Change to an Existing Task (allows you to change the due date, name, description, etc.)");
            System.out.println("4. Mark Task Complete");
            System.out.println("5. View Completed Tasks (provides a list of tasks that have been marked as completed)");
            System.out.println("6. View Past-Due Tasks");
            System.out.println("7. View High Priority Tasks");
            System.out.println("8. Search Tasks");
            System.out.println("9. Delete a Task");
            System.out.println("10. Exit");
            System.out.println(space);
            System.out.println(YELLOW + "Enter the number of your selection: " + RESET);

            String userInput = scanner.nextLine();

            if (userInput.equals("1")) {
                viewAllTasks();
            } else if (userInput.equals("2")) {
                addTask();
            } else if (userInput.equals("3")) {
                updateTask();
            } else if (userInput.equals("4")) {
                markAsComplete();
            } else if (userInput.equals("5")) {
                viewCompletedTasks();
            } else if (userInput.equals("6")) {
                viewPastDueTasks();
            } else if (userInput.equals("7")) {
                viewHighPriorityTasks();
            } else if (userInput.equals("8")) {
                searchSubMenu();
            } else if (userInput.equals("9")) {
                deleteTask();
            } else if (userInput.equals("10")) {
                break;
            } else {
                System.out.println("Invalid choice");
            }
        }
    }

            public void searchSubMenu () {
                while (true) {
                    System.out.println("How Would You Like to Search?");
                    System.out.println("1. Search by Task ID");
                    System.out.println("2. Search by Task Name");
                    System.out.println("3. Search by Priority");
                    System.out.println("5. Back to Main Menu");
                    System.out.println(space);
                    System.out.println(YELLOW + "Enter the number of your selection: " + RESET);
                    System.out.println(space);

                    String userInput = scanner.nextLine();


                    if (userInput.equals("1")) {
                        searchByTaskId();
                    } else if (userInput.equals("2")) {
                        searchByTaskName();
                    } else if (userInput.equals("3")) {
                        searchByPriority();
                    } else if (userInput.equals("4")) {
                        break;
                    } else {
                        System.out.println("That is an invalid choice. Please try again.");
                    }
                }
            }

            public void searchByTaskId () {
                System.out.println(space);
                System.out.println(YELLOW + "Enter the task ID:" + RESET);
                int taskIdSearched = Integer.parseInt(scanner.nextLine());

                Task taskToSearch = taskDao.getTaskById(taskIdSearched);
                if (taskToSearch != null) {
                    System.out.println(space);
                    System.out.println(BLUE + "Task found:" + RESET);
                    System.out.println(space);
                    System.out.println(BLUE + "Task ID: " + taskToSearch.getTaskId() + RESET);
                    System.out.println(BLUE + "Task Name: " + taskToSearch.getTaskName() + RESET);
                    System.out.println(BLUE + "Priority: " + taskToSearch.getPriority() + RESET);
                    System.out.println(BLUE + "Due Date: " + taskToSearch.getDueDate() + RESET);
                } else {
                    System.out.println(RED + "No tasks found." + RESET);
                }

            }


            public void searchByTaskName () {
                System.out.println(space);
                System.out.println(YELLOW + "Enter task name: " + RESET);
                String nameToSearch = scanner.nextLine();
                List<Task> tasks = taskDao.getTasksByName(nameToSearch);

                if (tasks.size() == 0) {
                    System.out.println(RED + "No tasks found with that task name." + RESET);
                } else {
                    System.out.println(space);
                    System.out.println(BLUE + "Tasks with the given name or a name that contains the searched phrase:" + RESET);
                    for (Task task : tasks) {
                        System.out.println(BLUE + "Task ID: " + task.getTaskId() + RESET);
                        System.out.println(BLUE + "Task Name: " + task.getTaskName() + RESET);
                        System.out.println(BLUE + "Description: " + task.getDescription() + RESET);
                        System.out.println(space);
                    }
                }
            }

            public void searchByPriority () {
                System.out.println(space);
                System.out.println(YELLOW + "Enter priority: " + RESET);
                String priorityToSearch = scanner.nextLine();
                List<Task> tasks = taskDao.getTasksByPriority(priorityToSearch);

                if (tasks.size() == 0) {
                    System.out.println(RED + "No tasks found with that priority." + RESET);

                } else {
                    System.out.println(space);
                    System.out.println(BLUE + "Tasks with your searched priority: " + RESET);

                    for (Task task : tasks) {
                        System.out.println(BLUE + "Task ID: " + task.getTaskId() + RESET);
                        System.out.println(BLUE + "Task Name: " + task.getTaskName() + RESET);
                        System.out.println(BLUE + "Description: " + task.getDescription() + RESET);
                        System.out.println(space);
                    }
                }
            }

            public void viewAllTasks () {
                System.out.println(space);
                List<Task> tasks = taskDao.getAllTasks();
                System.out.println("All Tasks: ");
                System.out.println(space);
                for (Task task : tasks) {
                    System.out.println(BLUE + "Task Id: " + task.getTaskId() + RESET);
                    System.out.println(BLUE + "Task Name: " + task.getTaskName() + RESET);
                    System.out.println(BLUE + "Description: " + task.getDescription() + RESET);
                    System.out.println(BLUE + "Due Date: " + task.getDueDate() + RESET);
                    System.out.println(BLUE + "Priority: " + task.getPriority() + RESET);
                    System.out.println(space);
                }
            }

            public void addTask () {
                Task newTask = null;

                System.out.println(space);
                System.out.println("Enter new task information");
                System.out.println(space);

                System.out.println(YELLOW + "Task Name: " + RESET);
                String taskName = scanner.nextLine();

                System.out.println(YELLOW + "Please choose the priority of this task (high, medium, low): " + RESET);
                String priority = scanner.nextLine();


                System.out.println(YELLOW + "Please enter a description for this task: " + RESET);
                String description = scanner.nextLine();


                System.out.println(YELLOW + "Please enter the due date for this task in this format -> YYYY-MM-DD" + RESET);
                LocalDate dueDate = LocalDate.parse(scanner.nextLine());

                newTask = new Task();
                newTask.setTaskName(taskName);
                newTask.setPriority(priority);
                newTask.setDescription(description);
                newTask.setDueDate(dueDate);
                taskDao.addTask(newTask);
                System.out.println("Your task has been added!");
            }

            public void updateTask () {
                List<Task> tasks = taskDao.getAllTasks();
                for (Task task : tasks) {
                    System.out.println("Task ID: " + task.getTaskId());
                    System.out.println("Task Name: " + task.getTaskName());
                    System.out.println("Task Due: " + task.getDueDate());
                    System.out.println(space);

                }
                System.out.println(YELLOW + "Enter the task ID number of the task you wish to update: " + RESET);
                int taskId = Integer.parseInt(scanner.nextLine());

                Task taskToUpdate = taskDao.getTaskById(taskId);
                if (taskToUpdate == null) {
                    System.out.println(RED + "Task not found." + RESET);
                }

                System.out.println("Update the task information");
                System.out.println(space);

                System.out.println("Task Name: ");
                String taskName = scanner.nextLine();

                System.out.println("Priority (high, medium, low): ");
                String priority = scanner.nextLine();

                System.out.println("Due Date (YYYY-MM-DD): ");
                LocalDate dueDate = LocalDate.parse(scanner.nextLine());

                System.out.println("Description: ");
                String description = scanner.nextLine();

                Task updatedTask = new Task();
                updatedTask.setTaskId(taskId);
                updatedTask.setTaskName(taskName);
                updatedTask.setPriority(priority);
                updatedTask.setDueDate(dueDate);
                updatedTask.setDescription(description);

                taskDao.updateTask(updatedTask);

                System.out.println("Task has been updated!");
            }

            public void markAsComplete () {
                List<Task> tasks = taskDao.getAllTasks();
                for (Task task : tasks) {
                    System.out.println("Task ID: " + task.getTaskId());
                    System.out.println("Task Name: " + task.getTaskName());
                    System.out.println("Task Due: " + task.getDueDate());
                    System.out.println(space);

                }
                System.out.println("Enter the task ID number of the task you wish to mark complete: ");
                int taskId = Integer.parseInt(scanner.nextLine());

                Task taskToMarkComplete = taskDao.getTaskById(taskId);
                if (taskToMarkComplete == null) {
                    System.out.println(RED + "Task not found." + RESET);
                }
                taskDao.markAsComplete(taskToMarkComplete);
                System.out.println("Task has been marked as completed!");

            }
            public void deleteTask () {
                System.out.println(YELLOW + "Enter the task ID number of the task you are deleting: " + RESET);
                int taskID = Integer.parseInt(scanner.nextLine());
                Task taskToDelete = taskDao.getTaskById(taskID);
                if (taskToDelete == null) {
                    System.out.println(RED + "Task with ID number: " + taskID + " was not found." + RESET);
                } else {
                    System.out.println(RED + "Are you sure you would like to delete the task? Enter 'Y' for yes and 'N' for no: " + RESET);
                    String response = scanner.nextLine();
                    if (response.equalsIgnoreCase("Y")) {
                        int rowsDeleted = taskDao.deleteTask(taskID);
                        if (rowsDeleted > 0) {
                            System.out.println(space);
                            System.out.println(YELLOW + "The task was deleted successfully!" + RESET);
                            System.out.println(space);
                        } else {
                            System.out.println(space);
                            System.out.println(RED + "The task could not be deleted" + RESET);
                            System.out.println(space);
                        }
                    } else {
                        System.out.println(space);
                        System.out.println("Cancelled.");
                        System.out.println(space);
                    }
                }
            }
            public void viewCompletedTasks () {
                System.out.println(space);
                List<Task> completedTasks = taskDao.getCompletedTasks();
                System.out.println("Completed Tasks:");
                if (completedTasks.size() == 0) {
                    System.out.println("No completed tasks found.");
                } else {
                    for (Task task : completedTasks) {
                        System.out.println(BLUE + "Task Name:" + task.getTaskName() + RESET);
                        System.out.println(space);
                    }
                }
            }

            public void viewPastDueTasks () {
                System.out.println(space);
                LocalDate todaysDate = LocalDate.now();
                List<Task> tasks = taskDao.getAllTasks();
                List<Task> pastDueTasks = new ArrayList<>();
                System.out.println("Past Due Tasks:");
                System.out.println(space);
                for (Task task : tasks) {
                    if (task.getDueDate().isBefore(todaysDate)) {
                        pastDueTasks.add(task);
                    }
                }
                if (pastDueTasks.size() > 0) {
                    for (Task pastDueTask : pastDueTasks) {
                        System.out.println(RED + "Task ID: " + pastDueTask.getTaskId() + RESET);
                        System.out.println(RED + "Task Name: " + pastDueTask.getTaskName() + RESET);
                        System.out.println(RED + "Task Due: " + pastDueTask.getDueDate() + RESET);
                        System.out.println(space);
                    }
                } else {
                    System.out.println("No past due tasks found.");
                }
            }

            public void viewHighPriorityTasks () {
                System.out.println(space);
                List<Task> highPriorityTasks = taskDao.getHighPriorityTasks();

                System.out.println("High Priority Tasks:");
                if (highPriorityTasks.size() == 0) {
                    System.out.println("No high priority tasks found.");
                } else {
                    for (Task task : highPriorityTasks) {
                        System.out.println(BLUE + "Task ID: " + task.getTaskId()+ RESET);
                        System.out.println(BLUE +"Task Name: :" + task.getTaskName()+ RESET);
                        System.out.println(BLUE +"Task Due: " + task.getDueDate()+ RESET);
                        System.out.println(BLUE +"Task Priority: " + task.getPriority()+ RESET);
                        System.out.println(space);
                    }

                }
            }
        }
