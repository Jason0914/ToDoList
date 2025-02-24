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

// 獲取所有交易記錄
export const getAllTransactions = () => {
  console.log("Fetching all transactions");
  return fetchWithCredentials(BASE_URL)
    .then((data) => data || [])
    .catch((error) => {
      console.error("Error fetching transactions:", error);
      return [];
    });
};

// 獲取指定日期範圍的交易記錄
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

// 獲取指定類別的交易記錄
export const getTransactionsByCategory = (category) => {
  return fetchWithCredentials(`${BASE_URL}/category/${category}`)
    .then((data) => data || [])
    .catch((error) => {
      console.error("Error fetching transactions by category:", error);
      return [];
    });
};

// 獲取交易統計
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
