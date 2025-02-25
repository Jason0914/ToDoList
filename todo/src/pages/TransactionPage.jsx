import React, { useState, useEffect } from "react";
import {
  getAllTransactions,
  getTransactionsByDateRange,
} from "../services/transactionService";
import TransactionList from "../components/transactions/TransactionList";
import TransactionForm from "../components/transactions/TransactionForm";
import TransactionSummary from "../components/transactions/TransactionSummary";
import TransactionChart from "../components/transactions/TransactionChart";

/**
 * 交易記錄頁面組件
 *
 * 提供交易記錄的管理功能，包括列表顯示、篩選、統計分析和圖表可視化。
 * 作為交易記錄管理的中央控制器，協調各個子組件和服務調用。
 *
 * @returns {JSX.Element} 交易記錄頁面
 */
function TransactionPage() {
  /**
   * 所有交易記錄狀態
   */
  const [allTransactions, setAllTransactions] = useState([]);

  /**
   * 過濾後的交易記錄狀態
   */
  const [filteredTransactions, setFilteredTransactions] = useState([]);

  /**
   * 日期範圍篩選狀態
   * 默認為當月第一天到當前日期
   */
  const [dateRange, setDateRange] = useState({
    start: new Date(new Date().setDate(1)), // 當月第一天
    end: new Date(),
  });

  /**
   * 表單顯示狀態
   */
  const [isFormOpen, setIsFormOpen] = useState(false);

  /**
   * 錯誤信息狀態
   */
  const [error, setError] = useState("");

  /**
   * 加載狀態
   */
  const [isLoading, setIsLoading] = useState(true);

  /**
   * 加載所有交易記錄數據
   */
  const loadData = async () => {
    try {
      setIsLoading(true);
      setError("");

      console.log("Loading all transactions data");
      // 調用 API 獲取所有交易記錄
      const transactionsData = await getAllTransactions();
      console.log("All Transactions Data:", transactionsData);
      setAllTransactions(transactionsData || []);

      // 立即進行篩選
      filterTransactionsByDate(transactionsData || [], dateRange);
    } catch (err) {
      console.error("Error loading data:", err);
      setError(`加載數據時出錯: ${err.message}`);
      setAllTransactions([]);
      setFilteredTransactions([]);
    } finally {
      setIsLoading(false);
    }
  };

  /**
   * 根據日期範圍篩選交易記錄
   *
   * @param {Array} transactions - 交易記錄數組
   * @param {Object} range - 日期範圍對象，包含 start 和 end 屬性
   */
  const filterTransactionsByDate = (transactions, range) => {
    console.log("Filtering transactions by date range:", range);

    // 確保日期是有效的 Date 對象
    const startDate = new Date(range.start);
    const endDate = new Date(range.end);

    // 設置日期的時分秒，確保範圍完整
    startDate.setHours(0, 0, 0, 0);
    endDate.setHours(23, 59, 59, 999);

    console.log("Using date range for filtering:", {
      startDate: startDate.toISOString(),
      endDate: endDate.toISOString(),
    });

    // 篩選在日期範圍內的交易記錄
    const filtered = transactions.filter((transaction) => {
      // 轉換交易日期字符串為 Date 對象
      const transactionDate = new Date(transaction.date);

      // 檢查日期是否在範圍內
      const isInRange =
        transactionDate >= startDate && transactionDate <= endDate;

      return isInRange;
    });

    console.log(
      `Filtered ${filtered.length} out of ${transactions.length} transactions`
    );
    setFilteredTransactions(filtered);
  };

  /**
   * 監聽日期範圍變化，重新篩選交易記錄
   */
  useEffect(() => {
    if (allTransactions.length > 0) {
      filterTransactionsByDate(allTransactions, dateRange);
    }
  }, [dateRange]);

  /**
   * 頁面載入時加載數據
   */
  useEffect(() => {
    loadData();
  }, []);

  /**
   * 處理表單提交
   * 關閉表單並重新加載數據
   */
  const handleFormSubmit = async () => {
    setIsFormOpen(false);
    await loadData(); // 重新加載數據
  };

  /**
   * 處理日期變更
   *
   * @param {string} field - 要更新的日期字段名稱（'start' 或 'end'）
   * @param {string} value - 新的日期值，格式為 YYYY-MM-DD
   */
  const handleDateChange = (field, value) => {
    const newDate = new Date(value);

    // 如果日期無效，不進行更新
    if (isNaN(newDate.getTime())) {
      console.error("Invalid date:", value);
      return;
    }

    setDateRange((prevRange) => {
      const newRange = {
        ...prevRange,
        [field]: newDate,
      };

      console.log(`Date ${field} changed to:`, newDate.toISOString());
      return newRange;
    });
  };

  return (
    <div className="container mt-4">
      {/* 頁面標題和錯誤信息 */}
      <div className="row mb-4">
        <div className="col">
          <h2 className="text-center">收支記錄</h2>
          {error && (
            <div className="alert alert-danger" role="alert">
              {error}
            </div>
          )}
        </div>
      </div>

      {/* 交易統計和圖表區域 */}
      <div className="row mb-4">
        <div className="col-md-6">
          <TransactionSummary
            transactions={filteredTransactions}
            isLoading={isLoading}
          />
        </div>
        <div className="col-md-6" style={{ minHeight: "650px" }}>
          <TransactionChart transactions={filteredTransactions} />
        </div>
      </div>

      {/* 篩選和添加按鈕區域 */}
      <div className="row mb-3">
        <div className="col d-flex justify-content-between align-items-center">
          {/* 日期範圍選擇器 */}
          <div>
            <input
              type="date"
              className="form-control d-inline-block me-2"
              style={{ width: "auto" }}
              value={dateRange.start.toISOString().split("T")[0]}
              onChange={(e) => handleDateChange("start", e.target.value)}
            />
            <span className="me-2">至</span>
            <input
              type="date"
              className="form-control d-inline-block me-2"
              style={{ width: "auto" }}
              value={dateRange.end.toISOString().split("T")[0]}
              onChange={(e) => handleDateChange("end", e.target.value)}
            />
          </div>

          {/* 新增交易按鈕 */}
          <button
            className="btn btn-primary"
            onClick={() => setIsFormOpen(true)}
          >
            新增交易
          </button>
        </div>
      </div>

      {/* 交易列表區域 */}
      <div className="row">
        <div className="col">
          <TransactionList
            transactions={filteredTransactions}
            onUpdate={loadData}
          />
        </div>
      </div>

      {/* 交易表單（條件渲染） */}
      {isFormOpen && (
        <TransactionForm
          onClose={() => setIsFormOpen(false)}
          onSubmit={handleFormSubmit}
        />
      )}
    </div>
  );
}

export default TransactionPage;
