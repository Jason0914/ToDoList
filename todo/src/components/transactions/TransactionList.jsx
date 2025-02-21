import React, { useState } from "react";
import { deleteTransaction } from "../../services/transactionService";
import TransactionForm from "./TransactionForm";

const TransactionList = ({ transactions, onUpdate }) => {
  const [editingTransaction, setEditingTransaction] = useState(null);

  const handleDelete = async (id) => {
    if (window.confirm("確定要刪除這筆交易記錄嗎？")) {
      try {
        await deleteTransaction(id);
        onUpdate();
      } catch (error) {
        alert("刪除失敗：" + error.message);
      }
    }
  };

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

  const formatAmount = (amount) => {
    return new Intl.NumberFormat("zh-TW", {
      style: "currency",
      currency: "TWD",
      minimumFractionDigits: 0,
    }).format(amount);
  };

  const handleEditComplete = () => {
    setEditingTransaction(null);
    onUpdate();
  };

  return (
    <>
      <div className="table-responsive">
        <table className="table table-hover">
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
          <tbody>
            {transactions.map((transaction) => (
              <tr key={transaction.id}>
                <td>{formatDate(transaction.date)}</td>
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
                <td>{transaction.category}</td>
                <td
                  className={
                    transaction.type === "INCOME"
                      ? "text-success"
                      : "text-danger"
                  }
                >
                  {formatAmount(transaction.amount)}
                </td>
                <td>{transaction.note}</td>
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
