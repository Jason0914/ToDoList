import React from "react";

const TransactionSummary = ({ transactions, isLoading }) => {
  console.log("TransactionSummary received transactions:", transactions);

  // 直接使用交易數據計算總額
  const calculateSummary = () => {
    if (!transactions || transactions.length === 0) {
      return {
        totalIncome: 0,
        totalExpense: 0,
        balance: 0,
      };
    }

    const totalIncome = transactions
      .filter((t) => t.type === "INCOME")
      .reduce((sum, t) => sum + Number(t.amount || 0), 0);

    const totalExpense = transactions
      .filter((t) => t.type === "EXPENSE")
      .reduce((sum, t) => sum + Number(t.amount || 0), 0);

    return {
      totalIncome,
      totalExpense,
      balance: totalIncome - totalExpense,
    };
  };

  const summary = calculateSummary();
  console.log("Calculated summary:", summary);

  const formatAmount = (amount) => {
    try {
      // 確保 amount 是數字，如果不是則使用默認值 0
      const numAmount =
        amount !== null && amount !== undefined ? Number(amount) : 0;

      return new Intl.NumberFormat("zh-TW", {
        style: "currency",
        currency: "TWD",
        minimumFractionDigits: 0,
      }).format(numAmount);
    } catch (error) {
      console.error("Error formatting amount:", error, "Value was:", amount);
      return "NT$0"; // 發生錯誤時的默認值
    }
  };

  // 處理加載狀態
  if (isLoading) {
    return (
      <div className="card">
        <div className="card-body">
          <h5 className="card-title">載入中...</h5>
          <div className="text-center mt-3">
            <div className="spinner-border text-primary" role="status">
              <span className="visually-hidden">Loading...</span>
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="card">
      <div className="card-body">
        <h5 className="card-title mb-4">收支統計</h5>
        <div className="row g-3">
          <div className="col-12">
            <div className="d-flex justify-content-between align-items-center">
              <span>總收入：</span>
              <span className="text-success h5 mb-0">
                {formatAmount(summary.totalIncome)}
              </span>
            </div>
          </div>
          <div className="col-12">
            <div className="d-flex justify-content-between align-items-center">
              <span>總支出：</span>
              <span className="text-danger h5 mb-0">
                {formatAmount(summary.totalExpense)}
              </span>
            </div>
          </div>
          <div className="col-12">
            <hr />
            <div className="d-flex justify-content-between align-items-center">
              <span>結餘：</span>
              <span
                className={`h4 mb-0 ${
                  summary.balance >= 0 ? "text-success" : "text-danger"
                }`}
              >
                {formatAmount(summary.balance)}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default TransactionSummary;
