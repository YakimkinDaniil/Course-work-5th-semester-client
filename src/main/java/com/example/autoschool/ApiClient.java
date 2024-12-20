package com.example.autoschool;

import com.example.autoschool.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Клиент для взаимодействия с API автошколы.
 */
public class ApiClient {
    private static final String BASE_URL = "http://localhost:8080";
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .build();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * Получает список всех обучающихся.
     *
     * @return Список обучающего.
     * @throws IOException если произошла ошибка при выполнении запроса.
     */
    public static List<Student> getAllStudents() throws IOException {
        String token = MainApp.getToken();
        Request request = new Request.Builder()
                .url(BASE_URL + "/students")
                .addHeader("Authorization", "Bearer " + token)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Неожиданный код ответа: " + response);
            }
            String responseBody = response.body().string();
            if (responseBody.contains("<")) {
                throw new IOException("Неожиданный HTML ответ");
            }
            return objectMapper.readValue(responseBody, new TypeReference<List<Student>>() {});
        }
    }

    /**
     * Создает нового обучающегося.
     *
     * @param student Обучающиеся для создания.
     * @return Созданный обучающийся.
     * @throws IOException если произошла ошибка при выполнении запроса.
     */
    public static Student createStudent(Student student) throws IOException {
        String token = MainApp.getToken();
        String json = objectMapper.writeValueAsString(student);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + "/students")
                .addHeader("Authorization", "Bearer " + token)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Неожиданный код ответа: " + response);
            }
            String responseBody = response.body().string();
            if (responseBody.contains("<")) {
                throw new IOException("Неожиданный HTML ответ");
            }
            return objectMapper.readValue(responseBody, Student.class);
        }
    }

    /**
     * Обновляет информацию об обучающемся.
     *
     * @param id Идентификатор обучающегося.
     * @param studentDetails Новые данные обучающегося.
     * @return Обновленный обучающийся.
     * @throws IOException если произошла ошибка при выполнении запроса.
     */
    public static Student updateStudent(Long id, Student studentDetails) throws IOException {
        String token = MainApp.getToken();
        String json = objectMapper.writeValueAsString(studentDetails);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + "/students/" + id)
                .addHeader("Authorization", "Bearer " + token)
                .put(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Неожиданный код ответа: " + response);
            }
            String responseBody = response.body().string();
            if (responseBody.contains("<")) {
                throw new IOException("Неожиданный HTML ответ");
            }
            return objectMapper.readValue(responseBody, Student.class);
        }
    }

    /**
     * Удаляет обучающегося по идентификатору.
     *
     * @param id Идентификатор обучающегося.
     * @throws IOException если произошла ошибка при выполнении запроса.
     */
    public static void deleteStudent(Long id) throws IOException {
        String token = MainApp.getToken();
        Request request = new Request.Builder()
                .url(BASE_URL + "/students/" + id)
                .addHeader("Authorization", "Bearer " + token)
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Неожиданный код ответа: " + response);
            }
        }
    }

    /**
     * Получает список всех групп.
     *
     * @return Список групп.
     * @throws IOException если произошла ошибка при выполнении запроса.
     */
    public static List<Group> getAllGroups() throws IOException {
        String token = MainApp.getToken();
        Request request = new Request.Builder()
                .url(BASE_URL + "/groups")
                .addHeader("Authorization", "Bearer " + token)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Неожиданный код ответа: " + response);
            }
            String responseBody = response.body().string();
            if (responseBody.contains("<")) {
                throw new IOException("Неожиданный HTML ответ");
            }
            return objectMapper.readValue(responseBody, new TypeReference<List<Group>>() {});
        }
    }

    /**
     * Создает новую группу.
     *
     * @param group Группа для создания.
     * @return Созданная группа.
     * @throws IOException если произошла ошибка при выполнении запроса.
     */
    public static Group createGroup(Group group) throws IOException {
        String token = MainApp.getToken();
        String json = objectMapper.writeValueAsString(group);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + "/groups")
                .addHeader("Authorization", "Bearer " + token)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Неожиданный код ответа: " + response);
            }
            String responseBody = response.body().string();
            if (responseBody.contains("<")) {
                throw new IOException("Неожиданный HTML ответ");
            }
            return objectMapper.readValue(responseBody, Group.class);
        }
    }

    /**
     * Обновляет информацию о группе.
     *
     * @param id Идентификатор группы.
     * @param group Новые данные группы.
     * @throws IOException если произошла ошибка при выполнении запроса.
     */
    public static void updateGroup(Long id, Group group) throws IOException {
        String url = BASE_URL + "/groups/" + id;
        String json = objectMapper.writeValueAsString(group);
        RequestBody requestBody = RequestBody.create(MediaType.get("application/json; charset=utf-8"), json);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + MainApp.getToken())
                .put(requestBody)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                if (response.code() == 401) {
                    String refreshedToken = refreshToken("admin", "password");
                    request = new Request.Builder()
                            .url(url)
                            .addHeader("Authorization", "Bearer " + refreshedToken)
                            .put(requestBody)
                            .build();
                    response = client.newCall(request).execute();
                }
                if (!response.isSuccessful()) {
                    throw new IOException("Неожиданный код ответа: " + response);
                }
            }
            String responseBody = response.body().string();
            if (responseBody.contains("<")) {
                throw new IOException("Неожиданный HTML ответ");
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * Удаляет группу по идентификатору.
     *
     * @param id Идентификатор группы.
     * @throws IOException если произошла ошибка при выполнении запроса.
     */
    public static void deleteGroup(Long id) throws IOException {
        String token = MainApp.getToken();
        Request request = new Request.Builder()
                .url(BASE_URL + "/groups/" + id)
                .addHeader("Authorization", "Bearer " + token)
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Неожиданный код ответа: " + response);
            }
        }
    }

    /**
     * Получает список всех результатов.
     *
     * @return Список результатов.
     * @throws IOException если произошла ошибка при выполнении запроса.
     */
    public static List<ResultDTO> getAllResults() throws IOException {
        String token = MainApp.getToken();
        Request request = new Request.Builder()
                .url(BASE_URL + "/results")
                .addHeader("Authorization", "Bearer " + token)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Неожиданный код ответа: " + response);
            }
            String responseBody = response.body().string();
            if (responseBody.contains("<")) {
                throw new IOException("Неожиданный HTML ответ");
            }
            List<ResultDTO> results = objectMapper.readValue(responseBody, new TypeReference<List<ResultDTO>>() {});
            for (ResultDTO result : results) {
            }
            return results;
        }
    }

    /**
     * Создает новый результат.
     *
     * @param result Результат для создания.
     * @return Созданный результат.
     * @throws IOException если произошла ошибка при выполнении запроса.
     */
    public static Result createResult(Result result) throws IOException {
        String token = MainApp.getToken();
        String json = objectMapper.writeValueAsString(result);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + "/results")
                .addHeader("Authorization", "Bearer " + token)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Неожиданный код ответа: " + response);
            }
            String responseBody = response.body().string();
            if (responseBody.contains("<")) {
                throw new IOException("Неожиданный HTML ответ");
            }
            return objectMapper.readValue(responseBody, Result.class);
        }
    }

    /**
     * Обновляет информацию о результате.
     *
     * @param id Идентификатор результата.
     * @param resultDetails Новые данные результата.
     * @return Обновленный результат.
     * @throws IOException если произошла ошибка при выполнении запроса.
     */
    public static Result updateResult(Long id, Result resultDetails) throws IOException {
        String token = MainApp.getToken();
        String json = objectMapper.writeValueAsString(resultDetails);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + "/results/" + id)
                .addHeader("Authorization", "Bearer " + token)
                .put(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Неожиданный код ответа: " + response);
            }
            String responseBody = response.body().string();
            if (responseBody.contains("<")) {
                throw new IOException("Неожиданный HTML ответ");
            }
            return objectMapper.readValue(responseBody, Result.class);
        }
    }

    /**
     * Удаляет результат по идентификатору.
     *
     * @param id Идентификатор результата.
     * @throws IOException если произошла ошибка при выполнении запроса.
     */
    public static void deleteResult(Long id) throws IOException {
        String token = MainApp.getToken();
        Request request = new Request.Builder()
                .url(BASE_URL + "/results/" + id)
                .addHeader("Authorization", "Bearer " + token)
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Неожиданный код ответа: " + response);
            }
        }
    }

    /**
     * Получает список всех инструкторов.
     *
     * @return Список инструкторов.
     * @throws IOException если произошла ошибка при выполнении запроса.
     */
    public static List<Instructor> getAllInstructors() throws IOException {
        String token = MainApp.getToken();
        Request request = new Request.Builder()
                .url(BASE_URL + "/instructors")
                .addHeader("Authorization", "Bearer " + token)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Неожиданный код ответа: " + response);
            }
            String responseBody = response.body().string();
            if (responseBody.contains("<")) {
                throw new IOException("Неожиданный HTML ответ");
            }
            return objectMapper.readValue(responseBody, new TypeReference<List<Instructor>>() {});
        }
    }

    /**
     * Создает нового инструктора.
     *
     * @param instructor Инструктор для создания.
     * @return Созданный инструктор.
     * @throws IOException если произошла ошибка при выполнении запроса.
     */
    public static Instructor createInstructor(Instructor instructor) throws IOException {
        String token = MainApp.getToken();
        String json = objectMapper.writeValueAsString(instructor);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + "/instructors")
                .addHeader("Authorization", "Bearer " + token)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Неожиданный код ответа: " + response);
            }
            String responseBody = response.body().string();
            if (responseBody.contains("<")) {
                throw new IOException("Неожиданный HTML ответ");
            }
            return objectMapper.readValue(responseBody, Instructor.class);
        }
    }

    /**
     * Обновляет информацию об инструкторе.
     *
     * @param instructor Инструктор с обновленными данными.
     * @throws IOException если произошла ошибка при выполнении запроса.
     */
    public static void updateInstructor(Instructor instructor) throws IOException {
        String url = BASE_URL + "/instructors/" + instructor.getId();
        String json = objectMapper.writeValueAsString(instructor);
        RequestBody requestBody = RequestBody.create(MediaType.get("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + MainApp.getToken())
                .put(requestBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                if (response.code() == 401) {
                    String refreshedToken = refreshToken("admin", "password");
                    request = new Request.Builder()
                            .url(url)
                            .addHeader("Authorization", "Bearer " + refreshedToken)
                            .put(requestBody)
                            .build();

                    try (Response newResponse = client.newCall(request).execute()) {
                        if (!newResponse.isSuccessful()) {
                            throw new IOException("Неожиданный код ответа: " + newResponse);
                        }
                        String responseBody = newResponse.body().string();
                        if (responseBody.contains("<")) {
                            throw new IOException("Unexpected HTML response");
                        }
                    }
                } else {
                    throw new IOException("Неожиданный код ответа: " + response);
                }
            } else {
                String responseBody = response.body().string();
                if (responseBody.contains("<")) {
                    throw new IOException("Неожиданный HTML ответ");
                }
            }
        }
    }

    /**
     * Удаляет инструктора по идентификатору.
     *
     * @param id Идентификатор инструктора.
     * @throws IOException если произошла ошибка при выполнении запроса.
     */
    public static void deleteInstructor(Long id) throws IOException {
        String token = MainApp.getToken();
        Request request = new Request.Builder()
                .url(BASE_URL + "/instructors/" + id)
                .addHeader("Authorization", "Bearer " + token)
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Неожиданный код ответа: " + response);
            }
        }
    }

    /**
     * Получает список всех занятий.
     *
     * @return Список занятий.
     * @throws IOException если произошла ошибка при выполнении запроса.
     */
    public static List<Lesson> getAllLessons() throws IOException {
        String token = MainApp.getToken();
        Request request = new Request.Builder()
                .url(BASE_URL + "/lessons")
                .addHeader("Authorization", "Bearer " + token)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Неожиданный код ответа: " + response);
            }
            String responseBody = response.body().string();
            if (responseBody.contains("<")) {
                throw new IOException("Неожиданный HTML ответ");
            }
            return objectMapper.readValue(responseBody, new TypeReference<List<Lesson>>() {});
        }
    }

    /**
     * Создает новое занятие.
     *
     * @param lesson Занятие для создания.
     * @return Созданное занятие.
     * @throws IOException если произошла ошибка при выполнении запроса.
     */
    public static Lesson createLesson(Lesson lesson) throws IOException {
        String token = MainApp.getToken();
        String json = objectMapper.writeValueAsString(lesson);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + "/lessons")
                .addHeader("Authorization", "Bearer " + token)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Неожиданный код ответа: " + response);
            }
            String responseBody = response.body().string();
            if (responseBody.contains("<")) {
                throw new IOException("Неожиданный HTML ответ");
            }
            return objectMapper.readValue(responseBody, Lesson.class);
        }
    }

    /**
     * Обновляет информацию о занятии.
     *
     * @param id Идентификатор занятия.
     * @param lesson Новые данные занятия.
     * @throws IOException если произошла ошибка при выполнении запроса.
     */
    public static void updateLesson(Long id, Lesson lesson) throws IOException {
        String url = BASE_URL + "/lessons/" + id;
        String json = objectMapper.writeValueAsString(lesson);
        RequestBody requestBody = RequestBody.create(MediaType.get("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + MainApp.getToken())
                .put(requestBody)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                if (response.code() == 401) {
                    String refreshedToken = refreshToken("admin", "password");
                    request = new Request.Builder()
                            .url(url)
                            .addHeader("Authorization", "Bearer " + refreshedToken)
                            .put(requestBody)
                            .build();
                    response = client.newCall(request).execute();
                }
                if (!response.isSuccessful()) {
                    throw new IOException("Неожиданный код ответа: " + response);
                }
            }
            String responseBody = response.body().string();
            if (responseBody.contains("<")) {
                throw new IOException("Неожиданный HTML ответ");
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * Удаляет занятие по идентификатору.
     *
     * @param id Идентификатор занятия.
     * @throws IOException если произошла ошибка при выполнении запроса.
     */
    public static void deleteLesson(Long id) throws IOException {
        String token = MainApp.getToken();
        Request request = new Request.Builder()
                .url(BASE_URL + "/lessons/" + id)
                .addHeader("Authorization", "Bearer " + token)
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Неожиданный код ответа: " + response);
            }
        }
    }

    /**
     * Получает список всех заявок.
     *
     * @return Список заявок.
     * @throws IOException если произошла ошибка при выполнении запроса.
     */
    public static List<App> getAllApplications() throws IOException {
        String token = MainApp.getToken();
        Request request = new Request.Builder()
                .url(BASE_URL + "/applications")
                .addHeader("Authorization", "Bearer " + token)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Неожиданный код ответа: " + response);
            }
            String responseBody = response.body().string();
            if (responseBody.contains("<")) {
                throw new IOException("Неожиданный HTML ответ");
            }
            return objectMapper.readValue(responseBody, new TypeReference<List<App>>() {});
        }
    }

    /**
     * Создает новую заявку.
     *
     * @param appDto Заявка для создания.
     * @return Созданная заявка.
     * @throws IOException если произошла ошибка при выполнении запроса.
     */
    public static ApplicationDTO createApp(ApplicationDTO appDto) throws IOException {
        String token = MainApp.getToken();
        String json = objectMapper.writeValueAsString(appDto);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + "/applications")
                .addHeader("Authorization", "Bearer " + token)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Неожиданный код ответа: " + response);
            }
            String responseBody = response.body().string();
            if (responseBody.contains("<")) {
                throw new IOException("Неожиданный HTML ответ");
            }
            return objectMapper.readValue(responseBody, ApplicationDTO.class);
        }
    }

    /**
     * Удаляет заявку по идентификатору.
     *
     * @param id Идентификатор заявки.
     * @throws IOException если произошла ошибка при выполнении запроса.
     */
    public static void deleteApp(Long id) throws IOException {
        String token = MainApp.getToken();
        Request request = new Request.Builder()
                .url(BASE_URL + "/applications/" + id)
                .addHeader("Authorization", "Bearer " + token)
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Неожиданный код ответа: " + response);
            }
        }
    }

    /**
     * Авторизует пользователя.
     *
     * @param username Имя пользователя.
     * @param password Пароль.
     * @return Токен авторизации.
     * @throws IOException если произошла ошибка при выполнении запроса.
     */
    public static String loginUser(String username, String password) throws IOException {
        UserDto userDto = new UserDto(username, password, null);
        String json = objectMapper.writeValueAsString(userDto);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + "/auth/login")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Неожиданный код ответа: " + response);
            }
            String token = response.body().string();
            MainApp.setToken(token);
            return token;
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param username Имя пользователя.
     * @param password Пароль.
     * @param role Роль пользователя.
     * @return Сообщение о результате регистрации.
     * @throws IOException если произошла ошибка при выполнении запроса.
     */
    public static String registerUser(String username, String password, String role) throws IOException {
        UserDto userDto = new UserDto(username, password, role);
        String json = objectMapper.writeValueAsString(userDto);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + "/auth/register")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Неожиданный код ответа: " + response);
            return response.body().string();
        }
    }

    /**
     * Обновляет токен авторизации.
     *
     * @param username Имя пользователя.
     * @param password Пароль.
     * @return Обновленный токен авторизации.
     * @throws IOException если произошла ошибка при выполнении запроса.
     */
    public static String refreshToken(String username, String password) throws IOException {
        UserDto userDto = new UserDto(username, password, null);
        String json = objectMapper.writeValueAsString(userDto);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + "/auth/login")
                .post(body)
                .build();
        Response response = null;
        String token = null;
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Неожиданный код ответа: " + response);
            }
            token = response.body().string();
            MainApp.setToken(token);
        } catch (IOException e) {
            throw e;
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return token;
    }
}
