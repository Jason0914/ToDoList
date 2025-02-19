import React from "react";
import TodoItem from "./TodoItem";

const TodoList = ({ todos, onToggleCompletion, onDelete }) => {
  return (
    <ul className="list-group">
      {todos.map((todo, index) => (
        <TodoItem
          key={todo.id}
          todo={todo}
          index={index + 1} // 從 1 開始的索引
          onToggleCompletion={onToggleCompletion}
          onDelete={onDelete}
        />
      ))}
    </ul>
  );
};

export default TodoList;
