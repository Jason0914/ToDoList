import React from "react";

/**
 * 待辦事項輸入組件
 *
 * 提供輸入框和添加按鈕，用於創建新的待辦事項。
 * 使用 Bootstrap 的輸入組結構，將輸入框和按鈕組合在一起。
 *
 * @param {Object} props - 組件屬性
 * @param {string} props.todo - 輸入框的值
 * @param {Function} props.onChange - 輸入變化時的回調函數
 * @param {Function} props.onAdd - 添加按鈕點擊時的回調函數
 * @returns {JSX.Element} 待辦事項輸入組件
 */
const TodoInput = ({ todo, onChange, onAdd }) => {
  return (
    <div className="input-group mb-3">
      {/* 輸入框 */}
      <input
        className="form-control"
        type="text"
        onChange={onChange}
        value={todo}
        placeholder="輸入新的待辦事項..."
      />

      {/* 添加按鈕 */}
      <button className="btn btn-primary" onClick={onAdd}>
        Add
      </button>
    </div>
  );
};

export default TodoInput;
