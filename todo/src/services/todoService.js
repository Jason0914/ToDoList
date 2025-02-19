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

// 統一的請求處理函數
const fetchWithCredentials = async (url, options = {}) => {
  try {
    const response = await fetch(url, {
      ...options,
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
        ...options.headers,
      },
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => null);
      throw new Error(
        errorData?.message || `HTTP error! status: ${response.status}`
      );
    }

    const result = await response.json();
    if (result.status === 200) {
      return result.data;
    }
    throw new Error(result.message || "操作失敗");
  } catch (error) {
    console.error("API request failed:", error);
    throw error;
  }
};

// 獲取所有待辦事項
export const fetchTodos = async () => {
  return fetchWithCredentials(BASE_URL);
};

// 新增待辦事項
export const addTodo = async (todo) => {
  return fetchWithCredentials(BASE_URL, {
    method: "POST",
    body: JSON.stringify(todo),
  });
};

// 更新待辦事項
export const updateTodo = async (todo) => {
  return fetchWithCredentials(`${BASE_URL}/${todo.id}`, {
    method: "PUT",
    body: JSON.stringify(todo),
  });
};

// 刪除待辦事項
export const deleteTodo = async (id) => {
  await fetchWithCredentials(`${BASE_URL}/${id}`, {
    method: "DELETE",
  });
  return true;
};
