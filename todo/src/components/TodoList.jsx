import React from "react";
import TodoItem from "./TodoItem";

/**
 * 待辦事項列表組件
 *
 * 顯示所有待辦事項的容器組件，將每個項目渲染為 TodoItem 組件。
 *
 * @param {Object} props - 組件屬性
 * @param {Array} props.todos - 待辦事項數組
 * @param {Function} props.onToggleCompletion - 切換完成狀態的回調函數
 * @param {Function} props.onDelete - 刪除待辦事項的回調函數
 * @returns {JSX.Element} 待辦事項列表
 */
const TodoList = ({ todos, onToggleCompletion, onDelete }) => {
  return (
    <ul className="list-group">
      {/* 映射待辦事項數組為 TodoItem 組件 */}
      {todos.map((todo, index) => (
        <TodoItem
          key={todo.id} // 使用唯一 ID 作為 key，提高渲染效率
          todo={todo}
          index={index + 1} // 從 1 開始的索引，用於顯示序號
          onToggleCompletion={onToggleCompletion}
          onDelete={onDelete}
        />
      ))}
    </ul>
  );
};

export default TodoList;
