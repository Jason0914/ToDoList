/**
 * WEB API TodoList Rest CRUD
 * ------------------------------------------------------------
 * GET    "http://localhost:8080/todolist/"     獲取所有待辦事項
 * POST   "http://localhost:8080/todolist/"     新增待辦事項
 * PUT    "http://localhost:8080/todolist/{id}" 更新待辦事項
 * DELETE "http://localhost:8080/todolist/{id}" 刪除待辦事項
 * ------------------------------------------------------------
 * */
const BASE_URL = "http://localhost:8080/todolist";

// 獲取請求頭，包含授權信息
const getHeaders = () => {
  const headers = {
    "Content-Type": "application/json",
  };
  const user = localStorage.getItem("user");
  if (user) {
    // 如果後端需要 JWT token，可以在這裡加入
    // headers['Authorization'] = `Bearer ${JSON.parse(user).token}`;
  }
  return headers;
};

// 統一的錯誤處理
const handleResponse = async (response) => {
  const result = await response.json();
  if (result.status === 200) {
    return result.data;
  }
  // 如果是 401 未授權，清除本地存儲並重新導向到登入頁
  if (response.status === 401) {
    localStorage.removeItem("user");
    window.location.href = "/login";
  }
  throw new Error(result.message || "操作失敗");
};

// 獲取所有待辦事項
export const fetchTodos = async () => {
  try {
    const response = await fetch(BASE_URL, {
      headers: getHeaders(),
    });
    return handleResponse(response);
  } catch (error) {
    console.error("Fetch todos error:", error);
    throw error;
  }
};

// 新增待辦事項
export const addTodo = async (todo) => {
  try {
    const response = await fetch(BASE_URL, {
      method: "POST",
      headers: getHeaders(),
      body: JSON.stringify(todo),
    });
    return handleResponse(response);
  } catch (error) {
    console.error("Add todo error:", error);
    throw error;
  }
};

// 更新待辦事項
export const updateTodo = async (updateTodo) => {
  try {
    const response = await fetch(`${BASE_URL}/${updateTodo.id}`, {
      method: "PUT",
      headers: getHeaders(),
      body: JSON.stringify(updateTodo),
    });
    return handleResponse(response);
  } catch (error) {
    console.error("Update todo error:", error);
    throw error;
  }
};

// 刪除待辦事項
export const deleteTodo = async (id) => {
  try {
    const response = await fetch(`${BASE_URL}/${id}`, {
      method: "DELETE",
      headers: getHeaders(),
    });
    await handleResponse(response);
    return true;
  } catch (error) {
    console.error("Delete todo error:", error);
    throw error;
  }
};
