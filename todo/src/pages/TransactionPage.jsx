import React, { useState, useEffect } from "react";
import {
  getAllTransactions,
  getTransactionsByDateRange,
} from "../services/transactionService";
import TransactionList from "../components/transactions/TransactionList";
import TransactionForm from "../components/transactions/TransactionForm";
import TransactionSummary from "../components/transactions/TransactionSummary";
import TransactionChart from "../components/transactions/TransactionChart";

function TransactionPage() {
  const [allTransactions, setAllTransactions] = useState([]); // 存儲所有交易
  const [filteredTransactions, setFilteredTransactions] = useState([]); // 過濾後的交易
  const [dateRange, setDateRange] = useState({
    start: new Date(new Date().setDate(1)), // 當月第一天
    end: new Date(),
  });
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(true);

  // 加載所有交易記錄
  const loadData = async () => {
    try {
      setIsLoading(true);
      setError("");

      console.log("Loading all transactions data");
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

  // 根據日期範圍篩選交易
  const filterTransactionsByDate = (transactions, range) => {
    console.log("Filtering transactions by date range:", range);

    // 確保日期是有效的 Date 對象
    const startDate = new Date(range.start);
    const endDate = new Date(range.end);

    // 設置日期的時分秒
    startDate.setHours(0, 0, 0, 0);
    endDate.setHours(23, 59, 59, 999);

    console.log("Using date range for filtering:", {
      startDate: startDate.toISOString(),
      endDate: endDate.toISOString(),
    });

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

  // 日期範圍變化時重新篩選
  useEffect(() => {
    if (allTransactions.length > 0) {
      filterTransactionsByDate(allTransactions, dateRange);
    }
  }, [dateRange]);

  // 首次加載和日期範圍變化時
  useEffect(() => {
    loadData();
  }, []);

  const handleFormSubmit = async () => {
    setIsFormOpen(false);
    await loadData(); // 重新加載數據
  };

  // 處理日期變更
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

      <div className="row mb-3">
        <div className="col d-flex justify-content-between align-items-center">
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
          <button
            className="btn btn-primary"
            onClick={() => setIsFormOpen(true)}
          >
            新增交易
          </button>
        </div>
      </div>

      <div className="row">
        <div className="col">
          <TransactionList
            transactions={filteredTransactions}
            onUpdate={loadData}
          />
        </div>
      </div>

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
