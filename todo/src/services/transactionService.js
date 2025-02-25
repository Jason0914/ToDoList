/**
 * 交易記錄 API 服務
 *
 * 提供交易記錄的增刪改查、篩選和統計功能的 API 調用方法。
 * 所有方法都返回 Promise，便於異步處理。
 *
 * 後端 API 端點位於: http://localhost:8080/api/transactions
 */
const BASE_URL = "http://localhost:8080/api/transactions";

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
      throw new Error(errorData?.message || `請求失敗: ${response.status}`);
    }

    // 解析響應數據
    const result = await response.json();
    console.log("API Response:", result); // 添加日誌以查看 API 的完整響應

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
 * 獲取所有交易記錄
 *
 * @returns {Promise<Array>} - 交易記錄列表
 */
export const getAllTransactions = () => {
  console.log("Fetching all transactions");
  return fetchWithCredentials(BASE_URL)
    .then((data) => data || [])
    .catch((error) => {
      console.error("Error fetching transactions:", error);
      return []; // 發生錯誤時返回空數組，避免 UI 崩潰
    });
};

/**
 * 獲取指定日期範圍的交易記錄
 *
 * @param {Date} start - 開始日期
 * @param {Date} end - 結束日期
 * @returns {Promise<Array>} - 符合條件的交易記錄列表
 */
export const getTransactionsByDateRange = (start, end) => {
  console.log("Fetching transactions by date range:", { start, end });
  return fetchWithCredentials(
    `${BASE_URL}/range?start=${start.toISOString()}&end=${end.toISOString()}`
  )
    .then((data) => data || [])
    .catch((error) => {
      console.error("Error fetching transactions by date range:", error);
      return [];
    });
};

/**
 * 獲取指定類別的交易記錄
 *
 * @param {string} category - 交易類別
 * @returns {Promise<Array>} - 符合條件的交易記錄列表
 */
export const getTransactionsByCategory = (category) => {
  return fetchWithCredentials(`${BASE_URL}/category/${category}`)
    .then((data) => data || [])
    .catch((error) => {
      console.error("Error fetching transactions by category:", error);
      return [];
    });
};

/**
 * 獲取交易統計
 *
 * 計算指定日期範圍內的總收入、總支出和結餘
 *
 * @param {Date} start - 開始日期
 * @param {Date} end - 結束日期
 * @returns {Promise<Object>} - 包含 totalIncome、totalExpense 和 balance 的對象
 */
export const getTransactionsSummary = (start, end) => {
  console.log("Fetching transaction summary for:", {
    startIso: start.toISOString(),
    endIso: end.toISOString(),
  });

  // 確保傳入的是有效日期格式
  const validStart = start instanceof Date ? start : new Date(start);
  const validEnd = end instanceof Date ? end : new Date(end);

  const url = `${BASE_URL}/summary?start=${validStart.toISOString()}&end=${validEnd.toISOString()}`;
  console.log("Summary request URL:", url);

  return fetchWithCredentials(url)
    .then((data) => {
      console.log("Transaction summary data received:", data);
      // 確保返回數據有預期的結構，如果沒有則提供默認值
      return {
        totalIncome: data?.totalIncome || 0,
        totalExpense: data?.totalExpense || 0,
        balance: data?.balance || 0,
      };
    })
    .catch((error) => {
      console.error("Error fetching summary:", error);
      // 返回默認值而不是拋出錯誤，避免 UI 中斷
      return {
        totalIncome: 0,
        totalExpense: 0,
        balance: 0,
      };
    });
};

/**
 * 創建新交易記錄
 *
 * @param {Object} transaction - 交易記錄數據
 * @returns {Promise<Object>} - 創建成功的交易記錄
 * @throws {Error} - 創建失敗時拋出錯誤
 */
export const createTransaction = (transaction) => {
  return fetchWithCredentials(BASE_URL, {
    method: "POST",
    body: JSON.stringify(transaction),
  });
};

/**
 * 更新交易記錄
 *
 * @param {Object} transaction - 交易記錄更新數據
 * @param {number} transaction.id - 交易記錄 ID
 * @returns {Promise<Object>} - 更新後的交易記錄
 * @throws {Error} - 更新失敗時拋出錯誤
 */
export const updateTransaction = (transaction) => {
  return fetchWithCredentials(`${BASE_URL}/${transaction.id}`, {
    method: "PUT",
    body: JSON.stringify(transaction),
  });
};

/**
 * 刪除交易記錄
 *
 * @param {number} id - 交易記錄 ID
 * @returns {Promise<Object>} - 刪除結果
 * @throws {Error} - 刪除失敗時拋出錯誤
 */
export const deleteTransaction = (id) => {
  return fetchWithCredentials(`${BASE_URL}/${id}`, {
    method: "DELETE",
  });
};
