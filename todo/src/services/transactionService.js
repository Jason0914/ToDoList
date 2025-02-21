const BASE_URL = "http://localhost:8080/api/transactions";

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
      throw new Error(errorData?.message || `請求失敗: ${response.status}`);
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

// 獲取所有交易記錄
export const getAllTransactions = () => {
  return fetchWithCredentials(BASE_URL);
};

// 獲取指定日期範圍的交易記錄
export const getTransactionsByDateRange = (start, end) => {
  return fetchWithCredentials(
    `${BASE_URL}/range?start=${start.toISOString()}&end=${end.toISOString()}`
  );
};

// 獲取指定類別的交易記錄
export const getTransactionsByCategory = (category) => {
  return fetchWithCredentials(`${BASE_URL}/category/${category}`);
};

// 獲取交易統計
export const getTransactionsSummary = (start, end) => {
  return fetchWithCredentials(
    `${BASE_URL}/summary?start=${start.toISOString()}&end=${end.toISOString()}`
  );
};

// 創建新交易記錄
export const createTransaction = (transaction) => {
  return fetchWithCredentials(BASE_URL, {
    method: "POST",
    body: JSON.stringify(transaction),
  });
};

// 更新交易記錄
export const updateTransaction = (transaction) => {
  return fetchWithCredentials(`${BASE_URL}/${transaction.id}`, {
    method: "PUT",
    body: JSON.stringify(transaction),
  });
};

// 刪除交易記錄
export const deleteTransaction = (id) => {
  return fetchWithCredentials(`${BASE_URL}/${id}`, {
    method: "DELETE",
  });
};
