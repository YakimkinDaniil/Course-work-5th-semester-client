package com.example.autoschool.view;

import com.example.autoschool.ApiClient;
import com.example.autoschool.model.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Панель администратора.
 */
public class AdminView extends VBox {
    private Stage stage;
    private String adminName;

    public AdminView(Stage stage, String adminName) {
        this.stage = stage;
        this.adminName = adminName;
        getStyleClass().add("admin-panel");

        Label adminLabel = new Label("Добро пожаловать, " + adminName + "!");
        adminLabel.getStyleClass().add("label");

        Button viewStudentsButton = new Button("Просмотр обучающихся");
        viewStudentsButton.getStyleClass().add("button");
        viewStudentsButton.setOnAction(event -> openViewStudentsWindow());

        Button addStudentButton = new Button("Добавить обучающегося");
        addStudentButton.getStyleClass().add("button");
        addStudentButton.setOnAction(event -> openAddStudentDialog());

        Button editStudentButton = new Button("Редактировать обучающегося");
        editStudentButton.getStyleClass().add("button");
        editStudentButton.setOnAction(event -> openEditStudentWindow());

        Button deleteStudentButton = new Button("Удалить обучающегося");
        deleteStudentButton.getStyleClass().add("button");
        deleteStudentButton.setOnAction(event -> openDeleteStudentWindow());

        Button viewInstructorsButton = new Button("Просмотр инструкторов");
        viewInstructorsButton.getStyleClass().add("button");
        viewInstructorsButton.setOnAction(event -> openViewInstructorsWindow());

        Button addInstructorButton = new Button("Добавить инструктора");
        addInstructorButton.getStyleClass().add("button");
        addInstructorButton.setOnAction(event -> openAddInstructorDialog());

        Button editInstructorButton = new Button("Редактировать инструктора");
        editInstructorButton.getStyleClass().add("button");
        editInstructorButton.setOnAction(event -> openEditInstructorWindow());

        Button deleteInstructorButton = new Button("Удалить инструктора");
        deleteInstructorButton.getStyleClass().add("button");
        deleteInstructorButton.setOnAction(event -> openDeleteInstructorWindow());

        Button viewGroupsButton = new Button("Просмотр групп");
        viewGroupsButton.getStyleClass().add("button");
        viewGroupsButton.setOnAction(event -> openViewGroupsWindow());

        Button addGroupButton = new Button("Добавить группу");
        addGroupButton.getStyleClass().add("button");
        addGroupButton.setOnAction(event -> openAddGroupDialog());

        Button editGroupButton = new Button("Редактировать группу");
        editGroupButton.getStyleClass().add("button");
        editGroupButton.setOnAction(event -> openEditGroupWindow());

        Button deleteGroupButton = new Button("Удалить группу");
        deleteGroupButton.getStyleClass().add("button");
        deleteGroupButton.setOnAction(event -> openDeleteGroupWindow());

        Button viewScheduleButton = new Button("Просмотр расписания");
        viewScheduleButton.getStyleClass().add("button");
        viewScheduleButton.setOnAction(event -> openViewScheduleWindow());

        Button addLessonButton = new Button("Добавить занятие");
        addLessonButton.getStyleClass().add("button");
        addLessonButton.setOnAction(event -> openAddLessonDialog());

        Button editScheduleButton = new Button("Редактировать расписание");
        editScheduleButton.getStyleClass().add("button");
        editScheduleButton.setOnAction(event -> openEditScheduleWindow());

        Button deleteScheduleButton = new Button("Удалить расписание");
        deleteScheduleButton.getStyleClass().add("button");
        deleteScheduleButton.setOnAction(event -> openDeleteScheduleWindow());

        Button viewApplicationsButton = new Button("Просмотр заявок");
        viewApplicationsButton.getStyleClass().add("button");
        viewApplicationsButton.setOnAction(event -> openViewApplicationsWindow());

        Button deleteApplicationsButton = new Button("Удаление заявок");
        deleteApplicationsButton.getStyleClass().add("button");
        deleteApplicationsButton.setOnAction(event -> openDeleteApplicationsWindow());

        Button backButton = new Button("Выход");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(event -> goBack());

        VBox studentsColumn = new VBox(10, viewStudentsButton, addStudentButton, editStudentButton, deleteStudentButton);
        VBox instructorsColumn = new VBox(10, viewInstructorsButton, addInstructorButton, editInstructorButton, deleteInstructorButton);
        VBox groupsColumn = new VBox(10, viewGroupsButton, addGroupButton, editGroupButton, deleteGroupButton);
        VBox scheduleColumn = new VBox(10, viewScheduleButton, addLessonButton, editScheduleButton, deleteScheduleButton);
        VBox applicationsColumn = new VBox(10, viewApplicationsButton, deleteApplicationsButton);

        HBox buttonBox = new HBox(20, studentsColumn, instructorsColumn, groupsColumn, scheduleColumn, applicationsColumn);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);

        Button aboutButton = new Button("Об авторе");
        aboutButton.getStyleClass().add("button");
        aboutButton.setOnAction(event -> AboutDialog.showAboutDialog());
        HBox aboutBox = new HBox(aboutButton);
        aboutBox.setAlignment(javafx.geometry.Pos.TOP_RIGHT);

        double buttonWidth = 250;
        for (Button button : new Button[]{viewStudentsButton, addStudentButton, editStudentButton, deleteStudentButton, viewInstructorsButton, addInstructorButton, editInstructorButton, deleteInstructorButton, viewGroupsButton, addGroupButton, editGroupButton, deleteGroupButton, viewScheduleButton, addLessonButton, editScheduleButton, deleteScheduleButton, viewApplicationsButton, deleteApplicationsButton, backButton}) {
            button.setPrefWidth(buttonWidth);
        }

        getChildren().addAll(aboutBox, adminLabel, buttonBox, backButton);
        setAlignment(javafx.geometry.Pos.CENTER);
        setSpacing(20);

        Scene scene = stage.getScene();
        if (scene != null) {
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        }
    }

    private void openViewStudentsWindow() {
        Stage viewStudentsStage = new Stage();
        viewStudentsStage.setTitle("Просмотр обучающихся");
        StudentsView studentsView = new StudentsView();
        Scene scene = new Scene(studentsView, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        viewStudentsStage.setScene(scene);
        viewStudentsStage.show();
    }

    private void openAddStudentDialog() {
        Optional<Student> result = StudentDialog.showStudentDialog();
        result.ifPresent(student -> {
            try {
                ApiClient.createStudent(student);
                showAlert("Успех", "Обучающийся успешно добавлен!");
            } catch (IOException e) {
                showAlert("Ошибка", "Ошибка при добавлении обучающегося: " + e.getMessage());
            }
        });
    }

    private void openEditStudentWindow() {
        Stage editStudentStage = new Stage();
        editStudentStage.setTitle("Редактирование обучающегося");
        EditStudentView editStudentView = new EditStudentView();
        Scene scene = new Scene(editStudentView, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        editStudentStage.setScene(scene);
        editStudentStage.show();
    }

    private void openDeleteStudentWindow() {
        Stage deleteStudentStage = new Stage();
        deleteStudentStage.setTitle("Удаление обучающегося");
        DeleteStudentView deleteStudentView = new DeleteStudentView();
        Scene scene = new Scene(deleteStudentView, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        deleteStudentStage.setScene(scene);
        deleteStudentStage.show();
    }

    private void openViewInstructorsWindow() {
        Stage viewInstructorsStage = new Stage();
        viewInstructorsStage.setTitle("Просмотр инструкторов");
        InstructorsView instructorsView = new InstructorsView();
        Scene scene = new Scene(instructorsView, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        viewInstructorsStage.setScene(scene);
        viewInstructorsStage.show();
    }

    private void openAddInstructorDialog() {
        Optional<Instructor> result = InstructorDialog.showInstructorDialog();
        result.ifPresent(instructor -> {
            try {
                ApiClient.createInstructor(instructor);
                showAlert("Успех", "Инструктор успешно добавлен!");
            } catch (IOException e) {
                showAlert("Ошибка", "Ошибка при добавлении инструктора: " + e.getMessage());
            }
        });
    }

    private void openEditInstructorWindow() {
        Stage editInstructorStage = new Stage();
        editInstructorStage.setTitle("Редактирование инструктора");
        EditInstructorsView editInstructorsView = new EditInstructorsView();
        Scene scene = new Scene(editInstructorsView, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        editInstructorStage.setScene(scene);
        editInstructorStage.show();
    }

    private void openDeleteInstructorWindow() {
        Stage deleteInstructorStage = new Stage();
        deleteInstructorStage.setTitle("Удаление инструктора");
        DeleteInstructorView deleteInstructorView = new DeleteInstructorView();
        Scene scene = new Scene(deleteInstructorView, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        deleteInstructorStage.setScene(scene);
        deleteInstructorStage.show();
    }

    private void openViewGroupsWindow() {
        Stage viewGroupsStage = new Stage();
        viewGroupsStage.setTitle("Просмотр групп");
        GroupsView groupsView = new GroupsView();
        Scene scene = new Scene(groupsView, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        viewGroupsStage.setScene(scene);
        viewGroupsStage.show();
    }

    private void openAddGroupDialog() {
        Optional<Group> result = GroupDialog.showGroupDialog(new Group());
        result.ifPresent(group -> {
            try {
                ApiClient.createGroup(group);
                showAlert("Успех", "Группа успешно добавлена!");
            } catch (IOException e) {
                showAlert("Ошибка", "Ошибка при добавлении группы: " + e.getMessage());
            }
        });
    }

    private void openEditGroupWindow() {
        Stage editGroupStage = new Stage();
        editGroupStage.setTitle("Редактирование группы");
        EditGroupsView editGroupsView = new EditGroupsView();
        Scene scene = new Scene(editGroupsView, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        editGroupStage.setScene(scene);
        editGroupStage.show();
    }

    private void openDeleteGroupWindow() {
        Stage deleteGroupStage = new Stage();
        deleteGroupStage.setTitle("Удаление группы");
        DeleteGroupView deleteGroupView = new DeleteGroupView();
        Scene scene = new Scene(deleteGroupView, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        deleteGroupStage.setScene(scene);
        deleteGroupStage.show();
    }

    private void openViewScheduleWindow() {
        Stage viewScheduleStage = new Stage();
        viewScheduleStage.setTitle("Просмотр расписания");
        try {
            List<Lesson> lessons = ApiClient.getAllLessons();
            ScheduleView scheduleView = new ScheduleView(lessons);
            Scene scene = new Scene(scheduleView, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            viewScheduleStage.setScene(scene);
            viewScheduleStage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Ошибка при получении расписания: " + e.getMessage());
        }
    }

    private void openAddLessonDialog() {
        Optional<Lesson> result = ScheduleDialog.showScheduleDialog();
        result.ifPresent(lesson -> {
            try {
                ApiClient.createLesson(lesson);
                showAlert("Успех", "Занятие успешно добавлено!");
            } catch (IOException e) {
                showAlert("Ошибка", "Ошибка при добавлении занятия: " + e.getMessage());
            }
        });
    }

    private void openEditScheduleWindow() {
        Stage editScheduleStage = new Stage();
        editScheduleStage.setTitle("Редактирование расписания");
        EditScheduleView editScheduleView = new EditScheduleView();
        Scene scene = new Scene(editScheduleView, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        editScheduleStage.setScene(scene);
        editScheduleStage.show();
    }

    private void openDeleteScheduleWindow() {
        Stage deleteScheduleStage = new Stage();
        deleteScheduleStage.setTitle("Удаление расписание");
        DeleteScheduleView deleteScheduleView = new DeleteScheduleView();
        Scene scene = new Scene(deleteScheduleView, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        deleteScheduleStage.setScene(scene);
        deleteScheduleStage.show();
    }

    private void openViewApplicationsWindow() {
        Stage viewApplicationsStage = new Stage();
        viewApplicationsStage.setTitle("Просмотр заявок");
        ApplicationsView applicationsView = new ApplicationsView();
        Scene scene = new Scene(applicationsView, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        viewApplicationsStage.setScene(scene);
        viewApplicationsStage.show();
    }

    private void openDeleteApplicationsWindow() {
        Stage deleteApplicationsStage = new Stage();
        deleteApplicationsStage.setTitle("Удаление заявок");
        DeleteApplicationsView deleteApplicationsView = new DeleteApplicationsView();
        Scene scene = new Scene(deleteApplicationsView, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        deleteApplicationsStage.setScene(scene);
        deleteApplicationsStage.show();
    }

    private void goBack() {
        stage.close();
    }

    private static void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
