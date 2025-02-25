import React from "react";

/**
 * 交易統計摘要組件
 *
 * 顯示交易記錄的總收入、總支出和結餘。
 * 提供數據載入中的狀態顯示。
 *
 * @param {Object} props - 組件屬性
 * @param {Array} props.transactions - 交易記錄數組
 * @param {boolean} props.isLoading - 數據載入狀態
 * @returns {JSX.Element} 交易統計摘要卡片
 */
const TransactionSummary = ({ transactions, isLoading }) => {
  console.log("TransactionSummary received transactions:", transactions);

  /**
   * 計算交易統計數據
   *
   * 從交易記錄中計算總收入、總支出和結餘
   *
   * @returns {Object} 包含 totalIncome, totalExpense 和 balance 的統計對象
   */
  const calculateSummary = () => {
    if (!transactions || transactions.length === 0) {
      return {
        totalIncome: 0,
        totalExpense: 0,
        balance: 0,
      };
    }

    // 計算總收入
    const totalIncome = transactions
      .filter((t) => t.type === "INCOME")
      .reduce((sum, t) => sum + Number(t.amount || 0), 0);

    // 計算總支出
    const totalExpense = transactions
      .filter((t) => t.type === "EXPENSE")
      .reduce((sum, t) => sum + Number(t.amount || 0), 0);

    // 計算結餘
    return {
      totalIncome,
      totalExpense,
      balance: totalIncome - totalExpense,
    };
  };

  // 獲取統計數據
  const summary = calculateSummary();
  console.log("Calculated summary:", summary);

  /**
   * 格式化金額
   * 將數字轉換為貨幣格式
   *
   * @param {number} amount - 金額數值
   * @returns {string} 格式化後的金額字符串
   */
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

  // 載入中狀態顯示
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

  // 統計數據顯示
  return (
    <div className="card">
      <div className="card-body">
        <h5 className="card-title mb-4">收支統計</h5>
        <div className="row g-3">
          {/* 總收入 */}
          <div className="col-12">
            <div className="d-flex justify-content-between align-items-center">
              <span>總收入：</span>
              <span className="text-success h5 mb-0">
                {formatAmount(summary.totalIncome)}
              </span>
            </div>
          </div>

          {/* 總支出 */}
          <div className="col-12">
            <div className="d-flex justify-content-between align-items-center">
              <span>總支出：</span>
              <span className="text-danger h5 mb-0">
                {formatAmount(summary.totalExpense)}
              </span>
            </div>
          </div>

          {/* 結餘 */}
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
