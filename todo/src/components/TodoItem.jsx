import React from "react";

const TodoItem = ({ todo, index, onToggleCompletion, onDelete }) => {
  return (
    <li
      className={`list-group-item d-flex justify-content-between align-items-center ${
        todo.completed ? "list-group-item-success" : ""
      }`}
    >
      <div className="d-flex align-items-center">
        <span className="me-3 text-secondary">#{index}</span>
        <span
          style={{
            textDecoration: todo.completed ? "line-through" : "none",
            cursor: "pointer",
          }}
          onClick={() => onToggleCompletion(todo.id)}
        >
          {todo.text}
        </span>
      </div>
      <div>
        <input
          className="me-2"
          type="checkbox"
          checked={todo.completed}
          onChange={() => onToggleCompletion(todo.id)}
        />
        <button
          className="btn btn-danger btn-sm"
          onClick={() => onDelete(todo.id)}
        >
          X
        </button>
      </div>
    </li>
  );
};

export default TodoItem;
