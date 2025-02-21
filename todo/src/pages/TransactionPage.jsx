import React, { useState, useEffect } from "react";
import {
  getAllTransactions,
  getTransactionsByDateRange,
  getTransactionsSummary,
} from "../services/transactionService";
import TransactionList from "../components/transactions/TransactionList";
import TransactionForm from "../components/transactions/TransactionForm";
import TransactionSummary from "../components/transactions/TransactionSummary";
import TransactionChart from "../components/transactions/TransactionChart";

function TransactionPage() {
  const [transactions, setTransactions] = useState([]);
  const [summary, setSummary] = useState(null);
  const [dateRange, setDateRange] = useState({
    start: new Date(new Date().setDate(1)), // 當月第一天
    end: new Date(),
  });
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [error, setError] = useState("");

  // 加載交易記錄和統計數據
  const loadData = async () => {
    try {
      setError("");
      const [transactionsData, summaryData] = await Promise.all([
        getAllTransactions(), // 改用 getAllTransactions
        getTransactionsSummary(dateRange.start, dateRange.end),
      ]);
      console.log("Transactions Data:", transactionsData);
      setTransactions(transactionsData);
      setSummary(summaryData);
    } catch (err) {
      console.error("Error loading data:", err);
      setError(err.message);
    }
  };

  useEffect(() => {
    loadData();
  }, [dateRange]); // 監聽 dateRange 變化

  const handleFormSubmit = async () => {
    setIsFormOpen(false);
    await loadData(); // 重新加載數據
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
          <TransactionSummary summary={summary} />
        </div>
        <div className="col-md-6" style={{ minHeight: "650px" }}>
          <TransactionChart transactions={transactions} />
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
              onChange={(e) =>
                setDateRange({
                  ...dateRange,
                  start: new Date(e.target.value),
                })
              }
            />
            <span className="me-2">至</span>
            <input
              type="date"
              className="form-control d-inline-block me-2"
              style={{ width: "auto" }}
              value={dateRange.end.toISOString().split("T")[0]}
              onChange={(e) =>
                setDateRange({
                  ...dateRange,
                  end: new Date(e.target.value),
                })
              }
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
          <TransactionList transactions={transactions} onUpdate={loadData} />
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
