import React, { useState } from "react";
import {
  createTransaction,
  updateTransaction,
} from "../../services/transactionService";

/**
 * 交易類別常量
 * 根據交易類型（收入/支出）定義可選類別
 */
const CATEGORIES = {
  INCOME: ["薪資", "獎金", "投資收入", "其他收入"],
  EXPENSE: ["飲食", "交通", "購物", "娛樂", "醫療", "住宿", "其他支出"],
};

/**
 * 交易記錄表單組件
 *
 * 提供添加和編輯交易記錄的表單界面。
 * 以模態對話框形式顯示，支持設置交易類型、類別、金額、日期時間和備註。
 *
 * @param {Object} props - 組件屬性
 * @param {Object} [props.transaction=null] - 要編輯的交易記錄，為 null 時表示新增模式
 * @param {Function} props.onClose - 關閉表單的回調函數
 * @param {Function} props.onSubmit - 表單提交成功的回調函數
 * @returns {JSX.Element} 交易記錄表單
 */
const TransactionForm = ({ transaction = null, onClose, onSubmit }) => {
  /**
   * 獲取初始日期時間
   * 編輯模式下使用交易記錄的日期，新增模式下使用當前時間
   *
   * @returns {Object} 包含 date 和 time 字符串的對象
   */
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

  /**
   * 表單數據狀態
   * 初始值根據是編輯模式還是新增模式設置
   */
  const [formData, setFormData] = useState({
    type: transaction?.type || "EXPENSE",
    amount: transaction?.amount || "",
    category: transaction?.category || "",
    note: transaction?.note || "",
    ...getInitialDateTime(),
  });

  /**
   * 錯誤信息狀態
   */
  const [error, setError] = useState("");

  /**
   * 處理表單字段變化
   *
   * @param {Object} e - 事件對象
   */
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));

    // 當類型變更時，重置類別選擇
    if (name === "type") {
      setFormData((prev) => ({
        ...prev,
        type: value,
        category: "",
      }));
    }
  };

  /**
   * 處理表單提交
   *
   * @param {Object} e - 事件對象
   */
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

      // 構建提交數據
      const data = {
        ...formData,
        amount: parseFloat(formData.amount),
        date: isoString,
      };

      // 根據是否有 ID 判斷是更新還是創建
      if (transaction?.id) {
        // 更新現有交易記錄
        await updateTransaction({ ...data, id: transaction.id });
      } else {
        // 創建新交易記錄
        await createTransaction(data);
      }

      // 調用成功回調
      onSubmit();
    } catch (err) {
      // 顯示錯誤信息
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
          {/* 模態框標題 */}
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

          {/* 模態框內容 */}
          <div className="modal-body">
            {/* 錯誤信息顯示 */}
            {error && <div className="alert alert-danger">{error}</div>}

            {/* 交易記錄表單 */}
            <form onSubmit={handleSubmit}>
              {/* 交易類型選擇 */}
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

              {/* 交易類別選擇 - 根據類型動態生成選項 */}
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

              {/* 金額輸入 */}
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

              {/* 日期和時間輸入 */}
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

              {/* 備註輸入 */}
              <div className="mb-3">
                <label className="form-label">備註</label>
                <textarea
                  name="note"
                  className="form-control"
                  value={formData.note}
                  onChange={handleChange}
                  rows="3"
                  placeholder="可選填"
                />
              </div>

              {/* 模態框底部按鈕 */}
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
