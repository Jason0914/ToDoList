import React, { useState } from "react";
import {
  createTransaction,
  updateTransaction,
} from "../../services/transactionService";

const CATEGORIES = {
  INCOME: ["薪資", "獎金", "投資收入", "其他收入"],
  EXPENSE: ["飲食", "交通", "購物", "娛樂", "醫療", "住宿", "其他支出"],
};

const TransactionForm = ({ transaction = null, onClose, onSubmit }) => {
  const getInitialDateTime = () => {
    if (transaction?.date) {
      const date = new Date(transaction.date);
      return {
        date: new Date(date.getTime() - date.getTimezoneOffset() * 60000)
          .toISOString()
          .split("T")[0],
        time: `${String(date.getHours()).padStart(2, "0")}:${String(
          date.getMinutes()
        ).padStart(2, "0")}`,
      };
    }
    const now = new Date();
    return {
      date: new Date(now.getTime() - now.getTimezoneOffset() * 60000)
        .toISOString()
        .split("T")[0],
      time: `${String(now.getHours()).padStart(2, "0")}:${String(
        now.getMinutes()
      ).padStart(2, "0")}`,
    };
  };

  const [formData, setFormData] = useState({
    type: transaction?.type || "EXPENSE",
    amount: transaction?.amount || "",
    category: transaction?.category || "",
    note: transaction?.note || "",
    ...getInitialDateTime(),
  });

  const [error, setError] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));

    if (name === "type") {
      setFormData((prev) => ({
        ...prev,
        type: value,
        category: "",
      }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // 組合日期和時間並處理時區
      const [year, month, day] = formData.date.split("-");
      const [hours, minutes] = formData.time.split(":");

      // 創建本地時間的 Date 對象
      const localDate = new Date(year, month - 1, day, hours, minutes);

      // 轉換為 ISO 字符串時考慮時區偏移
      const isoString = new Date(
        localDate.getTime() - localDate.getTimezoneOffset() * 60000
      ).toISOString();

      const data = {
        ...formData,
        amount: parseFloat(formData.amount),
        date: isoString,
      };

      if (transaction?.id) {
        await updateTransaction({ ...data, id: transaction.id });
      } else {
        await createTransaction(data);
      }
      onSubmit();
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div
      className="modal d-block"
      style={{ backgroundColor: "rgba(0,0,0,0.5)" }}
    >
      <div className="modal-dialog">
        <div className="modal-content">
          <div className="modal-header">
            <h5 className="modal-title">
              {transaction ? "編輯交易" : "新增交易"}
            </h5>
            <button
              type="button"
              className="btn-close"
              onClick={onClose}
            ></button>
          </div>
          <div className="modal-body">
            {error && <div className="alert alert-danger">{error}</div>}
            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label className="form-label">類型</label>
                <select
                  name="type"
                  className="form-select"
                  value={formData.type}
                  onChange={handleChange}
                  required
                >
                  <option value="EXPENSE">支出</option>
                  <option value="INCOME">收入</option>
                </select>
              </div>

              <div className="mb-3">
                <label className="form-label">類別</label>
                <select
                  name="category"
                  className="form-select"
                  value={formData.category}
                  onChange={handleChange}
                  required
                >
                  <option value="">請選擇類別</option>
                  {CATEGORIES[formData.type].map((category) => (
                    <option key={category} value={category}>
                      {category}
                    </option>
                  ))}
                </select>
              </div>

              <div className="mb-3">
                <label className="form-label">金額</label>
                <input
                  type="number"
                  name="amount"
                  className="form-control"
                  value={formData.amount}
                  onChange={handleChange}
                  min="0"
                  step="1"
                  required
                />
              </div>

              <div className="row mb-3">
                <div className="col">
                  <label className="form-label">日期</label>
                  <input
                    type="date"
                    name="date"
                    className="form-control"
                    value={formData.date}
                    onChange={handleChange}
                    required
                  />
                </div>
                <div className="col">
                  <label className="form-label">時間</label>
                  <input
                    type="time"
                    name="time"
                    className="form-control"
                    value={formData.time}
                    onChange={handleChange}
                    required
                  />
                </div>
              </div>

              <div className="mb-3">
                <label className="form-label">備註</label>
                <textarea
                  name="note"
                  className="form-control"
                  value={formData.note}
                  onChange={handleChange}
                  rows="3"
                />
              </div>

              <div className="modal-footer">
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={onClose}
                >
                  取消
                </button>
                <button type="submit" className="btn btn-primary">
                  {transaction ? "更新" : "新增"}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default TransactionForm;
