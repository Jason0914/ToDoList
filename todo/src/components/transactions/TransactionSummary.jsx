import React from "react";

const TransactionSummary = ({ summary }) => {
  const formatAmount = (amount) => {
    return new Intl.NumberFormat("zh-TW", {
      style: "currency",
      currency: "TWD",
      minimumFractionDigits: 0,
    }).format(amount || 0);
  };

  if (!summary) {
    return (
      <div className="card">
        <div className="card-body">
          <h5 className="card-title">載入中...</h5>
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
