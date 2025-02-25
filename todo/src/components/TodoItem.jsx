import React from "react";

/**
 * 待辦事項項目組件
 *
 * 顯示單個待辦事項的組件，包含文本、完成狀態的切換和刪除功能。
 * 根據完成狀態顯示不同的樣式。
 *
 * @param {Object} props - 組件屬性
 * @param {Object} props.todo - 待辦事項數據
 * @param {number} props.index - 待辦事項的序號
 * @param {Function} props.onToggleCompletion - 切換完成狀態的回調函數
 * @param {Function} props.onDelete - 刪除待辦事項的回調函數
 * @returns {JSX.Element} 待辦事項項目
 */
const TodoItem = ({ todo, index, onToggleCompletion, onDelete }) => {
  return (
    <li
      className={`list-group-item d-flex justify-content-between align-items-center ${
        todo.completed ? "list-group-item-success" : ""
      }`}
    >
      {/* 左側：序號和待辦事項文本 */}
      <div className="d-flex align-items-center">
        <span className="me-3 text-secondary">#{index}</span>
        <span
          style={{
            textDecoration: todo.completed ? "line-through" : "none", // 完成時文本添加刪除線
            cursor: "pointer", // 鼠標指針樣式，表示可點擊
          }}
          onClick={() => onToggleCompletion(todo.id)} // 點擊文本切換完成狀態
        >
          {todo.text}
        </span>
      </div>

      {/* 右側：狀態切換複選框和刪除按鈕 */}
      <div>
        <input
          className="me-2"
          type="checkbox"
          checked={todo.completed}
          onChange={() => onToggleCompletion(todo.id)} // 切換複選框也會切換完成狀態
        />
        <button
          className="btn btn-danger btn-sm"
          onClick={() => onDelete(todo.id)} // 點擊刪除按鈕刪除待辦事項
        >
          X
        </button>
      </div>
    </li>
  );
};

export default TodoItem;
