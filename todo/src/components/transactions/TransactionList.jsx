import React, { useState } from "react";
import { deleteTransaction } from "../../services/transactionService";
import TransactionForm from "./TransactionForm";

/**
 * 交易記錄列表組件
 *
 * 以表格形式顯示交易記錄，並提供編輯和刪除功能。
 *
 * @param {Object} props - 組件屬性
 * @param {Array} props.transactions - 交易記錄數組
 * @param {Function} props.onUpdate - 數據更新後的回調函數
 * @returns {JSX.Element} 交易記錄列表
 */
const TransactionList = ({ transactions, onUpdate }) => {
  /**
   * 當前正在編輯的交易記錄
   * null 表示沒有正在編輯的記錄
   */
  const [editingTransaction, setEditingTransaction] = useState(null);

  /**
   * 處理刪除交易記錄
   *
   * @param {number} id - 要刪除的交易記錄 ID
   */
  const handleDelete = async (id) => {
    // 確認刪除操作
    if (window.confirm("確定要刪除這筆交易記錄嗎？")) {
      try {
        // 調用 API 刪除交易記錄
        await deleteTransaction(id);
        // 通知父組件更新數據
        onUpdate();
      } catch (error) {
        alert("刪除失敗：" + error.message);
      }
    }
  };

  /**
   * 格式化日期時間
   * 將 ISO 日期字符串轉換為本地格式
   *
   * @param {string} dateString - ISO 格式的日期字符串
   * @returns {string} 格式化後的日期時間字符串
   */
  const formatDate = (dateString) => {
    const options = {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
      hour12: false,
    };
    return new Date(dateString)
      .toLocaleString("zh-TW", options)
      .replace(/\//g, "-");
  };

  /**
   * 格式化金額
   * 將數字轉換為貨幣格式
   *
   * @param {number} amount - 金額數值
   * @returns {string} 格式化後的金額字符串
   */
  const formatAmount = (amount) => {
    return new Intl.NumberFormat("zh-TW", {
      style: "currency",
      currency: "TWD",
      minimumFractionDigits: 0,
    }).format(amount);
  };

  /**
   * 處理編輯完成
   * 關閉編輯表單並更新數據
   */
  const handleEditComplete = () => {
    setEditingTransaction(null);
    onUpdate();
  };

  return (
    <>
      <div className="table-responsive">
        <table className="table table-hover">
          {/* 表頭 */}
          <thead>
            <tr>
              <th>日期</th>
              <th>類型</th>
              <th>類別</th>
              <th>金額</th>
              <th>備註</th>
              <th>操作</th>
            </tr>
          </thead>

          {/* 表體 - 顯示交易記錄 */}
          <tbody>
            {transactions.map((transaction) => (
              <tr key={transaction.id}>
                {/* 日期欄 */}
                <td>{formatDate(transaction.date)}</td>

                {/* 類型欄 - 根據收入/支出顯示不同顏色 */}
                <td>
                  <span
                    className={
                      transaction.type === "INCOME"
                        ? "text-success"
                        : "text-danger"
                    }
                  >
                    {transaction.type === "INCOME" ? "收入" : "支出"}
                  </span>
                </td>

                {/* 類別欄 */}
                <td>{transaction.category}</td>

                {/* 金額欄 - 根據收入/支出顯示不同顏色 */}
                <td
                  className={
                    transaction.type === "INCOME"
                      ? "text-success"
                      : "text-danger"
                  }
                >
                  {formatAmount(transaction.amount)}
                </td>

                {/* 備註欄 */}
                <td>{transaction.note}</td>

                {/* 操作按鈕欄 */}
                <td>
                  <button
                    className="btn btn-sm btn-outline-primary me-2"
                    onClick={() => setEditingTransaction(transaction)}
                  >
                    編輯
                  </button>
                  <button
                    className="btn btn-sm btn-outline-danger"
                    onClick={() => handleDelete(transaction.id)}
                  >
                    刪除
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* 編輯表單（條件渲染） */}
      {editingTransaction && (
        <TransactionForm
          transaction={editingTransaction}
          onClose={() => setEditingTransaction(null)}
          onSubmit={handleEditComplete}
        />
      )}
    </>
  );
};

export default TransactionList;
