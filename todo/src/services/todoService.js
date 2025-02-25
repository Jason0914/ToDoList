/**
 * 待辦事項 API 服務
 *
 * 提供待辦事項的增刪改查功能的 API 調用方法。
 * 所有方法都返回 Promise，便於異步處理。
 *
 * 後端 API 端點:
 * ------------------------------------------------------------
 * GET    "http://localhost:8080/todolist/"     獲取所有待辦事項
 * POST   "http://localhost:8080/todolist/"     新增待辦事項
 * PUT    "http://localhost:8080/todolist/{id}" 更新待辦事項
 * DELETE "http://localhost:8080/todolist/{id}" 刪除待辦事項
 * ------------------------------------------------------------
 */
const BASE_URL = "http://localhost:8080/todolist";

/**
 * 統一的請求處理函數
 *
 * 包含了錯誤處理和 cookies 發送設置，簡化 API 調用代碼。
 *
 * @param {string} url - 請求 URL
 * @param {Object} options - 請求配置選項
 * @returns {Promise<any>} - 解析後的響應數據
 * @throws {Error} - 請求失敗時拋出錯誤
 */
const fetchWithCredentials = async (url, options = {}) => {
  try {
    // 發送請求，包含 credentials 選項以發送 cookies
    const response = await fetch(url, {
      ...options,
      credentials: "include", // 重要：允許發送 cookies，用於會話認證
      headers: {
        "Content-Type": "application/json",
        ...options.headers,
      },
    });

    // 檢查響應狀態
    if (!response.ok) {
      const errorData = await response.json().catch(() => null);
      throw new Error(
        errorData?.message || `HTTP error! status: ${response.status}`
      );
    }

    // 解析響應數據
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

/**
 * 獲取所有待辦事項
 *
 * @returns {Promise<Array>} - 待辦事項列表
 * @throws {Error} - 請求失敗時拋出錯誤
 */
export const fetchTodos = async () => {
  return fetchWithCredentials(BASE_URL);
};

/**
 * 新增待辦事項
 *
 * @param {Object} todo - 待辦事項數據
 * @param {string} todo.text - 待辦事項文本
 * @param {boolean} [todo.completed=false] - 完成狀態
 * @returns {Promise<Object>} - 創建成功的待辦事項
 * @throws {Error} - 請求失敗時拋出錯誤
 */
export const addTodo = async (todo) => {
  return fetchWithCredentials(BASE_URL, {
    method: "POST",
    body: JSON.stringify(todo),
  });
};

/**
 * 更新待辦事項
 *
 * @param {Object} todo - 待辦事項更新數據
 * @param {number} todo.id - 待辦事項 ID
 * @param {string} todo.text - 待辦事項文本
 * @param {boolean} todo.completed - 完成狀態
 * @returns {Promise<Object>} - 更新後的待辦事項
 * @throws {Error} - 請求失敗時拋出錯誤
 */
export const updateTodo = async (todo) => {
  return fetchWithCredentials(`${BASE_URL}/${todo.id}`, {
    method: "PUT",
    body: JSON.stringify(todo),
  });
};

/**
 * 刪除待辦事項
 *
 * @param {number} id - 待辦事項 ID
 * @returns {Promise<boolean>} - 成功返回 true
 * @throws {Error} - 請求失敗時拋出錯誤
 */
export const deleteTodo = async (id) => {
  await fetchWithCredentials(`${BASE_URL}/${id}`, {
    method: "DELETE",
  });
  return true;
};
